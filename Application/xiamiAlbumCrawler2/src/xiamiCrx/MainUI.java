package xiamiCrx;

import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.plaf.FontUIResource;

/**
 * Main UI
 * @author banbao(990)
 * @version 1.0(with js)
 */
public class MainUI extends JFrame {
    private static final String CANNOT_FIND_IMAGES = "No Images Suffixed with \".png/.jpg/.jpeg\" in the Chosen Dir!";
    private static final String DEFAULT_IMAGE_DIR = "ImageWithDetails";
    private static final String TIPS = "步骤如下（谷歌浏览器为例）：\n"
            + "1. 打开网站： https://www.xiami.com/\n" + "2. 输入待查找的专辑名称，查找\n"
            + "3. 点击专辑\n" + "4. F12 打开控制台，点击 Console\n"
            + "5. 将 js/cout.js 文件中的内容复制到 console\n" + "6. 复制输出内容到文本框下载即可";
    private static final int IMAGE_SIZE = 500;
    private static final long serialVersionUID = 728088126521951556L;
    private static final Font DEFAULT_FONT = new Font("微软雅黑", Font.BOLD, 20);
    private JLabel jlAlbumImg = new JLabel("", JLabel.CENTER);
    private JLabel jlReset = new JLabel("重置", JLabel.CENTER);
    protected JComboBox<MyItem> jcAlbumName = new JComboBox<>();
    protected JTextArea jtaLink = new JTextArea("将浏览器中输出信息粘贴到这里", 10, 50);

    public JLabel makeHeader() {
        JLabel jlHeader = new JLabel("虾米音乐专辑封面下载器", JLabel.CENTER);
        jlHeader.setFont(new Font("微软雅黑", Font.BOLD, 20));
        return jlHeader;
    }

    /**
     * jpInfo = jtaLink + jp2;
     * jp2 = jTip + jlParse;
     */
    public JPanel makeInputInfo() {
        // jpInfo
        JPanel jpInfo = new JPanel();
        jpInfo.setLayout(new BorderLayout());
        setPaddingToJPanel(jpInfo, 10, 10, 10, 10);
        jpInfo.add(new JScrollPane(jtaLink));
        // jp2
        JPanel jp2 = new JPanel();
        setPaddingToJPanel(jp2, 10, 10, 10, 10);
        jp2.setLayout(new GridLayout(1, 3));
        JLabel jTip = new JLabel("操作提示", JLabel.CENTER);
        JLabel jpDownload = new JLabel("下载文件", JLabel.CENTER);
        JLabel jpClear = new JLabel("清空输入", JLabel.CENTER);
        JLabel jpSpecified = new JLabel("打开指定文件夹");
        jTip.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                new Thread(() -> {
                    try {
                        // open file use default explorer
                        Desktop.getDesktop().open(new File("js/cout.js"));
                    } catch (Exception e1) {
                        System.err.println("Open File \"js/cout.js\" Error!");
                        try {
                            Desktop.getDesktop().open(new File("js"));
                        } catch (Exception e2) {
                            System.err.println("Open Folder \"js\" Error!");
                        }
                    }
                }).start();
                // show tips
                JOptionPane.showMessageDialog(null, TIPS);
            }
        });
        jpDownload.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // disable
                myDisable();
                // start to download
                String str = jtaLink.getText();
                new Thread(() -> new Crawler(MainUI.this, str).start()).start();
            }
        });
        jpClear.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (jtaLink.isEditable()) {
                    jtaLink.setText("");
                }
            }
        });
        jpSpecified.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (jtaLink.isEditable()) {
                    showImagesFromSpecifiedDir();
                }
            }
        });
        jp2.add(jTip);
        jp2.add(jpDownload);
        jp2.add(jpClear);
        jp2.add(jpSpecified);
        jpInfo.add(jp2, BorderLayout.SOUTH);
        return jpInfo;
    }

    /**
     * cooperate with myDisable()
     */
    public void myEnable() {
        jtaLink.setEditable(true);
    }

    /**
     * cooperate with myEnable()
     */
    public void myDisable() {
        jtaLink.setEditable(false);
    }

    public JPanel makeAlbumPanel() {
        JPanel jpAlbum = new JPanel();
        jpAlbum.setLayout(new BorderLayout());
        // init
        // ActionsListener(choose one file to show)
        this.jcAlbumName.addActionListener((e) -> {
            @SuppressWarnings("unchecked")
            MyItem chosenItem = (MyItem) (((JComboBox<MyItem>) e.getSource())
                    .getSelectedItem());
            // when reset
            if (chosenItem == null) {
                return;
            }
            this.setTheAlbum(chosenItem.dirName, chosenItem.fileName);
        });
        updateAlbum(DEFAULT_IMAGE_DIR);
        jpAlbum.add(jlAlbumImg);
        jpAlbum.add(jcAlbumName, BorderLayout.SOUTH);
        return jpAlbum;
    }

    /**
     * update the JComboBox, change the images to show
     * @param dirName
     */
    public void updateAlbum(String dirName) {
        File dir = new File(dirName);
        String[] fileNames = dir.list((dirInList, fileName) -> {
            return fileName.endsWith(".png") || fileName.endsWith(".jpg")
                    || fileName.endsWith(".jpeg");
        });
        if (fileNames.length == 0) {
            System.err.println(CANNOT_FIND_IMAGES);
            SwingUtilities.invokeLater(() -> {
                this.jtaLink.setText(CANNOT_FIND_IMAGES);
            });
        } else {
            this.jcAlbumName.removeAllItems();
            for (String fileName : fileNames) {
                this.jcAlbumName.addItem(new MyItem(dirName, fileName));
            }
        }
    }

    /**
     * scale the image to IMAGE_SIZE * IMAGE_SIZE
     * @param oldBufferedImage
     * @return new scaled image
     */
    public ImageIcon getFitableImage(ImageIcon imageIcon) {
        Image oldImage = imageIcon.getImage();
        Image newImage = oldImage.getScaledInstance(IMAGE_SIZE, IMAGE_SIZE,
                Image.SCALE_SMOOTH);
        return new ImageIcon(newImage);
    }

    public void setTheAlbum(String dirName, String imageName) {
        final String albumName = dirName + File.separator + imageName;
        new Thread(() -> {
            BufferedImage biAlbum = null;
            try {
                biAlbum = ImageIO.read(new File(albumName));
            } catch (IOException e) {
                System.err.println("File \"" + albumName + "\" is not found!");
            }
            // File Found
            if (biAlbum != null) {
                ImageIcon imageIcon = new ImageIcon(biAlbum);
                ImageIcon finalCopy = getFitableImage(imageIcon);
                SwingUtilities.invokeLater(() -> {
                    // resize
                    this.jlAlbumImg.setIcon(finalCopy);
                });
            }
        }).start();
    }

    /**
     * set padding to JPanel(jp)
     */
    public JPanel setPaddingToJPanel(JPanel jp, int top, int left, int bottom,
            int right) {
        Border padding = BorderFactory.createEmptyBorder(top, left, bottom,
                right);
        jp.setBorder(padding);
        return jp;
    }

    /**
     * constructor with no args
     */
    public MainUI() {
        // set the font for JOptionPane
        UIManager.put("OptionPane.buttonFont",
                new FontUIResource(DEFAULT_FONT));
        UIManager.put("OptionPane.messageFont",
                new FontUIResource(DEFAULT_FONT));
        // construct the component
        JPanel jp1 = new JPanel();
        setPaddingToJPanel(jp1, 10, 0, 10, 0);
        jp1.setLayout(new BorderLayout(10, 0));
        jp1.add(makeHeader(), BorderLayout.NORTH);
        JPanel jp2 = new JPanel();
        setPaddingToJPanel(jp2, 0, 0, 50, 0);
        jp2.setLayout(new BorderLayout());
        jp2.add(makeInputInfo(), BorderLayout.NORTH);
        jp2.add(makeAlbumPanel());
        jp1.add(jp2);
        jlReset.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                MainUI.this.updateAlbum(DEFAULT_IMAGE_DIR);
            }
        });
        jp1.add(jlReset, BorderLayout.SOUTH);
        this.add(jp1);
    }

    /**
     * show images from specified dir
     */
    public void showImagesFromSpecifiedDir() {
        JFileChooser chooser = new JFileChooser(".");
        // only choose directory
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int returnVal = chooser.showOpenDialog(null);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            final String dirName = chooser.getSelectedFile().getAbsolutePath();
            System.out.println("You chose to open this directory: " + dirName);
            updateAlbum(dirName);
        }
    }

    /**
     * construct the GUI
     * @param args
     */
    public static void main(String... args) {
        MainUI xiami = new MainUI();
        SwingUtilities.invokeLater(() -> {
            // put center
            // xiami.setLocationRelativeTo(null);
            xiami.setSize(800, 1000);
            // put center
            Toolkit toolkit = Toolkit.getDefaultToolkit();
            int x = (int) (toolkit.getScreenSize().getWidth()
                    - xiami.getWidth()) / 2;
            int y = (int) (toolkit.getScreenSize().getHeight()
                    - xiami.getHeight()) / 2;
            xiami.setLocation(x, y);
            xiami.setTitle(xiami.getClass().getSimpleName());
            xiami.setDefaultCloseOperation(EXIT_ON_CLOSE);
            // xiami.setResizable(false);
            xiami.setVisible(true);
        });
    }
}
