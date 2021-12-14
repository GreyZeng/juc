package activeobject;

import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

// ActiveObjectインタフェースの実装クラス
class ActiveObjectImpl implements ActiveObject {
    private final ExecutorService service = Executors.newSingleThreadExecutor();

    // サービスの終了
    public void shutdown() {
        service.shutdown();
    }

    // 戻り値のある呼び出し
    public Future<String> makeString(final int count, final char fillchar) {
        // リクエスト
        class MakeStringRequest implements Callable<String> {
            public String call() {
                char[] buffer = new char[count];
                for (int i = 0; i < count; i++) {
                    buffer[i] = fillchar;
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                    }
                }
                return new String(buffer);
            }
        }
        // リクエストの発行
        return service.submit(new MakeStringRequest());
    }

    // 戻り値のない呼び出し
    public void displayString(final String string) {
        // リクエスト
        class DisplayStringRequest implements Runnable {
            public void run() {
                try {
                    System.out.println("displayString: " + string);
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                }
            }
        }
        // リクエストの発行
        service.execute(new DisplayStringRequest());
    }
}
