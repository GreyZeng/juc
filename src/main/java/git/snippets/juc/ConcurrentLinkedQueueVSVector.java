package git.snippets.juc;

import java.util.Vector;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * ConcurrentLinkedQueue底层用的是CAS操作。比Vector效率高，
 *
 * @author Grey
 */
public class ConcurrentLinkedQueueVSVector {


    public static void main(String[] args) {
        ConcurrentLinkedQueue<String> tickets = new ConcurrentLinkedQueue<>();
        Vector<String> tickets2 = new Vector<>();
        for (int i = 0; i < 10000; i++) {
            tickets.add("票编号：" + i);
            tickets2.add("票编号：" + i);
        }
        useConcurrentLinkedQueue(tickets);
        useVector(tickets2);
    }


    public static void useConcurrentLinkedQueue(ConcurrentLinkedQueue<String> tickets) {
        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                while (true) {
                    String s = tickets.poll();
                    if (s == null) {
                        break;
                    } else {
                        System.out.println("销售了--" + s);
                    }
                }
            }).start();
        }
    }


    public static void useVector(Vector<String> tickets) {
        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                while (tickets.size() > 0) {
                    System.out.println("销售了--" + tickets.remove(0));
                }
            }).start();
        }
    }
}
