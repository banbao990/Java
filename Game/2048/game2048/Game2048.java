/**
 * @author banbao(2019.05.18)
 * @version 1.0
 * @color http://2048game.com/
 */
package game2048;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.TextField;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

public class Game2048 {
    private static String player = "default";

    public static void main(String... args) {
        // 文件读写
        Global.writeIn();
        // 欢迎界面
        new Introduction();
        // 游戏界面
        Global.gjfm = new GameJFrame("2048", player);
        // 游戏结束界面
    }
}

class GameJFrame extends JFrame {
    // 方法都设置为静态，因为只有这么一个界面
    private static final int LENGTH = 16;
    private static final int ROW = 4;
    private static final int COL = 4;
    private static final long serialVersionUID = 1L;
    private static int numbers[] = new int[LENGTH];// 用于记录方块的上面对应的数字,0表示空白
    private static Nav nav = new Nav();
    private static JLabel player = new JLabel("", JLabel.CENTER);
    private static Body body = new Body();
    //private static JButton reflesh = new JButton("start");
    private static JLabel showDetails = new JLabel("show details",JLabel.CENTER);
    private static boolean gameOver = false;// 游戏结束
    private static int blankNumber = 16;
    private static boolean changed = false;// 用来就每次操作有没有移动方块，这样可以决定要不要生成一个新的砖块
    private static int score = 0;
    //private static String playerName;
    private static Detail detail = new Detail();

    GameJFrame(String name, String _player) {
        super(name);// 构造函数
        //playerName=_player;
        setTitle("2048 Game");// 设置标题
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(500, 800);// 设置大小
        setLocationRelativeTo(null);// 设置为窗体居中
        setResizable(false);// 设置界面大小不能够更改
        setLayout(null);// 设置布局为自己手动布局
        setVisible(true);
        getContentPane().setBackground(new Color(0xfaf8ef));
        // Nav nav = new Nav();
        getContentPane().add(nav);// getContentPane()
        // JLabel player = new JLabel("player: "+_player, JLabel.CENTER);
        player.setText("player: " + _player);
        player.setSize(400, 80);
        player.setLocation(50, 130);
        player.setFont(new Font("consolas", 0, 30));
        player.setHorizontalAlignment(JLabel.CENTER);
        player.setBackground(new Color(0xfafaee));
        player.setOpaque(true);
        getContentPane().add(player);
        // Body body = new Body();
        getContentPane().add(body);
        // 不知道怎么把焦点还给JFrame
        /*
         * JButton reflesh = new JButton("start");
         * reflesh.setSize(200, 50);
         * reflesh.setLocation(150, 670); reflesh.setFont(new Font("consolas", 0, 40));
         * reflesh.setBackground(new Color(0x8f7a66));
         * reflesh.setForeground(Color.WHITE); // btn添加事件监听器
         * reflesh.addActionListener((e) -> { newGame(); });
         * getContentPane().add(reflesh);
         */
        showDetails.setSize(250, 50);
        showDetails.setLocation(125, 670);
        showDetails.setFont(new Font("consolas", 0, 30));
        showDetails.setBackground(new Color(0xffc09f));
        showDetails.setOpaque(true);// 设置成不透明的背景色才能有效
        showDetails.setForeground(Color.black);
        // showDetails添加事件监听器
        showDetails.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e){
                    detail.setVisible(true);
                }
                @Override
                public void mouseExited(MouseEvent e){
                    detail.setVisible(false);
                }
        });

        getContentPane().add(showDetails);

        setFocusable(true);// 获得焦点
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    newGame();
                }
                if (!canMove()) {
                }
                if (!gameOver) {
                    switch (e.getKeyCode()) {
                    case KeyEvent.VK_LEFT:
                        left();
                        break;
                    case KeyEvent.VK_RIGHT:
                        right();
                        break;
                    case KeyEvent.VK_DOWN:
                        down();
                        break;
                    case KeyEvent.VK_UP:
                        up();
                        break;
                    }
                }
            }
        });
    }
    public JLabel getPlayer() {
        return player;
    }
    // 重新开始游戏
    public static void newGame() {
        gameOver = false;
        blankNumber = LENGTH - 2;
        score = 0;
        int r1, r2;
        r1 = (int) Math.floor(Math.random() * LENGTH);
        r2 = (int) Math.floor(Math.random() * LENGTH);
        while (r1 == r2) {
            r2 = (int) Math.floor(Math.random() * LENGTH);
        }
        for (int i = 0; i < LENGTH; i++) {
            if (i == r1 || i == r2) {
                numbers[i] = 2;
                final int iUse = i;
                SwingUtilities.invokeLater(() -> {
                    body.getDiv(iUse).setTextAndColor(2);
                });
                continue;
            }
            numbers[i] = 0;
            final int iUse = i;
            SwingUtilities.invokeLater(() -> {
                body.getDiv(iUse).setTextAndColor(0);
            });
        }
        SwingUtilities.invokeLater(() -> {
            nav.getScoreDiv(0).getScore().setText("" + 0);
        });
    }

    // 改变一列
    public static void changeCol(int col, boolean isUp) {
        boolean nowChanged = false;
        int[] temp = { 0, 0, 0, 0 };
        // 取出不是0的子序列
        for (int i = 0, j = 0; j < ROW; j++) {
            if (numbers[j * COL + col] != 0) {
                temp[i] = numbers[j * COL + col];
                i++;
            }
        }
        // 向下移动,需要改变一下temp的存储
        if (!isUp) {
            int zeroLocation = 0;
            for (int j = 0; j < ROW; j++) {
                if (temp[j] == 0) {
                    zeroLocation = j;
                    break;
                }
            }
            int tmp[] = new int[ROW];
            for (int i = 0; i < ROW; i++)
                tmp[i] = temp[(i + zeroLocation) % 4];
            for (int i = 0; i < ROW; i++)
                temp[i] = tmp[i];
        }
        // 一方面检查这里有没有交换过，另外一方面给全局的检查有没有交换过
        for (int j = 0; j < ROW; j++) {
            if (temp[j] != numbers[j * COL + col]) {
                changed = true;// 交换过位置
                nowChanged = true;
                break;
            }
        }
        if (!nowChanged)// 没有交换过就返回
            return;
        // 交换位置
        for (int j = 0; j < ROW; j++) {
            final int location = j * COL + col;
            numbers[location] = temp[j];
            SwingUtilities.invokeLater(() -> {
                body.getDiv(location).setTextAndColor(numbers[location]);
            });
        }
    }

    // 相邻两行相同的重合,注意只合并一次
    public static void mergeSameROW(final int col) {
        // 1,2行相同合并
        if ((numbers[col + COL] != 0) && (numbers[col + COL] <= 2048)
                && (numbers[col + COL] == numbers[col + COL * 2])) {
            mergeTwo(col + COL, col + COL * 2);
            return;// !important
        }
        // 0,1行相同合并
        if ((numbers[col] != 0) && (numbers[col] <= 2048) && (numbers[col] == numbers[col + COL])) {
            mergeTwo(col, col + COL);
            // return;//!important
        }
        // 2,3行相同合并
        if ((numbers[col + COL * 2] != 0) && (numbers[col + COL * 2] <= 2048)
                && (numbers[col + COL * 2] == numbers[col + COL * 3])) {
            mergeTwo(col + COL * 2, col + COL * 3);
            // return;//!important
        }
    }

    // 改变一行
    public static void changeROW(int row, boolean isLeft) {
        boolean nowChanged = false;
        int[] temp = { 0, 0, 0, 0 };
        // 取出不是0的子序列
        for (int i = 0, j = 0; j < COL; j++) {
            if (numbers[row * COL + j] != 0) {
                temp[i] = numbers[row * COL + j];
                i++;
            }
        }
        // 向右移动,需要改变一下temp的存储
        if (!isLeft) {
            int zeroLocation = 0;
            for (int j = 0; j < COL; j++) {
                if (temp[j] == 0) {
                    zeroLocation = j;
                    break;
                }
            }
            int tmp[] = new int[COL];
            for (int i = 0; i < COL; i++)
                tmp[i] = temp[(i + zeroLocation) % 4];
            for (int i = 0; i < COL; i++)
                temp[i] = tmp[i];
        }
        // 一方面检查这里有没有交换过，另外一方面给全局的检查有没有交换过
        for (int j = 0; j < COL; j++) {
            if (temp[j] != numbers[row * COL + j]) {
                changed = true;// 交换过位置
                nowChanged = true;
                break;
            }
        }
        if (!nowChanged)// 没有交换过就返回
            return;
        // 交换位置
        for (int j = 0; j < COL; j++) {
            final int location = row * COL + j;
            numbers[location] = temp[j];
            SwingUtilities.invokeLater(() -> {
                body.getDiv(location).setTextAndColor(numbers[location]);
            });
        }
    }

    // 相邻两列相同的重合,注意只合并一次
    public static void mergeSameCOL(final int row) {
        final int minLocation = row * COL;
        // 1,2列相同合并
        if ((numbers[minLocation + 1] != 0) && (numbers[minLocation + 1] <= 2048)
                && (numbers[minLocation + 1] == numbers[minLocation + 2])) {
            mergeTwo(minLocation + 1, minLocation + 2);
            return;// !important
        }
        // 0,1列相同合并
        if ((numbers[minLocation] != 0) && (numbers[minLocation] <= 2048)
                && (numbers[minLocation] == numbers[minLocation + 1])) {
            mergeTwo(minLocation, minLocation + 1);
            // return;//!important
        }
        // 2,3列相同合并
        if ((numbers[minLocation + 2] != 0) && (numbers[minLocation + 2] <= 2048)
                && (numbers[minLocation + 2] == numbers[minLocation + 3])) {
            mergeTwo(minLocation + 2, minLocation + 3);
            // return;//!important
        }
    }

    // 两块重合的函数
    // lc -> location
    public static void mergeTwo(final int lc1, final int lc2) {
        changed = true;// 交换过位置
        blankNumber++;
        score += numbers[lc1];
        // 设置分数
        SwingUtilities.invokeLater(() -> {
            nav.getScoreDiv(0).getScore().setText("" + score);
        });
        // 成绩高过最高分
        if (score > Integer.parseInt(nav.getScoreDiv(1).getScore().getText())) {
            SwingUtilities.invokeLater(() -> {
                nav.getScoreDiv(1).getScore().setText("" + score);
                Global.bestPlayer = Global.username;//成绩高过最高分就更新
                Global.bestScore = score;
                Global.gbBest.getlbl().setText(Global.bestPlayer+" : "+Global.bestScore);
            });
        }
        numbers[lc1] *= 2;
        numbers[lc2] = 0;
        SwingUtilities.invokeLater(() -> {
            body.getDiv(lc1).setTextAndColor(numbers[lc1]);
            body.getDiv(lc2).setTextAndColor(numbers[lc2]);
        });
    }

    // 移动up
    public static void up() {
        changed = false;
        for (int i = 0; i < COL; i++) {
            changeCol(i, true);
        }
        for (int i = 0; i < COL; i++) {
            mergeSameROW(i);
        }
        for (int i = 0; i < COL; i++) {
            changeCol(i, true);
        }
        if (!changed)
            return;
        reset();
        clearBlank();
    }

    // 移动right
    public static void right() {
        changed = false;
        for (int i = 0; i < ROW; i++) {
            changeROW(i, false);
        }
        for (int i = 0; i < ROW; i++) {
            mergeSameCOL(i);
        }
        for (int i = 0; i < ROW; i++) {
            changeROW(i, false);
        }
        if (!changed)
            return;
        reset();
        clearBlank();
    }

    // 移动down
    public static void down() {
        changed = false;
        for (int i = 0; i < COL; i++) {
            changeCol(i, false);
        }
        for (int i = 0; i < COL; i++) {
            mergeSameROW(i);
        }
        for (int i = 0; i < COL; i++) {
            changeCol(i, false);
        }
        if (!changed)
            return;
        reset();
        clearBlank();
    }

    // 移动left
    public static void left() {
        changed = false;
        for (int i = 0; i < ROW; i++) {
            changeROW(i, true);
        }
        for (int i = 0; i < ROW; i++) {
            mergeSameCOL(i);
        }
        for (int i = 0; i < ROW; i++) {
            changeROW(i, true);
        }
        if (!changed)
            return;
        reset();
        clearBlank();
    }

    // 重新设置移动后的界面
    public static void reset() {
        for (int i = 0; i < LENGTH; i++) {
            final int iUse = i;
            SwingUtilities.invokeLater(() -> {
                body.getDiv(iUse).setTextAndColor(numbers[iUse]);
            });
        }
    }

    // 不能移动
    // 需要更改一下，用于文件操作的最高分纪录
    public static boolean canMove() {
        // 有空地,说明还能走,游戏没有结束
        for (int i = 0; i < LENGTH; i++)
            if (numbers[i] == 0)
                return true;
        // 看是否有左右相连的空格数字一样
        for (int i = 0; i < ROW; i++)
            for (int j = 0; j < COL - 1; j++)
                if (numbers[i * COL + j] == numbers[i * COL + j + 1])
                    return true;
        // 看是否有上下相连的空格数字一样
        for (int i = 0; i < COL; i++)
            for (int j = 0; j < ROW - 1; j++)
                if (numbers[j * COL + i] == numbers[(j + 1) * COL + i])
                    return true;
        int nowBestScore = Integer.parseInt(nav.getScoreDiv(1).getScore().getText());
        if(((nowBestScore >= Global.bestScore)&&(Global.username.equals(Global.bestPlayer)))
                || nowBestScore > Global.bestScore) {//比之前最高分高或者已经更新过了(bestplayer那里)
            Global.writeOut(nowBestScore);//结束一盘游戏就进行文件读写
            Global.bestScore = nowBestScore;
            Global.bestPlayer = Global.username;
        }
        new Fail(score, Global.username);
        return false;
    }

    // 随机生成数字
    public static void clearBlank() {
        int number = (int) Math.floor(Math.random() * blankNumber);

        int randomNumber = 2;// 随机生成的数字 2:4:8 = 7:2:1
        double r3 = Math.random();
        if(r3>0.9)
            randomNumber = 8;
        else if(r3>0.7)
            randomNumber = 4;
        final int randomNumberUse = randomNumber;//用于invokeLater
        for (int i = 0; i < LENGTH; i++) {
            if (numbers[i] == 0) {
                if (number == 0) {
                    numbers[i] = randomNumberUse;
                    final int iUse = i;
                    SwingUtilities.invokeLater(() -> {
                        body.getDiv(iUse).setTextAndColor(randomNumberUse);
                    });
                    blankNumber--;
                    return;
                } else
                    number--;
            }
        }
    }


}

class Nav extends JPanel {
    private static final long serialVersionUID = 2049488188909508336L;
    private static Score sc[] = new Score[2];

    Nav() {
        super();
        setLayout(null);
        setVisible(true);
        setSize(500, 100);
        setLocation(0, 30);// 手动调位置
        setBackground(new Color(0xfaf8ef));
        JLabel lbl1 = new JLabel("2048", JLabel.CENTER);
        lbl1.setHorizontalAlignment(JLabel.CENTER);
        lbl1.setBackground(new Color(0xffeb8e));
        lbl1.setOpaque(true);// 设置成不透明的背景色才能有效
        lbl1.setLocation(10, 5);
        lbl1.setSize(150, 90);
        lbl1.setFont(new Font("consolas", 0, 45));
        lbl1.setVisible(true);
        add(lbl1);
        sc[0] = new Score("SCORE", true);
        sc[1] = new Score("BEST", false);
        add(sc[0]);
        add(sc[1]);
    }

    // 函数有毒,设置成成员变量ok
    Score getScoreDiv(int index) {
        return sc[index];
    }
}

class Body extends JPanel {
    private static final long serialVersionUID = -3758896259538699409L;
    private static Div divs[] = new Div[16];

    Body() {
        super();
        setVisible(true);
        GridLayout gl = new GridLayout(4, 4);
        gl.setHgap(5);// 设置上下左右间距
        gl.setVgap(5);
        setLayout(gl);
        setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        setSize(430, 430);
        setLocation(30, 215);
        setBackground(new Color(0xb7aa9c));
        // Div divs[] = new Div[16];
        for (int i = 0; i < 16; i++) {
            divs[i] = new Div(0);
            add(divs[i]);
        }
    }

    Div getDiv(int index) {
        return divs[index];
    }
}

class Div extends JLabel {
    private static final long serialVersionUID = 724783680347585002L;

    Div(int a) {
        super();
        if (a != 0)
            setText(a + "");// 设置文字
        else
            setText("");
        setBackground(getBackground(a));// 设置背景色
        setOpaque(true);// 设置成不透明的背景色才能有效
        setSize(100, 100);// 设置大小
        setHorizontalAlignment(JLabel.CENTER);// 设置文字居中
        setFont(new Font("consolas", 0, 45));
        setVisible(true);
    }

    // 得到背景色
    public Color getBackground(int value) {
        switch (value) {
        case 2:
            return new Color(0xeee4da);
        case 4:
            return new Color(0xede0c8);
        case 8:
            return new Color(0xf2b179);
        case 16:
            return new Color(0xf59563);
        case 32:
            return new Color(0xf67c5f);
        case 64:
            return new Color(0xf65e3b);
        case 128:
            return new Color(0xedcf72);
        case 256:
            return new Color(0xedcc61);
        case 512:
            return new Color(0xedc850);
        case 1024:
            return new Color(0xedc53f);
        case 2048:
            return new Color(0xedc22e);
        }
        return new Color(0xcdc1b4);// 没有数字的颜色
        // #edc22e 最高分
    }

    public void setTextAndColor(int number) {
        if (number == 0)
            setText("");
        else
            setText("" + number);
        setBackground(getBackground(number));
    }
}

class Score extends JPanel {
    private static final long serialVersionUID = 8152412781308334999L;
    private JLabel lbl1;
    private JLabel lbl2;

    Score(String name, boolean score) {
        super();
        GridLayout gl = new GridLayout(2, 1);
        lbl1 = new JLabel(name, JLabel.CENTER);
        lbl2 = new JLabel("" + 0, JLabel.CENTER);
        if(!score) {
            //lbl2.setText(Global.bestScore+"("+Global.bestPlayer+")");
            lbl2.setText(Global.bestScore+"");
            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e){
                    Global.gbBest.setVisible(true);
                }
                @Override
                public void mouseExited(MouseEvent e){
                    Global.gbBest.setVisible(false);
                }
            });
        }
        lbl1.setText(name);
        lbl1.setFont(new Font("consolas", 0, 30));
        lbl2.setFont(new Font("consolas", 0, 25));
        lbl1.setOpaque(true);
        lbl2.setOpaque(true);
        setLayout(gl);
        add(lbl1);
        add(lbl2);
        setVisible(true);
        setSize(150, 90);
        int left;// 设置位置
        if (score)
            left = 170;
        else
            left = 330;
        setLocation(left, 5);
    }

    public JLabel getScore() {
        return lbl2;
    }
}

class Detail extends JFrame{
    private static final long serialVersionUID = -9051478882939043723L;
    private JLabel jlb[] = new JLabel[3];
    Detail(){
        super("details");
        setLayout(new GridLayout(3,1));
        setVisible(false);//设置为不可见
        jlb[0] = new JLabel("1.default username:default",JLabel.CENTER);
        jlb[1] = new JLabel("2.strat game:ESC",JLabel.CENTER);
        jlb[2] = new JLabel("3.move:arrows",JLabel.CENTER);
        for(int i=0;i<3;i++) {
            jlb[i].setFont(new Font("consolas",0,30));
            jlb[i].setHorizontalAlignment(JLabel.CENTER);
            add(jlb[i]);
        }
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(500,400);
        setLocationRelativeTo(null);// 设置为窗体居中
        setResizable(false);// 设置界面大小不能够更改
    }
}

class Fail extends JFrame{
    private static final long serialVersionUID = 1L;
    private static JLabel jlb;
    Fail(int score, String _player){
        super("result");
        setLayout(null);
        setVisible(true);
        jlb = new JLabel(_player + "' " + "score is " + score);
        jlb.setLocation(0,0);
        jlb.setSize(500,200);
        jlb.setFont(new Font("consolas", 0, 30));
        jlb.setHorizontalAlignment(JLabel.CENTER);
        getContentPane().add(jlb);

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(500,200);
        setLocationRelativeTo(null);// 设置为窗体居中
        setResizable(false);// 设置界面大小不能够更改
    }
}

class Introduction extends JFrame{
    private static final long serialVersionUID = 7800793188088328215L;
    private JLabel jbl;
    private TextField tf;
    Introduction(){
        super("login");
        setLayout(new GridLayout(2,1));
        setVisible(true);
        setSize(400,200);
        setLocationRelativeTo(null);// 设置为窗体居中
        setResizable(false);// 设置界面大小不能够更改
        setAlwaysOnTop(true);//始终置顶
        jbl = new JLabel("input your name",JLabel.CENTER);
        jbl.setFont(new Font("consolas",0,30));
        tf = new TextField();
        tf.setFont(new Font("consolas",0,30));
        tf.addActionListener((e)->{
            String _username = ((TextField)e.getSource()).getText();
            if(!"".equals(_username))
                Global.username = _username;
            SwingUtilities.invokeLater(() -> {
                Global.gjfm.getPlayer().setText("player : "+Global.username);
                Global.gbBest.getlbl().setText(Global.bestPlayer+" : "+Global.bestScore);
            });
            setVisible(false);
        });
        getContentPane().add(jbl);
        getContentPane().add(tf);
    }
}

class Best extends JFrame{
    private static final long serialVersionUID = 1131129811706712130L;
    private JLabel lbl;
    Best(){
        super("best player");
        setLayout(null);
        setSize(410,210);
        setLocationRelativeTo(null);// 设置为窗体居中
        setResizable(false);//设置界面大小不能够更改
        setVisible(false);//设置为不可见
        lbl = new JLabel(Global.bestPlayer+" : "+Global.bestScore,JLabel.CENTER);
        lbl.setSize(400,200);
        lbl.setLocation(5,5);
        lbl.setFont(new Font("consolas",0,30));
        getContentPane().add(lbl);
    }
    JLabel getlbl() {
        return lbl;
    }
}

class Global{
    public static String username = "default";
    public static String bestPlayer = "default";
    public static int bestScore = 0;
    public static GameJFrame gjfm;
    public static Best gbBest = new Best();
    public final static String nowPathString = "data.2048";
    public static void writeOut(int _score) {
        File file = new File(Global.nowPathString);
        if(!file.exists()){
            try {
                file.createNewFile();
                System.out.println("create file successfully!");
            } catch (IOException e) {
                System.out.println("failed to create file!");
                e.printStackTrace();
            }
        }
        try {
            File fout = new File(Global.nowPathString);
            PrintWriter out = new PrintWriter(new FileWriter(fout));
            out.println(Global.username);
            out.println(_score);
            out.close();                            // 关闭缓冲读入流及文件读入流的连接.
        } catch (FileNotFoundException e1) {
            System.err.println("File not found!");
        } catch (IOException e2) {
            e2.printStackTrace();
        }
    }
    public static void writeIn() {
        File file = new File(Global.nowPathString);
        if(!file.exists()){
            try {
                file.createNewFile();
                System.out.println("create file successfully!");
            } catch (IOException e) {
                System.out.println("failed to create file!");
                e.printStackTrace();
            }
            return;
        }
        try {
            File fin = new File(Global.nowPathString);
            BufferedReader in = new BufferedReader(new FileReader(fin));
            String s = in.readLine();
            if(s!=null) {
                Global.bestPlayer = s;
                Global.bestScore = Integer.parseInt(in.readLine());
            }else {
                System.out.println("the file is empty!");
            }
            in.close();// 关闭缓冲读入流及文件读入流的连接.
        } catch (FileNotFoundException e1) {
            System.err.println("File not found!");
        } catch (IOException e2) {
            e2.printStackTrace();
        }
        Global.gbBest.getlbl().setText(Global.bestPlayer+" : "+Global.bestScore);//文件读写完成之后再次更新界面
    }
}
