package git.snippets.juc;


import java.util.Scanner;
import java.util.concurrent.TimeUnit;
/**
 * 并发编程三大特性之:可见性
 * @author <a href="mailto:410486047@qq.com">Grey</a>
 * @since 1.8
 */
public class ThreadVisible {
    
    static volatile   boolean  flag = true;
    public static void main(String[] args) throws InterruptedException {
        Thread t = new Thread(()->{
            while(flag) {
                // 如果这里调用了System.out.println()
                // 会无论flag有没有加volatile,数据都会同步
                // 因为System.out.println()背后调用的synchronized
                // System.out.println();
            }
            System.out.println("t end");
        });
        t.start();
        TimeUnit.SECONDS.sleep(3);
        flag = false;


        // volatile修饰引用变量
        new Thread(a::m,"t2").start();
        TimeUnit.SECONDS.sleep(2);
        a.flag = false;

        // 阻塞主线程,防止主线程直接执行完毕,看不到效果
        new Scanner(System.in).next();
    }
    private static volatile A a = new A();
    static class A {
        /*volatile*/ boolean flag = true;
        void m() {
            System.out.println("m start");
            while(flag){}
            System.out.println("m end");
        }   
    }
}
