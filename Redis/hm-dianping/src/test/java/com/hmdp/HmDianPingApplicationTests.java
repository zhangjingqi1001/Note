package com.hmdp;

import com.hmdp.service.ISeckillVoucherService;
import com.hmdp.service.impl.ShopServiceImpl;
import com.hmdp.utils.RedisIdWorker;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;

import javax.annotation.Resource;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
    void test4(){

    }


}
