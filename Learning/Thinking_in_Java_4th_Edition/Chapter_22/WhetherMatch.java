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
        tf1 = new JTextField("请在此输入字符串", 20),
        tf2 = new JTextField("请在此输入正则表达式", 20);
    private JButton
        b1 = new JButton("测试!"),
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
        setTitle("匹配");
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