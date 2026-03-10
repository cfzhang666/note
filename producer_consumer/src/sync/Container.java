package sync;

import java.util.LinkedList;

/**
 * 生产者消费者容器
 */
public class Container {

    private int capacity = 3;

    private LinkedList<Integer> list = new LinkedList<Integer>();

    public Container() {
    }

    public synchronized void put(int i) {
        if (list.size() >= capacity) {
            System.out.println("生产者："+ Thread.currentThread().getName()+"，缓冲区已满,生产者进入waiting...");
            try {
                this.wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        System.out.println("生产者生产: " + i + " 线程: " + Thread.currentThread().getName());
        list.add(i);
        this.notifyAll();
    }

    public synchronized void get() {
        if (list.isEmpty()) {
            System.out.println("消费者："+ Thread.currentThread().getName()+"，缓冲区为空,消费者进入waiting...");
            try {
                this.wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        Integer val = list.removeFirst();
        System.out.println("消费者消费: " + val + " 线程: " + Thread.currentThread().getName());
        this.notifyAll();
    }
}
