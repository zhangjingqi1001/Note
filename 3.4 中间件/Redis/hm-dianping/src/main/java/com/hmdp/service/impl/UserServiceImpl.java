package com.hmdp.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hmdp.dto.LoginFormDTO;
import com.hmdp.dto.Result;
import com.hmdp.dto.UserDTO;
import com.hmdp.entity.User;
import com.hmdp.mapper.UserMapper;
import com.hmdp.service.IUserService;
import com.hmdp.utils.RegexUtils;
import com.hmdp.utils.UserHolder;
import org.springframework.cglib.core.Local;
import org.springframework.data.redis.connection.BitFieldSubCommands;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author 虎哥
 * @since 2021-12-22
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public Result sendCode(String phone, HttpSession session) {
//      1.校验手机号
        if (RegexUtils.isPhoneInvalid(phone)) {
//              说明：RegexUtils使我们封装的一个类   isCodeInvalid是里面的静态方法，在这个静态方法里面又调用了另外一个静态方法得以实现
//      2.如果不符合，返回错误信息
            return Result.fail("手机号格式错误");
        }

//      3.符合，生成验证码    6代表生成的验证码的长度  RandomUtil使用这个工具类生成
        String code = RandomUtil.randomNumbers(6);
//      4.保存验证码到Redis       key必须是一个字符串，value是一个对象
//        stringRedisTemplate.opsForValue().set(phone,code);
        stringRedisTemplate.opsForValue().set("login:code:" + phone, code, 2, TimeUnit.MINUTES);

//      5.发送验证码
//        实现起来比较麻烦 我们使用日志假装发送
        log.debug("发送短信验证码成功，验证码：" + code);
        return Result.ok();
    }

    private static String USER_NICK_NAME_PREFIX = "user_";

    /**
     * 实现用户登录
     *
     * @param loginForm 登录的参数
     * @param session
     * @return
     */
    @Override
    public Result login(LoginFormDTO loginForm, HttpSession session) {
//      1.校验手机号
        if (RegexUtils.isPhoneInvalid(loginForm.getPhone())) {
//              说明：RegexUtils使我们封装的一个类   isCodeInvalid是里面的静态方法，在这个静态方法里面又调用了另外一个静态方法得以实现
//         1.2.如果不符合，返回错误信息
            return Result.fail("手机号格式错误");
        }
//     2.校验验证码
//      2.1 从Redis中获取真正正确的验证码
        String cacheCode = stringRedisTemplate.opsForValue().get("login:code:" + loginForm.getPhone());
//      2.2 获取用户输入的code
        String code = loginForm.getCode();
        if (cacheCode == null || !cacheCode.equals(code)) {
//3.不一致，报错
            return Result.fail("验证码错误");
        }

//4.一致，根据手机号查询用户  .one()代表查询一个  list()代表着查询多个
        User user = query().eq("phone", loginForm.getPhone()).one();

//5.判断用户是否存在
        if (user == null) {
//6.不存在，创建新用户并保存
            user = createUserWithPhone(loginForm.getPhone());
        }

//7.保存用户信息到Redis中
//      7.1 随机生成Token，作为登录令牌  我们使用import cn.hutool.core.lang.UUID;
//             true代表着不加中划线
        String token = UUID.randomUUID().toString(true);
//      7.2 将User对象转为HashMap存储
//             BeanUtil.copyProperties(user, UserDTO.class))  会自动的将user中的属性拷贝到UserDTO当中而且也创建出一个UserDTO对象
//             将User转化为UserDTO是为了提高数据的保密性，User中有各种信息都会返回到前端，这样很不友好
        UserDTO userDTO = BeanUtil.copyProperties(user, UserDTO.class);
//             转化为Map集合的时候，确保每一个都是String，否则就会出现异常！！！！！！！
//        new HashMap<>() 是一个空的map集合
//        setIgnoreNullValue(true) 忽略空值   setFieldValueEditor这个就是我们所需要的设置字段值，把每一个字段值都设置成Value属性
        Map<String, Object> userMap = BeanUtil.beanToMap(userDTO, new HashMap<>(),
                CopyOptions.create().setIgnoreNullValue(true)
                        .setFieldValueEditor((fieldName, fieldValue) -> fieldValue.toString()));
//      7.3 存储并设置有效期(session有效期是30分钟，超过30分钟不访问便被剔除，这里我们也可以设置30分钟)
//          put是一个一个的加不是很适合，这里使用putAll，一次性存入
        stringRedisTemplate.opsForHash().putAll("login:token:" + token, userMap);
//          这个地方存储的时候是不能设置有效期的，我们只能先存储再设置有效期
//      7.4  设置有效期(但是这个地方是到了30分钟就剔除，但是session不是这个样子的，剩下的操作我们需要在拦截器中设置）
        stringRedisTemplate.expire("login:token:" + token, 30, TimeUnit.MINUTES);

//      8. 返回token
        return Result.ok(token);
    }


    private User createUserWithPhone(String phone) {
//      1.创建用户
        User user = new User();
        user.setPhone(phone);
//       USER_NICK_NAME_PREFIX其实就是 "user_"，这样写更有逼格
        user.setNickName(USER_NICK_NAME_PREFIX + RandomUtil.randomString(10));
//        保存用户
        save(user);

        return user;
    }

    //  用户签到功能
    @Override
    public Result sign() {
//      TODO 1.获取当前登录用户
        Long userId = UserHolder.getUser().getId();

//      TODO 2.获取日期
        LocalDateTime now = LocalDateTime.now();

//      TODO 3.拼接key
        String keySuffix = now.format(DateTimeFormatter.ofPattern(":yyyyMM"));
        String key = "sign:" + userId + keySuffix;
//      TODO 4.今天是本月第几天，就向那个bit位存值
        int dayOfMonth = now.getDayOfMonth();
//      TODO 5.写入Redis SETBIT key offset 0/1
        stringRedisTemplate.opsForValue().setBit(key, dayOfMonth - 1, true);
        return Result.ok();
    }

    //  获取连续签到天数
    @Override
    public Result signCount() {

//      1.获取当前登录用户
        Long userId = UserHolder.getUser().getId();
//      2.获取日期
        LocalDateTime now = LocalDateTime.now();
//      3.拼接key
        String keySuffix = now.format(DateTimeFormatter.ofPattern(":yyyyMM"));
        String key = "sign:" + userId + keySuffix;
//      4.今天是本月第几天，就向那个bit位存值
        int dayOfMonth = now.getDayOfMonth();
//      TODO 5.获取本月截止今天为止的所有签到记录(返回的是一个十进制的数字)
//      因为可以同时执行查询、修改、自增功能，那这样的话返回值也会有多个，所以最终是一个list集合
        List<Long> result = stringRedisTemplate.opsForValue().bitField(key,
//              BitFieldSubCommands.create() 创建子命令
                BitFieldSubCommands.create()
//                      unsigned无符号， dayOfMonth表示截取多少bit位
                        .get(BitFieldSubCommands.BitFieldType.unsigned(dayOfMonth))
//                       表示从0开始查
                        .valueAt(0)
        );
        if (result == null || result.isEmpty()) {
            return Result.ok(0);
        }
//      我们这只执行了查询，所以集合中只有一个元素
        Long num = result.get(0);
        if (num == null || num == 0) {
            return Result.ok(0);
        }
//      TODO 6.循环遍历
        int count =0;
        while (true) {
//          TODO 6.1 数字与1做与运算，得到数字的最后一个bit位
//          TODO 6.2 判断这个bit位是否为0
            if ((num & 1) == 0) {
//              TODO 6.3如果为0，说明未签到，结束
                break;
            } else {
//             TODO 6.4如果不为0，说明已签到，计数器+1
               count++;
            }

//          TODO 6.5把数字右移动一位，抛弃最后一个bit位，继续下一个bit位
//          先右移一位，在赋值给num
            num >>>=1;
        }

        return Result.ok(count);
    }


}

