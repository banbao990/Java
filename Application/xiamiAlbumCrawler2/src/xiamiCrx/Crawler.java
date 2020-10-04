package xiamiCrx;

import java.awt.Desktop;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.SwingUtilities;

/**
 * deal with the doowload work
 * @author banbao990
 */
public class Crawler {
    public final static String DIR = "download" + File.separatorChar
            + new SimpleDateFormat("yyyyMMdd").format(new Date()).toString();
    static {
        // mkdir
        File dir = new File(DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    private final ArrayList<CrawlerItem> imageToDownload = new ArrayList<>();
    private final MainUI gui;

    public Crawler(MainUI gui, String info) {
        this.gui = gui;
        String[] infos = info.split("\n");
        for (String str : infos) {
            CrawlerItem item = new CrawlerItem(str);
            if (item.link != null) {
                this.imageToDownload.add(item);
            }
        }
    }

    /**
     * download the images
     */
    public void start() {
        downloadImage();
    }

    /**
     * thread pool th download iamges(use CountDownLatch)
     */
    private void downloadImage() {
        SwingUtilities.invokeLater(() -> {
            gui.jtaLink.setText(
                    "Ready to Download Images into \"" + DIR + "\"!\n");
        });
        ExecutorService exec = Executors.newCachedThreadPool();
        CountDownLatch latch = new CountDownLatch(imageToDownload.size());
        for (CrawlerItem ci : imageToDownload) {
            exec.execute(new DownloadImageThread(ci, latch, gui));
        }
        // shutdown
        exec.shutdown();
        try {
            latch.await();
        } catch (InterruptedException e) {
            System.err.println("Interrupted when latch waiting!");
            e.printStackTrace();
        } finally {
            // open the folder containting the images downloaded
            openDir(DIR);
            SwingUtilities.invokeLater(() -> {
                gui.jtaLink.append("Download End!");
                // enable
                gui.myEnable();
                // update
                if (this.imageToDownload.size() != 0) {
                    gui.jcAlbumName.removeAllItems();
                    for (CrawlerItem ci : imageToDownload) {
                        gui.jcAlbumName.addItem(new MyItem(DIR, ci.fileName));
                    }
                }
            });
        }
    }

    /**
     * open file use default explorer
     * @param dirName
     */
    private void openDir(String dirName) {
        try {
            Desktop.getDesktop().open(new File(dirName));
        } catch (Exception e1) {
            System.err.println("Open Dir " + DIR + " Error!");
        }
    }
}