package xiamiCrx;

/**
 * the item in the combo-box
 * @author banbao
 */
public class MyItem {
    private final static int ITEM_NUM = 4;
    public final String dirName, fileName;
    private final String toStringUse;

    public MyItem(String dirName, String fileName) {
        // TODO
        this.dirName = dirName;
        this.fileName = fileName;
        int dotPosition = -1;
        if ((dotPosition = fileName.lastIndexOf('.')) != -1) {
            fileName = fileName.substring(0, dotPosition);
        }
        /* author__album__date__grade */
        String[] infos = fileName.split("__");
        if (infos.length == ITEM_NUM) {
            toStringUse = infos[0] + " - " + infos[1] + " : date(" + infos[2]
                    + "), grade(" + infos[3] + ")";
        } else {
            toStringUse = fileName.replaceAll("__", " - ");
        }
    }

    @Override
    public String toString() {
        return toStringUse;
    }
}
