package top.trumeet.flarumsdk.internal.parser.converter;

import top.trumeet.flarumsdk.internal.parser.ObjectParser;
import top.trumeet.flarumsdk.internal.parser.jsonapi.Models.JSONApiObject;

import java.util.List;

/**
 * Created by Trumeet on 2017/9/30.
 */

public class ListConverter<T> implements ObjectParser.JsonObjectConverter<List<T>> {

    @Override
    public List<T> convert(JSONApiObject object, String responseString) {
        return ObjectParser.getAll(object.getData());
    }
}
