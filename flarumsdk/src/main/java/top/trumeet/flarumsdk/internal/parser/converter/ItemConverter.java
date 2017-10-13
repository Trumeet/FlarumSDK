package top.trumeet.flarumsdk.internal.parser.converter;

import top.trumeet.flarumsdk.Flarum;
import top.trumeet.flarumsdk.data.JSONApiObject;
import top.trumeet.flarumsdk.internal.parser.ObjectParser;

/**
 * Created by Trumeet on 2017/9/26.
 */

public class ItemConverter<T> implements ObjectParser.JsonObjectConverter<T> {
    @Override
    public T convert (JSONApiObject object, String responseString, Flarum apiManager) {
        return ObjectParser.getFirst(object.getData());
    }
}
