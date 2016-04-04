package appframe.module.http;

import java.io.File;
import java.net.Proxy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import appframe.module.http.task.HttpDownLoadTask;
import appframe.module.http.task.HttpTask;
import appframe.module.http.utils.HttpResult;
import appframe.utils.io.FileUtil;
import bolts.Continuation;
import bolts.Task;


/**
 * Created by ybao on 16/1/25.
 */
public class MultiDownManager {
    int index = 0;
    int size = 0;
    List<String> mUrls;
    List<String> errors;
    List<String> secs;
    String mDirPath;
    private HashMap<String, String> header;
    private Proxy proxy;

    MultiDownListener multiDownListener;
    TaskController taskController;

    boolean start = false;

    public void setProxy(Proxy proxy) {
        this.proxy = proxy;
    }

    public void setHeader(HashMap<String, String> header) {
        this.header = header;
    }

    Executor executor;


    public MultiDownManager(int threadCount) {
        this();
        if (threadCount != 0) {
            executor = Executors.newFixedThreadPool(threadCount);
        } else {
            executor = Executors.newCachedThreadPool();
        }
    }

    public MultiDownManager() {
        errors = new ArrayList<String>();
        secs = new ArrayList<String>();
    }

    public Task start(List<String> urls, String dirPath) {
        mUrls = urls;
        mDirPath = dirPath;
        size = urls.size();
        errors.clear();
        secs.clear();
        index = 0;
        start = true;
        taskController = new TaskController();
        return Task.call(new Callable<String>() {
            @Override
            public String call() throws Exception {
                FileUtil.createFolder(mDirPath);
                return mDirPath;
            }
        }, executor).onSuccessTask(new Continuation<String, Task<Void>>() {
            @Override
            public Task<Void> then(Task<String> task) throws Exception {
                List<Task<HttpResult<File>>> tasks = new ArrayList<Task<HttpResult<File>>>();
                for (int i = 0; i < size; i++) {
                    tasks.add(getDownLoadTask(i));
                }
                return Task.whenAll(tasks);
            }
        }, executor).continueWith(new Continuation<Void, Void>() {
            @Override
            public Void then(Task<Void> task) throws Exception {
                start = false;
                if (multiDownListener != null) {
                    multiDownListener.onFinish();
                }
                return null;
            }
        });
    }


    public Task<HttpResult<File>> getDownLoadTask(int index) {
        String url = mUrls.get(index);
        HttpDownLoadTask task = new HttpDownLoadTask();
        task.setUrl(url);
        task.setDirPath(mDirPath);
        task.setHttpRequestListener(null, index);
        task.setHeader(header);
        task.setProxy(proxy);
        task.setReplace(true);
        task.setRepeat(true);
        task.setRepeatCount(3);
        task.setTaskController(taskController);
        return pushInThreadPool(task);
    }

    public Task pushInThreadPool(HttpTask httpTask) {
        Task<HttpResult<File>> task = Task.call(httpTask, executor).continueWith(HttpResultContinuation, Task.UI_THREAD_EXECUTOR);
        return task;
    }

    Continuation<HttpResult<File>, Void> HttpResultContinuation = new Continuation<HttpResult<File>, Void>() {
        @Override
        public Void then(Task<HttpResult<File>> task) throws Exception {
            HttpResult<File> ht = task.getResult();
            if (ht.isSucceed()) {
                int tag = ht.getTag();
                if (tag >= 0 && tag < size) {
                    String url = mUrls.get(tag);
                    if (ht.isSucceed()) {
                        secs.add(url);
                    } else {
                        errors.add(url);
                    }
                }
            }
            index++;
            if (multiDownListener != null) {
                multiDownListener.onProgress((float) index / (float) size * 100.0f);
            }
            return null;
        }
    };

    public void stop() {
        start = false;
        if (taskController != null) {
            taskController.setCencl();
        }
    }

    public boolean isStart() {
        return start;
    }

    public void pause() {
        if (taskController != null) {
            taskController.setPause(true);
        }
    }

    public void recovery() {
        if (taskController != null) {
            taskController.setPause(false);
        }
    }


    public void setMultiDownListener(MultiDownListener multiDownListener) {
        this.multiDownListener = multiDownListener;
    }

    public interface MultiDownListener {
        void onProgress(float percent);

        void onFinish();
    }

    public List<String> getErrors() {
        return errors;
    }

    public List<String> getSecs() {
        return secs;
    }
}
