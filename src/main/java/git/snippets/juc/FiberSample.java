package git.snippets.juc;

import co.paralleluniverse.fibers.Fiber;
import co.paralleluniverse.fibers.SuspendExecution;
import co.paralleluniverse.strands.channels.Channel;
import co.paralleluniverse.strands.channels.Channels;

import java.util.concurrent.ExecutionException;

/**
 * Java协程示例
 * JDK 11 ~ JDK 15 没问题，
 *
 * JDK 16 开始，需要增加如下参数
 *
 * --add-exports=java.base/sun.nio.ch=ALL-UNNAMED --add-opens=java.base/java.lang=ALL-UNNAMED --add-opens=java.base/java.lang.reflect=ALL-UNNAMED --add-opens=java.base/java.io=ALL-UNNAMED --add-exports=jdk.unsupported/sun.misc=ALL-UNNAMED
 *
 * @since jdk11
 * 需要引入：quasar-core依赖包
 */
public class FiberSample {
    private static void printer(Channel<Integer> in) throws SuspendExecution, InterruptedException {
        Integer v;
        while ((v = in.receive()) != null) {
            System.out.println(v);
        }
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException, SuspendExecution {
        //定义两个Channel
        try (Channel<Integer> naturals = Channels.newChannel(-1); Channel<Integer> squares = Channels.newChannel(-1)) {

            //运行两个Fiber实现.
            new Fiber(() -> {
                for (int i = 0; i < 10; i++) {
                    naturals.send(i);
                }
                naturals.close();
            }).start();

            new Fiber(() -> {
                Integer v;
                while ((v = naturals.receive()) != null) {
                    squares.send(v * v);
                }
                squares.close();
            }).start();

            printer(squares);
        }

    }
}
