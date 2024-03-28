package com.hmdp.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.QueryChainWrapper;
import com.hmdp.dto.Result;
import com.hmdp.dto.UserDTO;
import com.hmdp.entity.Follow;
import com.hmdp.entity.User;
import com.hmdp.mapper.FollowMapper;
import com.hmdp.service.IFollowService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hmdp.service.IUserService;
import com.hmdp.utils.UserHolder;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author 虎哥
 * @since 2021-12-22
 */
@Service
public class FollowServiceImpl extends ServiceImpl<FollowMapper, Follow> implements IFollowService {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private IUserService userService;

//  关注和取消关注
    @Override
    public Result follow(Long followUserId, Boolean isFollow) {
//      1.获取登录用户
        Long userId = UserHolder.getUser().getId();
        String key = "follows:" + userId;
//      2.判断关注还是取关
        if (isFollow) {
//          2.1 关注，新增数据
            Follow follow = new Follow();
            follow.setUserId(userId);
            follow.setFollowUserId(followUserId);
//          TODO 操作数据库之后也要操作Redis，将关注列表放入到Redis
            boolean isSuccess = save(follow);
            if (isSuccess) {
//              把关注用户的id，让如redis的set集合 sadd userId followerUserId
                stringRedisTemplate.opsForSet().add(key, followUserId.toString());
            }
        } else {
//          2.2取关，删除 delete from tb_follow where userId=? and follow_user_id=?
            boolean isSuccess = remove(new QueryWrapper<Follow>()
                    .eq("user_id", userId)
                    .eq("follow_user_id", followUserId)
            );
//          TODO 取关的时候也从Redis中移除
            if (isSuccess) {
//              把关注用户的id，让如redis的set集合 sadd userId followerUserId
                stringRedisTemplate.opsForSet().remove(key, followUserId.toString());
            }
        }
//      3.返回结果
        return Result.ok();
    }

    //  查询是否关注
    @Override
    public Result isFollow(Long followUserId) {
//      1.获取登录用户
        Long userId = UserHolder.getUser().getId();
//      2.查询是否关注
//      select from tb_follow where userId=? and follow_user_id=?
        Integer count = query().eq("user_id", userId)
                .eq("follow_user_id", followUserId).count();
//      3.验证是否关注
//      大于0返回true，反之false
        return Result.ok(count > 0);
    }

    //  查看共同关注
    @Override
    public Result followCommons(Long id) {
//      TODO 1.获取当前用户
        Long userId = UserHolder.getUser().getId();
        String thisUserKey = "follows:" + userId;
        String targetUserKey = "follows:" + id;
//      TODO 2.求交集
        Set<String> intersect = stringRedisTemplate.opsForSet().intersect(thisUserKey, targetUserKey);
        if (intersect == null || intersect.isEmpty()) {
           return   Result.ok(Collections.emptyList());
        }
//      TODO 3.解析出ID
        List<Long> longList = intersect.stream().map(Long::valueOf).collect(Collectors.toList());
//      TODO 4.查询用户并转换成DTO
        List<UserDTO> userDTOList = userService.listByIds(longList)
                .stream()
                .map(user -> BeanUtil.copyProperties(user, UserDTO.class))
                .collect(Collectors.toList());
//      TODO 5.返回结果
        return Result.ok(userDTOList);
    }
}
