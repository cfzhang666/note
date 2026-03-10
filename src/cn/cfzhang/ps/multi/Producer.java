package cn.cfzhang.ps.multi;

public class Producer extends Thread {

    private Container container;

    private Integer val;

    public Producer(Container container, Integer val) {
        this.container = container;
        this.val = val;
    }

    @Override
    public void run() {
        container.put(val);
    }
}
