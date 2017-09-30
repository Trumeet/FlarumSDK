package top.trumeet.flarumsdk.internal.parser.converter;

import top.trumeet.flarumsdk.data.Forum;
import top.trumeet.flarumsdk.internal.parser.ObjectParser;
import top.trumeet.flarumsdk.internal.parser.jsonapi.Models.JSONApiObject;

/**
 * Created by Trumeet on 2017/9/26.
 */

public class ForumConverter implements ObjectParser.JsonObjectConverter<Forum> {
    @Override
    public Forum convert (JSONApiObject object, String responseString) {
        return ObjectParser.getFirst(object.getData());
    }
}
