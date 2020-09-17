/**
 * @author banbao
 * @comment modified from example code
 */

import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;

public class Ex29 extends JFrame {
    private JTextField color = new JTextField();
    private JButton
        colorSelector = new JButton("Select Color");
    public Ex29() {
        color.setEditable(false);
        colorSelector.addActionListener(
            (e) -> {
                Color chosenColor = JColorChooser.showDialog(
                    null, "Choose a Color", Color.RED);
                color.setText(chosenColor.toString());
            }
        );
        add(colorSelector, BorderLayout.SOUTH);
        color.setEditable(false);
        add(color, BorderLayout.NORTH);
    }
    public static void main(String...args) {
        MySwingUtil.run(new Ex29(), 200, 200);
    }
}


/* Output
*/
