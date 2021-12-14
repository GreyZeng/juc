import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;

public class Main {
    private static final int TASKS = 10;
    public static void main(String[] args) {
        // ���s����T�[�r�X
        ExecutorService service = Executors.newFixedThreadPool(3);
        try {
            for (int t = 0; t < TASKS; t++) {
                // ���O�ɏ������ރ^�X�N
                Runnable printTask = new Runnable() {
                    public void run() {
                        Log.println("Hello!");
                        Log.close();
                    }
                };
                // �^�X�N�̎��s
                service.execute(printTask);
            }
        } finally {
            service.shutdown();
        }
    }
}
