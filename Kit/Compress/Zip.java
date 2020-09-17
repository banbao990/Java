/**
 * @author banbao
 * ExitCode:
 * 命令行参数错误(1)
 * 
 * @todo 暂时不支持正则表达式
 */
import java.util.ArrayList;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;
import java.util.zip.CheckedInputStream;
import java.util.zip.CheckedOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.Adler32;
import java.util.zip.ZipFile;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.File;

public class Zip {
    private static final String tab = "    ";
    private static void error(int exitCode) {
        System.err.println(
            "Usage:\n"
            + tab
            + "(1) compress : java Zip -c file1 file2 ... -a dst [-m comment]\n"
            + tab + tab 
            + "can not change the order!\n"
            + tab 
            + "(2) decompress : java Zip -d file\n"
        );
        System.exit(exitCode);
    }
    
    public static void main(String[] args) throws IOException {
        // args error
        if(args.length < 2) { error(1); }

        // deal
        if("-c".equals(args[0])) { compress(args);}
        else if("-d".equals(args[0])) { decompress(args); }
        else { error(1); }
    }
    
    // -d file -a dst
    public static void decompress(String[] args) throws IOException {
        if(args.length != 4 || !("-a".equals(args[2]))) { error(1); }
        String dstDir = args[3];
        System.err.println("Reading file");
        FileInputStream fi = new FileInputStream(args[1]);
        CheckedInputStream csumi =
            new CheckedInputStream(fi, new Adler32());
        ZipInputStream in2 = new ZipInputStream(csumi);
        BufferedInputStream bis = new BufferedInputStream(in2);
        ZipEntry ze;
        while((ze = in2.getNextEntry()) != null) {
            System.err.println(tab + "Reading file " + ze);
            String dst = dstDir + File.separatorChar + ze.getName();
            // dir
            checkDirIsExisted(dst);
            BufferedOutputStream in = 
                new BufferedOutputStream(
                    new FileOutputStream(dst));
            int x;
            while((x = bis.read()) != -1) {
                in.write(x);
            }
            in.close();
        }
        System.err.println(tab + "Checksum: " + csumi.getChecksum().getValue());
        bis.close();
    }
    
    // -c file1 file2 ... -a dst [-m comment]
    public static void compress(String[] args) throws IOException {
        ArrayList<String> src = new ArrayList<>();
        String dst;
        int index = 1, length = args.length;
        String comment = "";
        // src
        for(;index < length && !("-a".equals(args[index])); ++index) {
            src.add(args[index]);
        }
        // lack of dst
        if(length - index < 2) { error(1); }
        dst = args[++index];
        checkDirIsExisted(dst);
        if(new File(dst).exists()) {
            System.err.println(dst + " is already existed!");
            System.exit(2);
        }
        // comment
        if(length > index + 1) {
            if(!("-m".equals(args[++index]))) { error(1); }
            for(++index; index < length; ++index){
                comment += args[index] + " ";
            }
        }
        //compress
        CheckedOutputStream csum =
            new CheckedOutputStream(
                new FileOutputStream(dst), new Adler32());
        ZipOutputStream zos = new ZipOutputStream(csum);
        BufferedOutputStream out = new BufferedOutputStream(zos);
        System.err.println(tab + "Comment : " + comment);
        zos.setComment(comment);
        // Missing method ZipInputStream.getComment()
        // https://bugs.java.com/bugdatabase/view_bug.do?bug_id=6550931
        /*
         *   It is difficult (at best) to add getComment() to ZipInputStream.
         *   The comment is stored in the zip's Central Directory, which is at the 
         *   end of the zip.  Adding a getComment() method would mean that 
         *   a ZipInputStream would have to act in a very un-stream-like manner: 
         *   seeking in to the Central Directory, extracting the comment.  
         *   While this behavior may be reasonable for files, it is not for streams.
         *   
         *   For what it's worth, the per-entry comment is also stored in the Central Directory.
        */
        for(String file : src) {
            System.err.println("Writing file " + file);
            BufferedInputStream in = 
                new BufferedInputStream(
                    new FileInputStream(file));
            zos.putNextEntry(new ZipEntry(file));
            int c;
            while((c = in.read()) != -1) {
                out.write(c);
            }
            in.close();
            out.flush();
        }
        out.close();
        // Checksum valid only after the file has been closed!
        System.err.println(tab + "Checksum: " + csum.getChecksum().getValue());
    }
    
    public static void checkDirIsExisted(String dst) {
        int index = dst.lastIndexOf(File.separatorChar);
        if(index != -1) {
            new File(dst.substring(0, index)).mkdirs();
        }
    }
}
