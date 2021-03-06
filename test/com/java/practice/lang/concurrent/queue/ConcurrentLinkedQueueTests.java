package com.java.practice.lang.concurrent.queue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.concurrent.*;

/**
 * 先进先出FIFO，线程安全，采用CAS操作，来保证元素的一致性。
 */
class ConcurrentLinkedQueueTests {
    /**
     * 定义装苹果的篮子
     */
    public static class Basket {
        // 篮子，能够容纳3个苹果
        ConcurrentLinkedQueue<String> basket = new ConcurrentLinkedQueue<String>();

        // 生产苹果，放入篮子
        void produce() throws InterruptedException {
            // put方法放入一个苹果，若basket满了，等到basket有位置
            basket.add("An apple");
        }

        // 消费苹果，从篮子中取走
        String consume() throws InterruptedException {
            // take方法取出一个苹果，若basket为空，等到basket有苹果为止(获取并移除此队列的头部)
            return basket.remove();
        }
    }

    // 定义苹果生产者
    static class Producer implements Runnable {
        private String instance;
        private Basket basket;

        Producer(String instance, Basket basket) {
            this.instance = instance;
            this.basket = basket;
        }

        public void run() {
            try {
                while (true) {
                    // 生产苹果
                    System.out.println("生产者准备生产苹果：" + instance);
                    basket.produce();
                    System.out.println("!生产者生产苹果完毕：" + instance);
                    // 休眠300ms
                    Thread.sleep(300);
                }
            } catch (InterruptedException ex) {
                System.out.println("Producer Interrupted");
            }
        }
    }

    // 定义苹果消费者
    static class Consumer implements Runnable {
        private String instance;
        private Basket basket;

        Consumer(String instance, Basket basket) {
            this.instance = instance;
            this.basket = basket;
        }

        public void run() {
            try {
                while (true) {
                    // 消费苹果
                    System.out.println("消费者准备消费苹果：" + instance);
                    System.out.println(basket.consume());
                    System.out.println("!消费者消费苹果完毕：" + instance);
                    // 休眠1000ms
                    Thread.sleep(1000);
                }
            } catch (InterruptedException ex) {
                System.out.println("Consumer Interrupted");
            }
        }
    }

    @Test
    @DisplayName("多线程模拟实现生产者／消费者模型")
    void run() {
        // 建立一个装苹果的篮子
        Basket basket = new Basket();
        ExecutorService service = Executors.newCachedThreadPool();
        Producer producer = new Producer("生产者001", basket);
        Producer producer2 = new Producer("生产者002", basket);
        Consumer consumer = new Consumer("消费者001", basket);
        service.submit(producer);
        service.submit(producer2);
        service.submit(consumer);
        try {
            Thread.sleep(1000 * 5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // 等待任务执行完成
        service.shutdown();
        // 马上结束，不等待任务结束
//        service.shutdownNow();
    }
}
