/**
 * @Title: HttpGet.java
 * @Package appframe.module.net.appframe.module.http
 * @Description: TODO
 * @author Ybao
 * @date 2015年6月29日 下午5:55:31
 * @version V1.0
 */
package appframe.module.http.mode;

import java.net.HttpURLConnection;

/**
 * @author Ybao
 * @ClassName: HttpGet
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @date 2015年6月29日 下午5:55:31
 */
public class HttpGet extends HttpMode {
    private final static String tag = "HttpGet";

    @Override
    public void initHttpURLConnection(HttpURLConnection httpConnection) throws Exception {
        httpConnection.setRequestMethod("GET");
    }
}
