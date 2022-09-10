package git.snippets.juc;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicStampedReference;

/**
 * @author <a href="mailto:410486047@qq.com">Grey</a>
 * @date 2022/9/10
 * @since
 */
public class ABATest {
    public static void main(String[] args) throws InterruptedException {
        abaCorrect();
    }

    private static void abaCorrect() throws InterruptedException {
        AtomicStampedReference<Integer> ref = new AtomicStampedReference<>(10, 0);
        Thread threadA = new Thread(() -> {
            try {
                int[] stamp = new int[1];
                Integer value = ref.get(stamp); //同时获取时间戳和数据，防止获取到数据和版本不是一致的

                System.out.println(String.format("%s 启动,当前值是:%s,版本:%s", Thread.currentThread().getName(), ref.getReference(), stamp[0]));
                TimeUnit.MILLISECONDS.sleep(1000);

                int newValue = value + 1;
                boolean writeOk = ref.compareAndSet(value, newValue, stamp[0], stamp[0] + 1);

                System.out.println(String.format("%s:%s,%s", Thread.currentThread().getName(), "10->11", writeOk ? stamp[0] + 1 : stamp[0]));
                stamp = new int[1];
                value = ref.get(stamp); //同时获取时间戳和数据，防止获取到数据和版本不是一致的
                newValue = value - 1;
                writeOk = ref.compareAndSet(value, newValue, stamp[0], stamp[0] + 1);
                System.out.println(String.format("%s:%s,%s", Thread.currentThread().getName(), "10->11->10", writeOk ? stamp[0] + 1 : stamp[0]));
            } catch (InterruptedException e) {
            }
        }, "线程A");

        Thread threadB = new Thread(() -> {
            try {
                int[] stamp = new int[1];
                Integer value = ref.get(stamp); //同时获取时间戳和数据，防止获取到数据和版本不是一致的

                System.out.println(String.format("%s 启动,当前值是:%s,版本:%s", Thread.currentThread().getName(), ref.getReference(), stamp[0]));
                TimeUnit.MILLISECONDS.sleep(2000);

                int newValue = value + 2;
                boolean writeOk = ref.compareAndSet(value, newValue, stamp[0], stamp[0] + 1);

                System.out.println(String.format("%s: index是预期的10:%s,新值是:%s,版本:%s", Thread.currentThread().getName(), writeOk, ref.getReference(), writeOk ? stamp[0] + 1 : stamp[0]));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, "线程B");

        threadA.start();
        threadB.start();

        threadA.join();
        threadB.join();
    }

}
