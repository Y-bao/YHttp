package appframe.module.http.task;

import java.util.Arrays;


public class HttpCachePriorityTask extends HttpCacheTask<String> {


    @Override
    public String run() throws Exception {
        String stringData = null;
        byte[] cacheData;
        String url = httpMode.getUrl();
        cacheData = getCache(url);
        if (cacheData != null) {
            stringData = new String(cacheData, getCharset());
            noticeSucceed(stringData);
        }
        byte[] byteData = httpMode.resultBytes();
        if (byteData != null) {
            if (cacheData == null || !Arrays.equals(byteData, cacheData)) {
                saveCache(url, byteData);
                stringData = new String(byteData, getCharset());
                noticeSucceed(stringData);
            }
        }
        return stringData;
    }
}