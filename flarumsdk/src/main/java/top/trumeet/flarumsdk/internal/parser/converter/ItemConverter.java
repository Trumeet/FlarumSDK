package top.trumeet.flarumsdk.internal.parser.converter;

import top.trumeet.flarumsdk.internal.parser.ObjectParser;
import top.trumeet.flarumsdk.internal.parser.jsonapi.Models.JSONApiObject;

/**
 * Created by Trumeet on 2017/9/26.
 */

public class ItemConverter<T> implements ObjectParser.JsonObjectConverter<T> {
    @Override
    public T convert (JSONApiObject object, String responseString) {
        return ObjectParser.getFirst(object.getData());
    }
}
