/**
 * @Title: HttpRequester.java
 * @Package appframe.module.net.appframe.module.http
 * @Description: TODO
 * @author Luck
 * @date 2015年6月23日 下午3:14:28
 * @version V1.0
 */
package appframe.module.http;

import android.util.Log;

import java.io.File;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.SocketAddress;
import java.util.HashMap;

import appframe.module.http.cache.diskcache.FileCacheManager;
import appframe.module.http.file.FileManager;
import appframe.module.http.listener.HttpDownLoadListener;
import appframe.module.http.listener.HttpRequestListener;
import appframe.module.http.task.HttpCachePriorityTask;
import appframe.module.http.task.HttpCanCacheTask;
import appframe.module.http.task.HttpDownLoadTask;
import appframe.module.http.task.HttpJustCacheTask;
import appframe.module.http.task.HttpTask;
import appframe.module.http.utils.HttpResult;
import bolts.Task;


/**
 * @author Luck
 * @ClassName: HttpRequester
 * @Description: TODO 网络请求工具
 * @date 2015年6月23日 下午3:14:28
 */
public class HttpRequester {

    private HashMap<String, String> header;
    private FileManager<byte[]> fileManager;
    private Proxy proxy;

    public HttpRequester() {
    }

    public void setHeader(HashMap<String, String> header) {
        this.header = header;
    }

    public void setFileManager(FileCacheManager<byte[]> fileManager) {
        this.fileManager = fileManager;
    }

    public void setProxy(Proxy proxy) {
        this.proxy = proxy;
    }

    public void setProxy(String proxyServer, int proxyPort) throws Exception {
        SocketAddress addr = new InetSocketAddress(proxyServer, proxyPort);
        proxy = new Proxy(Proxy.Type.HTTP, addr);
    }


    public Task<HttpResult<String>> get(String url, boolean cache, HttpRequestListener httpRequestListener, int tag, TaskController taskController) {
        Log.i("url", "GET:" + url);
        HttpCanCacheTask task = new HttpCanCacheTask();
        task.setUrl(url);
        task.setHttpRequestListener(httpRequestListener, tag);
        if (cache) {
            task.setFileManager(fileManager);
        }
        task.setHeader(header);
        task.setProxy(proxy);
        task.setTaskController(taskController);
        return pushInThreadPool(task);
    }


    public Task<HttpResult<String>> getCacheBefNet(String url, HttpRequestListener httpRequestListener, int tag, TaskController taskController) {
        Log.i("url", "GET_CACHE_BEF_NET:" + url);
        HttpCachePriorityTask task = new HttpCachePriorityTask();
        task.setUrl(url);
        task.setHttpRequestListener(httpRequestListener, tag);
        task.setFileManager(fileManager);
        task.setHeader(header);
        task.setProxy(proxy);
        task.setTaskController(taskController);
        return pushInThreadPool(task);
    }

    public Task<HttpResult<String>> getJustCache(String url, HttpRequestListener httpRequestListener, int tag, TaskController taskController) {
        Log.i("url", "GET_JUST_CACHE:" + url);
        HttpJustCacheTask task = new HttpJustCacheTask();
        task.setUrl(url);
        task.setHttpRequestListener(httpRequestListener, tag);
        task.setFileManager(fileManager);
        task.setHeader(header);
        task.setProxy(proxy);
        task.setTaskController(taskController);
        return pushInThreadPool(task);
    }

    public Task<HttpResult<String>> post(String url, String data, HttpRequestListener httpRequestListener, int tag, TaskController taskController) {
        Log.i("url", "post:" + url);
        HttpCanCacheTask task = new HttpCanCacheTask();
        task.setUrl(url);
        task.setData(data);
        task.setHttpRequestListener(httpRequestListener, tag);
        task.setHeader(header);
        task.setProxy(proxy);
        task.setTaskController(taskController);
        return pushInThreadPool(task);
    }


    public Task<HttpResult<File>> downLoad(String url, String path, boolean replace, boolean repeat, int repeatCount, HttpRequestListener httpRequestListener, int tag, TaskController taskController) {
        HttpDownLoadTask task = new HttpDownLoadTask();
        task.setUrl(url);
        task.setDirPath(path);
        task.setHttpRequestListener(httpRequestListener, tag);
        task.setHeader(header);
        task.setProxy(proxy);
        task.setReplace(replace);
        task.setRepeat(repeat);
        task.setRepeatCount(repeatCount);
        task.setTaskController(taskController);
        return pushInThreadPool(task);
    }


    public Task<HttpResult<String>> get(String url, boolean cache, HttpRequestListener httpRequestListener, int tag) {
        return get(url, cache, httpRequestListener, tag, null);
    }

    public Task<HttpResult<String>> getCacheBefNet(String url, HttpRequestListener httpRequestListener, int tag) {
        return getCacheBefNet(url, httpRequestListener, tag, null);
    }

    public Task<HttpResult<String>> getJustCache(String url, HttpRequestListener httpRequestListener, int tag) {
        return getJustCache(url, httpRequestListener, tag, null);
    }

    public Task<HttpResult<String>> post(String url, String data, HttpRequestListener httpRequestListener, int tag) {
        return post(url, data, httpRequestListener, tag, null);
    }

    public Task<HttpResult<File>> downLoad(String url, String path, boolean replace, boolean repeat, int repeatCount, HttpDownLoadListener httpDownLoadListener, int tag) {
        return downLoad(url, path, replace, repeat, repeatCount, httpDownLoadListener, tag, null);
    }


    public Task<HttpResult<String>> get(String url, boolean cache) {
        return get(url, cache, null, 0, null);
    }

    public Task<HttpResult<String>> getCacheBefNet(String url) {
        return getCacheBefNet(url, null, 0, null);
    }

    public Task<HttpResult<String>> getJustCache(String url) {
        return getJustCache(url, null, 0, null);
    }

    public Task<HttpResult<String>> post(String url, String data) {
        return post(url, data, null, 0, null);
    }

    public Task<HttpResult<File>> downLoad(String url, String path, boolean replace, boolean repeat, int repeatCount) {
        return downLoad(url, path, replace, repeat, repeatCount, null, 0, null);
    }

    public <T> Task<HttpResult<T>> pushInThreadPool(HttpTask<T> httpTask) {
        Task<HttpResult<T>> task = Task.callInBackground(httpTask);
        return task;
    }

}
