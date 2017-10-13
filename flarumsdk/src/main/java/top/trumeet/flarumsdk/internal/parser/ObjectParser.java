package top.trumeet.flarumsdk.internal.parser;

import top.trumeet.flarumsdk.Flarum;
import top.trumeet.flarumsdk.data.Data;
import top.trumeet.flarumsdk.data.JSONApiObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Trumeet on 2017/9/26.
 */

public class ObjectParser {
    /**
     * Convert {@link JSONApiObject} to T, like RxJava map()
     */
    public interface JsonObjectConverter<T> {
        T convert (JSONApiObject object, String responseString, Flarum apiManger);
    }

    @SuppressWarnings("unchecked")
    public static  <T> T getFirst (List<Data> data) {
        if (data == null || data.size() <= 0)
            return null;
        Object object = data.get(0);
        return (T)object;
    }

    @SuppressWarnings("unchecked")
    public static <T> List<T> getAll (List<Data> data) {
        if (data == null || data.size() <= 0)
            return null;
        List<T> list = new ArrayList<>(data.size());
        for (Object o
                 : data) {
            list.add((T) o);
        }
        return list;
    }
}
