public class Log {
    private static final ThreadLocal<TSLog> tsLogCollection = new ThreadLocal<TSLog>();

    // ���O������
    public static void println(String s) {
        getTSLog().println(s);
    }

    // ���O�����
    public static void close() {
        getTSLog().close();
    }

    // �X���b�h�ŗL�̃��O�𓾂�
    private static TSLog getTSLog() {
        TSLog tsLog = tsLogCollection.get();

        // ���̃X���b�h����̌Ăяo�����͂��߂ĂȂ�A�V�K�쐬���ēo�^����
        if (tsLog == null) {
            tsLog = new TSLog(Thread.currentThread().getName() + "-log.txt");
            tsLogCollection.set(tsLog);
            startWatcher(tsLog);
        }

        return tsLog;
    }

    // �X���b�h�̏I����҂X���b�h���N������
    private static void startWatcher(final TSLog tsLog) {
        // �I�����Ď��������̃X���b�h
        final Thread target = Thread.currentThread();
        // target���Ď�����X���b�h
        final Thread watcher = new Thread() {
            public void run() {
                System.out.println("startWatcher for " + target.getName() + " BEGIN");
                try {
                    target.join();
                } catch (InterruptedException e) {
                }
                tsLog.close();
                System.out.println("startWatcher for " + target.getName() + " END");
            }
        };
        // �Ď��̊J�n
        watcher.start();
    }
}
