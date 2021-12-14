import java.util.Map;
import java.util.HashMap;

public class Database<K,V> {
    private final Map<K,V> map = new HashMap<K,V>();

    // ���ׂăN���A����
    public synchronized void clear() {
        verySlowly();
        map.clear();
    }

    // key��value�����蓖�Ă�
    public synchronized void assign(K key, V value) {
        verySlowly();
        map.put(key, value);
    }

    // key�Ɋ��蓖�Ă��l���擾����
    public synchronized V retrieve(K key) {
        slowly();
        return map.get(key);
    }

    // �����Ɏ��Ԃ������邱�Ƃ��V�~�����[�g����
    private void slowly() {
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
        }
    }

    // �����ɂƂĂ����Ԃ������邱�Ƃ��V�~�����[�g����
    private void verySlowly() {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
        }
    }
}
