/**
 * @author banbao
 * @comment modified from example code
 */

import java.awt.BorderLayout;
import javax.swing.JTextArea;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

public class MenuInMenu extends JFrame{
    public MenuInMenu() {
        JMenuBar jmb = new JMenuBar();
        JMenu[] 
            jm1 = { 
                new JMenu("A"),
                new JMenu("B"),
                new JMenu("C"), 
            },
            jm2 = { 
                new JMenu("AA"),
                new JMenu("BB"),
                new JMenu("CC"), 
            },
            jm3 = { 
                new JMenu("AAA"),
                new JMenu("BBB"),
                new JMenu("CCC"), 
            };
        JMenuItem[]
            jmi1 = { 
                new JMenuItem("AAAA"),
                new JMenuItem("BBBB"),
                new JMenuItem("CCCC"), 
            };
        JTextArea jta = new JTextArea("NO USE!");
        jta.setEditable(false);
        for(int i = 0;i < 3;++i) { jm3[i].add(jmi1[i]); }
        for(JMenu jm : jm3) { jm2[0].add(jm); }
        for(JMenu jm : jm2) { jm1[0].add(jm); }
        for(JMenu jm : jm1) { jmb.add(jm); }
        add(BorderLayout.NORTH, jmb);
        add(jta);
        pack();
    }
    public static void main(String...args) {
        MySwingUtil.run(new MenuInMenu(), -1, -1);
    }
}


/* Output
*/
