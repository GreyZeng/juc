package git.snippets.juc;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * ConcurrentHashMap写效率未必比HashMap，HashTable高，但是读效率比这两者要高
 */
public class HashTableVSCHM {
    public static final int COUNT = 1000000;
    public static final int THREAD_COUNT = 100;
    static UUID[] keys = new UUID[COUNT];
    static UUID[] values = new UUID[COUNT];

    static {
        for (int i = 0; i < COUNT; i++) {
            keys[i] = UUID.randomUUID();
            values[i] = UUID.randomUUID();
        }
    }

    enum TYPE {
        HASHTABLE, CHM, HASHMAP
    }

    public static Map<UUID, UUID> choose(TYPE type) {
        switch (type) {
            case HASHMAP:
                Collections.synchronizedMap(new HashMap<>());
            case HASHTABLE:
                return new Hashtable<>();
            default:
                return new ConcurrentHashMap<>();
        }
    }

    public static void main(String[] args) {
        System.out.println("...use hashtable....");
        benchmark(choose(TYPE.HASHTABLE));
        System.out.println("...use HashMap....");
        benchmark(choose(TYPE.HASHMAP));
        System.out.println("...use ConcurrentHashMap....");
        benchmark(choose(TYPE.CHM));

    }

    public static void benchmark(Map<UUID, UUID> hashtable) {
        long start = System.currentTimeMillis();
        Thread[] threads = new Thread[THREAD_COUNT];
        for (int i = 0; i < threads.length; i++) {
            threads[i] = new MyThread(i * COUNT / THREAD_COUNT, hashtable);
        }
        Arrays.stream(threads).forEach(thread -> thread.start());
        Arrays.stream(threads).forEach(thread -> {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        long end = System.currentTimeMillis();
        System.out.println("size : " + hashtable.size());
        System.out.println("write cost " + (end - start) + "ms");
        start = System.currentTimeMillis();
        for (int i = 0; i < threads.length; i++) {
            threads[i] = new Thread(() -> {
                for (int j = 0; j < 10000000; j++) {
                    hashtable.get(keys[10]);
                }
            });
        }
        Arrays.stream(threads).forEach(thread -> thread.start());
        Arrays.stream(threads).forEach(thread -> {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        end = System.currentTimeMillis();
        System.out.println("read cost " + (end - start) + "ms");
    }

    static class MyThread extends Thread {
        int start;
        int gap = COUNT / THREAD_COUNT;
        Map<UUID, UUID> map;

        MyThread(int start, Map<UUID, UUID> map) {
            this.start = start;
            this.map = map;
        }

        @Override
        public void run() {
            for (int i = start; i < start + gap; i++) {
                map.put(keys[i], values[i]);
            }
        }
    }
}
