/**
 * @Title: HttpGet.java
 * @Package appframe.module.net.appframe.module.http
 * @Description: TODO
 * @author Ybao
 * @date 2015年6月29日 下午5:55:31
 * @version V1.0
 */
package appframe.module.http.mode;

import android.text.TextUtils;

import java.io.OutputStream;
import java.net.HttpURLConnection;

/**
 * @author Ybao
 * @ClassName: HttpGet
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @date 2015年6月29日 下午5:55:31
 */
public class HttpPost extends HttpGet {
    String data = null;

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public void initHttpURLConnection(HttpURLConnection httpConnection) throws Exception {
        if (TextUtils.isEmpty(data)) {
            super.initHttpURLConnection(httpConnection);
        } else {
            byte[] byteData = data.getBytes();
            httpConnection.setRequestMethod("POST");
            httpConnection.setDoInput(true);
            httpConnection.setDoOutput(true);
            httpConnection.setRequestProperty("Content_Type", "application/x-www-form-urlencoded");
            httpConnection.setRequestProperty("Content-Length", String.valueOf(byteData.length));
            OutputStream output = httpConnection.getOutputStream();
            output.write(byteData);
        }
    }
}
// conn.setRequestProperty("Accept", "*/*");
// conn.setRequestProperty("Connection", "Keep-Alive");
// conn.setRequestProperty(
// "User-Agent",
// "Mozilla/5.0 (Linux; Android 4.2.2; GT-I9505 Build/JDQ39) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/31.0.1650.59 Mobile Safari/537.36");

