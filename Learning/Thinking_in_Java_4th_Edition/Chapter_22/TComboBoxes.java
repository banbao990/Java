//: gui/ComboBoxes.java
// Using drop-down lists.
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class TComboBoxes extends JFrame {
    private String[] week = {
        "Monday", "Tuesday", "Wednesday", "Thursday",
        "Friday", "Saturday", "Sunday",
    };
    private JTextField t = new JTextField(15);
    private JComboBox<String> c = new JComboBox<>();
    private JButton b1 = new JButton("Change to Alldays");
    private JButton b2 = new JButton("Input/Select");
    private boolean workday = true;
    public TComboBoxes() {
        for(int i = 0; i < 5; ++i) {
            c.addItem(week[i]);
        }
        t.setEditable(false);
        b1.addActionListener(
            (e) -> {
                if(workday) {
                    workday = false;
                    c.addItem(week[5]);
                    c.addItem(week[6]);
                    b1.setText("Change to Workdays");
                } else {
                    workday = true;
                    c.removeItem(week[5]);
                    c.removeItem(week[6]);
                    b1.setText("Change to Alldays");
                }
            }
        );
        b2.addActionListener(
            (e) -> {
                c.setEditable(!c.isEditable());
            }
        );
        c.addActionListener(
            (e) -> {
                t.setText(
                    ((JComboBox)e.getSource())
                    .getSelectedItem()
                    .toString()
        );});
        setLayout(new FlowLayout());
        add(t);
        add(c);
        add(b1);
        add(b2);
    }
    public static void main(String[] args) {
        MySwingUtil.run(new TComboBoxes(), 200, 175);
    }
}