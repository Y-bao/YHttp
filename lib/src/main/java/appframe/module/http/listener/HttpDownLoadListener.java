package appframe.module.http.listener;


import appframe.module.http.utils.SizeInfos;

/**
 * Created by Y-bao on 2016/1/22.
 */
public interface HttpDownLoadListener extends HttpRequestListener {
    void onProgress(SizeInfos sizeInfos, int tag);
}
