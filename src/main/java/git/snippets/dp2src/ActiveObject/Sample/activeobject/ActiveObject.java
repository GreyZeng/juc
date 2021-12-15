package git.snippets.dp2src.ActiveObject.Sample.activeobject;

public interface ActiveObject {
    Result<String> makeString(int count, char fillchar);
    void displayString(String string);
}
