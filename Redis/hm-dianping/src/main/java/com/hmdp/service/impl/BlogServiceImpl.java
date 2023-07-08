package com.hmdp.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hmdp.dto.Result;
import com.hmdp.dto.ScrollResult;
import com.hmdp.dto.UserDTO;
import com.hmdp.entity.Blog;
import com.hmdp.entity.Follow;
import com.hmdp.entity.User;
import com.hmdp.mapper.BlogMapper;
import com.hmdp.service.IBlogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hmdp.service.IFollowService;
import com.hmdp.service.IUserService;
import com.hmdp.utils.SystemConstants;
import com.hmdp.utils.UserHolder;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
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
    @Resource
    private IFollowService followService;


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
            if (userDTO == null) {
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

        if (userDTO != null) {
            Double score = stringRedisTemplate.opsForZSet().score(key, userDTO.getId().toString());
            blog.setIsLike(score != null);
            return Result.ok(blog);
        }
//      运行到这里的话，说明UserDTO是null，那肯定不用去redis查询是否点赞，因为没有登录，怎么可能点赞，直接给false
        blog.setIsLike(false);
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
        if (top5Id == null || top5Id.isEmpty()) {
            return Result.ok(Collections.emptyList());
        }
//      2.解析出其中的用户id
        List<Long> idList = top5Id.stream().map(Long::valueOf).collect(Collectors.toList());
//      3.根据用户id查询用户
//      要有顺序 where id IN (5,1) order by FIELD(id,5,1)
//      将List变成字符串，用“，”拼接
        String idStr = StrUtil.join(",", idList);
//      List<User> usersList = userService.listByIds(idList);
//      query().in("id",idList)相当于 id IN (5,1)
        List<User> usersList = userService.query().in("id", idList)
//              last()表示在原有的sql后面进行拼接
                .last("order by FIELD(id," + idStr + ")").list();
        List<UserDTO> userDTOList = new ArrayList<>();
        for (User user : usersList) {
            userDTOList.add(BeanUtil.copyProperties(user, UserDTO.class));
        }
//      4.返回
        return Result.ok(userDTOList);
    }

    //  保存博客
    @Override
    public Result saveBlog(Blog blog) {
//      1.获取登录用户，也就是执行报错博客功能的用户
        UserDTO user = UserHolder.getUser();
        blog.setUserId(user.getId());
//      2.保存探店博文
        boolean isSuccess = save(blog);
        if (!isSuccess) {
            return Result.fail("新增笔记失败！");
        }
//      3.查询笔记作者的所有粉丝 select * from tb_follow where follow_user_id = ?
        List<Follow> followUserList = followService.query()
                .eq("follow_user_id", user.getId())
                .list();
//      4.推送笔记id给所有粉丝
        for (Follow follow : followUserList) {
//          4.1 获取粉丝id
            Long userId = follow.getUserId();
//          4.2 推送（每个粉丝都有自己的收件箱，每一个收件箱都是一个SortedSet）
            String key = "feed:" + userId;
            stringRedisTemplate.opsForZSet().add(key, blog.getId().toString(), System.currentTimeMillis());
        }
//      5.返回id
        return Result.ok(blog.getId());
    }

    //  实现滚动分页查询
//  查询的是收件箱里的所有笔记，然后做一个滚动查询
    @Override
    public Result queryBlogOfFollow(Long max, Integer offset) {
//      TODO 1.获取当前用户，找到自己的收件箱
        Long userId = UserHolder.getUser().getId();
//      TODO 2.查询收件箱
        String key = "feed:" + userId;
//      ZREVRANGEBYSCORE key max min WHTISCORES LIMIT offset count
        Set<ZSetOperations.TypedTuple<String>> typedTuples = stringRedisTemplate.opsForZSet()
                .reverseRangeByScoreWithScores(key, 0, max, offset, 3);
//      TODO 做非空判断
        if (typedTuples == null || typedTuples.isEmpty()) {
            return Result.ok();
        }
//      TODO 3.解析收件箱：blog.getId()、时间戳score System.currentTimeMillis()、offset偏移量
//      这个时候id是有序的
        List<Long> ids = new ArrayList<>(typedTuples.size());
        long minTime = 0L;
        int os = 1; //就是下一次查询的offset参数
        for (ZSetOperations.TypedTuple<String> tuple : typedTuples) {
//          TODO 3.1 获取id
            String id = tuple.getValue();
            ids.add(Long.valueOf(id));
//          TODO 3.2 获取分数(时间戳)
//          最后一个肯定就是最小
            long time = tuple.getScore().longValue();
            if (time == minTime) {
                os++;
            } else {
                minTime = time;
                os = 1;
            }
        }
//      TODO 4.根据blog的id查询blog
//      List<Blog> blogs = listByIds(ids);如果直接这么执行，id就不是有序的了，in(id1,id2,id3...)
        String idStr = StrUtil.join(",", ids);
        List<Blog> blogs = query().in("id", ids)
//              last()表示在原有的sql后面进行拼接
                .last("order by FIELD(id," + idStr + ")")
                .list();
        for (Blog blog : blogs) {
//          查询blog有关的用户
            queryBlogUser(blog);
//          查询blog是否被点赞
            isBlogLiked(blog);
        }

//      TODO 5.封装并返回
        ScrollResult scrollResult = new ScrollResult();
        scrollResult.setList(blogs);
        scrollResult.setOffset(os);
//      这一次的最小时间就是下一次的最大时间
        scrollResult.setMinTime(minTime);
        return Result.ok(scrollResult);
    }

    //  查询blog有关的用户
    private void queryBlogUser(Blog blog) {
        Long userId = blog.getUserId();
        User user = userService.getById(userId);
        blog.setName(user.getNickName());
        blog.setIcon(user.getIcon());
    }

    //  查询blog是否被点赞
    private void isBlogLiked(Blog blog) {
        // 1.获取登录用户
        UserDTO user = UserHolder.getUser();
        if (user == null) {
            // 用户未登录，无需查询是否点赞
            return;
        }
        Long userId = user.getId();
        // 2.判断当前登录用户是否已经点赞
        String key = "blog:liked:" + blog.getId();
        Double score = stringRedisTemplate.opsForZSet().score(key, userId.toString());
        blog.setIsLike(score != null);
    }

}
