package appframe.module.http.task;


import appframe.module.http.file.FileManager;

public abstract class HttpCacheTask<T> extends HttpTask<T> {
    private FileManager<byte[]> fileManager;


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