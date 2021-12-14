public class Bank {
    private int money;
    private String name;

    public Bank(String name, int money) {
        this.name = name;
        this.money = money;
    }

    // �a������
    public synchronized void deposit(int m) {
        money += m;
    }

    // �����o��
    public synchronized boolean withdraw(int m) {
        if (money >= m) {
            money -= m;
            return true;    // �����o����
        } else {
            return false;   // �c���s��
        }
    }

    public String getName() {
        return name;
    }
}
