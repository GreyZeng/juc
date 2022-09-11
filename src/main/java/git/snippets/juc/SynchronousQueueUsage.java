package git.snippets.juc;

import java.util.concurrent.SynchronousQueue;

/**
 * @author zenghui
 * @date 2020/3/26
 */
public class SynchronousQueueUsage {
    public static void main(String[] args) throws InterruptedException {
        final SynchronousQueue<Integer> queue = new SynchronousQueue<>();

        Thread producer = new Thread(() -> {
            System.out.println("put thread start");
            try {
                queue.put(1);
            } catch (InterruptedException e) {
            }
            System.out.println("put thread end");
        });

        Thread consumer = new Thread(() -> {
            System.out.println("take thread start");
            try {
                System.out.println("take from putThread: " + queue.take());
            } catch (InterruptedException e) {
            }
            System.out.println("take thread end");
        });
        producer.start();
        Thread.sleep(1000);
        consumer.start();
    }
}
