package appframe.module.http.task;


import appframe.module.http.mode.HttpMode;

public class HttpJustCacheTask extends BaseHttpCacheTask {

    public HttpJustCacheTask() {
        super();
    }

    public HttpJustCacheTask(HttpMode httpMode) {
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
        } else {
            byte[] byteData = httpMode.resultBytes();
            if (byteData != null) {
                saveCache(url, byteData);
                stringData = new String(byteData, getCharset());
                notice(listener,tag, stringData, cla);
            }
        }
        return stringData;
    }

}