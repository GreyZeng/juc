public class Main {
    public static void main(String[] args) {
        // スレッドを生成する
        Thread t = new Thread() {
            public void run() {
                while (true) {
                    try {
                        if (Thread.currentThread().isInterrupted()) {
                            throw new InterruptedException();
                        }
                        System.out.print(".");
                    } catch (InterruptedException e) {
                        System.out.print("*");
                    }
                }
            }
        };

        // スレッドを起動する
        t.start();

        // 5秒待つ
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
        }

        // スレッドに一度だけinterruptをかける
        t.interrupt();
    }
}
