package producer_consumer.sync;

public class MyTest1 {
    public static void main(String[] args) {

        Container container = new Container();
        new Producer(container).start();
        new Consumer(container).start();

    }
}
