package appframe.module.http.listener;

public interface HttpDataListener extends HttpRequestListener {
    void onGetData(String data, int tag);
}
