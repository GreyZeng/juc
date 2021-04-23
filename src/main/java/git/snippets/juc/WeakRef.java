package git.snippets.juc;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

/**
 * 弱引用遭到gc就会回收
 * ThreadLocal应用，缓存应用，WeakHashMap
 */
public class WeakRef {
    public static void main(String[] args) {
        WeakReference<T> reference = new WeakReference<>(new T());
        System.out.println(reference.get());
        System.gc();
        System.out.println(reference.get());


        ThreadLocal<T> tl = new ThreadLocal<>();
        tl.set(new T());
        tl.remove();


        HashMap map = new HashMap();
        T key = new T();
        map.put(key, "2");
        key = null;

        // map.remove(key);
        System.gc();
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(map.size());
    }

    static class T {
        T() {
        }

        @Override
        protected void finalize() {
            System.out.println("finalized");
        }
    }
}
