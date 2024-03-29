package git.snippets.dp2src.introduction1.jucThreadFactory;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

public class Main {
    public static void main(String[] args) {
        ThreadFactory factory = Executors.defaultThreadFactory();
        factory.newThread(new Printer("Nice!")).start();
        for (int i = 0; i < 10; i++) {
            System.out.print("Good!");
        }
    }
}
