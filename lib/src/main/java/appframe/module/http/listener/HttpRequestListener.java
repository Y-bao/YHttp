package appframe.module.http.listener;

import appframe.module.http.utils.HttpResult;

public interface HttpRequestListener<T> {
    void onFinish(HttpResult<T> httpResult);
}