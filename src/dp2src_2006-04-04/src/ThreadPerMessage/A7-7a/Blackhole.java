public class Blackhole {
    public static void enter(Object obj) {
        System.out.println("Step 1");
        magic(obj);
        System.out.println("Step 2");
        synchronized (obj) {
            System.out.println("Step 3 (never reached here)");  // �����ɂ͂��Ȃ�
        }
    }
    public static void magic(final Object obj) {
        // thread��obj�̃��b�N������Ė������[�v����X���b�h
        // thread�̖��O���K�[�h�����Ƃ��Ďg��
        Thread thread = new Thread() {      // inner class
            public void run() {
                synchronized (obj) {        // ������obj�̃��b�N�����
                    synchronized (this) {
                        this.setName("Locked"); // �K�[�h�����̕ω�
                        this.notifyAll();       // obj�̃��b�N����������Ƃ�ʒm
                    }
                    while (true) {
                        // �������[�v
                    }
                }
            }
        };
        synchronized (thread) {
            thread.setName("");
            thread.start();         // �X���b�h�̋N��
            // Guarded Suspension�p�^�[��
            while (thread.getName().equals("")) {
                try {
                    thread.wait();  // �V�����X���b�h��obj�̃��b�N�����̂�҂�
                } catch (InterruptedException e) {
                }
            }
        }
    }
}
