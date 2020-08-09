/**
 * @author banbao
 * ExitCode : 
 *  参数长度不正确(1)
 *  非压缩/解压缩参数(2)
 *  源文件不存在(3)
 *  目标文件已经存在(4)
 *  其他异常(5)
 */

import java.util.zip.GZIPOutputStream;
import java.util.zip.GZIPInputStream;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.File;

public class GZip {
    private static final String tab = "    ";
    private static void error(int exitCode) {
        System.err.println(
            "Usage:\n"
            + tab 
            + "(1) compress : java GZIP -c srcFileName [dstFileName]\n" 
            + tab
            + "(2) decompress : java GZIP -d srcFileName [dstFileName]\n"
            + tab + tab 
            + "if src's suffix is not \".gz\", dst is needed!"
        );
        System.exit(exitCode);
    }
    
    public static void main(String[] args)
        throws IOException {
        boolean compress = false;
        String src, dst;
        // check srcFile is existed
        if(args.length != 3 && args.length != 2) { error(1); }
        // args[0]
        if("-c".equals(args[0])) { compress = true; }
        else if("-d".equals(args[0])) { compress = false; }
        else { error(2); }
        // src
        src = args[1];
        if(!(new File(src).exists())) { 
            System.err.println("src is not exsited!");
            System.exit(3);
        }
        // dst
        if(args.length == 3) { dst = args[2]; }
        else {
            if(compress) { dst = src + ".gz"; }
            else {
                int index = src.lastIndexOf(".gz");
                if(index == -1) { error(6); }
                dst = src.substring(0, index); 
            }
        }
        if(new File(dst).exists()) { 
            System.err.println("dst is exsited!");
            System.exit(4);
        }
        // deal
        try{
            if(compress) { compress(src, dst); }
            else { decompress(src, dst); }
        } catch (Exception e) {
            System.out.println(e);
            System.exit(5);
        }
    }

    public static void compress(String src, String dst) 
        throws Exception {
        BufferedInputStream in = 
            new BufferedInputStream(
                new FileInputStream(src));
        BufferedOutputStream out = 
            new BufferedOutputStream(
                new GZIPOutputStream(
                    new FileOutputStream(dst)));
        System.err.println("Writing file");
        int c;
        while((c = in.read()) != -1) {
            out.write(c); 
            // 会进行自动的转换 out.write((byte)c)
        }
        out.close();
        in.close();
    }
    
    public static void decompress(String src, String dst) 
        throws Exception {
        System.out.println("Extract file");
        BufferedInputStream in = 
            new BufferedInputStream(
                new GZIPInputStream(
                    new FileInputStream(src)));
        BufferedOutputStream out = 
            new BufferedOutputStream(
                new FileOutputStream(dst));
        int s;
        while((s = in.read()) != -1) {
            out.write(s);
        }
        out.close();
        in.close();
    }
}