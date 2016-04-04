package appframe.utils.io;

import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Y-bao on 2015/11/17.
 */
public class IOUtil {

    public static File writeFile(String filename,
                                 String content, boolean isAppend) {
        return writeFile(filename, content.getBytes(), isAppend);
    }

    public static File writeFile(String filename, String content, String encoding, boolean isAppend) {
        File file = null;
        OutputStream os = null;
        try {
            file = FileUtil.createFile(filename);
            os = new FileOutputStream(file, isAppend);
            if (encoding.equals("")) {
                writeFile(filename, content.getBytes(), isAppend);
            } else {
                writeFile(filename, content.getBytes(encoding), isAppend);
            }
            os.flush();
        } catch (Exception e) {
            Log.e("FileUtil", "writeToSDCardFile:" + e.getMessage());
        } finally {
            try {
                if (os != null) {
                    os.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file;
    }

    public static File writeFile(String filename,
                                 byte[] content, boolean isAppend) {
        File file = null;
        OutputStream os = null;
        try {
            file = FileUtil.createFile(filename);
            os = new FileOutputStream(file, isAppend);
            os.write(content);
            os.flush();
        } catch (Exception e) {
            Log.e("FileUtil", "writeToSDCardFile:" + e.getMessage());
        } finally {
            try {
                if (os != null) {
                    os.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file;
    }

    /**
     * write data from inputstream to SDCard
     */
    public static File writeFromInput(String filename, InputStream input, boolean isAppend) {
        File file = null;
        OutputStream os = null;
        try {
            file = FileUtil.createFile(filename);
            os = new FileOutputStream(file, isAppend);
            byte[] data = new byte[FileUtil.KB];
            int length = -1;
            while ((length = input.read(data)) != -1) {
                os.write(data, 0, length);
            }
            os.flush();
        } catch (Exception e) {
            Log.e("FileUtil", "" + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                os.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return file;
    }

    public static String readFileToStr(String filename) {
        byte[] bytes = readFileToByte(filename);
        if (bytes != null) {
            return new String(bytes);
        }
        return null;
    }

    public static byte[] readFileToByte(String filename) {
        try {
            FileInputStream inputStream = readToInputStream(filename);
            return StreamUtil.InputStreamTOByte(inputStream);
        } catch (Exception e) {
            Log.e("IOUtil", "" + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public static FileInputStream readToInputStream(String filename) {
        File file;
        FileInputStream mStream;
        try {
            file = FileUtil.getFile(filename);
            mStream = new FileInputStream(file);
            return mStream;
        } catch (Exception e) {
            Log.e("IOUtil", "" + e.getMessage());
            e.printStackTrace();
        }
        return null;

    }
}
