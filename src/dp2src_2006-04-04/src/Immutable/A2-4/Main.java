public class Main {
    public static void main(String[] args) {
        // �C���X�^���X�쐬
        UserInfo userinfo = new UserInfo("Alice", "Alaska");

        // �\��
        System.out.println("userinfo = " + userinfo);

        // ��Ԃ�ύX
        StringBuffer info = userinfo.getInfo();
        info.replace(12, 17, "Bobby");  // 12�ȏ�17������"Alice"�̈ʒu

        // �ēx�\��
        System.out.println("userinfo = " + userinfo);
    }
}
