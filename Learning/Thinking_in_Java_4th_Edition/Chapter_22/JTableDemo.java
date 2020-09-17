/**
 * @author banbao
 * @comment modified from example code
 */

import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import javax.swing.table.*;

public class JTableDemo extends JFrame {
    final JTable table;
    final JTextField jtf = new JTextField();
    public JTableDemo() {
        TableModel dataModel = new AbstractTableModel() {
            public int getColumnCount() { return 10; }
            public int getRowCount() { return 10; }
            public Object getValueAt(int row, int col) {
                return new Integer(row*col);
            }
        };
        table = new JTable(dataModel);
        dataModel.addTableModelListener(new TableModelListener(){
            @Override
            public void tableChanged(TableModelEvent e) {
                jtf.setText(e.toString());
            }});
        JScrollPane scrollpane = new JScrollPane(table);
        add(scrollpane);
        jtf.setEditable(false);
        add(jtf, BorderLayout.NORTH);
    }
    public static void main(String...args) {
        MySwingUtil.run(new JTableDemo(), 200, 200);
    }
}


/* Output
*/
