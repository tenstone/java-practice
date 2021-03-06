package com.java.practice.lang.concurrent.multithread;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.concurrent.*;

/**
 * 系统需要处理非常多的执行时间很短的请求，如果每一个请求都开启一个新线程的话，系统就要不断的进行线程的创建和销毁，
 * 有时花在创建和销毁线程上的时间会比线程真正执行的时间还长。而且当线程数量太多时，系统不一定能受得了
 * <p>
 * Executor是一个接口，与线程池有关基本都要预期打交道
 */
class ExecutorsTests {


    @Test
    @DisplayName("创建线程池的靠谱方法")
    void ThreadPoolExecutorTest() {

        BlockingQueue workQueue = new SynchronousQueue<Runnable>();
        /**
         * Executors工厂内部封装了此方法
         */
        ExecutorService service = new ThreadPoolExecutor(
                1, // 核心线程数
                2, // 最大线程数
                30000, // 线程终结前，等待新任务的时间
                TimeUnit.SECONDS, // 时间单位
                workQueue  // 任务队列，挂起时用于execute提交的任务
        );
        service.shutdown();
    }


    /**
     * 以下为Executors工厂创建线程池的方法
     * 存在OOM的风险 OutOfMemory
     */
    @Test
    @DisplayName("创建固定数目线程的线程池")
    void FixedThreadPoolTest() throws InterruptedException {
        // 此处如果nThreads数量为1，则不会打印hello2
        ExecutorService service = Executors.newFixedThreadPool(1);
        service.execute(() -> {
            while (true) {
                System.out.println("hello1");
            }
        });
        service.execute(() -> {
            while (true) {
                System.out.println("hello2");
            }
        });
        TimeUnit.MILLISECONDS.sleep(1);
        service.shutdown();
    }

    @Test
    @DisplayName("可以看成newFixedThreadPool(无穷大), 线程复用")
    void CachedThreadPoolTest() throws InterruptedException {
        ExecutorService service = Executors.newCachedThreadPool();
        service.execute(() -> {
            while (true) {
                System.out.println("hello1");
            }
        });
        service.execute(() -> {
            while (true) {
                System.out.println("hello2");
            }
        });
        TimeUnit.MILLISECONDS.sleep(1);
        service.shutdown();
    }

    @Test
    @DisplayName("创建一个单线程化的Executor")
    void SingleThreadExecutorTest() throws InterruptedException {
        // 此处如果nThreads数量为1，则不会打印hello2
        ExecutorService service = Executors.newFixedThreadPool(1);
        service.execute(() -> {
            while (true) {
                System.out.println("hello1");
            }
        });
        service.execute(() -> {
            while (true) {
                System.out.println("hello2");
            }
        });
        TimeUnit.MILLISECONDS.sleep(1);
        service.shutdown();
    }

    /**
     * 用线程执行定时任务
     */
    @Test
    @DisplayName("创建一个支持定时及周期性的任务执行的线程池，多数情况下可用来替代Timer类")
    void ScheduledThreadPool() throws InterruptedException {
        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);
        scheduledExecutorService.scheduleAtFixedRate(() -> {
            System.out.println("test1");
        }, 1, 1, TimeUnit.SECONDS);
        Thread.sleep(1200);
    }

    // jdk8 新增一个线程池
}
