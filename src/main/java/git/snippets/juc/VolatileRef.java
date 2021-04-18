package git.snippets.juc;

import java.util.concurrent.TimeUnit;

 

/**
 * 被volatile关键字修饰的对象作为类变量或实例变量时，其对象中携带的类变量和实例变量也相当于被volatile关键字修饰了
 * @author <a href="mailto:410486047@qq.com">Grey</a>
 * @since 1.8
 */
public class VolatileRef {
	volatile M tag = new M();

    public static void main(String[] args) {
    	VolatileRef t = new VolatileRef();
        new Thread(t::m, "t1").start();
        try {
            TimeUnit.SECONDS.sleep(4);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        t.tag.n.x.stop = new Boolean(true);
    }

    void m() {
        while (!tag.n.x.stop) {
        }
    }
}

class M {
    N n = new N();
}

class N {
    X x = new X();
}

class X {
    public Boolean stop = new Boolean(false);
}
