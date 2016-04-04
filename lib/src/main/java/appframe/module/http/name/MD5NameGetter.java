package appframe.module.http.name;

import appframe.utils.serialize.MD5;

/**
 * Created by Y-bao on 2016/1/21.
 */
public class MD5NameGetter implements NameGetter {
    @Override
    public String getName(String str) {
        return MD5.encode(str.getBytes());
    }
}
