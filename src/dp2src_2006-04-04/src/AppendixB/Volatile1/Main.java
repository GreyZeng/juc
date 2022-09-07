class Runner extends Thread {
    private boolean quit = false;

    public void run() {
        while (!quit) {
            // ...
        }
        System.out.println("Done");
    }

    public void shutdown() {
        quit = true;
    }
}

public class Main {
    public static void main(String[] args) {
        Runner runner = new Runner();

        // �X���b�h���N������
        runner.start();

        // �X���b�h���I������
        runner.shutdown();
    }
}
