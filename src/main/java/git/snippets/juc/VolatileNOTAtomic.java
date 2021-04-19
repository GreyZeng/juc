package git.snippets.juc;

/**
 * Volatile保持线程之间的可见性(不保证操作的原子性)
 *
 * @author <a href="mailto:410486047@qq.com">Grey</a>
 * @date 2021/4/19
 * @since
 */
public class VolatileNOTAtomic {
    volatile static Data data;

    public static void main(String[] args) {
        Thread writer = new Thread(() -> {
            for (int i = 0; i < 10000; i++) {
                data = new Data(i, i);
            }
        });

        Thread reader = new Thread(() -> {
            while (data == null) {
            }
            int a = data.a;
            int b = data.b;
            if (a != b) {
                // 会出现这种情况是因为new Data(i,i)非原子操作，会产生中间状态的对象，导致a和b的值会不一致
                System.out.printf("a = %s, b=%s%n", a, b);
            }
        });
        writer.start();
        reader.start();
        try {
            writer.join();
            reader.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("end");
    }

    public static class Data {
        int a;
        int b;

        Data(int a, int b) {
            this.a = a;
            this.b = b;
        }
    }
}
