package git.snippets.dp2src.TwoPhaseTermination.Hook;

public class Main {
    public static void main(String[] args) {
        System.out.println("main:BEGIN");

        Thread.setDefaultUncaughtExceptionHandler(
                (thread, exception) -> {
                    System.out.println("****");
                    System.out.println("UncaughtExceptionHandler:BEGIN");
                    System.out.println("currentThread = " + Thread.currentThread());
                    System.out.println("thread = " + thread);
                    System.out.println("exception = " + exception);
                    System.out.println("UncaughtExceptionHandler:END");
                }
        );

        Runtime.getRuntime().addShutdownHook(
                new Thread(() -> {
                    System.out.println("****");
                    System.out.println("shutdown hook:BEGIN");
                    System.out.println("currentThread = " + Thread.currentThread());
                    System.out.println("shutdown hook:END");
                })
        );

        new Thread("MyThread") {
            public void run() {
                System.out.println("MyThread:BEGIN");
                System.out.println("MyThread:SLEEP...");

                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                }

                System.out.println("MyThread:DIVIDE");

                int x = 1 / 0;

                System.out.println("MyThread:END");
            }
        }.start();

        System.out.println("main:END");
    }
}
