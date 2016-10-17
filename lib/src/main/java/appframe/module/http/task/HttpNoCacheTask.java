package appframe.module.http.task;


import appframe.module.http.mode.HttpMode;

public class HttpNoCacheTask extends BaseJsonHttpTask {
    public HttpNoCacheTask() {
        super();
    }

    public HttpNoCacheTask(HttpMode httpMode) {
        super(httpMode);
    }

    @Override
    public <T> String run(Class<T> cla, Object listener, int tag) throws Exception {
        String stringData = null;
        byte[] byteData = httpMode.resultBytes();
        if (byteData != null) {
            stringData = new String(byteData, getCharset());
            notice(listener,tag, stringData, cla);
        }
        return stringData;
    }
}