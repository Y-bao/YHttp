package appframe.module.http.utils;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import appframe.module.http.listener.HttpDataListener;
import appframe.module.http.listener.HttpRequestListener;


public class HttpCallback extends Handler {

    public static final int START = 0;
    public static final int SUCCEED = 1;
    public static final int FAILED = 2;
    public static final int ERROR = 3;
    public static final int FAILED_TO_RESTART = 4;
    public static final int CANCEL = 6;
    public HttpRequestListener httpRequestListener;
    public int tag;

    public HttpCallback(HttpRequestListener httpRequestListener, int tag) {
        super(Looper.getMainLooper());
        this.httpRequestListener = httpRequestListener;
        this.tag = tag;
    }

    @Override
    public void handleMessage(Message msg) {
        if (httpRequestListener == null) {
            return;
        }
        switch (msg.what) {
            case START:
                httpRequestListener.onStart(tag);
                break;
            default:
                try {
                    String str = "";
                    if (msg.obj != null) {
                        str = msg.obj.toString();
                    }
                    httpRequestListener.onFinish(msg.what, str, tag);
                } catch (Exception e) {
                    httpRequestListener.onFinish(FAILED, e.getMessage(), tag);
                }
        }
        return;
    }

    public void sendData(int state) {
        sendMessage(obtainMessage(state));
    }

    public void sendData(int state, Object obj) {
        if (state == HttpCallback.SUCCEED && httpRequestListener != null && httpRequestListener instanceof HttpDataListener) {
            ((HttpDataListener) httpRequestListener).onGetData(obj != null ? obj.toString() : "", tag);
        }
        sendMessage(obtainMessage(state, obj));
    }
}
