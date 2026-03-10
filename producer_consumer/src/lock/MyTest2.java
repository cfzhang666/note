package lock;


public class MyTest2 {
    public static void main(String[] args) {

        Container container = new Container();
        new Producer(container).start();
        new Consumer(container).start();
    }
}
