class Something {
    // final�ȃC���X�^���X�t�B�[���h
    private final int x;
    // �N���X�t�B�[���h
    private static Something last = null;

    // �R���X�g���N�^
    private Something() {
        // final�t�B�[���h�𖾎��I�ɏ���������
        x = 123;
    }

    // new�œ���ꂽ�C���X�^���X��last�֑������
    public static Something create() {
        last = new Something();
        return last;
    }

    // last�o�R��final�t�B�[���h�̒l��\������
    public static void print() {
        if (last != null) {
            System.out.println(last.x);
        }
    }
}

public class Main {
    public static void main(String[] args) {
        // �X���b�hA
        new Thread() {
            public void run() {
                Something.create();
            }
        }.start();

        // �X���b�hB
        new Thread() {
            public void run() {
                Something.print();
            }
        }.start();
    }
}
