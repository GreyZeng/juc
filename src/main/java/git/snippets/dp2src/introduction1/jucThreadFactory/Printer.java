package git.snippets.dp2src.introduction1.jucThreadFactory;

public class Printer implements Runnable {
    private final String message;

    public Printer(String message) {
        this.message = message;
    }

    public void run() {
        for (int i = 0; i < 10; i++) {
            System.out.print(message);
        }
    }
}
