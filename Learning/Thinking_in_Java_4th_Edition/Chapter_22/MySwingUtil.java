/**
 * @author banbao
 * @comment modified from example code
 */

import javax.swing.SwingUtilities;
import javax.swing.JFrame;

public class MySwingUtil {
    // https://blog.csdn.net/weixin_30617737/article/details/95740965
    public static void setSize(JFrame frame, int width, int height) {
        // 一定要在JFrame显示之前调用
        frame.pack();
        // 在默认的 setResizable(true); 情况下,titleDefaultHeight = 27,
        // 但如果 setResizable(false); titleDefaultHeight = 25
        // 默认开发阶段使用windows 经典主题,标题栏高度= 27
        int titleDefaultHeight = frame.isResizable() ? 27 : 25; 
        int containerHeight = frame.getContentPane().getHeight();
        // windows 经典主题或xp主题下 = 1
        // System.out.println("containerHeight:" + containerHeight); 
        // 取得的高度和container的高度是一样的
        // int rootPaneHeight = frame.getRootPane().getHeight(); 

        int frameHeight = frame.getHeight();
        // windows经典主题下= 28, windows xp 主题下 = 33
        // System.out.println("frameHeight:" + frameHeight); 

        // 这里就可以得到当前标题栏的实际高度
        int titleHeight = frameHeight - containerHeight; 
        if (titleHeight > titleDefaultHeight) {
            //得到当前使用的主题,标题栏 - 经典主题下的标题栏高度 = 差值
            int h = titleHeight - titleDefaultHeight; 
            //最终确定窗口的高度
            frame.setSize(width, height + h); 
        } else {
            frame.setSize(width, height);
        }
    }
    
    public static void run(
        final JFrame f, final int width, final int height) {
        SwingUtilities.invokeLater(
        () -> {
            f.setLocationRelativeTo(null); // 设置为窗体居中
            f.setTitle(f.getClass().getSimpleName());
            f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            if((width > 0) && (height > 0)) {
                setSize(f, width, height);
            }
            f.setVisible(true);
        }
        );
    }
}
