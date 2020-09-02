/**
 * @author banbao
 * @comment modified from example code
 * @exercise 6
 */

import static net.mindview.util.SwingConsole.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class WhetherMatch extends JFrame {
    private JTextField 
        tf1 = new JTextField("���ڴ������ַ���", 20),
        tf2 = new JTextField("���ڴ�����������ʽ", 20);
    private JButton
        b1 = new JButton("����!"),
        b2 = new JButton("Ready!");
        
   public WhetherMatch() {
       setLayout(new FlowLayout());
        add(tf1);
        add(tf2);
        /*
        b1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String str = tf1.getText();
                String regex = tf2.getText();
                b2.setText("" + str.matches(regex));
            }
        });
        */
        b1.addActionListener(
            (e) -> {
                String str = tf1.getText();
                String regex = tf2.getText();
                b2.setText("" + str.matches(regex));
            }
        );
        add(b1);
        add(b2);
        setTitle("ƥ��");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300, 300);
        setVisible(true);
    }
    public static void main(String...args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new WhetherMatch();
            }
        });
    }
}


/* Output
*/