public class Host {
    public static void execute(int count) {
        for (int i = 0; i < count; i++) {
            doHeavyJob();
        }
    }
    private static void doHeavyJob() {
        // �ȉ��́A
        // �u�L�����Z���s�\�ȏd�������v�̑�p
        // �i��10�b�ԉ�郋�[�v�j
        System.out.println("doHeavyJob BEGIN");
        long start = System.currentTimeMillis();
        while (start + 10000 > System.currentTimeMillis()) {
            // busy loop
        }
        System.out.println("doHeavyJob END");
    }
}
