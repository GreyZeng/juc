package git.snippets.dp2src.ActiveObject.jucSample;


import git.snippets.dp2src.ActiveObject.jucSample.activeobject.ActiveObject;

import java.util.concurrent.CancellationException;
import java.util.concurrent.RejectedExecutionException;

public class DisplayClientThread extends Thread {
    private final ActiveObject activeObject;

    public DisplayClientThread(String name, ActiveObject activeObject) {
        super(name);
        this.activeObject = activeObject;
    }

    public void run() {
        try {
            for (int i = 0; true; i++) {
                String string = Thread.currentThread().getName() + " " + i;
                activeObject.displayString(string);
                Thread.sleep(200);
            }
        } catch (RejectedExecutionException | CancellationException | InterruptedException e) {
            System.out.println(Thread.currentThread().getName() + ":" + e);
        }
    }
}
