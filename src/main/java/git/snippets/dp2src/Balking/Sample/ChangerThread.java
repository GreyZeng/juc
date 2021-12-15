package git.snippets.dp2src.Balking.Sample;

import java.io.IOException;
import java.util.Random;

public class ChangerThread extends Thread {
    private final Data data;
    private final Random random = new Random();
    public ChangerThread(String name, Data data) {
        super(name);
        this.data = data;
    }
    public void run() {
        try {
            for (int i = 0; true; i++) {
                data.change("No." + i);             // �f�[�^��ύX����
                Thread.sleep(random.nextInt(1000)); // �d���̂���
                data.save();                        // �����I�ɕۑ�����
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
