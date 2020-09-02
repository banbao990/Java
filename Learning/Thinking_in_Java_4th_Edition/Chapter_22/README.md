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
+ [HelloSwing](..\Example_Code\gui\HelloSwing.java)

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
+ [SubmitSwingProgram](..\Example_Code\gui\SubmitSwingProgram.java)

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

+ 包装构造函数 [SwingConsole](..\Example_Code\net\mindview\util\SwingConsole.java)



## 22.3 创建按钮

+ [Button1](..\Example_Code\gui\Button1.java)

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
+ [Button2](..\Example_Code\gui\Button2.java)
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

+ [示例代码](..\Example_Code\gui\BorderLayout1.java)



### 22.6.2 FlowLayout

+ 直接将组件从左到右 “流动” 到窗体上，直至占满上方的空间，然后向下移一行
+ **从左到右，从上到下**
+ 组件将呈现出合适的大小（`JButton` 就是标签的大小）
+ [示例代码](..\Example_Code\gui\FlowLayout1.java)



### 22.6.3 GridLayout

+ 创建一个用于放置组件的表格（行 `x` 列）
+ 按照**从左到右、从上到下**的顺序加入
+ [示例代码](..\Example_Code\gui\GridLayout1.java)



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









[HelloSwing](..\Example_Code\gui\TextArea.java)