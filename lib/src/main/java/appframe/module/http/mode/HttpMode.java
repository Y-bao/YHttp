/**
 * @Title: HttpMode.java
 * @Package appframe.module.net.appframe.module.http.mode
 * @Description: TODO
 * @author Ybao
 * @date 2015年6月29日 下午5:56:28
 * @version V1.0
 */
package appframe.module.http.mode;

import android.util.Log;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.SocketAddress;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import appframe.utils.io.StreamUtil;


/**
 * @author Ybao
 * @ClassName: HttpMode
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @date 2015年6月29日 下午5:56:28
 */
public abstract class HttpMode {
    public static final String tag = "HttpMode";
    public static final int DEF_CONNECT_TIMEOUT = 10000;
    public static final int DEF_READ_TIMEOUT = 20000;

    protected String url;
    protected Proxy proxy;
    protected HashMap<String, String> header;
    protected int connectTimeout;
    protected int readTimeout;

    HttpURLConnection httpURLConnection;

    public HttpMode() {
        connectTimeout = DEF_CONNECT_TIMEOUT;
        readTimeout = DEF_READ_TIMEOUT;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setHeader(HashMap<String, String> header) {
        this.header = header;
    }

    public void setProxy(Proxy proxy) {
        this.proxy = proxy;
    }

    public void setProxy(String proxyServer, int proxyPort) throws Exception {
        SocketAddress addr = new InetSocketAddress(proxyServer, proxyPort);
        proxy = new Proxy(Proxy.Type.HTTP, addr);
    }

    public void setConnectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    public void setReadTimeout(int readTimeout) {
        this.readTimeout = readTimeout;
    }

    public String getUrl() {
        return url;
    }

    public Proxy getProxy() {
        return proxy;
    }

    public HashMap<String, String> getHeader() {
        return header;
    }


    public int getConnectTimeout() {
        return connectTimeout;
    }

    public int getReadTimeout() {
        return readTimeout;
    }

    protected void initHeaders(HttpURLConnection httpConnection) {
        if (header != null) {
            for (String key : header.keySet()) {
                httpConnection.setRequestProperty(key, header.get(key));
            }
        }
    }


    public abstract void initHttpURLConnection(HttpURLConnection httpConnection) throws Exception;

    public HttpMode init() throws Exception {
        Log.i("url", getUrl());
        if (proxy == null) {
            httpURLConnection = (HttpURLConnection) new URL(url).openConnection();
        } else {
            httpURLConnection = (HttpURLConnection) new URL(url).openConnection(proxy);
        }
        httpURLConnection.setConnectTimeout(connectTimeout);
        httpURLConnection.setReadTimeout(readTimeout);
        initHeaders(httpURLConnection);
        initHttpURLConnection(httpURLConnection);
        httpURLConnection.setInstanceFollowRedirects(true);
        return this;
    }

    public boolean request() throws Exception {
        init();
        outputHeaders(httpURLConnection);
        int responseCode = httpURLConnection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_MOVED_TEMP || responseCode == HttpURLConnection.HTTP_MOVED_PERM) {
            url = httpURLConnection.getHeaderField("location");
            return request();

        }
        if (responseCode >= 200 && responseCode < 300) {
            return true;
        }
        return false;
    }

    public HttpURLConnection getHttpURLConnection() {
        return httpURLConnection;
    }


    public void outputHeaders(HttpURLConnection conn) {
        Map<String, List<String>> headers = conn.getHeaderFields();
        Iterator<String> iterator = headers.keySet().iterator();
        while (iterator.hasNext()) {
            String key = iterator.next();
            String value = "";
            for (String v : headers.get(key)) {
                value = ";" + v;
            }
            Log.d(tag, key + "=" + value);
        }
    }

    public InputStream resultStream() {
        try {
            if (request()) {
                return httpURLConnection.getInputStream();
            }
        } catch (Exception e) {
            Log.d(tag, e.getMessage());
        }
        return null;
    }

    public byte[] resultBytes() {
        try {
            InputStream inputStream = resultStream();
            if (inputStream == null) {
                return null;
            }
            byte[] bytes = StreamUtil.InputStreamTOByte(inputStream);
            httpURLConnection.disconnect();
            return bytes;
        } catch (Exception e) {
            Log.d(tag, e.getMessage());
        }
        return null;
    }

    public String resultStr() {
        try {
            InputStream inputStream = resultStream();
            if (inputStream == null) {
                return null;
            }
            String str = StreamUtil.InputStreamTOString(inputStream);
            httpURLConnection.disconnect();
            return str;
        } catch (Exception e) {
            Log.d(tag, e.getMessage());
        }
        return null;
    }

    public long getContentLength() {
        return httpURLConnection.getContentLength();
    }

    public boolean usingProxy() {
        return httpURLConnection.usingProxy();
    }

    public void setUserAgent(String userAgent) {
        httpURLConnection.setRequestProperty("User-Agent", userAgent);
    }

    public void setFollowRedirects(boolean followRedirects) {
        HttpURLConnection.setFollowRedirects(followRedirects);
    }
}
