public class Main {
    private static final int TASKS = 10;
    public static void main(String[] args) {
        for (int t = 0; t < TASKS; t++) {
            // ���O�ɏ������ރ^�X�N
            Runnable printTask = new Runnable() {
                public void run() {
                    Log.println("Hello!");
                    Log.close();
                }
            };
            // �^�X�N�̎��s
            new Thread(printTask).start();
        }
    }
}
