package git.snippets.dp2src.ActiveObject.Sample;


import git.snippets.dp2src.ActiveObject.Sample.activeobject.ActiveObject;
import git.snippets.dp2src.ActiveObject.Sample.activeobject.ActiveObjectFactory;

public class Main {
    public static void main(String[] args) {
        ActiveObject activeObject = ActiveObjectFactory.createActiveObject();
        new MakerClientThread("Alice", activeObject).start();
        new MakerClientThread("Bobby", activeObject).start();
        new DisplayClientThread("Chris", activeObject).start();
    }
}
