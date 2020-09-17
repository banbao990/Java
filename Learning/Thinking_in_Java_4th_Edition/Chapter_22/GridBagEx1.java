/**
 * @author banbao
 * @comment doc:java/awt/GridBagLayout.html
 */

import java.awt.*;
import javax.swing.*;
import java.util.*;

public class GridBagEx1 extends JFrame {
    public GridBagEx1(String str) {
        super(str);
        init();
        setSize(500, 190);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    protected void makebutton(String name,
                              GridBagLayout gridbag,
                              GridBagConstraints c) {
        JButton button = new JButton(name);
        gridbag.setConstraints(button, c);
        add(button);
    }

    public void init() {
        // 布局管理器
        GridBagLayout gridbag = new GridBagLayout();
        // 约束(每一个构件都需要有一个约束)
        GridBagConstraints c = new GridBagConstraints();
        
        // 设置字体以及布局管理器
        setFont(new Font("SansSerif", Font.PLAIN, 14));
        setLayout(gridbag);

        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1.0;
        makebutton("Button1", gridbag, c);
        makebutton("Button2", gridbag, c);
        makebutton("Button3", gridbag, c);

        c.gridwidth = GridBagConstraints.REMAINDER; //end row
        makebutton("Button4", gridbag, c);

        c.weightx = 0.0;                //reset to the default
        makebutton("Button5", gridbag, c); //another row

        c.gridwidth = GridBagConstraints.RELATIVE; //next-to-last in row
        makebutton("Button6", gridbag, c);

        c.gridwidth = GridBagConstraints.REMAINDER; //end row
        makebutton("Button7", gridbag, c);

        c.gridwidth = 1;                //reset to the default
        c.gridheight = 2;
        c.weighty = 1.0;
        makebutton("Button8", gridbag, c);

        c.weighty = 0.0;                //reset to the default
        c.gridwidth = GridBagConstraints.REMAINDER; //end row
        c.gridheight = 1;               //reset to the default
        makebutton("Button9", gridbag, c);
        makebutton("Button10", gridbag, c);

        pack();
    }

    public static void main(String args[]) {
        SwingUtilities.invokeLater(
            () -> {
                new GridBagEx1("GridBag Layout Example");
            }
        );
    }
}
