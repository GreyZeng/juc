package git.snippets.dp2src.ThreadPerMessage.jucSample5;

import java.util.concurrent.Executor;

public class Host {
    private final Helper helper = new Helper();
    private final Executor executor;

    public Host(Executor executor) {
        this.executor = executor;
    }

    public void request(final int count, final char c) {
        System.out.println("    request(" + count + ", " + c + ") BEGIN");
        executor.execute(
                () -> helper.handle(count, c)
        );
        System.out.println("    request(" + count + ", " + c + ") END");
    }
}
