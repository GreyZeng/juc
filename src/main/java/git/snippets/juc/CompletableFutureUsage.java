package git.snippets.juc;

import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * 假设你能够提供一个服务
 * 这个服务查询各大电商网站同一类产品的价格并汇总展示
 */
public class CompletableFutureUsage {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        way1();
        way2();
    }

    public static void way1() {
        long start = System.currentTimeMillis();
        System.out.println("p1 " + priceOfJD());
        System.out.println("p2 " + priceOfTB());
        System.out.println("p3 " + priceOfTM());
        long end = System.currentTimeMillis();
        System.out.println("串行执行，耗时(ms):" + (end - start));
    }

    public static void way2() throws ExecutionException, InterruptedException {
        long start = System.currentTimeMillis();
        CompletableFuture<Double> p1 = CompletableFuture.supplyAsync(() -> priceOfJD());
        CompletableFuture<Double> p2 = CompletableFuture.supplyAsync(() -> priceOfTB());
        CompletableFuture<Double> p3 = CompletableFuture.supplyAsync(() -> priceOfTM());
        CompletableFuture.allOf(p1, p2, p3).join();
        System.out.println("p1 " + p1.get());
        System.out.println("p2 " + p2.get());
        System.out.println("p3 " + p3.get());
        long end = System.currentTimeMillis();
        System.out.println("使用CompletableFuture并行执行，耗时(ms): " + (end - start));
    }

    private static double priceOfTM() {
        delay();
        return 1.00;
    }

    private static double priceOfTB() {
        delay();
        return 2.00;
    }

    private static double priceOfJD() {
        delay();
        return 3.00;
    }

    /*private static double priceOfAmazon() {
        delay();
        throw new RuntimeException("product not exist!");
    }*/

    private static void delay() {
        int time = new Random().nextInt(500);
        try {
            TimeUnit.MILLISECONDS.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // System.out.printf("After %s sleep!\n", time);
    }
}
