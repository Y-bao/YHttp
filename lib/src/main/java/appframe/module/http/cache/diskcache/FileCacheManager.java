package appframe.module.http.cache.diskcache;

import java.io.File;
import java.util.Arrays;
import java.util.Comparator;

import appframe.module.http.file.FileManager;


public abstract class FileCacheManager<T> extends FileManager<T> {
    protected int cacheMB;
    protected int minSDSize;

    public FileCacheManager() {
        cacheMB = 10;
        filerName = "cache";
        minSDSize = 10;
    }

    public FileCacheManager<T> setCacheMB(int cacheMB) {
        this.cacheMB = cacheMB;
        return this;
    }

    public FileCacheManager<T> setMinSDSize(int minSDSize) {
        this.minSDSize = minSDSize;
        return this;
    }

    @Override
    public T getFile(String filePath)  {
        updateFileTime(filePath);
        return super.getFile(filePath);
    }

    public void saveFile(T bm, String filePath) {
        super.saveFile(bm, filePath);
        sortCache();
    }

    public boolean sortCache() {
        File dir = new File(this.dir);
        File[] files = dir.listFiles();
        if (files == null) {
            return true;
        }

        int dirSize = 0;
        for (int i = 0; i < files.length; i++) {
            dirSize += files[i].length();
        }

        if (dirSize > cacheMB * MB) {
            int removeFactor = (int) ((0.4 * files.length) + 1);
            Arrays.sort(files, new FileLastModifSort());
            for (int i = 0; i < removeFactor; i++) {
                files[i].delete();
            }
        }
        return true;
    }

    public boolean removeCache() {
        return false;
    }

    public static void updateFileTime(String path) {
        File file = new File(path);
        long newModifiedTime = System.currentTimeMillis();
        file.setLastModified(newModifiedTime);
    }

    public class FileLastModifSort implements Comparator<File> {
        public int compare(File arg0, File arg1) {
            if (arg0.lastModified() > arg1.lastModified()) {
                return 1;
            } else if (arg0.lastModified() == arg1.lastModified()) {
                return 0;
            } else {
                return -1;
            }
        }
    }
}