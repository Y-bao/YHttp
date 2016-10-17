package appframe.module.http.task;

import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import java.net.Proxy;
import java.util.HashMap;

import appframe.module.http.TaskController;
import appframe.module.http.listener.HttpDataListener;
import appframe.module.http.listener.HttpRequestListener;
import appframe.module.http.mode.HttpGet;
import appframe.module.http.mode.HttpMode;
import appframe.module.http.mode.HttpPost;
import appframe.module.http.utils.HttpResult;

public abstract class BaseHttpTask {
    public static final String DEF_CHARSET = "UTF-8";

    protected String charset;
    protected HttpMode httpMode;
    protected boolean repeat = false;
    protected int repeatCount = 2;
    protected long repeatTime = 3000;
    protected long waitTime = 3000;
    TaskController taskController;

    public BaseHttpTask() {
        this(null);
    }

    public BaseHttpTask(HttpMode httpMode) {
        charset = DEF_CHARSET;
        if (httpMode == null) {
            this.httpMode = new HttpGet();
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

    public HttpResult<String> execute() {
        return execute(String.class);
    }

    public <T> HttpResult<T> execute(Class<T> cla) {
        return execute(cla, null, 0);
    }

    public HttpResult<String> execute(HttpRequestListener<String> listener, int tag) {
        return execute(String.class, listener, tag);
    }

    public <T> HttpResult<T> execute(Class<T> cla, HttpRequestListener<T> listener, int tag) {
        return executeTask(cla, listener, tag);
    }
//
//    public HttpResult<String> execute(HttpDataListener<String> listener, int tag) {
//        return execute(String.class, listener, tag);
//    }
//
//    public <T> HttpResult<T> execute(Class<T> cla, HttpDataListener<String> listener, int tag) {
//        return executeTask(cla, listener, tag);
//    }

    private <T> HttpResult<T> executeTask(Class<T> cla, Object listener, int tag) {
        HttpResult<T> result = new HttpResult<T>();
        int times = 0;
        while (true) {
            if (isCancel()) {
                result.setCencl();
                break;
            }
            try {
                T t = null;
                String str = this.run(cla, listener, tag);
                if (!TextUtils.isEmpty(str)) {
                    if (cla == String.class) {
                        t = (T) str;
                    } else {
                        t = strToT(str, cla);
                    }
                }
                if (t != null) {
                    result.setData(t);
                } else {
                    throw new RuntimeException("Request Failed");
                }
            } catch (Exception e) {
                result.setError(e);
            }

            times++;
            if (result.isSucceed()) {
                break;
            } else if (isCancel()) {
                result.setCencl();
                break;
            } else if (repeat && (repeatCount < 0 || repeatCount > times)) {
                try {
                    Thread.sleep(repeatTime);
                } catch (Exception e) {
                }
                continue;
            }
            break;
        }
        result.setTag(tag);
        return result;
    }

    public abstract <T> String run(Class<T> cla, Object listener, int tag) throws Exception;

    protected abstract <T> T strToT(String str, Class<T> cla);

    protected void checkThreadState() throws Exception {
        if (isCancel()) {
            Thread.sleep(waitTime);
            Thread.currentThread().interrupt();
        }
        while (isPause()) {
            Thread.sleep(waitTime);
        }
    }

    protected <T> void notice(final Object object, int tag, String str, Class<T> cla) {
        if (object != null) {
            final HttpResult<T> httpResult = new HttpResult<>();
            T t = null;
            if (!TextUtils.isEmpty(str)) {
                if (cla == String.class) {
                    t = (T) str;
                } else {
                    t = strToT(str, cla);
                }
            }
            httpResult.setData(t);
            httpResult.setTag(tag);
            if (object instanceof HttpRequestListener) {
                if (object instanceof HttpDataListener) {
                    ((HttpDataListener<T>) object).onGetData(httpResult);
                }
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        ((HttpRequestListener<T>) object).onFinish(httpResult);
                    }
                });
//            } else if(object instanceof  ){

            }
        }
    }

    public class Builder {

    }
}