package git.snippets.juc;

import java.util.concurrent.SynchronousQueue;

/**
 * @author zenghui
 * @date 2020/3/26
 */
public class SynchronousQueueUsage {
    public static void main(String[] args) throws InterruptedException {
        SynchronousQueue<String> queue = new SynchronousQueue<>();
        new Thread(() -> {
            try {
                System.out.println(queue.take());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
        queue.put("aa");
        System.out.println(queue.size());
    }
}
