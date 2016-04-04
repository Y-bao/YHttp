package appframe.module.http.task;

import android.text.TextUtils;

import java.net.Proxy;
import java.util.HashMap;
import java.util.concurrent.Callable;

import appframe.module.http.TaskController;
import appframe.module.http.listener.HttpRequestListener;
import appframe.module.http.mode.HttpMode;
import appframe.module.http.mode.HttpPost;
import appframe.module.http.utils.HttpCallback;
import appframe.module.http.utils.HttpResult;

public abstract class HttpTask<T> implements Callable<HttpResult<T>> {
    public static final String DEF_CHARSET = "UTF-8";

    protected String charset;
    protected HttpMode httpMode;
    protected HttpCallback httpCallback;
    protected boolean repeat = false;
    protected int repeatCount = 2;
    protected long repeatTime = 3000;
    protected long waitTime = 3000;
    private int tag = 0;
    TaskController taskController;

    public HttpTask() {
        this(null);
    }

    public HttpTask(HttpMode httpMode) {
        charset = DEF_CHARSET;
        if (httpMode == null) {
            this.httpMode = new HttpPost();
        } else {
            this.httpMode = httpMode;
        }
    }

    public void setTaskController(TaskController taskController) {
        this.taskController = taskController;
    }

    public void setRepeat(boolean repeat) {
        this.repeat = repeat;
    }

    public void setRepeatCount(int repeatCount) {
        this.repeatCount = repeatCount;
    }

    public void setRepeatTime(long repeatTime) {
        this.repeatTime = repeatTime;
    }

    public void setHeader(HashMap<String, String> header) {
        httpMode.setHeader(header);
    }

    public String getCharset() {
        return charset;
    }

    public void setCharset(String charset) {
        if (TextUtils.isEmpty(charset)) {
            this.charset = DEF_CHARSET;
        }
        this.charset = charset;
    }


    public void setConnectTimeout(int connectTimeout) {
        httpMode.setConnectTimeout(connectTimeout);
    }

    public void setReadTimeout(int readTimeout) {
        httpMode.setReadTimeout(readTimeout);
    }

    public void setProxy(Proxy proxy) {
        httpMode.setProxy(proxy);
    }

    public void setUrl(String url) {
        httpMode.setUrl(url);
    }

    public void setData(String data) {
        if (httpMode instanceof HttpPost) {
            ((HttpPost) httpMode).setData(data);
        }
    }

    public HttpMode getHttpMode() {
        return httpMode;
    }

    public void setHttpRequestListener(HttpRequestListener httpRequestListener, int tag) {
        this.tag = tag;
        httpCallback = new HttpCallback(httpRequestListener, tag);
    }


    public boolean isCancel() {
        if (taskController != null) {
            return taskController.isCencl();
        }
        return false;
    }

    public boolean isPause() {
        if (taskController != null) {
            return taskController.isPause();
        }
        return false;
    }

    @Override
    public HttpResult<T> call() {
        HttpResult<T> result = new HttpResult<T>();
        int times = 0;
        while (true) {
            if (isCancel()) {
                noticeCencl();
                result.setCencl();
                break;
            }
            noticeStart();
            try {
                T t = this.run();
                if (t != null) {
                    result.setData(t);
                } else {
                    result.setFailed("Request Failed");
                }
            } catch (Exception e) {
                result.setError(e);
            }

            times++;
            if (result.isSucceed()) {
                break;
            } else if (isCancel()) {
                noticeCencl();
                result.setCencl();
                break;
            } else if (repeat && (repeatCount < 0 || repeatCount > times)) {
                try {
                    noticeFailedToRestart();
                    Thread.sleep(repeatTime);
                } catch (Exception e) {
                }
                continue;
            } else {
                if (result.isError()) {
                    noticeError(result.getException().getMessage());
                } else {
                    noticeFailed(result.getMsg());
                }
                break;
            }

        }
        result.setTag(tag);
        return result;
    }

    public abstract T run() throws Exception;


    private void noticeStart() {
        if (httpCallback != null) {
            httpCallback.sendData(HttpCallback.START);
        }
    }

    protected void noticeSucceed(T data) {
        if (httpCallback == null) {
            return;
        }
        httpCallback.sendData(HttpCallback.SUCCEED, data);
    }

    private void noticeFailed(String msg) {
        if (httpCallback == null) {
            return;
        }
        httpCallback.sendData(HttpCallback.FAILED, msg);
    }

    private void noticeError(String msg) {
        if (httpCallback == null) {
            return;
        }
        httpCallback.sendData(HttpCallback.ERROR, "Request Error:" + msg);
    }

    private void noticeFailedToRestart() {
        if (httpCallback == null) {
            return;
        }
        httpCallback.sendData(HttpCallback.FAILED_TO_RESTART, "" + repeatTime);
    }

    private void noticeCencl() {
        if (httpCallback == null) {
            return;
        }
        httpCallback.sendData(HttpCallback.CANCEL);
    }

    protected void checkThreadState() throws Exception {
        if (isCancel()) {
            Thread.sleep(waitTime);
            Thread.currentThread().interrupt();
        }
        while (isPause()) {
            Thread.sleep(waitTime);
        }
    }

    public class Builder {

    }
}