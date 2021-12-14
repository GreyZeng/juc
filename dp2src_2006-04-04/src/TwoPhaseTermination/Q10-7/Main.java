public class Main {
    public static void main(String[] args) {
        System.out.println("main: BEGIN");
        try {
            // スレッドの起動
            HanoiThread t = new HanoiThread();
            t.start();

            // 少し時間をあける
            Thread.sleep(10000);

            // スレッドの終了要求
            System.out.println("main: shutdownRequest");
            t.shutdownRequest();

            // スレッドの終了を待つ
            System.out.println("main: join");
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("main: END");
    }
}
