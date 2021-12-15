package git.snippets.dp2src.ActiveObject.jucSample;


import git.snippets.dp2src.ActiveObject.jucSample.activeobject.ActiveObject;
import git.snippets.dp2src.ActiveObject.jucSample.activeobject.ActiveObjectFactory;

public class Main {
    public static void main(String[] args) {
        ActiveObject activeObject = ActiveObjectFactory.createActiveObject();
        try {
            new MakerClientThread("Alice", activeObject).start();
            new MakerClientThread("Bobby", activeObject).start();
            new DisplayClientThread("Chris", activeObject).start();
            Thread.sleep(5000);
        } catch (InterruptedException e) {
        } finally {
            System.out.println("*** shutdown ***");
            activeObject.shutdown();
        }
    }
}
