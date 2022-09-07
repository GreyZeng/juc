// �~���������삷�邱�Ƃ͕ۏ؂���Ȃ�
import java.util.Date;

public class MySystem {
    private static MySystem instance = null;
    private Date date = new Date();
    private MySystem() {
    }
    public Date getDate() {
        return date;
    }
    public static MySystem getInstance() {
        if (instance == null) {                 // (a) 1��ڂ�test
            synchronized (MySystem.class) {     // (b) synchronized�u���b�N�ɓ���
                if (instance == null) {         // (c) 2��ڂ�test
                    instance = new MySystem();  // (d) set
                }
            }                                   // (e) synchronized�u���b�N����o��
        }
        return instance;                        // (f)
    }
}
