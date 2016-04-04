package appframe.utils.io;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author Ybao
 */
public class StreamUtil {
    final static int BUFFER_SIZE = 4096;

    /**
     * InputStream2byte
     *
     * @param in InputStream
     * @return byte[]
     * @throws IOException
     */
    public static byte[] InputStreamTOByte(InputStream in) throws IOException {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] data = new byte[BUFFER_SIZE];
        int count;
        while ((count = in.read(data, 0, BUFFER_SIZE)) != -1) {
            outStream.write(data, 0, count);
        }
        outStream.close();
        return outStream.toByteArray();
    }

    /**
     * InputStream2String
     *
     * @param in InputStream
     * @return String
     * @throws Exception
     */
    public static String InputStreamTOString(InputStream in) throws Exception {
        return InputStreamTOString(in, "utf-8");
    }

    /**
     * InputStream2String
     *
     * @param in
     * @param charsetName
     * @return
     * @throws Exception
     */
    public static String InputStreamTOString(InputStream in, String charsetName) throws Exception {

        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] data = new byte[BUFFER_SIZE];
        int count = -1;
        while ((count = in.read(data, 0, BUFFER_SIZE)) != -1)
            outStream.write(data, 0, count);

        data = null;
        return new String(outStream.toByteArray(), charsetName);
    }
}
