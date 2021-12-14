import java.util.Random;

import java.util.concurrent.Semaphore;

class Log {
    public static void println(String s) {
        System.out.println(Thread.currentThread().getName() + ": " + s);
    }
}

// ���̐��������郊�\�[�X
class BoundedResource {
    private final Semaphore semaphore;
    private final int permits;
    private final static Random random = new Random(314159);

    // �R���X�g���N�^(permits�̓��\�[�X�̌�)
    public BoundedResource(int permits) {
        this.semaphore = new Semaphore(permits);
        this.permits = permits;
    }

    // ���\�[�X���g�p����
    public void use() throws InterruptedException {
        semaphore.acquire();
        try {
            doUse();
        } finally {
            semaphore.release();
        }
    }

    // ���\�[�X�����ۂɎg�p����(�����ł�Thread.sleep���Ă��邾��)
    protected void doUse() throws InterruptedException {
        Log.println("BEGIN: used = " + (permits - semaphore.availablePermits()));
        Thread.sleep(random.nextInt(500));
        Log.println("END:   used = " + (permits - semaphore.availablePermits()));
    }
}

// ���\�[�X�𗘗p����X���b�h
class UserThread extends Thread {
    private final static Random random = new Random(26535);
    private final BoundedResource resource;

    public UserThread(BoundedResource resource) {
        this.resource = resource;
    }

    public void run() {
        try {
            while (true) {
                resource.use();
                Thread.sleep(random.nextInt(3000));
            }
        } catch (InterruptedException e) {
        }
    }
}

public class Main {
    public static void main(String[] args) {
        // 3�̃��\�[�X��p�ӂ���
        BoundedResource resource = new BoundedResource(3);

        // 10�̃X���b�h�����p����
        for (int i = 0; i < 10; i++) {
            new UserThread(resource).start();
        }
    }
}
