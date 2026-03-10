package cn.cfzhang.ps.sync;

/**
 * 消费者
 */
public class Consumer extends Thread {

    private Container container;

    public Consumer(Container container) {
        this.container = container;
    }

    @Override
    public void run() {
        for (int i = 0; i < 6; i++) {
            container.get();
        }
    }
}
