package com.hmdp;

import com.hmdp.entity.Shop;
import com.hmdp.service.ISeckillVoucherService;
import com.hmdp.service.impl.ShopServiceImpl;
import com.hmdp.utils.RedisIdWorker;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.core.StringRedisTemplate;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@SpringBootTest
class HmDianPingApplicationTests {

    @Resource
    private ShopServiceImpl shopService;

    @Test
    void test() {
        shopService.saveShop2Redis(1L, 10L);
    }

//    @Resource
//    private RedisIdWorker redisIdWorker;
//
//    private ExecutorService es = Executors.newFixedThreadPool(500);
//
//    @Test
//    void test2() throws InterruptedException {
//        CountDownLatch latch = new CountDownLatch(300);
//
////      创建任务
//        Runnable task = () -> {
//            for (int i = 0; i < 100; i++) {
//                long id = redisIdWorker.nextId("order");
//                System.out.println("id=" + id);
//            }
//            latch.countDown();
//        };
//        long begin = System.currentTimeMillis();
////      提交300次任务，每一次任务存100个，总共30000个
//        for (int i = 0; i < 300; i++) {
//            es.submit(task);
//        }
//
//        latch.await(); //等待300个countDown结束，再继续向下进行，否则等待
//
//        long end = System.currentTimeMillis();
//        System.out.println(begin - end);
//
//    }
//
//    @Test
//    void test3() {
//        long id = redisIdWorker.nextId("order");
//        System.out.println(id);
//    }


    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Test
    void loadShopData() {
//      TODO 1. 查询所有店铺信息
        List<Shop> list = shopService.list();
//      TODO 2. 把店铺分组，按照typeId分组，id一致的放到一个集合
        Map<Long, List<Shop>> map = list.stream().collect(Collectors.groupingBy(shop -> shop.getTypeId()));
//      TODO 3. 分批完成存储写入Redis
        for (Map.Entry<Long, List<Shop>> entry : map.entrySet()) {
//          TODO 3.1 获取类型id
            Long typeId = entry.getKey();
            String key = "shop:geo:" + typeId;
//          TODO 3.2 获取同类型的店铺集合
            List<Shop> value = entry.getValue();
//          TODO 3.3 写入Redis GEOADD key 经度 纬度 member
//          方法1：效率比较低，不采用
//            for (Shop shop : value) {
//              坐标我们可以一个个指定，也可以直接new一个Point对象
//              stringRedisTemplate.opsForGeo().add(key,new Point(shop.getX(),shop.getY()),shop.getId().toString());
//              方法2：
//            }
//          方法2
            List<RedisGeoCommands.GeoLocation<String>> locations = new ArrayList<>();

            for (Shop shop : value) {
//          下面泛型的类型是member的类型
                locations.add(new RedisGeoCommands.GeoLocation<>(
                        shop.getId().toString(),
                        new Point(shop.getX(), shop.getY())
                ));
            }
//          批量操作
            stringRedisTemplate.opsForGeo().add(key,locations);

        }

    }


    @Resource
    private RedisIdWorker redisIdWorker;

    private ExecutorService es = Executors.newFixedThreadPool(500);

    @Test
    void testIdWorker() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(300);

        Runnable task = () -> {
            for (int i = 0; i < 100; i++) {
                long id = redisIdWorker.nextId("order");
                System.out.println("id = " + id);
            }
            latch.countDown();
        };
        long begin = System.currentTimeMillis();
        for (int i = 0; i < 300; i++) {
            es.submit(task);
        }
        System.out.println("laizhelile");
        latch.await();
        long end = System.currentTimeMillis();
        System.out.println("time = " + (end - begin));
    }

    @Resource
    private ISeckillVoucherService seckillVoucherService;

    @Test
    void test4() {
     stringRedisTemplate.opsForHyperLogLog();
    }

    @Test
    void testHyperLogLog() {
        String[] values = new String[1000];
        int j = 0;
        for (int i = 0; i < 1000000; i++) {
            j = i % 1000;
            values[j] = "user_" + i;
            if(j == 999){
                // 发送到Redis
                stringRedisTemplate.opsForHyperLogLog().add("hl2", values);
            }
        }
        // 统计数量
        Long count = stringRedisTemplate.opsForHyperLogLog().size("hl2");
        System.out.println("count = " + count);
    }


}
