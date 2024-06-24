package com.zhangjingqi;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadPoolExample {
    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(5); // 创建一个容量为 5 的线程池
        for (int i = 0; i < 10; i++) {
            executorService.execute(new Task(i)); // 提交任务，由线程池中空闲的线程执行
        }
        executorService.shutdown(); // 调用任务完成，关闭线程池
    }

    static class Task implements Runnable {
        private int taskNum;

        public Task(int num) {
            this.taskNum = num;
        }

        @Override
        public void run() { // 线程池中的线程会调用该方法进行具体任务的执行
            System.out.println("正在执行task " + taskNum);
            try {
                Thread.sleep(1000); // 模拟任务耗时
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("task " + taskNum + " 执行完毕");
        }
    }
}