package git.snippets.juc;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.StampedLock;

// 乐观写
public class StampedLockOptimistic {
	 

	private static final StampedLock lock = new StampedLock();


    private static int num1 = 1;


    private static int num2 = 1;

    /**
     * 修改成员变量的值，+1
     *
     * @return
     */
    private static int sum() {
        System.out.println("求和方法被执行了");
        //获取乐观读
        long stamp = lock.tryOptimisticRead();
        int cnum1 = num1;
        int cnum2 = num2;
        System.out.println("获取到的成员变量值,cnum1：" + cnum1 + "   cnum2：" + cnum2);
        try {
            //休眠3秒，目的是为了让其他线程修改掉成员变量的值。
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //判断在运行期间是否存在写操作   true：不存在   false:存在
        if (!lock.validate(stamp)) {
            System.out.println("存在写操作！");
            //存在写锁
            //升级悲观读锁
            stamp = lock.readLock();
            try {
                System.out.println("升级悲观读锁");
                cnum1 = num1;
                cnum2 = num2;
                System.out.println("重新获取了成员变量的值=========== cnum1="+cnum1  +"    cnum2="+cnum2);
            } finally {
                //释放悲观读锁
                lock.unlock(stamp);
            }
        }
         return cnum1 + cnum2;
    }

    //使用写锁修改成员变量的值
    private static void updateNum() {
        long stamp = lock.writeLock();
        try {
            num1 = 2;
            num2 = 2;
        } finally {
            lock.unlock(stamp);
        }
    }


    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(() -> {
            int sum = sum();
            System.out.println("求和结果：" + sum);
        });
        t1.start();
        //休眠1秒，目的为了让线程t1能执行到获取成员变量之后
        Thread.sleep(1000);
        updateNum();
        t1.join();
        System.out.println("执行完毕");

    } 

}
