public class Main {
    public static void main(String[] args) {
        // �X���b�hA
        new Thread() {
            public void run() {
                System.out.println(MySystem.getInstance().getDate());
            }
        }.start();

        // �X���b�hB
        new Thread() {
            public void run() {
                System.out.println(MySystem.getInstance().getDate());
            }
        }.start();
    }
}
