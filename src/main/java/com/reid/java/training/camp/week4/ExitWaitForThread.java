package com.reid.java.training.camp.week4;


import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReentrantLock;

public class ExitWaitForThread {

    /**
     * 1. 使用线程的join方法
     */
    public static void useJoin() throws InterruptedException {
        Thread thread = new Thread(() -> {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("thread down");
        });
        thread.start();
        thread.join();

        System.out.println("main down");
    }

    /**
     * 2. 使用变量控制
     */
    private volatile static boolean exit = false;
    public static void useVariable() throws InterruptedException {
        Thread thread = new Thread(() -> {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            exit = true;
            System.out.println("thread down");
        });
        thread.start();

        while (!exit) {
            Thread.sleep(500);
        }
        System.out.println("main down");
    }

    /**
     * 3. 使用Synchronized关键字
     */
    public static void useSynchronized() throws InterruptedException {
        Object monitor = new Object();
        Thread thread = new Thread(() -> {
            synchronized (monitor) {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("thread down");
            }
        });
        thread.start();

        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        synchronized (monitor) {
            System.out.println("main down");
        }
    }

    /**
     * 4. sleep
     */
    public static void useSleep() throws InterruptedException {
        Thread thread = new Thread(() -> {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("thread down");
        });
        thread.start();

        try {
            Thread.sleep(2100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("main down");
    }

    /**
     * 5. wait/notify
     */
    public static void useWaitNotify() throws InterruptedException {
        Object monitor = new Object();
        Thread thread = new Thread(() -> {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            synchronized (monitor) {
                monitor.notifyAll();
            }
            System.out.println("thread down");
        });
        thread.start();

        synchronized (monitor) {
            monitor.wait();
        }
        System.out.println("main down");
    }

    /**
     * 6. 使用Lock
     */
    public static void useLock() throws InterruptedException {
        ReentrantLock reentrantLock = new ReentrantLock();
        Thread thread = new Thread(() -> {
            reentrantLock.lock();
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                reentrantLock.unlock();
            }
            System.out.println("thread down");
        });
        thread.start();

        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        reentrantLock.lock();
        System.out.println("main down");
        reentrantLock.unlock();
    }

    /**
     * 7. CountDownLatch
     * @throws InterruptedException
     */
    public static void useCountDownLatch() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(1);

        Thread thread = new Thread(() -> {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("thread down");
            countDownLatch.countDown();
        });
        thread.start();

        countDownLatch.await();
        System.out.println("main down");
    }

    /**
     * 8. CyclicBarrier
     * @throws InterruptedException
     */
    public static void useCyclicBarrier() throws InterruptedException, BrokenBarrierException {
        CyclicBarrier cyclicBarrier = new CyclicBarrier(2);

        Thread thread = new Thread(() -> {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("thread down");
            try {
                cyclicBarrier.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (BrokenBarrierException e) {
                e.printStackTrace();
            }
        });
        thread.start();

        cyclicBarrier.await();
        System.out.println("main down");
    }

    /**
     * 9. 信号量
     */
    public static void useSemaphore() throws InterruptedException {
        Semaphore semaphore = new Semaphore(1);

        Thread thread = new Thread(() -> {
            try {
                semaphore.acquire();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("thread down");
            semaphore.release();
        });
        thread.start();

        Thread.sleep(10);
        semaphore.acquire();
        System.out.println("main down");
        semaphore.release();
    }

    /**
     * 10. 阻塞队列
     */
    public static void useBlockQueue() throws InterruptedException {
        ArrayBlockingQueue arrayBlockingQueue = new ArrayBlockingQueue(10);

        Thread thread = new Thread(() -> {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("thread down");
            arrayBlockingQueue.add(new Object());
        });
        thread.start();

        arrayBlockingQueue.take();
        System.out.println("main down");
    }

    /**
     * 11. CAS操作
     */
    public static class CasObject {

        public int sign = 0;

    }
    private static Unsafe reflectGetUnsafe() throws NoSuchFieldException, IllegalAccessException {
        Field field = Unsafe.class.getDeclaredField("theUnsafe");
        field.setAccessible(true);
        return (Unsafe) field.get(null);
    }
    public static void useCAS() throws InterruptedException, NoSuchFieldException, IllegalAccessException {
        CasObject casObject = new CasObject();
        Unsafe unsafe = reflectGetUnsafe();
        long offset = unsafe.objectFieldOffset(CasObject.class.getField("sign"));

        Thread thread = new Thread(() -> {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("thread down");
            unsafe.compareAndSwapInt(casObject, offset, 0, 1);
        });
        thread.start();

        while (unsafe.getInt(casObject, offset) == 0) {
            Thread.sleep(100);
        }
        System.out.println("main down");
    }

    /**
     * 12. Atomic*原子类
     */
    public static void useAtomic() throws InterruptedException {
        AtomicBoolean atomicBoolean = new AtomicBoolean(false);
        Thread thread = new Thread(() -> {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("thread down");
            atomicBoolean.compareAndSet(false, true);
        });
        thread.start();

        while (!atomicBoolean.get()) {
            Thread.sleep(100);
        }
        System.out.println("main down");
    }

    public static void main(String[] args) throws InterruptedException, BrokenBarrierException, NoSuchFieldException, IllegalAccessException {
        useCAS();
    }
}
