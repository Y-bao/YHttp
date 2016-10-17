/**
 * @Title: HttpRequester.java
 * @Package appframe.module.net.appframe.module.http
 * @Description: TODO
 * @author Luck
 * @date 2015年6月23日 下午3:14:28
 * @version V1.0
 */
package appframe.module.http;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.SocketAddress;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Callable;

import appframe.module.http.cache.diskcache.FileCacheManager;
import appframe.module.http.file.FileManager;
import appframe.module.http.listener.HttpDataListener;
import appframe.module.http.listener.HttpRequestListener;
import appframe.module.http.mode.HttpPost;
import appframe.module.http.task.HttpCachePriorityTask;
import appframe.module.http.task.HttpCacheTask;
import appframe.module.http.task.HttpDownLoadTask;
import appframe.module.http.task.HttpJustCacheTask;
import appframe.module.http.task.HttpNoCacheTask;
import appframe.module.http.task.BaseHttpTask;
import appframe.module.http.utils.HttpResult;
import bolts.Continuation;
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

    /*****************************************************/
    public <T> Task<HttpResult<T>> get(String url, boolean cache, Class<T> cla, TaskController taskController, HttpRequestListener<T> listener, int tag) {
        HttpCacheTask task = new HttpCacheTask();
        task.setUrl(url);
        if (cache) {
            task.setFileManager(fileManager);
        }
        task.setHeader(header);
        task.setProxy(proxy);
        task.setTaskController(taskController);
        return pushInThreadPool(task, cla, listener, tag);
    }


    public <T> Task<HttpResult<T>> getCacheBefNet(String url, Class<T> cla, TaskController taskController, HttpRequestListener<T> listener, int tag) {
//        Log.i("url", "GET_CACHE_BEF_NET:" + url);
        HttpCachePriorityTask task = new HttpCachePriorityTask();
        task.setUrl(url);
        task.setFileManager(fileManager);
        task.setHeader(header);
        task.setProxy(proxy);
        task.setTaskController(taskController);
        return pushInThreadPool(task, cla, listener, tag);
    }

    public <T> Task<HttpResult<T>> getJustCache(String url, Class<T> cla, TaskController taskController, HttpRequestListener<T> listener, int tag) {
        HttpJustCacheTask task = new HttpJustCacheTask();
        task.setUrl(url);
        task.setFileManager(fileManager);
        task.setHeader(header);
        task.setProxy(proxy);
        task.setTaskController(taskController);
        return pushInThreadPool(task, cla, listener, tag);
    }

    public <T> Task<HttpResult<T>> post(String url, String data, Class<T> cla, TaskController taskController, boolean lock, HttpRequestListener<T> listener, int tag) {
        HttpCacheTask task = new HttpCacheTask(new HttpPost());
        task.setUrl(url);
        task.setData(data);
        task.setHeader(header);
        task.setProxy(proxy);
        task.setTaskController(taskController);
        return pushInThreadPool(task, cla, lock, listener, tag);
    }


    public Task<HttpResult<File>> downLoad(String url, String path, boolean replace, boolean repeat, int repeatCount, TaskController taskController) {
        HttpDownLoadTask task = new HttpDownLoadTask();
        task.setUrl(url);
        task.setDirPath(path);
//        task.setHttpRequestListener(httpRequestListener, tag);
        task.setHeader(header);
        task.setProxy(proxy);
        task.setReplace(replace);
        task.setRepeat(repeat);
        task.setRepeatCount(repeatCount);
        task.setTaskController(taskController);
        return pushInThreadPool(task, File.class, null, 0);
    }


    /*****************************************************/
    public <T> Task<HttpResult<T>> get(String url, boolean cache, Class<T> cla, HttpRequestListener<T> listener, int tag) {
        return get(url, cache, cla, null, listener, tag);
    }

    public <T> Task<HttpResult<T>> getCacheBefNet(String url, Class<T> cla, HttpRequestListener<T> listener, int tag) {
        return getCacheBefNet(url, cla, null, listener, tag);
    }

    public <T> Task<HttpResult<T>> getJustCache(String url, Class<T> cla, HttpRequestListener<T> listener, int tag) {
        return getJustCache(url, cla, null, listener, tag);
    }

    public <T> Task<HttpResult<T>> post(String url, String data, Class<T> cla, HttpRequestListener<T> listener, int tag, boolean lock) {
        return post(url, data, cla, null, lock, listener, tag);
    }

    /*****************************************************/

    public Task<HttpResult<String>> get(String url, boolean cache, HttpRequestListener<String> listener, int tag) {
        return get(url, cache, String.class, listener, tag);
    }

    public Task<HttpResult<String>> getCacheBefNet(String url, HttpRequestListener<String> listener, int tag) {
        return getCacheBefNet(url, String.class, listener, tag);
    }

    public Task<HttpResult<String>> getJustCache(String url, HttpRequestListener<String> listener, int tag) {
        return getJustCache(url, String.class, listener, tag);
    }

    public Task<HttpResult<String>> post(String url, String data, HttpRequestListener<String> listener, int tag, boolean lock) {
        return post(url, data, String.class, listener, tag, lock);
    }

//    public Task<HttpResult<File>> downLoad(String url, String path, boolean replace, boolean repeat, int repeatCount) {
//        return downLoad(url, path, replace, repeat, repeatCount,null);
//    }


    /*****************************************************/
    public <T> Task<HttpResult<T>> get(String url, boolean cache, Class<T> cla) {
        return get(url, cache, cla, null, 0);
    }

    public <T> Task<HttpResult<T>> getCacheBefNet(String url, Class<T> cla) {
        return getCacheBefNet(url, cla, null, 0);
    }

    public <T> Task<HttpResult<T>> getJustCache(String url, Class<T> cla) {
        return getJustCache(url, cla, null, 0);
    }

    public <T> Task<HttpResult<T>> post(String url, String data, Class<T> cla, boolean lock) {
        return post(url, data, cla, null, 0, lock);
    }

    /*****************************************************/
    public Task<HttpResult<String>> get(String url, boolean cache) {
        return get(url, cache, String.class);
    }

    public Task<HttpResult<String>> getCacheBefNet(String url) {
        return getCacheBefNet(url, String.class);
    }

    public Task<HttpResult<String>> getJustCache(String url) {
        return getJustCache(url, String.class);
    }

    public Task<HttpResult<String>> post(String url, String data, boolean lock) {
        return post(url, data, String.class, lock);
    }

    public <T> Task<HttpResult<T>> pushInThreadPool(final BaseHttpTask httpTask, final Class<T> cla, final HttpRequestListener<T> listener, final int tag) {
        Task<HttpResult<T>> task = Task.callInBackground(new Callable<HttpResult<T>>() {
            @Override
            public HttpResult<T> call() throws Exception {
                HttpResult<T> httpResult = httpTask.execute(cla, listener, tag);
                return httpResult;
            }
        });
        return task;
    }

    Set<String> hh = new HashSet<>();

    public <T> Task<HttpResult<T>> pushInThreadPool(BaseHttpTask httpTask, final Class<T> cla, boolean lock, HttpRequestListener<T> listener, int tag) {
        Task<HttpResult<T>> task = null;
        if (lock) {
            final String url = httpTask.getHttpMode().getUrl();
            if (!hh.contains(url)) {
                hh.add(url);
                task = pushInThreadPool(httpTask, cla, listener, tag).continueWithTask(new Continuation<HttpResult<T>, Task<HttpResult<T>>>() {
                    @Override
                    public Task<HttpResult<T>> then(Task<HttpResult<T>> task) throws Exception {
                        hh.remove(url);
                        return task;
                    }
                });
            } else {
                task = Task.cancelled();
            }
        } else {
            task = pushInThreadPool(httpTask, cla, listener, tag);
        }
        return task;
    }


    public HttpNoCacheTask createPostNoCache(String url) {
        HttpNoCacheTask task = new HttpNoCacheTask(new HttpPost());
        task.setUrl(url);
        task.setHeader(header);
        task.setProxy(proxy);
        return task;
    }

}
