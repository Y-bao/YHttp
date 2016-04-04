package appframe.module.http.cache.diskcache;


import appframe.utils.io.IOUtil;

public class StrCacheManager extends FileCacheManager<byte[]> {
    public String wholesaleConv = ".cache";

    @Override
    public byte[] onGetFile(String filePath) {
        filePath = filePath + wholesaleConv;
        return IOUtil.readFileToByte(filePath);
    }

    @Override
    public void onSaveFile(byte[] dataBytes, String filePath) {
        if (dataBytes == null) {
            return;
        }
        filePath = filePath + wholesaleConv;
        IOUtil.writeFile(filePath, dataBytes, false);
    }
}