/**
 * @author banbao
 * @comment modified from example code
 */

import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;

public class Ex22 extends JFrame {
    private class MyChangeListener implements ChangeListener {
        @Override
        public void stateChanged(ChangeEvent e) {
            int r = red.getValue(),
                g = green.getValue(),
                b = blue.getValue();
            jp.setBackground(new Color(r, g, b));
            jtf.setText(String.format("(R,G,B):(%d,%d,%d)",r,g,b));
            validate();
        }
    }
    // component
    private JPanel jp = new JPanel();
    private JTextField jtf = new JTextField("Show RGB Here!");
    private JSlider 
        red = new JSlider(0, 255, 255),
        green = new JSlider(0, 255, 245),
        blue = new JSlider(0, 255, 238);
    private MyChangeListener cl = new MyChangeListener();
    public Ex22() {
        // supporting component
        JPanel jp2 = new JPanel();
        // setLayout(new BorderLayout());
        jtf.setEditable(false);
        red.addChangeListener(cl);
        green.addChangeListener(cl);
        blue.addChangeListener(cl);
        red.setToolTipText("Red");
        green.setToolTipText("Green");
        blue.setToolTipText("Blue");
        jp2.setLayout(new GridLayout(3, 1));
        jp2.add(red); jp2.add(green); jp2.add(blue);
        add(jtf, BorderLayout.NORTH);
        add(jp);
        add(jp2, BorderLayout.SOUTH);
        // initial
        cl.stateChanged(new ChangeEvent(red));
    }
    public static void main(String...args) {
        MySwingUtil.run(new Ex22(), 200, 250);
    }
}


/* Output
*/
