package git.snippets.juc;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedTransferQueue;

public class TransferQueueUsage {
    static LinkedTransferQueue<String> lnkTransQueue = new LinkedTransferQueue<>();

    public static void main(String[] args) {
        ExecutorService exService = Executors.newFixedThreadPool(2);
        Producer producer = new Producer();
        Consumer consumer = new Consumer();
        exService.execute(producer);
        exService.execute(consumer);
        exService.shutdown();
    }

    static class Producer implements Runnable {
        @Override
        public void run() {
            for (int i = 0; i < 3; i++) {
                try {
                    System.out.println("Producer is waiting to transfer...");
                    lnkTransQueue.transfer("A" + i);
                    System.out.println("producer transferred element: A" + i);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    static class Consumer implements Runnable {
        @Override
        public void run() {
            for (int i = 0; i < 3; i++) {
                try {
                    System.out.println("Consumer is waiting to take element...");
                    String s = lnkTransQueue.take();
                    System.out.println("Consumer received Element: " + s);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
