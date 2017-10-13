package top.trumeet.flarumsdk.internal.parser;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import top.trumeet.flarumsdk.data.Content;

import java.lang.reflect.Type;

/**
 * Created by Trumeet on 2017/10/10.
 */
public class ContentParser implements JsonDeserializer<Content> {
    @Override
    public Content deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        // We'll not do any thing there.
        return null;
    }
}
