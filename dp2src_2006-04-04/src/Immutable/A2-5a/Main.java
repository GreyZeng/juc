public class Main {
    public static void main(String[] args) {
        // �C���X�^���X�쐬
        Point p1 = new Point(0, 0);
        Point p2 = new Point(100, 0);
        Line line = new Line(p1, p2);

        // �\��
        System.out.println("line = " + line);

        // ��Ԃ�ύX
        p1.x = 150;
        p2.x = 150;
        p2.y = 250;

        // �ēx�\��
        System.out.println("line = " + line);
    }
}
