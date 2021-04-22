作者：[Grey](https://www.cnblogs.com/greyzeng/)

原文地址：[Java多线程学习笔记](https://www.cnblogs.com/greyzeng/p/14176141.html)


## 什么是程序，进程和线程？

- 程序是计算机的可执行文件
- 进程是计算机资源分配的基本单位
- 线程是资源调度执行的基本单位
  - 一个程序里面不同的执行路径
  - 多个线程共享进程中的资源

## 线程和进程的关系

线程就是轻量级进程，是程序执行的最小单位。

多进程的方式也可以实现并发，为什么我们要使用多线程？

1. 共享资源在线程间的通信比较容易。
2. 线程开销更小。

## 进程和线程的区别？

- 进程是一个独立的运行环境，而线程是在进程中执行的一个任务。他们两个本质的区别是是否单独占有内存地址空间及其它系统资源（比如I/O）。
- 进程单独占有一定的内存地址空间，所以进程间存在内存隔离，数据是分开的，数据共享复杂但是同步简单，各个进程之间互不干扰；而线程共享所属进程占有的内存地址空间和资源，数据共享简单，但是同步复杂。
- 进程单独占有一定的内存地址空间，一个进程出现问题不会影响其他进程，不影响主程序的稳定性，可靠性高；一个线程崩溃可能影响整个程序的稳定性，可靠性较低。
- 进程单独占有一定的内存地址空间，进程的创建和销毁不仅需要保存寄存器和栈信息，还需要资源的分配回收以及页调度，开销较大；线程只需要保存寄存器和栈信息，开销较小。
- 进程是操作系统进行资源分配的基本单位，而线程是操作系统进行调度的基本单位，即CPU分配时间的单位。

## 什么是线程切换？

从底层角度上看，CPU主要由如下三部分组成，分别是：

- ALU: 计算单元
- Registers: 寄存器组
- PC：存储到底执行到哪条指令

T1线程在执行的时候，将T1线程的指令放在PC，数据放在Registers，假设此时要切换成T2线程，T1线程的指令和数据放cache，然后把T2线程的指令放PC，数据放Registers，执行T2线程即可。

以上的整个过程是通过操作系统来调度的，且线程的调度是要消耗资源的，所以，线程不是设置越多越好。

## 单核CPU设定多线程是否有意义？

有意义，因为线程的操作中可能有不消耗CPU的操作，比如：等待网络的传输，或者线程sleep，此时就可以让出CPU去执行其他线程。可以充分利用CPU资源。

- CPU密集型
- IO密集型

## 线程数量是不是设置地越大越好？

不是，因为线程切换要消耗资源。

示例：
单线程和多线程来累加1亿个数。-> CountSum.java

## 工作线程数（线程池中线程数量）设多少合适？

- 和CPU的核数有关

- 最好是通过压测来评估。通过profiler性能分析工具jProfiler，或者Arthas

- 公式
```
N = Ncpu * Ucpu * (1 + W/C)
```

其中：

- Ncpu是处理器的核的数目，可以通过Runtime.getRuntime().availableProcessors() 得到

- Ucpu是期望的CPU利用率（该值应该介于0和1之间）

- W/C是等待时间和计算时间的比率。

## Java中创建线程的方式

1. 继承Thread类，重写run方法
2. 实现Runnable接口，实现run方法，这比方式1更好，因为一个类实现了Runnable以后，还可以继承其他类
3. 使用lambda表达式
4. 通过线程池创建
5. 通过Callable/Future创建（需要返回值的时候）

具体示例可见：HelloThread.java

## 线程状态

- NEW 

> 线程刚刚创建，还没有启动
> 即：刚刚New Thread的时候，还没有调用start方法时候，就是这个状态


- RUNNABLE

> 可运行状态，由线程调度器可以安排执行，包括以下两种情况：
> - READY
> - RUNNING
> 
> READY和RUNNING通过yield来切换   

- WAITING
> 等待被唤醒

- TIMED_WAITING
> 隔一段时间后自动唤醒

- BLOCKED

> 被阻塞，正在等待锁
> 只有在synchronized的时候在会进入BLOCKED状态

- TERMINATED
> 线程执行完毕后，是这个状态

## 线程状态切换

![java_thread_state](https://img2020.cnblogs.com/blog/683206/202104/683206-20210412191009944-1947255770.png)

## 线程基本操作

### sleep

当前线程睡一段时间

### yield

这是一个静态方法，一旦执行，它会使当前线程让出一下CPU。但要注意，让出CPU并不表示当前线程不执行了。当前线程在让出CPU后，还会进行CPU资源的争夺，但是是否能够再次被分配到就不一定了。

### join

等待另外一个线程的结束，当前线程才会运行

```java
public class ThreadBasicOperation {
    static volatile int sum = 0;

    public static void main(String[] args) throws Exception {
        Thread t = new Thread(()->{
            for (int i = 1; i <= 100; i++) {
                sum += i;
            }
        });
        t.start();
        // join 方法表示主线程愿意等待子线程执行完毕后才继续执行
        // 如果不使用join方法，那么sum输出的可能是一个很小的值，因为还没等子线程
        // 执行完毕后，主线程就已经执行了打印sum的操作
        t.join();
        System.out.println(sum);
    }
}
```

示例代码：ThreadBasicOperation.java

### interrupt

- interrupt()
> 打断某个线程(设置标志位)

- isInterrupted()
> 查询某线程是否被打断过(查询标志位)

- static interrupted
> 查询当前线程是否被打断过，并重置打断标志位

示例代码：ThreadInterrupt.java

## 如何结束一个线程

### 不推荐的方式

- stop方法
- suspend/resume方法
  

以上两种方式都不建议使用, 因为会产生数据不一致的问题，因为会释放所有的锁。

### 优雅的方式

如果不依赖循环的具体次数或者中间状态, 可以通过设置标志位的方式来控制

```java
public class ThreadFinished {
    private static volatile boolean flag = true;
    public static void main(String[] args) throws InterruptedException {

        // 推荐方式:设置标志位
        Thread t3 = new Thread(() -> {
            long i = 0L;
            while (flag) {
                i++;
            }
            System.out.println("count sum i = " + i);
        });
        t3.start();
        TimeUnit.SECONDS.sleep(1);
        flag = false;
    }
}
```

如果要依赖循环的具体次数或者中间状态, 则可以用interrupt方式

```java
public class ThreadFinished {

    public static void main(String[] args) throws InterruptedException {
        // 推荐方式:使用interrupt
        Thread t4 = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {

            }
            System.out.println("t4 end");
        });
        t4.start();
        TimeUnit.SECONDS.sleep(1);
        t4.interrupt();
    }
}
```

示例代码: ThreadFinished.java


## 并发编程的三大特性

### 可见性

每个线程会保存一份拷贝到线程本地缓存,使用volatile,可以保持线程之间数据可见性。

如下示例: ThreadVisible.java

```java
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
        boolean flag = true;
        void m() {
            System.out.println("m start");
            while(flag){}
            System.out.println("m end");
        }   
    }
}
```

代码说明: 
- 如在上述代码的死循环中增加了System.out.println(), 则会强制同步flag的值,无论flag本身有没有加volatile。
- 如果volatile修饰一个引用对象,如果对象的属性(成员变量)发生了改变,volatile不能保证其他线程可以观察到该变化。

关于三级缓存

![3_cache](https://img2020.cnblogs.com/blog/683206/202104/683206-20210413223658983-1491978414.png)

如上图，内存读出的数据会在L3，L2，L1上都存一份。所谓线程数据的可见性，指的就是内存中的某个数据，假如第一个CPU的一个核读取到了，和其他的核读取到这个数据之间的可见性。

在从内存中读取数据的时候，根据的是程序局部性的原理，按块来读取，这样可以提高效率，充分发挥总线CPU针脚等一次性读取更多数据的能力。

所以这里引入了一个缓存行的概念，目前一个缓存行多用**64个字节**来表示。

如何来验证CPU读取缓存行这件事，我们可以通过一个示例来说明：

```java
public class CacheLinePadding {
    public static T[] arr = new T[2];

    static {
        arr[0] = new T();
        arr[1] = new T();
    }

    public static void main(String[] args) throws Exception {
        Thread t1 = new Thread(() -> {
            for (long i = 0; i < 1000_0000L; i++) {
                arr[0].x = i;
            }
        });

        Thread t2 = new Thread(() -> {
            for (long i = 0; i < 1000_0000L; i++) {
                arr[1].x = i;
            }
        });

        final long start = System.nanoTime();
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        System.out.println((System.nanoTime() - start) / 100_0000);
    }

    private static class Padding {
        public volatile long p1, p2, p3, p4, p5, p6, p7;
    }

    private static class T /**extends Padding*/ {
        public volatile long x = 0L;
    } 
}

```
说明：以上代码，T这个类extends Padding与否，会影响整个流程的执行时间，如果继承了，会减少执行时间，因为继承Padding后，arr[0]和arr[1]一定不在同一个缓存行里面，所以不需要同步数据，速度就更快一些了。

> jdk1.8增加了一个注解：@Contended，标注了以后，不会在同一缓存行, 仅适用于jdk1.8
还需要增加jvm参数

```
-XX:-RestrictContended
```

CPU为每个缓存行标记四种状态（使用两位） 

- Exclusive
- Invalid
- Shared
- Modified

参考：[【并发编程】MESI--CPU缓存一致性协议](https://www.cnblogs.com/z00377750/p/9180644.html)

### 有序性

为什么会出现乱序执行呢？因为CPU为了提高效率，可能在执行某些指令的时候，不按顺序执行（指令前后没有依赖关系的时候）

乱序存在的条件是：不影响单线程的最终一致性(as - if - serial)

验证乱序执行的程序示例 DisOrder.java：

```java
public class DisOrder {
    private static int x = 0, y = 0;
    private static int a = 0, b = 0;

    // 以下程序可能会执行比较长的时间
    public static void main(String[] args) throws InterruptedException {
        int i = 0;
        for (;;) {
            i++;
            x = 0;
            y = 0;
            a = 0;
            b = 0;
            Thread one = new Thread(() -> {
                // 由于线程one先启动，下面这句话让它等一等线程two. 读着可根据自己电脑的实际性能适当调整等待时间.
                shortWait(100000);
                a = 1;
                x = b;
            });

            Thread other = new Thread(() -> {
                b = 1;
                y = a;
            });
            one.start();
            other.start();
            one.join();
            other.join();
            String result = "第" + i + "次 (" + x + "," + y + "）";
            if (x == 0 && y == 0) {
                // 出现这个分支，说明指令出现了重排
                // 否则不可能 x和y同时都为0
                System.err.println(result);
                break;
            } else {
                // System.out.println(result);
            }
        }
    }

    public static void shortWait(long interval) {
        long start = System.nanoTime();
        long end;
        do {
            end = System.nanoTime();
        } while (start + interval >= end);
    }
}
```

如上示例，如果指令不出现乱序，那么x和y不可能同时为0，通过执行这个程序可以验证出来，在我本机测试的结果是：

执行到第1425295次 出现了x和y同时为0的情况。



### 原子性

程序的原子性是指整个程序中的所有操作，要么全部完成，要么全部失败，不可能滞留在中间某个环节；在多个线程一起执行的时候，一个操作一旦开始，就不会被其他线程所打断。


一个示例：
```java
class T {
    m = 9;
}
```
对象T在创建过程中，背后其实是包含了多条执行语句的，由于有CPU乱序执行的情况，所以极有可能会在初始化过程中生成以一个半初始化对象t，这个t的m等于0（还没有来得及做赋值操作）

所以，不要在某个类的构造方法中启动一个线程，这样会导致this对象逸出，因为这个类的对象可能还来不及执行初始化操作，就启动了一个线程，导致了异常情况。


volatile一方面可以保证线程数据之间的可见性，另外一方面，也可以防止类似这样的指令重排，所以
所以，单例模式中，DCL方式的单例一定要加volatile修饰：

```java
public class Singleton6 {
    private volatile static Singleton6 INSTANCE;

    private Singleton6() {
    }

    public static Singleton6 getInstance() {
        if (INSTANCE == null) {
            synchronized (Singleton6.class) {
                if (INSTANCE == null) {
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    INSTANCE = new Singleton6();
                }
            }
        }
        return INSTANCE;
    }
}
```

具体可以参考[设计模式学习笔记](https://www.cnblogs.com/greyzeng/p/14107751.html) 中单例模式的说明。

## CAS

比较与交换的意思

举个例子：

> 内存有个值是3，如果用Java通过多线程去访问这个数，每个线程都要把这个值+1。

之前是需要加锁，即synchronized关键字来控制。但是JUC的包出现后，有了CAS操作，可以不需要加锁来处理，流程是：

第一个线程：把3拿过来，线程本地区域做计算+1，然后把4写回去，
第二个线程：也把3这个数拿过来，线程本地区域做计算+3后，在回写回去的时候，会做一次比较，如果原来的值还是3，那么说明这个值之前没有被打扰过，就可以把4写回去，如果这个值变了，假设变为了4，那么说明这个值已经被其他线程修改过了，那么第二个线程需要重新执行一次，即把最新的4拿过来继续计算，回写回去的时候，继续做比较，如果内存中的值依然是4，说明没有其他线程处理过，第二个线程就可以把5回写回去了。

流程图如下：
![cas_case](https://img2020.cnblogs.com/blog/683206/202104/683206-20210407160938520-132921153.png)

### ABA问题

CAS会出现一个ABA的问题，即在一个线程回写值的时候，其他线程其实动过那个原始值，只不过其他线程操作后这个值依然是原始值。

如何来解决ABA问题呢？

我们可以通过版本号或者时间戳来控制，比如数据原始的版本是1.0，处理后，我们把这个数据的版本改成变成2.0版本, 时间戳来控制也一样，

以Java为例，AtomicStampedReference这个类，它内部不仅维护了对象值，还维护了一个时间戳。

当AtomicStampedReference对应的数值被修改时，除了更新数据本身外，还必须要更新时间戳。

当AtomicStampedReference设置对象值时，对象值以及时间戳都必须满足期望值，写入才会成功。

因此，即使对象值被反复读写，写回原值，只要时间戳发生变化，就能防止不恰当的写入。

### CAS的底层实现

> Unsafe.cpp-->Atom::cmpxchg-->Atomic_linux_x86_inline.hpp-->调用了汇编的LOCK_IF_MP方法
>
> Multiple_processor

```
lock cmpxchg
```

虽然cmpxchg指令不是原子的，但是加了lock指令后，则cmpxhg被上锁，不允许被打断。 在单核CPU中，无须加lock，在多核CPU中，必须加lock，可以参考stackoverflow上的这个回答:

[is-x86-cmpxchg-atomic-if-so-why-does-it-need-lock](https://stackoverflow.com/questions/27837731/is-x86-cmpxchg-atomic-if-so-why-does-it-need-lock/44273130#44273130)

使用CAS好处

jdk早期是重量级别锁 ，通过0x80中断 进行用户态和内核态转换，所以效率比较低，有了CAS操作，大大提升了效率。

## 对象的内存布局(Hotspot实现)

### 使用jol查看一个对象的内存布局

我们可以通过jol包来查看一下某个对象的内存布局

引入jol依赖
```xml
<dependency>
  <groupId>org.openjdk.jol</groupId>
  <artifactId>jol-core</artifactId>
  <version>0.15</version>
</dependency>
```

示例代码(ObjectModel.java)

```java
public class ObjectModel {
    public static void main(String[] args) {
        T o = new T();
        String s = ClassLayout.parseInstance(o).toPrintable();
        System.out.println(s);
    }
}
class  T{
     
}

```
配置VM参数，开启指针压缩

```shell
-XX:+UseCompressedClassPointers
```

运行结果如下：
```
OFF  SZ   TYPE DESCRIPTION               VALUE
  0   8        (object header: mark)     0x0000000000000005 (biasable; age: 0)
  8   4        (object header: class)    0x00067248
 12   4        (object alignment gap)    
Instance size: 16 bytes
Space losses: 0 bytes internal + 4 bytes external = 4 bytes total
```

其中8个字节的markword

4个字节的类型指针，可以找到T.class

这里一共是12个字节， 由于字节数务必是8的整数倍，所以补上4个字节，共16个字节

我们修改一下T这个类

```java
class  T{
    public int a = 3;
    public long b = 3l;
}
```

再次执行,可以看到结果是
```
OFF  SZ   TYPE DESCRIPTION               VALUE
  0   8        (object header: mark)     0x0000000000000005 (biasable; age: 0)
  8   4        (object header: class)    0x00067248
 12   4    int T.a                       3
 16   8   long T.b                       3
Instance size: 24 bytes
Space losses: 0 bytes internal + 0 bytes external = 0 bytes total
```

其中多了4位表示int这个成员变量，多了8位表示long这个成员变量, 相加等于24，正好是8的整数倍，不需要补齐。

### 内存布局详细说明

![object_model_of_hotspot](https://img2020.cnblogs.com/blog/683206/202104/683206-20210415125727387-1817094465.png)

使用synchronized就是修改了对象的markword信息，markword中还记录了GC信息，Hashcode信息


### 锁升级过程

![image.png](https://cdn.nlark.com/yuque/0/2020/png/757806/1588516010526-faeed51e-6a56-47bd-9844-4ba608af3935.png#align=left&display=inline&height=312&margin=%5Bobject%20Object%5D&name=image.png&originHeight=312&originWidth=661&size=70593&status=done&style=none&width=661)

#### 偏向锁

> synchronized代码段多数时间是一个线程在运行，谁先来，这个就偏向谁，用当前线程标记一下。

#### 轻量级锁（自旋锁，无锁）

> 偏向锁撤销，然后竞争，每个线程在自己线程栈中存一个LR（lock record）锁记录

> 偏向锁和轻量级锁都是用户空间完成的，重量级锁需要向操作系统申请。
> 两个线程争抢的方式将lock record的指针，指针指向哪个线程的LR，哪个线程就拿到锁，另外的线程用CAS的方式继续竞争

#### 重量级锁

> JVM的ObjectMonitor去操作系统申请。


如果发生异常，synchronized会自动释放锁

```java
public class ExceptionCauseUnLock {
    /*volatile */ boolean stop = false;

    public static void main(String[] args) {
        ExceptionCauseUnLock t = new ExceptionCauseUnLock();
        new Thread(t::m, "t1").start();
        try {
            TimeUnit.SECONDS.sleep(4);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (t.stop) {
            int m = 1 / 0;
        }
    }

    synchronized void m() {
        while (!stop) {
            stop = true;
        }
    }
}
```

interpreteRuntime.cpp --> monitorenter

![image.png](https://cdn.nlark.com/yuque/0/2020/png/757806/1588517861099-20a991ba-7540-4f12-a1a9-47ec17a2eb6d.png#align=left&display=inline&height=520&margin=%5Bobject%20Object%5D&name=image.png&originHeight=520&originWidth=894&size=371759&status=done&style=none&width=894)


### 锁重入

synchronized是可重入锁
可重入次数必须记录，因为解锁需要对应可重入次数的记录
偏向锁：记录在线程栈中，每重入一次，LR+1，备份原来的markword
轻量级锁：类似偏向锁
重量级锁：记录在ObjectMonitor的一个字段中

自旋锁什么时候升级为重量级锁？
- 有线程超过十次自旋
- -XX：PreBlockSpin（jdk1.6之前）
- 自旋的线程超过CPU核数一半
- jdk1.6 以后，JVM自己控制


### 为什么有偏向锁启动和偏向锁未启动？

未启动：普通对象001
已启动：匿名偏向101

### 为什么有自旋锁还需要重量级锁？

因为自旋会占用CPU时间，消耗CPU资源，如果自旋的线程多，CPU资源会被消耗，所以会升级成重量级锁（队列）例如：ObjectMonitor里面的WaitSet，重量级锁会把线程都丢到WaitSet中冻结, 不需要消耗CPU资源

### 偏向锁是否一定比自旋锁效率高？

明确知道多线程的情况下，不一定。
因为偏向锁在多线程情况下，会涉及到锁撤销，这个时候直接使用自旋锁，JVM启动过程，会有很多线程竞争，比如启动的时候，肯定是多线程的，所以默认情况，启动时候不打开偏向锁，过一段时间再打开。
有一个参数可以配置：BiasedLockingStartupDelay默认是4s钟

### 偏向锁状态下，调用了wait方法，直接升级成重量级锁

一个线程拿20个对象进行加锁，批量锁的重偏向（20个对象），批量锁撤销（变成轻量级锁）（40个对象）， 通过Epoch中的值和对应的类对象里面记录的值比较。




## synchronized

### 锁定对象

```java
public class SynchronizedObject implements Runnable {
    static SynchronizedObject instance = new SynchronizedObject();
    final Object object = new Object();
    static volatile int i = 0;

    @Override
    public void run() {
        for (int j = 0; j < 1000000; j++) {
            // 任何线程要执行下面的代码，必须先拿到object的锁
            synchronized (object) {
                i++;
            }
        }
    }
    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(instance);
        Thread t2 = new Thread(instance);
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        System.out.println(i);
    }
}
```

### 锁定方法

- 锁定静态方法相当于锁定当前类

```java
public class SynchronizedStatic implements Runnable {
    static SynchronizedStatic instance = new SynchronizedStatic();
    static volatile int i = 0;

    @Override
    public void run() {
        increase();
    }

    // 相当于synchronized(SynchronizedStatic.class)
    synchronized static void increase() {
        for (int j = 0; j < 1000000; j++) {
            i++;
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(instance);
        Thread t2 = new Thread(instance);
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        System.out.println(i);
    }
}
```
- 锁定非静态方法相当于锁定该对象的实例或synchronized(this)

```java
public class SynchronizedMethod implements Runnable {
    static SynchronizedMethod instance = new SynchronizedMethod();
    static volatile int i = 0;

    @Override
    public void run() {
        increase();
    }
    void increase() {
        for (int j = 0; j < 1000000; j++) {
            // 任何线程要执行下面的代码，必须先拿到object的锁
            synchronized (this) {
                i++;
            }
        }
    }
    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(instance);
        Thread t2 = new Thread(instance);
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        System.out.println(i);
    }
}
```

### 脏读

```java
public class DirtyRead {
  String name;
  double balance;

  public static void main(String[] args) {
    DirtyRead a = new DirtyRead();
    Thread thread = new Thread(() -> a.set("zhangsan", 100.0));

    thread.start();
    try {
      TimeUnit.SECONDS.sleep(1);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    System.out.println(a.getBalance("zhangsan"));
    try {
      TimeUnit.SECONDS.sleep(2);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    System.out.println(a.getBalance("zhangsan"));
  }

  public synchronized void set(String name, double balance) {
    this.name = name;

    try {
      Thread.sleep(2000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }


    this.balance = balance;
  }

  // 如果get方法不加synchronized关键字，就会出现脏读情况
  public /*synchronized*/ double getBalance(String name) {
    return this.balance;
  }
}
```
其中的getBalance方法，如果不加synchronized，就会产生脏读的问题。

### 可重入锁

> 一个同步方法可以调用另外一个同步方法，
> 一个线程已经拥有某个对象的锁，再次申请的时候仍然会得到该对象的锁（可重入锁）
> 子类synchronized，如果调用父类的synchronize方法：super.method(),如果不可重入，直接就会死锁。


```java
public class SynchronizedReentry implements Runnable {
    public static void main(String[] args) throws IOException {
        SynchronizedReentry myRun = new SynchronizedReentry();
        Thread thread = new Thread(myRun, "t1");
        Thread thread2 = new Thread(myRun, "t2");
        thread.start();
        thread2.start();
        System.in.read();

    }

    synchronized void m1(String content) {
        System.out.println(this);
        System.out.println("m1 get content is " + content);
        m2(content);
    }

    synchronized void m2(String content) {
        System.out.println(this);
        System.out.println("m2 get content is " + content);

    }

    @Override
    public void run() {
        m1(Thread.currentThread().getName());
    }
}

```

程序在执行过程中，如果出现异常，默认情况锁会被释放 ,所以，在并发处理的过程中，有异常要多加小心，不然可能会发生不一致的情况。比如，在一个web app处理过程中，多个servlet线程共同访问同一个资源，这时如果异常处理不合适， 在第一个线程中抛出异常，其他线程就会进入同步代码区，有可能会访问到异常产生时的数据。因此要非常小心的处理同步业务逻辑中的异常。

示例见：

SynchronizedException.java

### synchronized的底层实现

在早期的JDK使用的是OS的重量级锁

后来的改进锁升级的概念：

synchronized (Object)

- markword 记录这个线程ID （使用偏向锁）
- 如果线程争用：升级为 自旋锁
- 10次自旋以后，升级为重量级锁 - OS

所以：

- 执行时间短（加锁代码），线程数少，用自旋
- 执行时间长，线程数多，用系统锁



### 如何模拟死锁

```java
public class DeadLock implements Runnable {
  int flag = 1;
  static Object o1 = new Object();
  static Object o2 = new Object();

  public static void main(String[] args) {
    DeadLock lock = new DeadLock();
    DeadLock lock2 = new DeadLock();
    lock.flag = 1;
    lock2.flag = 0;
    Thread t1 = new Thread(lock);
    Thread t2 = new Thread(lock2);
    t1.start();
    t2.start();
  }

  @Override
  public void run() {
    System.out.println("flag = " + flag);
    if (flag == 1) {
      synchronized (o2) {
        try {
          Thread.sleep(500);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
        synchronized (o1) {
          System.out.println("1");
        }
      }
    }
    if (flag == 0) {
      synchronized (o1) {
        try {
          Thread.sleep(500);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }

        synchronized (o2) {
          System.out.println("0");
        }
      }
    }
  }
}
```

### synchronized不能锁定String常量，Integer，Long等基础类型

见示例：

SynchronizedBasicType.java

### 锁定某对象o，如果o的属性发生改变，不影响锁的使用; 但是如果o变成另外一个对象，则锁定的对象发生改变, 应该避免将锁定对象的引用变成另外的对象

```java
public class SyncSameObject {
    Object object = new Object();

    public static void main(String[] args) {
        SyncSameObject t = new SyncSameObject();
        new Thread(t::m).start();
        Thread t2 = new Thread(t::m, "t2");
        //锁对象发生改变，所以t2线程得以执行，如果注释掉这句话，线程2将永远得不到执行机会
        t.object = new Object();

        t2.start();
    }

    void m() {
        synchronized (object) {
            while (true) {
                try {
                    TimeUnit.SECONDS.sleep(2);
                    System.out.println("current thread is " + Thread.currentThread().getName());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
```

如果不执行

```java
t.object = new Object() 
```

这句话，m2线程将永远得不到执行。


## volatile

- 保持线程之间的可见性(不保证操作的原子性)，依赖这个MESI协议
- 防止指令重排序，CPU的load fence和store fence原语支持

CPU原来执行指令一步一步执行，现在是流水线执行，编译以后可能会产生指令的重排序，这样可以提高性能

关于volatile不保证原子性的代码示例：

```java
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
```

volatile并不能保证多个线程共同修改running变量时所带来的不一致问题，也就是说volatile不能替代synchronized, 示例程序：

```java
public class VolatileCanNotReplaceSynchronized {
    volatile int count = 0;
    int count2 = 0;

    public static void main(String[] args) {
        VolatileCanNotReplaceSynchronized t = new VolatileCanNotReplaceSynchronized();
        List<Thread> threads = new ArrayList<>();
        List<Thread> threads2 = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            threads.add(new Thread(t::m));
            threads2.add(new Thread(t::m2));
        }
        threads.forEach(item -> item.start());
        threads2.forEach(item -> item.start());
        threads.forEach(item -> {
            try {
                item.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        threads2.forEach(item -> {
            try {
                item.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        System.out.println(t.count);
        System.out.println(t.count2);
    }

    void m() {
        for (int i = 0; i < 1000; i++) {
            count++;
        }
    }

    synchronized void m2() {
        for (int i = 0; i < 1000; i++) {
            count2++;
        }
    }
}
```

### DCL为什么一定要加volatile？

DCL示例:

```java
public class Singleton6 {
	private volatile static Singleton6 INSTANCE;

	private Singleton6() {
	}

	public static Singleton6 getInstance() {
		if (INSTANCE == null) {
			synchronized (Singleton6.class) {
				if (INSTANCE == null) {
					try {
						Thread.sleep(1);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					INSTANCE = new Singleton6();
				}
			}
		}
		return INSTANCE;
	}
}
```

在New对象的时候，编译完实际上是分了三步

1. 对象申请内存，成员变量会被赋初始值
2. 成员变量设为真实值
3. 成员变量赋给对象

指令重排序可能会导致2和3进行指令重排，导致下一个线程拿到一个半初始化的对象，导致单例被破坏。所以DCL必须加Volitile

### volatile修饰引用对象

> 被volatile关键字修饰的对象作为类变量或实例变量时，其对象中携带的类变量和实例变量也相当于被volatile关键字修饰了

示例见：VolatileRef.java


## AtomicLong, LongAddr, Synchronized效率之争

需要实际测试一下。

示例见：

- AddByAtomicLong.java（无锁操作）
- AddByLongAdder.java （LongAdder采用了分段锁，分段锁又是CAS实现的。多段并行运行，在线程数比较多的情况下，效率比较高。线程数少的情况下没什么优势。）
- AddBySynchronized.java

在大数据量的情况下，LongAddr的效率最高。参考

- [从LONGADDER看更高效的无锁实现](https://coolshell.cn/articles/11454.html)
  
- [Java 8 Performance Improvements: LongAdder vs AtomicLong](http://blog.palominolabs.com/2014/02/10/java-8-performance-improvements-longadder-vs-atomiclong/)


## ReentrantLock

**其中“ReentrantReadWriteLock”，“读锁的插队策略”,"锁的升降级" 部分参考了如下文档中的内容**

[Java中的共享锁和排他锁（以读写锁ReentrantReadWriteLock为例）](https://blog.csdn.net/fanrenxiang/article/details/104312606)

### ReentrantLock vs sychronized

可重入锁，可以替代sychronized

比sychronized强大的地方在于:

1. 可以tryLock，尝试若干时间片内获取锁。 见： ReentrantLockTryLock.java

2. 可以用lockInterruptibly，在lock的时候可以被打断，一旦被打断，可以作出响应，而sychronized一旦wait后，必须得让别人notify，才能醒来。见：ReentrantLockInterrupt.java

3. 可以设置公平与否，公平的概念是，每个线程来了以后会检查等待队列里面会不会有等待的线程，如果有，则进入队列等待。见：ReentrantLockFair.java

**注：在使用ReentrantLock的时候一定要记得unlock，因为如果使用synchronized遇到异常，jvm会自动释放锁，但是用ReentrantLock必须手动释放锁，因此经常在finally中进行锁的释放** 

详见：

- ReentrantLockAndSynchronized.java

- SynchronizedException.java

### ReentrantReadWriteLock


> 在ReentrantReadWriteLock中包含读锁和写锁， 
> 其中读锁是可以多线程共享的，即共享锁， 而写锁是排他锁，在更改时候不允许其他线程操作。 
> 读写锁其实是一把锁，所以会有同一时刻不允许读写锁共存的规定。 
> 之所以要细分读锁和写锁也是为了提高效率，将读和写分离，


示例：

```java
public class ReentrantLockReadAndWrite {

  private static ReentrantReadWriteLock reentrantLock = new ReentrantReadWriteLock();
  private static ReentrantReadWriteLock.ReadLock readLock = reentrantLock.readLock();
  private static ReentrantReadWriteLock.WriteLock writeLock = reentrantLock.writeLock();

  public static void read() {
    readLock.lock();
    try {
      System.out.println(Thread.currentThread().getName() + "获取读锁，开始执行");
      Thread.sleep(1000);
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      readLock.unlock();
      System.out.println(Thread.currentThread().getName() + "释放读锁");
    }
  }

  public static void write() {
    writeLock.lock();
    try {
      System.out.println(Thread.currentThread().getName() + "获取写锁，开始执行");
      Thread.sleep(1000);
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      writeLock.unlock();
      System.out.println(Thread.currentThread().getName() + "释放写锁");
    }
  }

  public static void main(String[] args) {
    new Thread(() -> read(), "Thread1").start();
    new Thread(() -> read(), "Thread2").start();
    new Thread(() -> write(), "Thread3").start();
    new Thread(() -> write(), "Thread4").start();
  }
}
```
### 读锁的插队策略

设想如下场景：

在非公平的ReentrantReadWriteLock锁中，线程2和线程4正在同时读取，线程3想要写入，拿不到锁（同一时刻是不允许读写锁共存的），于是进入等待队列， 线程5不在队列里，现在过来想要读取，

策略1

> 如果允许读插队，就是说线程5读先于线程3写操作执行，因为读锁是共享锁，不影响后面的线程3的写操作， 
> 这种策略可以提高一定的效率，却可能导致像线程3这样的线程一直在等待中，因为可能线程5读操作之后又来了n个线程也进行读操作，造成线程饥饿；

策略2

> 不允许插队，即线程5的读操作必须排在线程3的写操作之后，放入队列中，排在线程3之后，这样能避免线程饥饿。 
> 事实上ReentrantReadWriteLock在非公平情况下，读锁采用的就是策略2：不允许读锁插队，避免线程饥饿。更加确切的说是：在非公平锁情况下，允许写锁插队，也允许读锁插队，

但是读锁插队的前提是队列中的头节点不能是想获取写锁的线程。

以上还在非公平ReentrantReadWriteLock锁中，

在公平锁中，读写锁都是是不允许插队的，严格按照线程请求获取锁顺序执行。

示例见：ReentrantLockCut.java

### 锁的升降级

 
在ReentrantReadWriteLock读写锁中，只支持写锁降级为读锁，而不支持读锁升级为写锁, 

之所以ReentrantReadWriteLock不支持锁的升级（其它锁可以支持），主要是避免死锁，

例如两个线程A和B都在读， A升级要求B释放读锁，B升级要求A释放读锁，互相等待形成死循环。

如果能严格保证每次都只有一个线程升级那也是可以的。


示例见：ReentrantReadWriteLockUpAndDown.java

## CountDownLatch vs join

类似门闩的概念，可以替代join，但是比join灵活，因为一个线程里面可以多次countDown，但是join一定要等线程完成才能执行。

```java
public class CountDownLatchAndJoin {
	public static void main(String[] args) {
		useCountDownLatch();
		useJoin();
	}

	public static void useCountDownLatch() {
		// use countdownlatch
		long start = System.currentTimeMillis();
		Thread[] threads = new Thread[100000];
		CountDownLatch latch = new CountDownLatch(threads.length);

		for (int i = 0; i < threads.length; i++) {
			threads[i] = new Thread(() -> {
				int result = 0;
				for (int i1 = 0; i1 < 1000; i1++) {
					result += i1;
				}
				// System.out.println("Current thread " + Thread.currentThread().getName() + " finish cal result " + result);
				latch.countDown();
			});
		}
		for (Thread thread : threads) {
			thread.start();
		}
		try {
			latch.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		long end = System.currentTimeMillis();

		System.out.println("end latch down, time is " + (end - start));

	}

	public static void useJoin() {
		long start = System.currentTimeMillis();

		// use join
		Thread[] threads = new Thread[100000];

		for (int i = 0; i < threads.length; i++) {
			threads[i] = new Thread(() -> {
				int result = 0;
				for (int i1 = 0; i1 < 1000; i1++) {
					result += i1;
				}
				// System.out.println("Current thread " + Thread.currentThread().getName() + " finish cal result " + result);
			});
		}
		for (Thread thread : threads) {
			thread.start();
		}
		for (Thread thread : threads) {
			try {
				thread.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		long end = System.currentTimeMillis();

		System.out.println("end join, time is " + (end - start));
	}
}
```

## CyclicBarrier

类似栅栏，类比：满了20个乘客就发车 这样的场景。

比如：一个程序可能收集如下来源的数据：

1. 数据库
2. 网络
3. 文件

程序可以并发执行，用线程操作1，2，3，然后操作完毕后再合并, 然后执行后续的逻辑操作，就可以使用CyclicBarrier

代码示例见：CyclicBarrierTest.java

## Guava RateLimiter

采用令牌桶算法，用于限流

代码示例见：RateLimiterUsage.java


## Phaser（Since jdk1.7）

遗传算法，可以用这个结婚的场景模拟：
假设婚礼的宾客有5个人，加上新郎和新娘，一共7个人。
我们可以把这7个人看成7个线程，有如下步骤要执行。

1. 到达婚礼现场
2. 吃饭
3. 离开
4. 拥抱（只有新郎和新娘线程可以执行）

每个阶段执行完毕后才能执行下一个阶段，其中hug阶段只有新郎新娘这两个线程才能执行。


以上需求，我们可以通过Phaser来实现，具体代码和注释如下：

```java
public class PhaserUsage {
    static final Random R = new Random();
    static WeddingPhaser phaser = new WeddingPhaser();

    static void millSleep() {
        try {
            TimeUnit.MILLISECONDS.sleep(R.nextInt(1000));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        // 宾客的人数
        final int guestNum = 5;
        // 新郎和新娘
        final int mainNum = 2;
        phaser.bulkRegister(mainNum + guestNum);
        for (int i = 0; i < guestNum; i++) {
            new Thread(new Person("宾客" + i)).start();
        }
        new Thread(new Person("新娘")).start();
        new Thread(new Person("新郎")).start();
    }

    static class WeddingPhaser extends Phaser {
        @Override
        protected boolean onAdvance(int phase, int registeredParties) {
            switch (phase) {
                case 0:
                    System.out.println("所有人到齐");
                    return false;
                case 1:
                    System.out.println("所有人吃饭");
                    return false;
                case 2:
                    System.out.println("所有人离开");
                    return false;
                case 3:
                    System.out.println("新郎新娘入拥抱");
                    return true;
                default:
                    return true;
            }
        }
    }
    static class Person implements Runnable {
        String name;
        Person(String name) {
            this.name = name;
        }
        @Override
        public void run() {
            // 先到达婚礼现场
            arrive();
            // 吃饭
            eat();
            // 离开
            leave();
            // 拥抱，只保留新郎和新娘两个线程可以执行
            hug();
        }
        private void arrive() {
            millSleep();
            System.out.println("name:" + name + " 到来");
            phaser.arriveAndAwaitAdvance();
        }
        private void eat() {
            millSleep();
            System.out.println("name:" + name + " 吃饭");
            phaser.arriveAndAwaitAdvance();
        }
        private void leave() {
            millSleep();
            System.out.println("name:" + name + " 离开");
            phaser.arriveAndAwaitAdvance();
        }
        private void hug() {
            if ("新娘".equals(name) || "新郎".equals(name)) {
                millSleep();
                System.out.println("新娘新郎拥抱");
                phaser.arriveAndAwaitAdvance();
            } else {
                phaser.arriveAndDeregister();
            }
        } 
    }
}
```

## StampedLock

StampedLock其实是对读写锁的一种改进，它支持在读同时进行一个写操作,也就是说，它的性能将会比读写锁更快。

更通俗的讲就是在读锁没有释放的时候是可以获取到一个写锁，获取到写锁之后，读锁阻塞，这一点和读写锁一致，唯一的区别在于读写锁不支持在没有释放读锁的时候获取写锁。

### StampedLock三种模式

- 悲观读：与读写锁的读写类似，允许多个线程获取悲观读锁
- 写锁：与读写锁的写锁类似，写锁和悲观读是互斥的。
- 乐观读：无锁机制，类似于数据库中的乐观锁，它支持在不释放乐观读的时候是可以获取到一个写锁的，这点和读写锁不同

参考： [【并发编程】面试官：有没有比读写锁更快的锁？](https://blog.csdn.net/qq_33220089/article/details/105173632)


示例代码：

悲观读 + 写锁 StampedLockPessimistic.java

乐观读：StampedLockOptimistic.java


### 使用StampedLock的注意事项

1.看名字就能看出来StampedLock不支持重入锁。

2.它适用于读多写少的情况，如果不是这中情况，请慎用，性能可能还不如synchronized。

3.StampedLock的悲观读锁、写锁不支持条件变量。

4.千万不能中断阻塞的悲观读锁或写锁，如果调用阻塞线程的interrupt()，会导致cpu飙升，如果希望StampedLock支持中断操作，请使用readLockInterruptibly（悲观读锁）与writeLockInterruptibly（写锁）。


## Semaphore

表示信号量，有如下两个操作：
s.acquire（） 信号量-1
s.release（） 信号量+1

到0以后，就不能执行了

这个可以用于限流

例如：

有N个线程来访问，我需要限制同时运行的只有信号量大小的线程数，示例代码：

```java
public class C08_04_Semaphore {
    public static void main(String[] args) {
        Semaphore semaphore = new Semaphore(1);
        new Thread(() -> {
            try {
                semaphore.acquire();
                TimeUnit.SECONDS.sleep(2);
                System.out.println("Thread 1 executed");
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                semaphore.release();
            }
        }).start();

        new Thread(() -> {
            try {
                semaphore.acquire();
                TimeUnit.SECONDS.sleep(2);
                System.out.println("Thread 2 executed");
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                semaphore.release();
            }
        }).start();
    }
}
```

可以有公平和非公平的方式进行配置。

## Exchanger

用于线程之间交换数据，exchange()方法是阻塞的，所以要两个exchange同时执行到才会触发交换。

```java
public class ExchangerUsage {
    static Exchanger<String> semaphore = new Exchanger<>();

    public static void main(String[] args) {
        new Thread(() -> {
            String s = "T1";
            try {
                s = semaphore.exchange(s);
                TimeUnit.SECONDS.sleep(2);
                System.out.println("Thread 1(T1) executed, Result is " + s);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
        new Thread(() -> {
            String s = "T2";
            try {
                s = semaphore.exchange(s);
                TimeUnit.SECONDS.sleep(2);
                System.out.println("Thread 2(T2) executed, Result is " + s);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
}

```

## LockSupport

其他锁的底层用的是AQS

原先让线程等待需要wait/await，现在仅需要LockSupport.park

原先叫醒线程需要notify/notifyAll，现在仅需要LockSupport.unpark, 还可以叫醒指定线程，

示例代码：

```java
public class LockSupportUsage {
    public static void main(String[] args) {
        Thread t = new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                try {
                    if (i == 5) {
                        LockSupport.park();
                    }
                    if (i == 8) {
                        LockSupport.park();
                    }
                    TimeUnit.SECONDS.sleep(1);
                    System.out.println(i);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        t.start();
        // unpark可以先于park调用
        //LockSupport.unpark(t);
        try {
            TimeUnit.SECONDS.sleep(8);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        LockSupport.unpark(t);
        System.out.println("after 8 seconds");
    }
}
```

## 相关练习

## 题目1

> 实现一个容器，提供两个方法，add，size写两个线程，线程1添加10个元素到容器中，线程2实现监控元素的个数，当个数到5个时，线程2给出提示并结束

方法1. 使用wait + notify 实现

```java
private static void useNotifyAndWait() {
		 List<Integer> list = Collections.synchronizedList(new ArrayList<>());
			final Object o = new Object();
			Thread adder = new Thread(() -> {
				synchronized (o) {
					for (int i = 0; i < 10; i++) {
						try {
							TimeUnit.SECONDS.sleep(1);
						} catch (InterruptedException e1) {
							e1.printStackTrace();
						}
						list.add(i);
						System.out.println("list add the " + i + " element ");
						if (list.size() == 5) {
							o.notify();
							try {
								o.wait();
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
					}
					o.notify();
				}
			});
			Thread monitor = new Thread(() -> {
				synchronized (o) {
					while (true) {
						if (list.size() == 5) {
							System.out.println("filled 5 elements");
						}
						o.notify();
						try {
							o.wait();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						break;
					}
				}
			});
			adder.start();
			monitor.start();
	}
```


## 思维导图

[processon](https://www.processon.com/view/5ec513425653bb6f2a1f7da8)

## 源码

[Github](https://github.com/GreyZeng/juc)


## 参考资料

[多线程与高并发-马士兵](https://ke.qq.com/course/3132461?tuin=b09cbb87)

[实战Java高并发程序设计(第2版)](https://book.douban.com/subject/30358019/)

[深入浅出Java多线程](http://concurrent.redspider.group/RedSpider.html)

[Java并发编程实战](https://book.douban.com/subject/10484692/)

[【并发编程】MESI--CPU缓存一致性协议](https://www.cnblogs.com/z00377750/p/9180644.html)

[【并发编程】细说并发编程的三大特性](https://zhuanlan.zhihu.com/p/274569273)

[设计模式学习笔记](https://www.cnblogs.com/greyzeng/p/14107751.html)

[从LONGADDER看更高效的无锁实现](https://coolshell.cn/articles/11454.html)

[Java 8 Performance Improvements: LongAdder vs AtomicLong](http://blog.palominolabs.com/2014/02/10/java-8-performance-improvements-longadder-vs-atomiclong/)

[Java中的共享锁和排他锁（以读写锁ReentrantReadWriteLock为例）](https://blog.csdn.net/fanrenxiang/article/details/104312606)

[【并发编程】面试官：有没有比读写锁更快的锁？](https://blog.csdn.net/qq_33220089/article/details/105173632)