package git.snippets.juc;

import java.util.Random;
import java.util.concurrent.Phaser;
import java.util.concurrent.TimeUnit;

public class PhaserUsage {
    static final Random R = new Random();
    static WeddingPhaser phaser = new WeddingPhaser();

    static void millSleep() {
        try {
            TimeUnit.MILLISECONDS.sleep(R.nextInt(1000));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        // 宾客的人数
        final int guestNum = 5;
        // 新郎和新娘
        final int mainNum = 2;
        phaser.bulkRegister(mainNum + guestNum);
        for (int i = 0; i < guestNum; i++) {
            new Thread(new Person("宾客" + i)).start();
        }
        new Thread(new Person("新娘")).start();
        new Thread(new Person("新郎")).start();
    }

    static class WeddingPhaser extends Phaser {
        @Override
        protected boolean onAdvance(int phase, int registeredParties) {
            switch (phase) {
                case 0:
                    System.out.println("所有人到齐");
                    return false;
                case 1:
                    System.out.println("所有人吃饭");
                    return false;
                case 2:
                    System.out.println("所有人离开");
                    return false;
                case 3:
                    System.out.println("新郎新娘拥抱");
                    return true;
                default:
                    return true;
            }
        }
    }
    static class Person implements Runnable {
        String name;
        Person(String name) {
            this.name = name;
        }
        @Override
        public void run() {
            // 先到达婚礼现场
            arrive();
            // 吃饭
            eat();
            // 离开
            leave();
            // 拥抱，只保留新郎和新娘两个线程可以执行
            hug();
        }
        private void arrive() {
            millSleep();
            System.out.println("name:" + name + " 到来");
            phaser.arriveAndAwaitAdvance();
        }
        private void eat() {
            millSleep();
            System.out.println("name:" + name + " 吃饭");
            phaser.arriveAndAwaitAdvance();
        }
        private void leave() {
            millSleep();
            System.out.println("name:" + name + " 离开");
            phaser.arriveAndAwaitAdvance();
        }
        private void hug() {
            if ("新娘".equals(name) || "新郎".equals(name)) {
                millSleep();
                System.out.println("新娘新郎拥抱");
                phaser.arriveAndAwaitAdvance();
            } else {
                phaser.arriveAndDeregister();
            }
        }
    }
}
