package appframe.module.http.listener;

public interface HttpRequestListener {
    void onStart(int tag);

    void onFinish(int result, String data, int tag);
}