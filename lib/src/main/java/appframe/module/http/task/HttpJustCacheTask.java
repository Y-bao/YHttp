package appframe.module.http.task;


public class HttpJustCacheTask extends HttpCacheTask<String> {

    @Override
    public String run() throws Exception {
        String stringData = null;
        byte[] cacheData;
        String url = httpMode.getUrl();
        cacheData = getCache(url);
        if (cacheData != null) {
            stringData = new String(cacheData, getCharset());
            noticeSucceed(stringData);
        } else {
            byte[] byteData = httpMode.resultBytes();
            if (byteData != null) {
                saveCache(url, byteData);
                stringData = new String(byteData, getCharset());
                noticeSucceed(stringData);
            }
        }
        return stringData;
    }

}