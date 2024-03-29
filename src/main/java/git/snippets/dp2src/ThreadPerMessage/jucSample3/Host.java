package git.snippets.dp2src.ThreadPerMessage.jucSample3;

import java.util.concurrent.ThreadFactory;

public class Host {
    private final Helper helper = new Helper();
    private final ThreadFactory threadFactory;

    public Host(ThreadFactory threadFactory) {
        this.threadFactory = threadFactory;
    }

    public void request(final int count, final char c) {
        System.out.println("    request(" + count + ", " + c + ") BEGIN");
        threadFactory.newThread(
                () -> helper.handle(count, c)
        ).start();
        System.out.println("    request(" + count + ", " + c + ") END");
    }
}
