package xiamiCrx;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.CountDownLatch;

import javax.swing.SwingUtilities;

/**
 * single thread to download one image
 * @author banbao(990)
 */
public class DownloadImageThread implements Runnable {
    private final CountDownLatch latch;
    private final CrawlerItem ci;
    private final MainUI gui;

    public DownloadImageThread(CrawlerItem ci, CountDownLatch latch,
            MainUI gui) {
        this.ci = ci;
        this.latch = latch;
        this.gui = gui;
    }

    @Override
    public void run() {
        downloadImage();
        latch.countDown();
    }

    // TODO exeption control is not safe
    public void downloadImage() {
        String fileName = Crawler.DIR + File.separatorChar + ci.fileName;
        final String outputString;
        if (new File(fileName).exists()) {
            outputString = fileName + " is already exsited!\n";
        } else {
            URL url = null;
            try {
                url = new URL(ci.link);
            } catch (MalformedURLException e) {
                System.err.println(e);
                return;
            }
            try (InputStream input = url.openStream();
                    OutputStream output = new FileOutputStream(fileName)) {
                byte[] data = new byte[1024];
                int length;
                while ((length = input.read(data)) != -1) {
                    output.write(data, 0, length);
                }
            } catch (IOException e) {
                System.err.println("Download Fails!");
            }
            outputString = fileName + " Download Successfully!\n";
        }
        System.out.format(outputString);
        SwingUtilities.invokeLater(() -> {
            gui.jtaLink.append(outputString);
        });
    }
}
