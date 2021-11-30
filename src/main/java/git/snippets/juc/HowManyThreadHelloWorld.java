package git.snippets.juc;

import java.util.Map;

/**
 * 一个Hello World程序启动了几个线程
 * 1.8下和11下线程数量不一样
 *
 * @author <a href="mailto:410486047@qq.com">Grey</a>
 * @date 2021/11/30
 * @since 1.8
 */
public class HowManyThreadHelloWorld {


    public static void main(String[] args) {
        Thread t = Thread.currentThread();
        System.out.println("\n线程：" + t.getName() + "\n");
        System.out.println("hello world!");

        for (Map.Entry<Thread, StackTraceElement[]> entry : Thread.getAllStackTraces().entrySet()) {
            Thread thread = entry.getKey();

            StackTraceElement[] stackTraceElements = entry.getValue();

            if (thread.equals(Thread.currentThread())) {
                continue;
            }

            System.out.println("\n线程： " + thread.getName() + "\n");
            for (StackTraceElement element : stackTraceElements) {
                System.out.println("\t" + element + "\n");
            }
        }
    }
}
