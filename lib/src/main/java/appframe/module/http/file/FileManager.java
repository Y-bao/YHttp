package appframe.module.http.file;

import android.os.Environment;

import java.io.File;

import appframe.module.http.name.MD5NameGetter;
import appframe.module.http.name.NameGetter;


public abstract class FileManager<T> {
    protected static final int MB = 1024 * 1024;
    protected String path;
    protected String filerName;
    protected String dir;
    protected NameGetter nameGetter = new MD5NameGetter();

    public FileManager() {
        path = Environment.getExternalStoragePublicDirectory("YbaoFiles").getAbsolutePath();
        filerName = "files";
        dir = path + File.separator + filerName;
    }

    public FileManager<T> setPath(String path) {
        this.path = path;
        dir = path + File.separator + filerName;
        return this;
    }

    public FileManager<T> setFilerName(String filerName) {
        this.filerName = filerName;
        dir = path + File.separator + filerName;
        return this;
    }

    public void saveFile(T bm, String str) {
        onSaveFile(bm, dir + File.separator + getName(str));
    }

    public T getFile(String str) {
        return onGetFile(dir + File.separator + getName(str));
    }

    protected abstract void onSaveFile(T bm, String filePath);

    protected abstract T onGetFile(String filePath);

    public String getName(String str) {
        return nameGetter.getName(str);
    }

}