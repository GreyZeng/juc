package git.snippets.juc;

import java.text.DecimalFormat;
import java.util.Random;
import java.util.concurrent.CountDownLatch;

/**
 * 多线程求1亿个Double类型的数据
 *
 * @author <a href="mailto:410486047@qq.com">Grey</a>
 * @date 2021/7/7
 * @since
 */
public class CountSum {
    private static final double[] NUMS = new double[1_0000_0000];
    private static final Random R = new Random();
    private static final DecimalFormat FORMAT = new DecimalFormat("0.00");

    static {
        for (int i = 0; i < NUMS.length; i++) {
            NUMS[i] = R.nextDouble();
        }
    }

    public static void rand() {
        for (int i = 0; i < NUMS.length; i++) {
            NUMS[i] = R.nextDouble();
        }
    }

    /**
     * 单线程计算一亿个Double类型的数据之和
     *
     * @return
     */
    public static String m1() {
        long start = System.currentTimeMillis();
        double result = 0.0;
        for (double num : NUMS) {
            result += num;
        }
        long end = System.currentTimeMillis();
        System.out.println("计算1亿个随机Double类型数据之和[单线程], 结果是：result = " + FORMAT.format(result) + " 耗时 : " + (end - start) + "ms");
        return String.valueOf(FORMAT.format(result));
    }


    static double result1 = 0.0, result2 = 0.0, result = 0.0;

    /**
     * 两个线程计算一亿个Double类型的数据之和
     *
     * @return
     */
    private static String m2() throws Exception {
        long start = System.currentTimeMillis();
        result1 = 0.0;
        result2 = 0.0;
        int len = (NUMS.length >> 1);
        Thread t1 = new Thread(() -> {
            for (int i = 0; i < len; i++) {
                result1 += NUMS[i];
            }
        });
        Thread t2 = new Thread(() -> {
            for (int i = len; i < NUMS.length; i++) {
                result2 += NUMS[i];
            }
        });

        t1.start();
        t2.start();
        t1.join();
        t2.join();

        result = result1 + result2;
        long end = System.currentTimeMillis();
        System.out.println("计算1亿个随机Double类型数据之和[2个线程], 结果是：result = " + FORMAT.format(result) + " 耗时 : " + (end - start) + "ms");
        return String.valueOf(FORMAT.format(result));
    }

    private static String m3() throws Exception {
        long start = System.currentTimeMillis();
        final int threadCount = 10;
        Thread[] threads = new Thread[threadCount];
        double[] results = new double[threadCount];

        final int segmentCount = NUMS.length / threadCount;
        CountDownLatch latch = new CountDownLatch(threadCount);
        for (int i = 0; i < threadCount; i++) {
            int m = i;
            threads[i] = new Thread(() -> {
                for (int j = m * segmentCount; j < (m + 1) * segmentCount && j < NUMS.length; j++) {
                    results[m] += NUMS[j];
                }
                latch.countDown();
            });

        }
        double resultM3 = 0.0;

        for (Thread t : threads) {
            t.start();
        }
        latch.await();
        for (double v : results) {
            resultM3 += v;
        }

        long end = System.currentTimeMillis();
        System.out.println("计算1亿个随机Double类型数据之和[10个线程], 结果是：result = " + FORMAT.format(resultM3) + " 耗时 : " + (end - start) + "ms");
        return String.valueOf(FORMAT.format(resultM3));
    }

    public static void main(String[] args) throws Exception {
        int testCount = 10;
        boolean correct = true;
        for (int i = 0; i < testCount; i++) {
            rand();
            String s = m1();
            String s1 = m2();
            String s2 = m3();
            if (!s1.equals(s2) || !s1.equals(s)) {
                System.out.println("oops!");
                System.out.println(s1);
                System.out.println(s2);
                System.out.println(s);
                correct = false;
                break;
            }
        }
        if (correct) {
            System.out.println("test finished");
        }
    }
}
