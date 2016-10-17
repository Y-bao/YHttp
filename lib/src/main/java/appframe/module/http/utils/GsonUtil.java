package appframe.module.http.utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

public class GsonUtil {
    public static <T> T getObject(String Json, Class<T> type) {
        T t = new Gson().fromJson(Json, type);
        return t;
    }

    public static <T> T getObject(String Json, Type type) {
        T t = new Gson().fromJson(Json, type);
        return t;
    }

    public static Map<String, String> getMap(String Json) {
        Map<String, String> map = new Gson().fromJson(Json, new TypeToken<Map<String, String>>() {
        }.getType());
        return map;
    }

    public static <T> List<T> getObjectList(String Json) {
        List<T> list = new Gson().fromJson(Json, new TypeToken<List<T>>() {
        }.getType());
        return list;
    }

    public static List<String> getStringList(String Json) {
        List<String> retStr = new Gson().fromJson(Json, new TypeToken<List<String>>() {
        }.getType());
        return retStr;
    }

    public static List<Map<String, Object>> getMapList(String Json) {
        List<Map<String, Object>> retStr = new Gson().fromJson(Json,
                new TypeToken<List<Map<String, Object>>>() {
                }.getType());
        return retStr;
    }

    public static List<List<String>> getListList(String Json) {
        List<List<String>> retStr = new Gson().fromJson(Json, new TypeToken<List<List<String>>>() {
        }.getType());
        return retStr;
    }

    public static String toJson(Object mObject) {
        return new Gson().toJson(mObject);
    }

}
