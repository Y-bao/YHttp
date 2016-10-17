package appframe.module.http.task;

import appframe.module.http.mode.HttpMode;
import appframe.module.http.utils.GsonUtil;

public abstract class BaseJsonHttpTask extends BaseHttpTask {

    public BaseJsonHttpTask() {
        super();
    }

    public BaseJsonHttpTask(HttpMode httpMode) {
        super(httpMode);
    }

    @Override
    protected <T> T strToT(String str, Class<T> cla) {
        return GsonUtil.getObject(str, cla);
    }
}