package git.snippets.dp2src.Balking.Sample;

import java.io.IOException;

public class SaverThread extends Thread {
    private final Data data;
    public SaverThread(String name, Data data) {
        super(name);
        this.data = data;
    }
    public void run() {
        try {
            while (true) {
                data.save();            // �f�[�^��ۑ����悤�Ƃ���
                Thread.sleep(1000);     // ��1�b�x��
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
