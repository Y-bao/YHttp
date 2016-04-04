package appframe.module.http.utils;

import android.os.Message;

import appframe.module.http.listener.HttpDownLoadListener;


/**
 * Created by Y-bao on 2016/1/22.
 */
public class HttpDownCallBack extends HttpCallback {
    public static final int PROGRESS = -1;

    public HttpDownCallBack(HttpDownLoadListener httpDownLoadListener, int tag) {
        super(httpDownLoadListener, tag);
    }

    @Override
    public void handleMessage(Message msg) {
        if (httpRequestListener == null) {
            return;
        }
        switch (msg.what) {
            case PROGRESS:
                if (httpRequestListener != null && httpRequestListener instanceof HttpDownLoadListener) {
                    SizeInfos sizeInfos = (SizeInfos) msg.obj;
                    ((HttpDownLoadListener) httpRequestListener).onProgress(sizeInfos, tag);
                }
                return;
        }
        super.handleMessage(msg);
    }
}
