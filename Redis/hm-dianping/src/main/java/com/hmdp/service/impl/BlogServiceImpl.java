package com.hmdp.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hmdp.dto.Result;
import com.hmdp.dto.UserDTO;
import com.hmdp.entity.Blog;
import com.hmdp.entity.User;
import com.hmdp.mapper.BlogMapper;
import com.hmdp.service.IBlogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hmdp.service.IUserService;
import com.hmdp.utils.SystemConstants;
import com.hmdp.utils.UserHolder;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
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
public class BlogServiceImpl extends ServiceImpl<BlogMapper, Blog> implements IBlogService {
    @Resource
    private IUserService userService;
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public Result queryHotBlog(Integer current) {

        // 根据用户查询
        Page<Blog> page = query()
                .orderByDesc("liked")
                .page(new Page<>(current, SystemConstants.MAX_PAGE_SIZE));
        // 获取当前页数据
        List<Blog> records = page.getRecords();
        // 查询用户
        records.forEach(blog -> {
            Long userId = blog.getUserId();
            User user = userService.getById(userId);
            blog.setName(user.getNickName());
            blog.setIcon(user.getIcon());
//          判断是否被点过赞
            String key = "blog:liked:" + userId;
//          改造为SortedSet
//          TODO bug改造
            UserDTO userDTO = UserHolder.getUser();
//          未登录，直接返回
            if (userDTO==null){
                return;
            }
            Double score = stringRedisTemplate.opsForZSet().score(key, userDTO.getId().toString());
//          score有值的话，下面就是true
            blog.setIsLike(score != null);
        });
        return Result.ok(records);
    }

    @Override
    public Result queryBlogById(Long id) {
//      1.查询blog
        Blog blog = getById(id);
        if (blog == null) {
            return Result.fail("博客不存在");
        }
//      2.查询blog有关的用户
        Long userId = blog.getUserId();
        User user = userService.getById(userId);

        blog.setName(user.getNickName());
        blog.setIcon(user.getIcon());
//      查询Blog是否被点赞了
        String key = "blog:liked:" + id;
//      改造为SortedSet
//      TODO 解决bug
        UserDTO userDTO = UserHolder.getUser();

        if ( userDTO != null){
            Double score = stringRedisTemplate.opsForZSet().score(key, userDTO.getId().toString());
            blog.setIsLike(score != null);
            return Result.ok(blog);
        }
//      运行到这里的话，说明UserDTO是null，那肯定不用去redis查询是否点赞，因为没有登录，怎么可能点赞，直接给false
        blog.setIsLike( false);
        return Result.ok(blog);
    }

    @Override
    public Result likeBlog(Long id) {
//      1.获取登录用户
        Long userId = UserHolder.getUser().getId();
//      2.判断当前登录用户是否已经点赞
//      首先判断一下是否是他的成员,key是笔记id，value是用户id
        String key = "blog:liked:" + id;
//      使用SortedSet判断是否存在
//      Boolean isMember = stringRedisTemplate.opsForSet().isMember(key, userId.toString());
        Double score = stringRedisTemplate.opsForZSet().score(key, userId.toString());

//      3.如果未点赞，可以点赞，使用SortedSet的score进行判断
        if (score == null) {

//          3.1 数据库点赞+1
            boolean isSuccess = update().setSql("liked = liked+1").eq("id", id).update();
//          3.2 保存用户到Redis的set集合
            if (isSuccess) {
//              改为SortedSet,分数score使用时间戳就行
//              保存用户到Redis的SortedSet集合 zadd key value score
                stringRedisTemplate.opsForZSet().add(key, userId.toString(), System.currentTimeMillis());
            }
        } else {
//          4.如果已经点赞，取消点赞
//          4.1 数据库点赞数-1
            boolean isSuccess = update().setSql("liked = liked-1").eq("id", id).update();
//          4.2 把用户从Redis的set集合移除
            if (isSuccess) {
//              改为SortedSet,其他不变
                stringRedisTemplate.opsForZSet().remove(key, userId.toString());
            }
        }
        return Result.ok();
    }

    //  查询点赞Top5
    @Override
    public Result queryBlogLikes(Long id) {
        String key = "blog:liked:" + id;
//      1.查询top5的点赞用户 zrang key 0 4
        Set<String> top5Id = stringRedisTemplate.opsForZSet().range(key, 0, 4);
        if (top5Id == null ||top5Id.isEmpty() ){
            return Result.ok(Collections.emptyList());
        }
//      2.解析出其中的用户id
        List<Long> idList = top5Id.stream().map(Long::valueOf).collect(Collectors.toList());
//      3.根据用户id查询用户
//      TODO 要有顺序 where id IN (5,1) order by FIELD(id,5,1)
//      将List变成字符串，用“，”拼接
        String idStr = StrUtil.join(",", idList);
//      List<User> usersList = userService.listByIds(idList);
//      query().in("id",idList)相当于 id IN (5,1)
        List<User> usersList = userService.query().in("id",idList)
//              last()表示在原有的sql后面进行拼接
                .last("order by FIELD(id,"+idStr+")").list();
        List<UserDTO> userDTOList = new ArrayList<>();
        for (User user : usersList) {
                userDTOList.add(BeanUtil.copyProperties(user,UserDTO.class));
        }
//      4.返回
        return Result.ok(userDTOList);
    }
}
