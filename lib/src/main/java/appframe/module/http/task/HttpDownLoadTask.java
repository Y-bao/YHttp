package appframe.module.http.task;

import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import appframe.module.http.mode.HttpMode;
import appframe.module.http.utils.HttpResult;


public class HttpDownLoadTask extends BaseHttpTask {
    private final static String LOADING_BASE_NAME = ".downloadybao";
    private String dirPath = "";
    private boolean replace = true;

    public HttpDownLoadTask() {
        super();
    }

    public HttpDownLoadTask(HttpMode httpMode) {
        super(httpMode);
    }

    public void setReplace(boolean replace) {
        this.replace = replace;
    }

    public void setDirPath(String dirPath) {
        this.dirPath = dirPath;
    }

    private String name = "";

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public <T> String run(Class<T> cla, Object listener, int tag) throws Exception {
        InputStream inputStream = httpMode.resultStream();
        if (inputStream != null) {
            if (TextUtils.isEmpty(name)) {
                String[] urlname = httpMode.getUrl().split("/");
                name = urlname[urlname.length - 1];
            }
            String loadingName = name + LOADING_BASE_NAME;
            File loadingfile = new File(dirPath, loadingName);
            FileOutputStream fileOutputStream = new FileOutputStream(loadingfile, false);
            InputStream in = new BufferedInputStream(inputStream);
            OutputStream out = new BufferedOutputStream(fileOutputStream);
            long size = httpMode.getContentLength();
            byte[] buffer = new byte[1024];
            long sam = 0;
            int len;
            while ((len = in.read(buffer)) > -1) {
                checkThreadState();
                out.write(buffer, 0, len);
                sam += len;
            }
            in.close();
            out.close();
            File file = new File(dirPath, name);
            loadingfile.renameTo(file);
//            noticeSucceed(file);
            Log.i("url", "DownLoad:" + httpMode.getUrl());
            return file.getAbsolutePath();
        }
        Log.i("url", "DownLoad:" + httpMode.getUrl());
        return null;
    }

    public HttpResult<File> executeReFile(Class<File> cla) {
        return super.execute(cla);
    }

    @Override
    protected <T> T strToT(String str, Class<T> cla) {
        if (cla == File.class) {
            return (T) new File(str);
        }
        return null;
    }
}