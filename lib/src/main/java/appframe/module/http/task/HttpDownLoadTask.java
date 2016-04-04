package appframe.module.http.task;

import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import appframe.module.http.listener.HttpDownLoadListener;
import appframe.module.http.listener.HttpRequestListener;
import appframe.module.http.utils.HttpDownCallBack;
import appframe.module.http.utils.SizeInfos;


public class HttpDownLoadTask extends HttpTask<File> {
    private final static String LOADING_BASE_NAME = ".downloadybao";
    private String dirPath = "";
    private boolean replace = true;

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
    public File run() throws Exception {
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
                nativeSize(size, sam);
            }
            in.close();
            out.close();
            File file = new File(dirPath, name);
            loadingfile.renameTo(file);
            noticeSucceed(file);
            Log.i("url", "DownLoad:" + httpMode.getUrl());
            return file;
        }
        Log.i("url", "DownLoad:" + httpMode.getUrl());
        return null;
    }


    @Override
    public void setHttpRequestListener(HttpRequestListener httpRequestListener, int tag) {
        if (httpRequestListener != null && httpRequestListener instanceof HttpDownLoadListener) {
            httpCallback = new HttpDownCallBack((HttpDownLoadListener) httpRequestListener, tag);
            return;
        }
        super.setHttpRequestListener(httpRequestListener, tag);
    }


    protected void nativeSize(long size, long nowSize) {
        if (httpCallback != null) {
            SizeInfos sizeInfos = new SizeInfos(size, nowSize, nowSize / (float) size * 100);
            httpCallback.sendData(HttpDownCallBack.PROGRESS, sizeInfos);
        }
    }
}