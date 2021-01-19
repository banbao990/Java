# Chapter_22 图形化用户界面

> [总目录](../README.md)

---

[TOC]

---

+ `GUI`：`graphical user interface`（用户图形界面）
+ 设计原则：让简单的事情变得容易，让困难的事情变得可行
+ `Java 1.0` 中的 `AWT`（`abstract window toolkit` 抽象窗口工具包）
    + 表现的并不好，限制很多，只能使用 `4` 种字体
    + 不是面向对象的
+ `Java 1.1` 在 `AWT` 中引入了**事件模型**
+ `Java 2` 开始 **`Java` 基础类库**（`JFC`）几乎替换了所有内容
+ `GUI` 部分称为 `Swing`
    + `Swing` 是一组易于使用、易于理解的 `JavaBeans`
    + 可以通过 **拖放** 进行编程，也可以手动编程



## 22.1 applet

+ 基本上被废弃了



## 22.2 Swing 基础

+ 大多数 `Swing` 应用都被构建在基础的 `JFrame` 内部
+ `JFrame` 在任何操作系统中都可以创建视窗视图
+ [HelloSwing](../Example_Code/gui/HelloSwing.java)

```java
//: gui/HelloSwing.java
import javax.swing.*;
public class HelloSwing {
    public static void main(String[] args) {
        // 设置标题
        JFrame frame = new JFrame("Hello Swing");

        // 点击'x'发生的事件
        // 默认行为什么也不做(也不会关闭程序)
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // 设置大小(像素为单位)
        frame.setSize(300, 100);

        // 显示 Jframe(不设置就没了)
        frame.setVisible(true);
    }
}
```

+ 在 `JFrame` 中添加 `JLabel`

```java
JFrame frame = new JFrame("Hello Swing");
JLabel label = new JLabel("A Label");

// 添加 JLabel
frame.add(label);

// 设置文字
label.setText("Hey! This is Different!");
```

+ `Swing` 有自己专用的线程来接收 `UI` 事件并更新屏幕
    + 如果程序员从其他线程着手对屏幕进行操作，可能会引发各种并发问题
+ 通用方式

```java
// 这种技术是来自于 AWT 类库
SwingUtilities.invokeLater(Runnable)
```

+ 例如上述代码中的修改 `JLabel` 内的文字

```java
SwingUtilities.invokeLater(new Runnable() {
    public void run() {
        label.setText("Hey! This is Different!");
    }
});
```

+ 当程序中的所有代码都遵循通过 `SwingUtilities.invokeLater()` 来提交操作的方式
    + 整个程序就不会有任何并发问题
    + **包括启动程序本身**（`main` 也不应该调用 `Swing` 方法，也需要向事件队列提交任务）
+ [SubmitSwingProgram](../Example_Code/gui/SubmitSwingProgram.java)

```java
//: gui/SubmitSwingProgram.java
import javax.swing.*;
import java.util.concurrent.*;

public class SubmitSwingProgram extends JFrame {
    JLabel label;
    public SubmitSwingProgram() {
        super("Hello Swing");
        label = new JLabel("A Label");
        add(label);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300, 100);
        setVisible(true);
    }
    static SubmitSwingProgram ssp;
    public static void main(String[] args) throws Exception {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() { ssp = new SubmitSwingProgram(); }
        });
        TimeUnit.SECONDS.sleep(1);
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                ssp.label.setText("Hey! This is Different!");
            }
        });
    }
}
```

+ 注意 `sleep()` 中止事件分发线程不是一个好主意



### 22.2.1 一个显示框架

+ 包装构造函数 [SwingConsole](../Example_Code/net/mindview/util/SwingConsole.java)



## 22.3 创建按钮

+ [Button1](../Example_Code/gui/Button1.java)

```java
// 持有引用
private JButton
    b1 = new JButton("Button 1"),
    b2 = new JButton("Button 2");

// 在构造器中加入 Button
public Button1() {
    setLayout(new FlowLayout()); // 布局管理器
    add(b1);
    add(b2);
}
```

+ 布局管理器 `Layout`
    + `JFrame` 默认 `BorderLayout`
+ `FlowLayout` 使得控件可以在窗体从左到右、从上到下均匀连续分布



## 22.4 捕获事件

+ **事件驱动编程**
    + 将事件同处理时间的代码连接起来
    + `Swing`：接口（图形组件），实现（当和组件相关的事件发生时，需要执行的代码）
+ **注册特定事件**
+ 例如对于 `JButton` 来说，注册点击事件 `addActionListener(ActionListener)`
+ [Button2](../Example_Code/gui/Button2.java)
    + `JTextField`

```java
b1.addActionListener(new ActionListener() {
    public void actionPerformed(ActionEvent e) {
        String name = ((JButton)e.getSource()).getText();
        txt.setText(name);
    }
});
```

+ `ActionListener` 常常使用匿名类实现

```java
// lambda 表达式实现
b1.addActionListener(
    (e) -> {
        String name = ((JButton)e.getSource()).getText();
        txt.setText(name);
    }
);
```



## 22.5 文本区域

+ `JTextField`：单行文本
+ `JTextArea`：多行文本
    + `append()` 方法
+ `JScrollPane` 控制上下左右滚动



## 22.6 控制布局

+ 布局管理器管理布局
+ `Container` 都有 `setLayout()` 的方法来设置布局

```java
BorderLayout.java
CardLayout.java
FlowLayout.java
GridBagLayout.java
GridLayout.java
```



### 22.6.1 BorderLayout

+ `JFrame` 的默认布局管理器
+ 有 `5` 个位置

```java
// 除了 CENTER 之外
// 加入的组件将被沿着一个方向压缩到最小尺寸
// 同时在另一个方向上拉伸到最大尺寸

BorderLayout.NORTH
BorderLayout.SOUTH
BorderLayout.EAST
BorderLayout.WEST
BorderLayout.CENTER // 中间向四周延展,直至触碰到其他组件或者边框(以覆盖中央区域)
```

```java
// 示例用法
add(BorderLayout.NORTH, new JButton("North"));
// 默认 CENTER
add(new JButton("Center"));
// 重复添加会被覆盖
add(new JButton("Center2"));
```

+ [示例代码](../Example_Code/gui/BorderLayout1.java)



### 22.6.2 FlowLayout

+ 直接将组件从左到右 “流动” 到窗体上，直至占满上方的空间，然后向下移一行
+ **从左到右，从上到下**
+ 组件将呈现出合适的大小（`JButton` 就是标签的大小）
+ [示例代码](../Example_Code/gui/FlowLayout1.java)



### 22.6.3 GridLayout

+ 创建一个用于放置组件的表格（行 `x` 列）
+ 按照**从左到右、从上到下**的顺序加入
+ [示例代码](../Example_Code/gui/GridLayout1.java)



### 22.6.4 GridBagLayout

+ 强大的功能，一般适用于 `GUI` 构造工具
+ `TableLayout` 作为一种替代品（可从[官网](http://java.sun.com)下载）
+ 一个简单的示例程序
    + [PhoneModel](PhoneModel.java)



### 22.6.5 绝对定位

+ `setLayout(null)`
+ 为每一个组件调用 `setBounds() / reshape()` 方法
    + 可以在构造器或者 `paint()` 中调用这些方法



### 22.6.6 BoxLayout

+ `javax.swing.BoxLayout`
+ `GridLayout` 的简化版本



### 22.6.7 最好的方式是什么

+ `GUI` 构造工具



## 22.7 Swing 事件模型

### 22.7.1 事件与监听器的类型

+ `addXXXListener(),removeXXXListener()`

| Event, listener interface, and add- and remove-methods       | Components supporting this event                             |
| ------------------------------------------------------------ | ------------------------------------------------------------ |
| ActionEvent<br/>ActionListener<br/>addActionListener()<br/>removeActionListener() | **JButton, JList, JTextField, JMenuItem** and its derivatives including JCheckBoxMenuItem, JMenu, and JRadioButtonMenuItem |
| AdjustmentEvent<br/>AdjustmentListener<br/>addAdjustmentListener() <br/>removeAdjustmentListener() | JScrollbar and anything you create that implements the Adjustable interface |
| ComponentEvent<br/>ComponentListener<br/>addComponentListener()<br/>removeComponentListener() | *Component and its derivatives, including JButton, JCheckBox, JComboBox, Container, JPanel, JApplet, JScrollPane, Window, JDialog, JFileDialog, JFrame, JLabel, JList, JScrollbar, JTextArea, and JTextField |
| ContainerEvent<br/>addContainerListener()<br/>removeContainerListener() | Container and its derivatives, JScrollPane, Window, JDialog, JFileDialog, and JFrame |
| FocusEvent<br/>FocusListener<br/>addFocusListener()<br/>removeFocusListener() | Component and derivatives*                                   |
| KeyEvent<br/>KeyListener<br/>addKeyListener()<br/>removeKeyListener() | Component and derivatives*                                   |
| MouseEvent (for both clicks and motion)<br/>MouseListener<br/>addMouseListener()<br/>removeMouseListener() | Component and derivatives*                                   |
| MouseEvent6 (for both clicks and motion)<br/>MouseMotionListener<br/>addMouseMotionListener()<br/>removeMouseMotionListener() | Component and derivatives*                                   |
| WindowEvent<br/>WindowListener<br/>addWindowListener()<br/>removeWindowListener() | Window and its derivatives, including JDialog, JFileDialog,<br/>and JFrame |
| ItemEvent<br/>ItemListener<br/>addItemListener()<br/>removeItemListener() | JCheckBox,<br/>JCheckBoxMenuItem,<br/>JComboBox, JList, and anything<br/>that implements the<br/>ItemSelectable interface |
| TextEvent<br/>TextListener<br/>addTextListener()<br/>removeTextListener() | Anything derived from JTextComponent, including JTextArea and JTextField |

+ `showMethod` 的 `GUI` 版本
    + [showAddListeners](../Example_Code/gui/showAddListeners.java)
+ 监听器及其适配器，方法（中文书 `P815`）



#### 22.7.1.1 使用监听器适配器进行简化

+ 对监听器中的接口做了没有操作的实现
+ 适用于接口中有多个方法需要实现，但是只需要用到其中的部分方法

```java
public abstract class MouseAdapter
    implements MouseListener, MouseWheelListener, MouseMotionListener {
    public void mouseClicked(MouseEvent e) {}
    public void mousePressed(MouseEvent e) {}
    public void mouseReleased(MouseEvent e) {}
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}
    public void mouseWheelMoved(MouseWheelEvent e){}
    public void mouseDragged(MouseEvent e){}
    public void mouseMoved(MouseEvent e){}
}
```

+ 注意使用 `@Override` 注解避免出错



### 22.7.2 跟踪多个事件

+ [TrackEvent](../Example_Code/gui/TrackEvent.java)
    + 一个事件注册的好例子



## 22.8 Swing 组件一览

### 22.8.1 按钮

+ 复选框，单选按钮，菜单项
    + 都是继承自 `AbstractButton`
+ [Buttons](../Example_Code/gui/Buttons.java)

```java
JToggleButton;    // 保留当前状态(按下/弹出)
JCheckBox;
JRadioButton;
BasicArrowButton; // java.swing.plaf.basic.*;
```

```java
TitleBorder();    // java.swing.border.*;
```



#### 22.8.1.1 按钮组 ButtonGroup

+ 单选按钮的排他行为
+ [ButtonGroups](../Example_Code/gui/ButtonGroups.java)
    + 技巧：初始化的时候将其初始化为 `new JButton("Failed")` ，这样失败后可以快速显示

```java
ButtonGroup bg = new ButtonGroup();
// ...
AbstractButton ab = new JButton("failed");
// ...
ab = (AbstractButton)ctor.newInstance(id);
// ...
bg.add(ab);
```



### 22.8.2 图标

+ 可以在 `JLable` 或者任何从 `AbstractButton` 继承的组件中使用 `Icon`
+ [Faces](../Example_Code/gui/Faces.java)

```java
Icon[] faces = new Icon[]{
    new ImageIcon(getClass().getResource("Face0.gif")),
    // ...
    new ImageIcon(getClass().getResource("Face4.gif")),
};
JButton jb = new JButton("JButton", faces[0]);
// ...
jb.setIcon(faces[3]);
// 其它的一些方法 ...
jb.setRolloverEnabled(true);
jb.setRolloverIcon(faces[1]);
jb.setPressedIcon(faces[2]);
jb.setDisabledIcon(faces[4]);
jb.setToolTipText("Yow!"); // 移动到上面时跳出提示语
```



### 22.8.3 工具提示

+ `JComponet` 包含方法 `setToolTipText(String)`



### 22.8.4 文本域

+ `JTextField` 组件的其他功能
+ [TextFields](../Example_Code/gui/TextFields.java)

```java
class T1 implements DocumentListener {
    public void changedUpdate(DocumentEvent e) {}
    public void insertUpdate(DocumentEvent e) {}
    public void removeUpdate(DocumentEvent e) {}
}
```



### 22.8.5 边框

+ `JComponent` 有一个 `setBorder()` 方法
+ [Borders](../Example_Code/gui/Borders.java)

```java
JPanel jp = new JPanel();
Border b = new TitledBorder("Title");
jp.setBorder(b);
/*
new TitledBorder("Title")
new EtchedBorder()
new LineBorder(Color.BLUE)
new MatteBorder(5,5,30,30,Color.GREEN)
new BevelBorder(BevelBorder.RAISED)
new SoftBevelBorder(BevelBorder.LOWERED)
new CompoundBorder(
    new EtchedBorder(),
    new LineBorder(Color.RED)
)
*/
```



### 22.8.6 一个迷你的编辑器

```java
JTextPane
```

+ [TextPane](../Example_Code/gui/TextPane.java)



### 22.8.7 复选框

+ [CheckBoxes](../Example_Code/gui/CheckBoxes.java)

```java
JTextArea t = new JTextArea(6, 15);
```



### 22.8.8 单选按钮

+ [RadioButtons](../Example_Code/gui/RadioButtons.java)

```java
ButtonGroup g = new ButtonGroup();
JRadioButton
    rb1 = new JRadioButton("one", false),
    rb2 = new JRadioButton("two", false),
    rb3 = new JRadioButton("three", false);
// ...
g.add(rb1); g.add(rb2); g.add(rb3);
// 加入 JFrame 的时候还是要加入 JRadioButton,而不是 ButtonGroup
add(rb1);
add(rb2);
add(rb3);
```



### 22.8.9 组合框（下拉列表）

+ 相较于动态改变单选按钮而言，不容易造成冲突
+ `JComboBox` 默认只能选择，不能输入
    + `setEditable()`
+ [ComboBoxes](../Example_Code/gui/ComboBoxes.java)

```java
JComboBox<String> c = new JComboBox<>(); // 泛型
// 更多方法查看 JDK
c.addItem("ee");
c.removeItem("ee");
c.setEditble(!c.isEditable());
```



### 22.8.10 列表框

+ 总是在屏幕上占据一定的空间
+ `JList` 支持 `ctrl,shift` 多选操作
+ [List](../Example_Code/gui/List.java)

```java
DefaultListModel lItems = new DefaultListModel();
JList lst = new JList(lItems); // lst.setModel(lItems);
// ...
lst.getSelectedValues(); // E[](Deprecated)
lst.getSelectedValuesList();// List<E>
```





### 22.8.11 页签面板

+ `JTabbedPane`
+ [TabbedPane1](../Example_Code/gui/TabbedPane1.java)

```java
JTabbedPane tabs = new JTabbedPane();
// ...
tabs.addTab(flavors[i],
            new JButton("Tabbed pane " + i++));
// ...
tabs.addChangeListener(new ChangeListener() {
    public void stateChanged(ChangeEvent e) {
        txt.setText("Tab selected: " +
                    tabs.getSelectedIndex());
    }
});
```

+ 页签太多会自动换行



### 22.8.12 消息框

+ `JOptionPane`

```java
// 静态方法
JOptionPane.showMessageDialog();
JOptionPane.showConfirmDialog();
```

+ [MessageBoxes](../Example_Code/gui/MessageBoxes.java)

```java
// showMessageDialog()
JOptionPane.showMessageDialog(null,"There's a bug on you!", "Hey!",
                              JOptionPane.ERROR_MESSAGE);

// showConfirmDialog()
JOptionPane.showConfirmDialog(null, "or no", "choose yes",
                              JOptionPane.YES_NO_OPTION);

// showInputDialog()
String val = JOptionPane.showInputDialog(
    "How many fingers do you see?");

// showOptionDialog()
Object[] options = { "Red", "Green" };
int sel = JOptionPane.showOptionDialog(
    null, "Choose a Color!", "Warning",
    JOptionPane.DEFAULT_OPTION,
    JOptionPane.WARNING_MESSAGE, null,
    options, options[0]);
if(sel != JOptionPane.CLOSED_OPTION)
    txt.setText("Color Selected: " + options[sel]);

// showInputDialog()
Object[] selections = {"First", "Second", "Third"};
Object val = JOptionPane.showInputDialog(
    null, "Choose one", "Input",
    JOptionPane.INFORMATION_MESSAGE,
    null, selections, selections[0]);
if(val != null)
    txt.setText(val.toString());
```



### 22.8.13 菜单

+ `JApplet,JFrame,JDialog` 都有 `setJMenuBar()` 方法，接受一个 `JMenuBar` 对象
    + 某个特定组件只能持有一个 `JMenuBar` 对象
+ `JMenuBar <- JMenu <- JMenuItem`
    + `JMenuItem <- ActionListener`
+ [SimpleMenus](../Example_Code/gui/SimpleMenus.java)
+ `JCheckBoxMenu` 提供了一个复选标记，用来表明菜单项是否被选中
+ `JRadioButtonMenuItem` 包含了一个单选按钮
+ [Menus](../Example_Code/gui/Menus.java) 不错的一个例子

```java
// 1. 设置快捷键
// (1) JMenuItem
// Adding a menu shortcut (mnemonic) is very
// simple, but only JMenuItems can have them
// in their constructors:
new JMenuItem("Foo", KeyEvent.VK_F);
// (2) Other
safety[0].setActionCommand("Guard");
safety[0].setMnemonic(KeyEvent.VK_G);

// 2. 刷新 JFrame
// Validates this container and all of its subcomponents.
validate(); // java.awt.Container

// 3. JMenu 中画一个分界线
JMenu m = new JMenu("Flavors");
m.addSeparator();

// 4. JMenu 嵌套使用

// 5. getActionCommand()/setActionCommand
// 给组件设置一个别称(不使用名字是为了移植性,不需要修改代码)
```

+ 嵌套使用 `JMenu`
    + [MenuInMenu](MenuInMenu.java)



### 22.8.14 弹出式菜单

+ `JPopupMenu` 右键单击弹出菜单
+ 要实现一个 `JPopupMenu`，最直接的方法就是创建一个继承自 `MouseAdapter` 的内部类，
+ 然后对希望每个具有弹出式行为的组件都添加一个该内部类的对象
+ [Popup](../Example_Code/gui/Popup.java)
    + 设计技巧

```java
//: gui/Popup.java
// Creating popup menus with Swing.
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import static net.mindview.util.SwingConsole.*;

public class Popup extends JFrame {
    private JPopupMenu popup = new JPopupMenu();
    private JTextField t = new JTextField(10);
    public Popup() {
        setLayout(new FlowLayout());
        add(t);
        ActionListener al = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                t.setText(((JMenuItem)e.getSource()).getText());
            }
        };
        JMenuItem m = new JMenuItem("Hither");
        m.addActionListener(al);
        popup.add(m);
        m = new JMenuItem("Yon");
        m.addActionListener(al);
        popup.add(m);
        m = new JMenuItem("Afar");
        m.addActionListener(al);
        popup.add(m);
        popup.addSeparator();
        m = new JMenuItem("Stay Here");
        m.addActionListener(al);
        popup.add(m);
        PopupListener pl = new PopupListener();
        addMouseListener(pl);
        t.addMouseListener(pl);
    }
    class PopupListener extends MouseAdapter {
        public void mousePressed(MouseEvent e) {
            maybeShowPopup(e);
        }
        public void mouseReleased(MouseEvent e) {
            maybeShowPopup(e);
        }
        private void maybeShowPopup(MouseEvent e) {
            if(e.isPopupTrigger())
                popup.show(e.getComponent(), e.getX(), e.getY());
        }
    }
    public static void main(String[] args) {
        run(new Popup(), 300, 200);
    }
}
```



### 22.8.15 绘图

+ 在任何的 `JComponent` 上面个都可以绘图
+ 但是典型做法从 `JPanel` 继承，覆盖 `paintComponent()` 方法
+ [SineWave](../Example_Code/gui/SineWave.java)

```java
public void paintComponent(Graphics g) {
    super.paintComponent(g); // necessary
    // ... your code here!
}
```

```java
// JSlider(int min, int max, int value)
JSlider adjustCycles = new JSlider(1, 30, 5);
// changeListener
adjustCycles.addChangeListener(new ChangeListener() {
    public void stateChanged(ChangeEvent e) {
        sines.setCycles(
            ((JSlider)e.getSource()).getValue());
    }
});
```

+ [Ex22](Ex22.java) `RGB` 显色
+ [Ex23](E23_RotatingSquare.java) 示例代码
    + **建议看一看**

```java
// E23_RotatingSquare.java
import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.geom.*;
import static net.mindview.util.SwingConsole.*;
class SquareRotate extends JPanel {
    private Rectangle2D square =
        new Rectangle2D.Float(-50f, -50f, 100f, 100f);
    private AffineTransform
        rot = new AffineTransform(),
    scale = new AffineTransform();
    private volatile int speed;
    private int boxSize;
    public SquareRotate() {
        setSpeed(5);
        setBoxSize(10);
        new Thread(new Runnable() {
            public void run() {
                for(;;) {
                    SquareRotate.this.repaint();
                    try {
                        Thread.sleep(1000 / speed);
                    } catch(InterruptedException ignore) {}
                }
            }
        }).start();
    }
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;
        // Makes point (0,0) as the center of this canvas
        g2.translate(getWidth() / 2, getHeight() / 2);
        g2.scale(boxSize / 10.0, boxSize / 10.0);
        g2.setPaint(Color.blue);
        rot.rotate(Math.toRadians(20));
        g2.transform(rot);
        g2.draw(square);
    }
    public void setSpeed(int speed) {
        this.speed = speed;
    }
    public void setBoxSize(int boxSize) {
        this.boxSize = boxSize;
    }
}
```

+ [E25](E25_AnimatedSine.java) 动态的正弦波
    + 每隔一定时间重设坐标，然后重绘



### 22.8.16 对话框

+ 目的是处理一些具体问题，可以将其与原先的问题分离开来
+ 继承自 `JDialog`
+ [Dialogs](../Example_Code/gui/Dialogs.java)

```java
class MyDialog extends JDialog {
    public MyDialog(JFrame parent) {
        super(parent, "My dialog", true);
        setLayout(new FlowLayout());
        add(new JLabel("Here is my dialog"));
        JButton ok = new JButton("OK");
        ok.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose(); // Closes the dialog
            }
        });
        add(ok);
        setSize(150,125);
    }
}
```

+ [TicTacToe](../Example_Code/gui/TicTacToe.java)
+ 受启发可以写一个扫雷的小程序



### 22.8.17 文件对话框

+ `JFileChooser`
+ [FileChooserTest](../Example_Code/gui/FileChooserTest.java)

```java
class OpenL implements ActionListener {
    public void actionPerformed(ActionEvent e) {
        JFileChooser c = new JFileChooser();
        // Demonstrate "Open" dialog:
        int rVal = c.showOpenDialog(FileChooserTest.this);
        if(rVal == JFileChooser.APPROVE_OPTION) {
            fileName.setText(c.getSelectedFile().getName());
            dir.setText(c.getCurrentDirectory().toString());
        }
        if(rVal == JFileChooser.CANCEL_OPTION) {
            fileName.setText("You pressed cancel");
            dir.setText("");
        }
    }
}

class SaveL implements ActionListener {
    public void actionPerformed(ActionEvent e) {
        JFileChooser c = new JFileChooser();
        // Demonstrate "Save" dialog:
        int rVal = c.showSaveDialog(FileChooserTest.this);
        if(rVal == JFileChooser.APPROVE_OPTION) {
            fileName.setText(c.getSelectedFile().getName());
            dir.setText(c.getCurrentDirectory().toString());
        }
        if(rVal == JFileChooser.CANCEL_OPTION) {
            fileName.setText("You pressed cancel");
            dir.setText("");
        }
    }
}
```

+ 具体应用查看 `JDK`
    + 例如使用 **过滤器** 来缩小可供选择的文件名范围
+ [Ex29](Ex29.java) `JColorChooser`



### 22.8.18 Swing 组件上的 HTML

+ 任何能接受文本的组件都可以接受 `HTML` 文本，且能够根据 `HTML` 的规则重新格式化文本
+ [HTMLButton](../Example_Code/gui/HTMLButton.java)

```java
public class HTMLButton extends JFrame {
    private JButton b = new JButton(
        "<html><b><font size=+2>" +
        "<center>Hello!<br><i>Press me now!");
    public HTMLButton() {
        b.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                add(new JLabel("<html>" +
                               "<i><font size=+4>Kapow!"));
                // Force a re-layout to include the new label:
                validate();
            }
        });
        setLayout(new FlowLayout());
        add(b);
    }
}
```



### 22.8.19 滑块和进度条

+ 滑块 `JSlider`
+ 进度条 `JProgressBar`
+ [Progress](../Example_Code/gui/Progress.java)

```java
public class Progress extends JFrame {
    private JProgressBar pb = new JProgressBar();
    private ProgressMonitor pm = new ProgressMonitor(
        this, "Monitoring Progress", "Test", 0, 100);
    private JSlider sb =
        new JSlider(JSlider.HORIZONTAL, 0, 100, 60);
    public Progress() {
        setLayout(new GridLayout(2,1));
        add(pb);
        pm.setProgress(0);
        pm.setMillisToPopup(1000);
        sb.setValue(0);
        sb.setPaintTicks(true);
        sb.setMajorTickSpacing(20);
        sb.setMinorTickSpacing(5);
        sb.setBorder(new TitledBorder("Slide Me"));
        pb.setModel(sb.getModel()); // Share model
        add(sb);
        sb.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                pm.setProgress(sb.getValue());
            }
        });
    }
}
```

+ 关联两个组件的关键在于共享模型
    + 监听器设置也 `OK`
    + 共享更方便



### 20.8.20 选择外观

+ 要确保在创建任何可视组件之前调用

```java
try {
    UIManager.setLookAndFeel(
        UIManager.getCrossPlatformLookAndFeelClassName() // 默认的跨平台
        // UIManager.getSystemLookAndFeelClassName() // 当前操作系统
        // "com.sun.java.swing.plaf.motif.MotifLookAndFeel"
        // "com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel"
        // "com.sun.java.swing.plaf.gtk.GTKLookAndFeel" // Windows 好像打不开
        // "com.sun.java.swing.plaf.windows.WindowsLookAndFeel"
        // "com.sun.java.swing.plaf.windows.WindowsClassicLookAndFeel"

    );
} catch(Exception e) {
    e.printStackTrace();
}
```

+ [LookAndFeel](../Example_Code/gui/LookAndFeel.java)



### 22.8.21 树、表格、剪切板

+ https://www.mindviewllc.com/
+ [JTreeDemo](JTreeDemo.java)

```java
// javax.swing.JTree
protected static TreeModel getDefaultTreeModel() {
    DefaultMutableTreeNodev root = new DefaultMutableTreeNode("JTree");
    DefaultMutableTreeNode parent;

    parent = new DefaultMutableTreeNode("colors");
    root.add(parent);
    parent.add(new DefaultMutableTreeNode("blue"));
    parent.add(new DefaultMutableTreeNode("violet"));
    parent.add(new DefaultMutableTreeNode("red"));
    parent.add(new DefaultMutableTreeNode("yellow"));

    parent = new DefaultMutableTreeNode("sports");
    root.add(parent);
    parent.add(new DefaultMutableTreeNode("basketball"));
    parent.add(new DefaultMutableTreeNode("soccer"));
    parent.add(new DefaultMutableTreeNode("football"));
    parent.add(new DefaultMutableTreeNode("hockey"));

    parent = new DefaultMutableTreeNode("food");
    root.add(parent);
    parent.add(new DefaultMutableTreeNode("hot dogs"));
    parent.add(new DefaultMutableTreeNode("pizza"));
    parent.add(new DefaultMutableTreeNode("ravioli"));
    parent.add(new DefaultMutableTreeNode("bananas"));
    return new DefaultTreeModel(root);
}
```

+ [JTableDemo](JTableDemo.java)
+ https://docs.oracle.com/javase/tutorial/
    + [SimpleTableDemo](SimpleTableDemo.java)
    + [TableDialogEditDemo](components/TableDialogEditDemo.java)



## 22.9 JNLP 与 Java Web

+ `Java Network Launch Protocol` ：`Java` 网络发布协议
    + 结合了 `applet` 和 **应用程序** 的优点
+ 需要将 `javaws.jar` 引入 `Class Path`
+ 步骤
    + 先写一个标准应用程序
    + 写一个启动文件 `*.jnlp`（`xml` 文件）
    + `*.html` 使用文件



## 22.10 Swing 与 并发

+ `Swing` 事件分发线程
    + `SwingUtilities.invokeLater()`
    + 通过从事件队列中拉出每个事件并依次执行它们



### 22.10.1 长期运行的任务

+ 常犯的错误之一
    + **意外使用了事件分发线程来运行长任务**
+ [LongRunningTask](../Example_Code/gui/LongRunningTask.java)
    + 按下 `b1` 时甚至按钮都不会马上弹起

```java
new Thread(
    () -> {
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch(InterruptedException e) {
            System.out.println("Task interrupted");
            return;
        }
        System.out.println("Task completed");
    }
).start();
```

+ 修改方法
    + 直接关闭整个线程池
        + [InterruptableLongRunningTask](../Example_Code/gui/InterruptableLongRunningTask.java)
    + 只关闭当前执行护着等待执行的任务，允许继续添加任务
        + `Callable/Future`
        + [TaskItem](../Example_Code/net/mindview/util/TaskItem.java)
        + [TaskManager](../Example_Code/net/mindview/util/TaskManager.java)
        + [InterruptableLongRunningCallable](../Example_Code/gui/InterruptableLongRunningCallable.java)
+ 为最终用户提供某种可视线索，一表示任务正在运行及其执行进度
    + `JProgressBar/ProgressMonitor`
+ [MonitoredLongRunningCallable](../Example_Code/gui/MonitoredLongRunningCallable.java)
    + **这个还挺实用的**（但是这个好像并没有联动）



### 22.10.2 可视化线程机制

+ [ColorBoxes](../Example_Code/gui/ColorBoxes.java)
    + 花里胡哨，分块换颜色
+ `repaint()` 也只是向事件分发线程提交了一个任务，不是强制马上执行
+ [E34](E34_ColorStars.java) 示例代码
    + **怎么画星星**

```java
public abstract void fillPolygon(int xPoints[], int yPoints[],
                                 int nPoints);
```



## 22.11 可视化编程与 JavaBean

### 22.11.1 JavaBean 是什么

+ 一个命名规则，辅助于 `IDE` 的可视化编程
    + 对于一个名称为 `xxx` 的属性，通常有两个方法 `getXxx()/setXxx()`
    + 对于布尔型属性，可以使用 `get/set` 或者 `is/set`
    + `Bean` 的普通方法不需要遵循上述命名规则，但是必须都是 `public` 的
    + 对于事件要使用 `Swing` 中处理监听器的方式
        + `addXxxListener(XxxListener)`
        + `removeXxxListener(XxxListener)`
+ [Frog](../Example_Code/frogbean/Frog.java) 一个简单的 `JavaBean`

```java
// 名称上不对应也 OK
private boolean jmpr;
public boolean isJumper() { return jmpr; }
public void setJumper(boolean j) { jmpr = j; }
```



### 22.11.2 使用 Introspector 抽取出 BeanInfo

+ （1）反射机制
+ （2）`Introspector ` 的 `getBeanInfo(Class)` 方法
    + 可以完全侦测这个类，返回一个 `BeanInfo` 对象
    + 可以获取 `Bean` 的属性、方法、事件
+ [BeanDumper](../Example_Code/gui/BeanDumper.java)

```java
// 参数表示在哪一个继承层次上停止查询
BeanInfo bi = Introspector.getBeanInfo(bean, Object.class);
// ...
for(PropertyDescriptor d: bi.getPropertyDescriptors()){
    Class<?> p = d.getPropertyType(); // 可能是 null
    print("Property type:\n  " + p.getName() +
          "Property name:\n  " + d.getName());
    Method readMethod = d.getReadMethod(); // null
    Method writeMethod = d.getWriteMethod();
}
// ...
for(MethodDescriptor m : bi.getMethodDescriptors())
    print(m.getMethod().toString());

print("Event support:");
for(EventSetDescriptor e: bi.getEventSetDescriptors()){
    print("Listener type:\n  " +
          e.getListenerType().getName());
    for(Method lm : e.getListenerMethods())
        print("Listener method:\n  " + lm.getName());
    for(MethodDescriptor lmd :
        e.getListenerMethodDescriptors() )
        print("Method descriptor:\n  " + lmd.getMethod());
    Method addListener= e.getAddListenerMethod();
    Method removeListener = e.getRemoveListenerMethod();
}
```



### 22.11.3 一个更复杂的 Bean

+ [BangBean](../Example_Code/bangbean/BangBean.java)
+ [BangBeanTest](../Example_Code/bangbean/BangBeanTest.java)



### 22.11.4 JavaBean 与 同步

+ 当创建 `Bean` 的时候，我们必须要假设它可能会在多线程的环境下运行

    + 尽可能让 `public` 方法都是 `synchronized` 的

        + **尽可能短**

    + 当一个多路事件出发了一组对该事件感兴趣的监听器时，

        你必须假定，在你遍历列表进行通知的同时，监听器可能会被添加或者移除

+ [BangBean2](../Example_Code/gui/BangBean2.java)

```java
// 多路事件
private ArrayList<ActionListener> actionListeners =
    new ArrayList<ActionListener>();

public synchronized void
    addActionListener(ActionListener l) {
    actionListeners.add(l);
}

public synchronized void
    removeActionListener(ActionListener l) {
    actionListeners.remove(l);
}

public void notifyListeners() {
    ActionEvent a = new ActionEvent(BangBean2.this,
                                    ActionEvent.ACTION_PERFORMED, null);
    ArrayList<ActionListener> lv = null;
    // 复制一波
    synchronized(this) {
        lv = new ArrayList<ActionListener>(actionListeners);
    }
    // Call all the listener methods:
    for(ActionListener al : lv)
        al.actionPerformed(a);
}

```

+ 其他方法是否需要设置为 `synchronized` 的考量
    + 这个方法会修改对象中关键变量的状态吗？
    + 这个方法是否依赖于那些关键变量？
    + 基类版本的方法是否是同步的
+ `paint()/paintCompnent()` 方法的执行必须尽可能的快
    + 要是发现需要同步这些方法，基本上是设计有问题



### 22.11.5 把 Bean 打包

+ `MANIFEST` 清单文件
    + 注意要有两个换行

```txt
Manifest-Version: 1.0
Name: bangbean/BangBean.class
Java-Bean: True


```

+ 接下来可以直接导入支持 `JavaBean` 的`IDE` 中



### 22.11.6 对 Bean 更高级的支持

+ 使用数组表示多重属性，这称为**索引属性**
+ 属性可以被**绑定**，绑定 `ProperityChangeEvent` 事件
+ 属性可以被**约束**
    + 如果属性的改变是不可接受的，其他对象可以否决这个改变，
    + 这些对象也是通过 `ProperityChangeEvent` 事件得到通知的，
    + 而且能够抛出 `ProperityVetoException` 异常来阻止属性的改变，恢复旧值



### 22.11.7 有关 Bean 的其它读物

+ `JavaBeans(1998)`，`Elliotte Rusty Harold`



## 22.12 Swing 的可替代选择

+ 用于 `Web` 之上的客户端 `GUI` 使用的 `MacroMedia` 的 `Flex` 编程系统的 `MacroMedia Flash`
+ 用于桌面应用的开源的 `Eclipse` 标准工具包 `Standarf Widget Toolkit(SWT)` 类库



## 22.13 用 Flex 构建 Flash Web 客户端

+ `Flex` 由基于 `XML` 和脚本的编程模型（类`HTMl,JS`）以及组件库构成
+ 使用 `MXML` 语法来声明布局管理和工具控件
+ 使用动态脚本机制来添加事件处理和服务调用代码
    + 他们会将用户界面链接到 `Java` 类、数据模型、`Web Service` 等
+ `Flex` 编译器接受 `MXML` 和脚本文件，然后将其编译成字节码
    + `Flash` 字节码的格式称为 `SWF`
+ 客户端的 `Flash` 虚拟机的操作方式与 `Java` 虚拟机类似
+ 具体细节看书

```txt
22.13.1 Hello, Flex
22.13.2 编译 MXML
22.13.3 MXML 与 ActionScript
22.13.4 容器与控制
22.13.5 效果与样式
22.13.7 链接到 Java
22.13.8 数据模型与数据绑定
22.13.9 构建与部署
```




## 22.14 创建 SWT 应用

+ `Swing` 采用的方式：将所有的 `UI` 组件逐个像素的构建，一边提供所有想要的组件
    + 这样无需知道底层操作系统是否拥有这些组件
+ `SWT` 采用了中间路线
    + 如果操作系统提供本地组件，那么就直接使用这些本地组件
    + 如果不提供就合成这些组件
+ `SWT` 可以自动地利用本地操作系统的功能
    + 例如 `Windows` 具有的 “子像素呈现” 机制



### 22.14.1 安装 SWT

+ [swt.jar](https://download.eclipse.org/eclipse/downloads/drops4/R-4.16-202006040540/#SWT)
+ [javadoc](https://www.eclipse.org/swt/javadoc.php)



### 22.14.2 Hello, SWT

+ [HelloSWT](../Example_Code/swt/HelloSWT.java)

```java
// swt/HelloSWT.java
import org.eclipse.swt.widgets.*;
public class HelloSWT {
    public static void main(String [] args) {
        Display display = new Display(); // 管理 SWT 和底层操作系统之间的连接
        Shell shell = new Shell(display);
        shell.setText("Hi there, SWT!"); // Title bar
        shell.open(); // 显示窗口以及这样的应用程序
        while(!shell.isDisposed()) {
            // 一个应用程序只能有一个 Display 对象
            if(!display.readAndDispatch()){
                display.sleep();
            }
        }
        display.dispose(); // 显式释放资源(因为可能都是底层操作系统的资源)
    }
}
```

+ `Swing` 隐藏了事件循环，但是 `SWT` 要求你显式编写

+ `Swing` 会创建后台的事件分发线程，但是 `SWT` 你的 `main()` 线程将处理 `UI`

    + `Swing` 默认 `2` 个线程，`SWT` 默认一个线程
    + 某种程度上，不大可能在用线程处理 `UI` 时搞得一塌糊涂

+ 你不必像使用 `Swing` 那样向事件分发线程提交任务，`SWT` 会帮你做

    + 如果试图在错误的线程中操作部件，还会抛出异常

    + 但是长期运行的操作仍然需要开新线程

        + 为此，`SWT` 提供了 `3` 个可以在 `Display` 对象上调用的方法

        ```java
        asyncExec(Runnable);
        syncExec(Runnable);
        timerExec(int, Runnable);
        ```

+ `readAndDispatch()`
    + 若事件队列中存在更多的事件在等待处理，返回 `true`（此时立即重新调用）
    + 若无，则返回 `false`（可以等待一段时间再检查）
+ 证明 `Shell` 是主窗口 [ShellsAreMainWindows](../Example_Code/swt/ShellsAreMainWindows.java)
+ `SWT` 的布局管理器
    + [DisplayProperties](../Example_Code/swt/DisplayProperties.java)
    + 感觉和 `Swing` 风格差别比较大
+ 在 `SWT` 中，所有部件都必须具有泛化类型 `Composite` 的**父对象**（并且在构造器中作为第一个参数）
+ `SWT` 是绝对基于构造器的
    + 有很多不通过构造器很难或者根本不可能修改的属性



### 22.14.3 根除冗余代码

+ 抽象出基本操作
    + [SWTApplication](../Example_Code/swt/util/SWTApplication.java)
    + [SWTConsole](../Example_Code/swt/util/SWTConsole.java)
+ [DisplayEnvironment](../Example_Code/swt/DisplayEnvironment.java)



### 22.14.4 菜单

+ [Menus](../Example_Code/swt/Menus.java)
+ `Menu` 必须置于某个 `Shell` 之上
+ `Composite.getShell()` 可以获取 `Shell`



### 22.14.5 页签面板、按钮和事件

+ `widget` 部件 ：`org.eclipse.swt.widget`
+ [TabbedPane](../Example_Code/swt/TabbedPane.java)
    + 很多部件的使用方法
    + 竟然还有 `Browser`



### 22.14.6 图形

+ [SineWave](../Example_Code/swt/SineWave.java)
    + `Swing` 修改的
+ `redraw()` 对应 `addPaintListener(PaintListener)`

```java
// org.eclipse.swt.widgets.Canvas
addPaintListener(new PaintListener() {
    public void paintControl(PaintEvent e) {
        int maxWidth = getSize().x;
        double hstep = (double)maxWidth / (double)points;
        int maxHeight = getSize().y;
        pts = new int[points];
        for(int i = 0; i < points; i++)
            pts[i] = (int)((sines[i] * maxHeight / 2 * .95)
                           + (maxHeight / 2));
        e.gc.setForeground(
            e.display.getSystemColor(SWT.COLOR_RED));
        for(int i = 1; i < points; i++) {
            int x1 = (int)((i - 1) * hstep);
            int x2 = (int)(i * hstep);
            int y1 = pts[i - 1];
            int y2 = pts[i];
            e.gc.drawLine(x1, y1, x2, y2);
        }
    }
});
```





### 22.14.7 SWT 中的并发

+ `SWT`  版本的 [ColorBoxes](../Example_Code/swt/ColorBoxes.java)
+ 将 `redraw()` 提交给 `Display.asyncExec(Runnable)`
    + 类似于 `SwingUtilies.invokeLater()`
+ `SWT` 默认不是双缓存，`Swing` 默认双缓存
    + `SWT` 可视化显示会有瑕疵



### 22.14.8 SWT 还是 Swing





[JTabbedPane](../Example_Code/swt/TabbedPane.java)
