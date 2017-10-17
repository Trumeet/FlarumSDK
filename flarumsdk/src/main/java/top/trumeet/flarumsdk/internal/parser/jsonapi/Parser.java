package top.trumeet.flarumsdk.internal.parser.jsonapi;

import com.google.gson.*;
import top.trumeet.flarumsdk.data.*;
import top.trumeet.flarumsdk.data.Error;

import javax.annotation.Nullable;
import java.lang.reflect.Type;
import java.util.*;

/**
 * Created by Trumeet on 2017/10/10.
 * Gson JSON API parser, written by Trumeet.
 * ALPHA VERSION
 * Following http://jsonapi.org.cn/format/
 */
public class Parser implements JsonDeserializer<JSONApiObject> {
    private Map<String /* type */, Class> mapping;

    public Parser(Map<String, Class> mapping) {
        this.mapping = mapping;
    }

    @Override
    public JSONApiObject deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JSONApiObject object = new JSONApiObject();
        JsonObject rootObject = jsonElement.getAsJsonObject();
        /*
        if (!rootObject.has("data")) {
        // Always true ???
            return object;
        }
        */
        JsonElement data = rootObject.get("data");
        if (data == null) {
            // Try errors?
            JsonElement errors = rootObject.get("errors");
            if (errors != null) {
                object.setErrors(parseErrors(errors, jsonDeserializationContext));
            }
            return object;
        }

        JsonElement links = rootObject.get("links");
        if (links != null) {
            object.setLinks(parseLinks(links, jsonDeserializationContext));
        }

        // Parse includes
        JsonElement included = rootObject.get("included");
        if (included != null && included.isJsonArray()) {
            object.setIncluded(parseData(included.getAsJsonArray()
                    , jsonDeserializationContext));
        }

        object.setData(parseData(data, jsonDeserializationContext));

        return object;
    }

    /**
     * Parse "links" json object to {@link Link} object.
     *
     * "links": {
     * "self": "/articles/1/relationships/author",
     * "related": "/articles/1/author"
     * }
     *
     * @param linksObject Json object, MUST is object
     * @return Link list, null for no links or not match
     */
    @Nullable
    private Links parseLinks (JsonElement linksObject, JsonDeserializationContext context) {
        return (Links) context.deserialize(linksObject, Links.class);
    }

    /**
     * Parse errors to {@link Error} list
     *
     * Support error list:
     * {
     *     "errors": [
     *          {
     *              "status": "233",
     *              "code": "bad"
     *          }
     *      ]
     * }
     *
     * @param element "errors" top-level json element:
     *     "errors": [
     *          {
     *              "status": "233",
     *              "code": "bad"
     *          }
     *      ]
     * @return Error models
     */
    @Nullable
    private List<top.trumeet.flarumsdk.data.Error> parseErrors (JsonElement element, JsonDeserializationContext context) {
        if (element.isJsonArray()) {
            List<Error> list = new ArrayList<>();
            for (JsonElement jsonElement : element.getAsJsonArray()) {
                if (jsonElement.isJsonObject()) {
                    Error model = parseSingleError(jsonElement.getAsJsonObject(),
                            context);
                    if (model != null) list.add(model);
                }
            }
            return list;
        }
        System.err.println("This root object is not array, just skipping");
        return null;
    }

    private Error parseSingleError (JsonObject object, JsonDeserializationContext context) {
        return context.deserialize(object, Error.class);
    }
    private List<Data> parseData (JsonElement data, JsonDeserializationContext context) {
        List<Data> list = new ArrayList<>(1);
        if (data.isJsonArray()) {
            // Parse "data": [
            // ...]
            for (JsonElement element : data.getAsJsonArray()) {
                if (!element.isJsonObject()) continue;
                Data dataSingle = parseSingleData(element.getAsJsonObject(), context);
                if (dataSingle != null) list.add(dataSingle);
            }
        } else if (data.isJsonObject()) {
            // Parse "data" : {
            // ...}
            list.add(parseSingleData(data.getAsJsonObject(), context));
        } else {
            System.err.println("This data element is not array and object, just skipping");
            return null;
        }
        return list;
    }

    private Data parseSingleData (JsonObject element, JsonDeserializationContext context) {
        //System.out.println(element);
        String typeValue = element.get("type")
                .getAsString();
        String id = element.get("id")
                .getAsString();

        if (!mapping.containsKey(typeValue)) {
            System.err.println("Type " + typeValue + " not mapped, skipping");
            return null;
        }

        Class target = mapping.get(typeValue);
        /*
        if (!target.getSuperclass().getName().equals(Resource.class.getName())) {
            System.err.println("Type class " + target + " is not extending Resource.");
            return null;
        }
        */

        // Init data
        Data data;

        // Parse attributes
        if (element.has("attributes")) {
            if (!target.getSuperclass().getName().equals(Data.class.getName())) {
                System.err.println("Type class " + target + " is not extending Data.");
                return null;
            }
            data = (Data)parseAttributes(context, element, target);
        } else {
            data = new Data();
        }

        // Init data
        data.setId(id);
        data.setType(typeValue);

        // Parse relationships
        if (element.has("relationships")) {
            data.setRelationships(parseRelationships(element, context));
        }

        return data;
    }

    private <T> T parseAttributes (JsonDeserializationContext context,
                                   JsonObject element, Class<T> target) {
        //System.out.println(element.get("attributes"));
        return context.deserialize(element.get("attributes"), target);
    }

    private Map<String, List<Data>> parseRelationships (JsonObject dataElement,
                                                                        JsonDeserializationContext context) {
        JsonObject relationshipsJson = dataElement.getAsJsonObject("relationships");
        Map<String, List<Data>> relationships = new HashMap<>();
        Set<Map.Entry<String, JsonElement>> entries = relationshipsJson.entrySet();
        for (Map.Entry<String, JsonElement> entry : entries) {
            List<Data> list = relationships.get(entry.getKey());
            if (list == null) list = new ArrayList<>();
            JsonElement dataParentObject = relationshipsJson.get(entry.getKey())
                    .getAsJsonObject()
                    .get("data");
            if (dataParentObject.isJsonArray()) {
                for (JsonElement data :
                        dataParentObject.getAsJsonArray()) {
                    JsonObject dataObj = data.getAsJsonObject();
                    Data realData = parseSingleData(dataObj, context);
                    if (realData != null) {
                        list.add(realData);
                    }
                }
            } else if (dataParentObject.isJsonObject()) {
                JsonObject dataObj = dataParentObject.getAsJsonObject();
                //System.out.println(dataObj);
                Data realData = parseSingleData(dataObj, context);
                if (realData != null) {
                    list.add(realData);
                }
            } else {
                System.err.println("This relationship/include is not array or object, just skipping");
                continue;
            }
            relationships.put(entry.getKey(), list);
        }
        return relationships;
    }
}
