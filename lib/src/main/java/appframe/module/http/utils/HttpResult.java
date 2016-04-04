package appframe.module.http.utils;

/**
 * Created by ybao on 16/2/1.
 */
public class HttpResult<T> {
    public static final int SUCCEED = 1;
    public static final int FAILED = 2;
    public static final int ERROR = 3;
    public static final int CENCL = 4;
    T data;
    Exception exception;
    String msg;
    public int state = FAILED;
    int tag = 0;

    public void setData(T data) {
        state = SUCCEED;
        this.data = data;
        this.exception = null;
        this.msg = null;
    }

    public void setError(Exception exception) {
        state = ERROR;
        this.exception = exception;
        this.data = null;
        this.msg = null;
    }

    public void setFailed(String msg) {
        state = FAILED;
        this.msg = msg;
        this.data = null;
        this.exception = null;
    }

    public void setCencl() {
        state = CENCL;
        this.data = null;
        this.exception = null;
        this.msg = null;
    }

    public T getData() {
        return data;
    }

    public Exception getException() {
        return exception;
    }

    public String getMsg() {
        return msg;
    }

    public int getState() {
        return state;
    }

    public boolean isSucceed() {
        return state == SUCCEED;
    }


    public boolean isError() {
        return state == ERROR;
    }

    public boolean isFailed() {
        return state == FAILED;
    }

    public void setTag(int tag) {
        this.tag = tag;
    }

    public int getTag() {
        return tag;
    }
}
