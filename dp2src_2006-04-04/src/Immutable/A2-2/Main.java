public class Main {
    public static void main(String[] args) {
        String s = "BAT";
        String t = s.replace('B', 'C'); // 'B'��'C'�ɒu��
        System.out.println("s = " + s); // replace�����s�������s
        System.out.println("t = " + t); // replace�̖߂�lt
        if (s == t) {
            System.out.println("s == t");
        } else {
            System.out.println("s != t");
        }
    }
}
