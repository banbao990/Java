/**
 * @author banbao
 * @comment modified from example code
 */

import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.tree.*;

public class JTreeDemo extends JFrame {
    final JTree tree = new JTree();
    final JTextField jtf = new JTextField();
    public JTreeDemo() {
        jtf.setEditable(false);
        add(jtf, BorderLayout.NORTH);
        MouseListener ml = new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                int selRow = tree.getRowForLocation(e.getX(), e.getY());
                TreePath selPath = tree.getPathForLocation(e.getX(), e.getY());
                if(selRow != -1) {
                    if(e.getClickCount() == 1) {
                        jtf.setText("Single " + selRow + " " + selPath);
                    } else if(e.getClickCount() == 2) {
                        jtf.setText("Double " + selRow + " " + selPath);
                    }
                }
            }
        };
        tree.addMouseListener(ml);
        add(tree);
    }
    public static void main(String...args) {
        MySwingUtil.run(new JTreeDemo(), 200, 200);
    }
}


/* Output
*/
