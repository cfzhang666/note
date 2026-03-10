package producer_consumer.multi;


import java.util.ArrayList;
import java.util.List;

public class MyTest3 {
    public static void main(String[] args) {

        List<Thread> threads = new ArrayList<>();
        Container container = new Container();

        for (int i = 0; i < 6; i++) {
            threads.add(new Producer(container, i));
        }

        for (int i = 0; i < 6; i++) {
            threads.add(new Consumer(container));
        }

        for (Thread thread : threads) {
            thread.start();
        }
    }
}
