package producer_consumer.multi;

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

    private LinkedList<Integer> list = new LinkedList<>();

    private Lock lock = new ReentrantLock();

    //消费者条件
    private Condition notEmpty = lock.newCondition();

    //生产者条件
    private Condition notFull = lock.newCondition();

    public void put(int i) {
        boolean flag = false;
        try {
            flag = lock.tryLock(3, TimeUnit.SECONDS);
            while (list.size() >= capacity) {
                System.out.println("生产者："+ Thread.currentThread().getName()+"，缓冲区已满,生产者进入waiting...");
                notFull.await();
            }
            System.out.println("生产者："+ Thread.currentThread().getName()+"，add：" + i);
            list.add(i);

            //唤醒消费者线程
            notEmpty.signalAll();
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
            while (list.isEmpty()) {
                System.out.println("消费者：" + Thread.currentThread().getName() + "，缓冲区为空,消费者进入waiting...");
                notEmpty.await();
            }
            Integer val = list.removeFirst();
            System.out.println("消费者："+ Thread.currentThread().getName()+"，value：" + val);

            //唤醒生产者线程
            notFull.signalAll();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            if (flag) {
                lock.unlock();
            }
        }
    }
}

/**
 * java.util.NoSuchElementException
 *     at java.util.LinkedList.removeFirst(LinkedList.java:270)
 *     at cn.cfzhang.ps.multi.Container.get(Container.java:56)
 *     at cn.cfzhang.ps.multi.Consumer.run(Consumer.java:18)
 *
 * ● 问题原因：这是使用 Condition 时的经典 bug。
 *                    ↓
 *   当调用 notEmpty.signalAll() 时，所有等待的消费者线程都会被唤醒。但是只有一个生产者放入了一个元素，所以只有一个消费者能  成功消费。其他被唤醒的消费者没有重新检查列表是否为空就直接调用 list.removeFirst()，导致 NoSuchElementException。
 *   修复方法：将 if 改为 while，这样被唤醒的线程会重新检查条件：                                                                                                                                                                                rt
 *   - get(): while (list.isEmpty()) - 消费者唤醒后重新检查列表是否为空
 *   - put(): while (list.size() >= capacity) - 生产者唤醒后重新检查列表是否已满
 *
 *   这是 condition variable 使用的标准模式，可以防止"虚假唤醒"（spurious wakeup）和多线程竞争问题。
 */
