import java.io.IOException;
import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.SwingUtilities;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.JPanel;

import activeobject.ActiveObjectFactory;
import activeobject.ActiveObject;
import activeobject.Result;

public class MyFrame extends JFrame implements ActionListener {
    private final JTextField textfield = new JTextField("word", 10);
    private final JButton button = new JButton("Search");
    private final JTextArea textarea = new JTextArea(20, 30);
    private final ActiveObject activeObject = ActiveObjectFactory.createActiveObject();
    private final static String NEWLINE = System.getProperty("line.separator");

    public MyFrame() {
        super("ActiveObject Sample");
        getContentPane().setLayout(new BorderLayout());

        // North
        JPanel north = new JPanel();
        north.add(new JLabel("Search:"));
        north.add(textfield);
        north.add(button);
        button.addActionListener(this);

        // Center
        JScrollPane center = new JScrollPane(textarea);

        // Layout
        getContentPane().add(north, BorderLayout.NORTH);
        getContentPane().add(center, BorderLayout.CENTER);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setVisible(true);
    }

    // Searchボタンが押されたとき
    public void actionPerformed(ActionEvent e) {
        searchWord(textfield.getText());
    }

    // 表示
    private void println(String line) {
        textarea.append(line + NEWLINE);
    }

    // 検索
    private void searchWord(final String word) {
        // 検索の呼び出し
        final Result<String> result = activeObject.search(word);
        println("Searching " + word + "...");
        // 検索結果を待つスレッド
        new Thread() {
            public void run() {
                // 結果を待つ
                final String url = result.getResultValue();
                // 結果が得られたのでイベント・ディスパッチング・スレッドに表示を依頼
                SwingUtilities.invokeLater(
                    new Runnable() {
                        public void run() {
                            MyFrame.this.println("word = " + word + ", URL = " + url);
                        }
                    }
                );
            }
        }.start();
    }
}
