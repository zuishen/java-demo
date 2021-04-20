package work.jimmmy.zip;

import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZipTest {
    private static final String PATH = "./";

    public static void main(String[] args) {
        try {
            compressFile("uuid", Arrays.asList("fs", "alarm"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void compressFile(String src, List<String> subfile) throws IOException {
        String fileName = src + "-" + String.join("-", subfile);
        ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(PATH + fileName + ".zip"));
        System.out.println(new File(PATH).getAbsoluteFile());
        for (String name : subfile) {
            compress(PATH + "/" + src, name, zos);
        }
        zos.close();
    }

    private static void compress(String src, String subFile, ZipOutputStream zos) throws IOException {
        File file = new File(src + "/" + subFile);
        if (file.isFile()) {
            zos.putNextEntry(new ZipEntry(subFile));
            byte[] buffer = new byte[1024];
            int len = 0;
            try (FileInputStream fis = new FileInputStream(src + "/" + subFile)) {
                while ((len = fis.read(buffer)) != -1) {
                    zos.write(buffer, 0, len);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            zos.closeEntry();
        } else if (file.list() != null && file.list().length != 0){
            for (String f : file.list()) {
                compress(src, subFile + "/" + f, zos);
            }
        } else {
            zos.putNextEntry(new ZipEntry(subFile + "/"));
        }

    }
}
