package appframe.module.http.task;


public class HttpCanCacheTask extends HttpCacheTask<String> {

    @Override
    public String run() throws Exception {
        String stringData = null;
        String url = httpMode.getUrl();
        byte[] byteData = httpMode.resultBytes();
        if (byteData == null) {
            byteData = getCache(url);
        } else {
            saveCache(url, byteData);
        }
        if (byteData != null) {
            stringData = new String(byteData, getCharset());
            noticeSucceed(stringData);
        }
        return stringData;
    }
}