package top.trumeet.flarumsdk.internal.parser.converter;

import top.trumeet.flarumsdk.Flarum;
import top.trumeet.flarumsdk.data.JSONApiObject;
import top.trumeet.flarumsdk.internal.parser.ObjectParser;

import java.util.List;

/**
 * Created by Trumeet on 2017/9/30.
 */

public class ListConverter<T> implements ObjectParser.JsonObjectConverter<List<T>> {

    @Override
    public List<T> convert(JSONApiObject object, String responseString, Flarum apiManager) {
        return ObjectParser.getAll(object.getData());
    }
}
