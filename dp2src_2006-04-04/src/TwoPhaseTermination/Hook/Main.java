public class Main {
    public static void main(String[] args) {
        System.out.println("main:BEGIN");

        // (1) キャッチされない例外のハンドラを設定する
        Thread.setDefaultUncaughtExceptionHandler(
            new Thread.UncaughtExceptionHandler() {
                public void uncaughtException(Thread thread, Throwable exception) {
                    System.out.println("****");
                    System.out.println("UncaughtExceptionHandler:BEGIN");
                    System.out.println("currentThread = " + Thread.currentThread());
                    System.out.println("thread = " + thread);
                    System.out.println("exception = " + exception);
                    System.out.println("UncaughtExceptionHandler:END");
                }
            }
        );

        // (2) シャットダウン・フックを設定する
        Runtime.getRuntime().addShutdownHook(
            new Thread() {
                public void run() {
                    System.out.println("****");
                    System.out.println("shutdown hook:BEGIN");
                    System.out.println("currentThread = " + Thread.currentThread());
                    System.out.println("shutdown hook:END");
                }
            }
        );

        // (3) 約3秒後に「0による整数の割り算」を行うスレッドを起動する
        new Thread("MyThread") {
            public void run() {
                System.out.println("MyThread:BEGIN");
                System.out.println("MyThread:SLEEP...");

                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                }

                System.out.println("MyThread:DIVIDE");

                // 「0による整数の割り算」
                int x = 1 / 0;

                // ここには来ない
                System.out.println("MyThread:END");
            }
        }.start();

        System.out.println("main:END");
    }
}
