/**
 * @author banbao
 * @comment modified from example code
 */

import javax.swing.SwingUtilities;
import javax.swing.JFrame;

public class MySwingUtil {
    // https://blog.csdn.net/weixin_30617737/article/details/95740965
    public static void setSize(JFrame frame, int width, int height) {
        // һ��Ҫ��JFrame��ʾ֮ǰ����
        frame.pack();
        // ��Ĭ�ϵ� setResizable(true); �����,titleDefaultHeight = 27,
        // ����� setResizable(false); titleDefaultHeight = 25
        // Ĭ�Ͽ����׶�ʹ��windows ��������,�������߶�= 27
        int titleDefaultHeight = frame.isResizable() ? 27 : 25; 
        int containerHeight = frame.getContentPane().getHeight();
        // windows ���������xp������ = 1
        // System.out.println("containerHeight:" + containerHeight); 
        // ȡ�õĸ߶Ⱥ�container�ĸ߶���һ����
        // int rootPaneHeight = frame.getRootPane().getHeight(); 

        int frameHeight = frame.getHeight();
        // windows����������= 28, windows xp ������ = 33
        // System.out.println("frameHeight:" + frameHeight); 

        // ����Ϳ��Եõ���ǰ��������ʵ�ʸ߶�
        int titleHeight = frameHeight - containerHeight; 
        if (titleHeight > titleDefaultHeight) {
            //�õ���ǰʹ�õ�����,������ - ���������µı������߶� = ��ֵ
            int h = titleHeight - titleDefaultHeight; 
            //����ȷ�����ڵĸ߶�
            frame.setSize(width, height + h); 
        } else {
            frame.setSize(width, height);
        }
    }
    
    public static void run(
        final JFrame f, final int width, final int height) {
        SwingUtilities.invokeLater(
        () -> {
            f.setLocationRelativeTo(null); // ����Ϊ�������
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