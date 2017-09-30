package top.trumeet.flarumsdk.internal.parser.converter;

import java.util.List;

import top.trumeet.flarumsdk.data.Notification;
import top.trumeet.flarumsdk.internal.parser.ObjectParser;
import top.trumeet.flarumsdk.internal.parser.jsonapi.Models.JSONApiObject;

/**
 * Created by Trumeet on 2017/9/30.
 */

public class NotificationConverter implements ObjectParser.JsonObjectConverter<List<Notification>> {

    @Override
    public List<Notification> convert(JSONApiObject object, String responseString) {
        return ObjectParser.getAll(object.getData());
    }
}
