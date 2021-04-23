package git.snippets.juc;

import java.io.IOException;
import java.lang.ref.SoftReference;
import java.util.concurrent.TimeUnit;

/**
 * heap将装不下，这时候系统会垃圾回收，先回收一次，如果不够，会把软引用干掉
 * 软引用，适合做缓存
 * 示例需要把Vm options设置为:-Xms20M -Xmx20M
 */
public class SoftRef {
    public static void main(String[] args) throws IOException {
        SoftReference<byte[]> reference = new SoftReference<>(new byte[1024 * 1024 * 10]);
        System.out.println(reference.get());
        System.gc();
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println(reference.get());

        byte[] bytes = new byte[1024 * 1024 * 10];

        System.out.println(reference.get());
        System.in.read();
    }
}
