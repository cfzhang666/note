package lock;

import java.util.LinkedList;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 容器
 */
public class Container {

    private int capacity = 3;

    private LinkedList<Integer> list = new LinkedList<Integer>();

    private Lock lock = new ReentrantLock();

    //消费者条件
    private Condition notEmpty = lock.newCondition();

    //生产者条件
    private Condition notFull = lock.newCondition();

    public void put(int i) {
        boolean flag = false;
        try {
            flag = lock.tryLock(3, TimeUnit.SECONDS);
            if (list.size() >= capacity) {
                System.out.println("生产者：" + Thread.currentThread().getName() + "，缓冲区已满,生产者进入waiting...");
                notFull.await();
            }
            System.out.println("生产者生产: " + i + " 线程: " + Thread.currentThread().getName());
            list.add(i);

            //唤醒消费者
            notEmpty.signal();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            if (flag) {
                lock.unlock();
            }
        }
    }

    public void get() {
        boolean flag = false;
        try {
            flag = lock.tryLock(3, TimeUnit.SECONDS);
            if (list.isEmpty()) {
                System.out.println("消费者：" + Thread.currentThread().getName() + "，缓冲区为空,消费者进入waiting...");
                notEmpty.await();
            }
            Integer val = list.removeFirst();
            System.out.println("消费者消费: " + val + " 线程: " + Thread.currentThread().getName());

            //唤醒生产者
            notFull.signal();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            if (flag) {
                lock.unlock();
            }
        }
    }
}
