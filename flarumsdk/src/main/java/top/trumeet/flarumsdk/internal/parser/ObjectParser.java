package top.trumeet.flarumsdk.internal.parser;

import java.util.ArrayList;
import java.util.List;

import top.trumeet.flarumsdk.internal.parser.jsonapi.Models.JSONApiObject;
import top.trumeet.flarumsdk.internal.parser.jsonapi.Models.Resource;

/**
 * Created by Trumeet on 2017/9/26.
 */

public class ObjectParser {
    /**
     * Convert {@link JSONApiObject} to T, like RxJava map()
     */
    public interface JsonObjectConverter<T> {
        T convert (JSONApiObject object, String responseString);
    }

    @SuppressWarnings("unchecked")
    public static  <T> T getFirst (List<Resource> data) {
        if (data == null || data.size() <= 0)
            return null;
        Object object = data.get(0);
        return (T)object;
    }

    @SuppressWarnings("unchecked")
    public static <T> List<T> getAll (List<Resource> data) {
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
