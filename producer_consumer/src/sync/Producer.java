package sync;

/**
 * 生产者
 */
public class Producer extends Thread {

    private Container container;

    public Producer(Container container) {
        this.container = container;
    }

    @Override
    public void run() {
        for (int i = 0; i < 6; i++) {
            container.put(i);
        }
    }
}
