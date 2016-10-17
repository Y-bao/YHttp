package appframe.module.http.task;


import appframe.module.http.file.FileManager;
import appframe.module.http.mode.HttpMode;

public abstract class BaseHttpCacheTask extends BaseJsonHttpTask {
    private FileManager<byte[]> fileManager;

    public BaseHttpCacheTask() {
        super();
    }

    public BaseHttpCacheTask(HttpMode httpMode) {
        super(httpMode);
    }


    public FileManager<byte[]> getFileManager() {
        return fileManager;
    }

    public void setFileManager(FileManager<byte[]> fileManager) {
        this.fileManager = fileManager;
    }


    protected byte[] getCache(String name) {
        if (fileManager != null) {
            return fileManager.getFile(name);
        }
        return null;
    }

    protected void saveCache(String name, byte[] data) {
        if (fileManager != null) {
            fileManager.saveFile(data, name);
        }
    }

}