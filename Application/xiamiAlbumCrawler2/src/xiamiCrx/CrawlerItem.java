package xiamiCrx;

/**
 * items in the Crawler's list
 * @author banbao(990)
 */
public class CrawlerItem {
    // should change all reference to ELE_NUM
    private static final int ELE_NUM = 5;
    public final String link, fileName;
    private final String toStringUse;

    public CrawlerItem(String info) {
        String[] infos = info.split(",,");
        if (infos.length != ELE_NUM) {
            this.link = this.fileName = this.toStringUse = null;
        } else {
            /* author,,album,,date,,grade,,link*/
            // ELE_NUM
            String tempFileName = infos[0] + "__" + infos[1] + "__" + infos[2]
                    + "__" + infos[3];
            this.link = infos[4];
            int index = this.link.lastIndexOf(".");
            String suffix = index == -1 ? ".png" : link.substring(index);
            this.toStringUse = tempFileName.replaceAll("__", " ");
            this.fileName = tempFileName.replaceAll("/", "+") + suffix;
        }
    }

    @Override
    public String toString() {
        return toStringUse;
    }
}
