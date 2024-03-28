package com.zhangjingqi;



public class WaitSleepCase {
    static final Object LOCK = new Object();

    public static void main(String[] args) throws  InterruptedException {
//        sleeping();
//        waiting();
        illegalWait();
    }

    private static void illegalWait() throws InterruptedException {
        LOCK.wait();
    }

    private static void waiting() throws InterruptedException {
        Thread t1 = new Thread(() -> {
            synchronized (LOCK) {
                try {
                    System.out.println("waiting.....");
//                    get("t").debug("waiting...");
                    LOCK.wait(5000L);
                } catch (InterruptedException e) {
//                    get("t").debug("interrupted...");
                    e.printStackTrace();
                }
            }
        }, "t1");
        t1.start();
        Thread.sleep(100);
        synchronized (LOCK) {
            System.out.println("other");
//            main.debug("other...");
        }
    }

    private static void sleeping() throws InterruptedException {
        Thread t1 = new Thread(() -> {
            synchronized (LOCK) {
                try {
                    System.out.println("t sleeping....");
//                    get("t").debug("sleeping...");
                    Thread.sleep(5000L);
                } catch (InterruptedException e) {
                    System.out.println("interrupted");
//                    get("t").debug("interrupted...");
                    e.printStackTrace();
                }
            }
        }, "t1");
        t1.start();
        Thread.sleep(100);
        synchronized (LOCK) {
//            main.debug("other...");
            System.out.println("other");
        }
    }
}