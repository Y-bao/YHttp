package appframe.module.http.listener;

import appframe.module.http.utils.HttpResult;

public interface HttpDataListener<T> extends HttpRequestListener<T> {
    void onGetData(HttpResult<T> data);
}
