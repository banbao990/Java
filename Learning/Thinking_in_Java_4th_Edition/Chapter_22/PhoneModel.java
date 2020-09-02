/**
 * @author banbao
 * @comment modified from example code
 */

import java.awt.*;
import javax.swing.*;
import java.util.*;

public class PhoneModel extends JFrame {
    // private JPanel panel;
    public PhoneModel(String str) {
        super(str);
        init();
        // setContentPane(panel);
        pack();
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    private void makeButton(String name,
                              GridBagLayout gbl,
                              GridBagConstraints gbc) {
        JButton jb = new JButton(name);
        gbl.setConstraints(jb, gbc);
        add(jb);
        // gbl.addLayoutComponent(jb, gbc);
        // panel.add(jb);
    }

    private void init() {
        // 布局管理器
        GridBagLayout gbl = new GridBagLayout();
        // 约束(每一个构件都需要有一个约束)
        // 不需要每次更新,因为在 setConstraints() 内部调用了 clone()
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH; // 太大了向四周扩展
        // 设置布局
        setLayout(gbl);
        // panel = new JPanel(gbl);
        // 屏幕
        gbc.weightx = gbc.weighty = 1.0;
        gbc.ipady = 150; // 变高
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 3;
        gbc.gridheight = 4;
        gbc.insets = new Insets(10, 10, 10, 10); // 设置外边框
        // makeButton("Screen", gbl, gbc);
        JTextArea jta = new JTextArea();
        jta.setEditable(false);
        gbl.setConstraints(jta, gbc);
        add(jta);
        
        gbc.ipady = 0; // 恢复原来高度
        gbc.weighty = 0; // 竖直不扩展
        gbc.insets = new Insets(5, 5, 5, 5); // 设置外边框
        // 功能按键组
        // 左功能键
        gbc.gridy = 4;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        makeButton("···", gbl, gbc);

        // 中间功能键
        gbc.gridx = 1;
        gbc.gridheight = 2;
        makeButton("<^>", gbl, gbc);

        // 右功能键
        gbc.gridx = 2;
        gbc.gridheight = 1;
        makeButton("···", gbl, gbc);

        // 拨号键
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        makeButton("G", gbl, gbc);

        // 挂机键
        gbc.gridx = 2;
        makeButton("R", gbl, gbc);

        // 数字按键组
        for(int i = 1;i <= 12; ++i) {
            gbc.gridx = (i - 1)%3;
            gbc.gridy = 6 + (i - 1)/3;
            String sign = (i<=9) ? Integer.toString(i) : getSign(i);
            makeButton(sign, gbl, gbc);
        }
    }

    private String getSign(int i) {
        if(i == 10) return "*";
        if(i == 11) return "0";
        if(i == 12) return "#";
        return null;
    }
    public static void main(String...args) {
        SwingUtilities.invokeLater(
            () -> {
                new PhoneModel("PhoneModel");
            }
        );
    }
}