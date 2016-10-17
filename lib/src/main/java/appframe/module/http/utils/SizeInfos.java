package appframe.module.http.utils;

/**
 * Created by Y-bao on 2016/1/22.
 */
public class SizeInfos {
    long size;
    long nowSize;
    float percent;

    public SizeInfos(long size, long nowSize, float percent) {
        this.size = size;
        this.nowSize = nowSize;
        this.percent = percent;
    }

    public float getPercent() {
        return percent;
    }

    public long getNowSize() {
        return nowSize;
    }

    public long getSize() {
        return size;
    }
}
