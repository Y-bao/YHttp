package appframe.module.http.task;

import java.util.Arrays;

import appframe.module.http.mode.HttpMode;


public class HttpCachePriorityTask extends BaseHttpCacheTask {


    public HttpCachePriorityTask() {
        super();
    }

    public HttpCachePriorityTask(HttpMode httpMode) {
        super(httpMode);
    }

    @Override
    public <T> String run(Class<T> cla, Object listener, int tag) throws Exception {
        String stringData = null;
        byte[] cacheData;
        String url = httpMode.getUrl();
        cacheData = getCache(url);
        if (cacheData != null) {
            stringData = new String(cacheData, getCharset());
            notice(listener,tag, stringData, cla);
        }
        byte[] byteData = httpMode.resultBytes();
        if (byteData != null) {
            if (cacheData == null || !Arrays.equals(byteData, cacheData)) {
                saveCache(url, byteData);
                stringData = new String(byteData, getCharset());
                notice(listener,tag, stringData, cla);
            }
        }
        return stringData;
    }
}