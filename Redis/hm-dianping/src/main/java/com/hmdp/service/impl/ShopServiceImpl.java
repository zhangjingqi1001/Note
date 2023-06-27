package com.hmdp.service.impl;

import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.hmdp.dto.Result;
import com.hmdp.entity.Shop;
import com.hmdp.mapper.ShopMapper;
import com.hmdp.service.IShopService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hmdp.utils.CacheClient;
import com.hmdp.utils.RedisData;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author 虎哥
 * @since 2021-12-22
 */
@Service
public class ShopServiceImpl extends ServiceImpl<ShopMapper, Shop> implements IShopService {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private CacheClient cacheClient;

    @Override
    public Result queryById(Long id) {
//      工具类解决缓存穿透
        Function<Long,Shop> function = new Function<Long, Shop>() {
            @Override
            public Shop apply(Long id) {
                return getById(id);
            }
        };
        Shop shop = cacheClient.queryWithPassThrough("cache:shop:",id,Shop.class,function,20L,TimeUnit.MINUTES);

        if(shop==null){
            return Result.fail("商铺不存在");
        }

//      缓存穿透
//        Shop shop = queryWithPassThrough(id);
//      互斥锁解决缓存击穿
//        Shop shop = queryWithMutex(id);
//      利用逻辑过期解决缓存击穿
//        Shop shop = queryWithLogicalExpire(id);

        return Result.ok(shop);
    }

    private static final ExecutorService CACHE_REBUILD_EXECUTOR = Executors.newFixedThreadPool(10);;

    // 利用逻辑过期解决缓存击穿  "cache:shop:","lock:shop:",10L
    public Shop queryWithLogicalExpire(Long id) {
        String key = "cache:shop:" + id;
//      TODO 1.从redis查询商铺缓存
        String shopJson = stringRedisTemplate.opsForValue().get("cache:shop:" + id);
//      TODO 2. 判断时Redis是否命中
        if (StrUtil.isBlank(shopJson)) {
//          TODO 3.缓存不存在，直接返回空
            return null;
        }
//      TODO 4.存在,需要先把JSON反序列化为对象
        RedisData redisData = JSONUtil.toBean(shopJson, RedisData.class);
//      因为我们在RedisData中设置data属性就是Object类型，所以当我们取的时候程序并不知道我们是什么类型，我们加一个强转就好了
        JSONObject shopData = (JSONObject) redisData.getData();
        Shop shop = JSONUtil.toBean(shopData, Shop.class);
        LocalDateTime expireTime = redisData.getExpireTime();

//      TODO 5.判断是否过期
        if (expireTime.isAfter(LocalDateTime.now())) {
//          TODO 5.1 未过期,返回商铺信息
            return shop;
        }

//      TODO 5.2 已过期,需要缓存重建
//      TODO 6.  缓存重建
//      TODO 6.1 获取互斥锁
        String lockKey = "lock:shop:" + id;
        boolean isLock = tryLock(lockKey);

//      TODO 6.2 判断是否获取锁成功
        if (isLock) {
//      TODO 6.3 成功，获取锁成功应该再次检测Redis缓存是否过期，做DoubleCheck，如果存在则无序重建缓存
            shopJson = stringRedisTemplate.opsForValue().get("cache:shop:" + id);
            redisData = JSONUtil.toBean(shopJson, RedisData.class);
            shopData = (JSONObject) redisData.getData();
            shop = JSONUtil.toBean(shopData, Shop.class);
            expireTime = redisData.getExpireTime();
            if (expireTime.isAfter(LocalDateTime.now())) {
//          TODO 未过期,返回商铺信息
                return shop;
            }

//       TODO 成功,但是缓存过期了，开启独立线程,实现缓存重建（建议使用线程池）
            CACHE_REBUILD_EXECUTOR.submit(()->{
                try{
                    //              重建缓存
                    this.saveShop2Redis(id,30L);
                }catch (Exception e){
                    throw new RuntimeException(e);
                }finally {
//                  释放锁
                    unlock(lockKey);
                }
            });

        }
//      TODO 6.4 失败,返回已经过期的商品信息
        return shop;
    }


    //  缓存传统解决方案 缓存穿透
    public Shop queryWithPassThrough(Long id) {
//        TODO 1. 从Redis查询商铺缓存
        //可以选择Hash结构，没问题，也能String
        String shopJson = stringRedisTemplate.opsForValue().get("cache:shop:" + id);

//      TODO 2. 判断时Redis是否命中
        if (StrUtil.isNotBlank(shopJson)) {
//      TODO 3. 存在，返回商户信息
//            Shop shop = JSONUtil.toBean(shopJson, Shop.class);
            return JSONUtil.toBean(shopJson, Shop.class);
        }
//      TODO 多判断一步，命中的是否是空值
//        运行到这里，说明上面的if没有进去，->说明StrUtil.isNotBlank(shopJson)是false ->shopJson两种情况 空白字符串或者null
        if (shopJson != null) {
//           不能等于null，就一定是一个空字符串
            return null;
        }

//      TODO 4. 不存在，向数据库进行查询
        Shop shop = getById(id);
//      TODO 5. 数据库不存在，返回错误
        if (shop == null) {
//       将空值写入redis
            stringRedisTemplate.opsForValue().set("cache:shop:" + id, "", 2, TimeUnit.MINUTES);
//       返回错误信息
            return null;
        }
//      TODO 6. 存在,写入Redis
        String shopTOJson = JSONUtil.toJsonStr(shop);
        stringRedisTemplate.opsForValue().set("cache:shop:" + id, shopTOJson, 30, TimeUnit.MINUTES);

//      TODO 7. 返回最终结果
        return shop;
    }

    /**
     * 缓存击穿
     *
     * @param id
     * @return
     */
    public Shop queryWithMutex(Long id) {
//      1.从redis查询商铺缓存    这个地方可以用hash，我们这里用String演示一下
        String shopJson = stringRedisTemplate.opsForValue().get("cache:shop:" + id);
//      2.判断Redis中是否存在
        if (StrUtil.isNotBlank(shopJson)) {
//      3.不为空，存在，直接返回(得将JSON字符串转化成对应的对象）
            Shop shop = JSONUtil.toBean(shopJson, Shop.class);
            return shop;
        }
//       缓存穿透这里得再添加一个看看是否是空值
        if (shopJson != null) {
//           不能等于null，就一定是一个空字符串
            return null;
        }
//      TODO 4.实现缓存重建
        Shop shop = null;
        try {
//         4.1获取互斥锁   锁的key和缓存key不一样
            boolean isLock = tryLock("lock:shop" + id);
//         4.2判断是否获取成功
            if (!isLock) {
//         4.3失败，则休眠并重试  重试就是重新执行查询动作,使用递归
                Thread.sleep(50);
                return queryWithMutex(id);
            }
//         4.4.成功，根据id查询数据库
//          在此做缓存DoubleCheck，看看缓存中有无对应数据
            shopJson = stringRedisTemplate.opsForValue().get("cache:shop:" + id);
            //判断Redis中是否存在
            if (StrUtil.isNotBlank(shopJson)) {
//           不为空，存在，直接返回(得将JSON字符串转化成对应的对象）
                shop = JSONUtil.toBean(shopJson, Shop.class);
                return shop;
            }
//       缓存穿透这里得再添加一个看看是否是空值
            if (shopJson != null) {
//           不能等于null，就一定是一个空字符串
                return null;
            }
//          根据id查询数据库
            shop = getById(id);
//       5. 判断数据库中是否存在
            if (shop == null) {
//       6. 不存在返回错误，并且将空值写入Redis   此时存放Redis就是空值
                stringRedisTemplate.opsForValue().set("cache:shop:" + id, "", 2, TimeUnit.MINUTES);
                return null;
            }
//      7.  存在，写入Redis
            stringRedisTemplate.opsForValue().set("cache:shop:" + id, JSONUtil.toJsonStr(shop), 30, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
//        8.释放互斥锁
            unlock("lock:shop" + id);
        }
//      9.返回
        return shop;
    }

    @Override
    @Transactional
    public Result update(Shop shop) {
        if (shop.getId() == null) {
            return Result.fail("店铺id不能为空");
        }
//      TODO 1.更新数据库
        updateById(shop);
//      TODO 2.删除缓存
        stringRedisTemplate.delete("cache:shop:" + shop.getId());

        return Result.ok();
    }

    //    拿到锁
    private boolean tryLock(String key) {
        //setIfAbsent方法就是Redis中的setnx
        //在Redis命令行中的运行结果就是0或者1，但是在这的运行结果是true或false，但是返回的是Boolean类型，封装类
        Boolean flag = stringRedisTemplate.opsForValue().setIfAbsent(key, "1", 10, TimeUnit.SECONDS);
        //不建议直接返回：会自动拆箱，有时候会出现空指针
        return BooleanUtil.isTrue(flag);
    }

    //    释放锁
    private void unlock(String key) {
        stringRedisTemplate.delete(key);
    }

    //  将店铺信息保存到Redis中
    public void saveShop2Redis(Long id, Long expireSeconds) {
//      1.查询店铺数据
        Shop shop = getById(id);
//      2.封装成逻辑过期
        RedisData redisData = new RedisData();
        redisData.setData(shop);
//      plusSeconds(expireSeconds) 在当前时间的基础上增加多少秒
        redisData.setExpireTime(LocalDateTime.now().plusSeconds(expireSeconds));
//      3.写入Redis
        stringRedisTemplate.opsForValue().set("cache:shop:" + id, JSONUtil.toJsonStr(redisData));

    }
}
