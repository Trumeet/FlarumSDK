package top.trumeet.flarumsdk.internal.parser.jsonapi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import top.trumeet.flarumsdk.internal.parser.jsonapi.Annotations.Excluded;
import top.trumeet.flarumsdk.internal.parser.jsonapi.Annotations.Id;
import top.trumeet.flarumsdk.internal.parser.jsonapi.Annotations.SerialName;
import top.trumeet.flarumsdk.internal.parser.jsonapi.Annotations.Type;
import top.trumeet.flarumsdk.internal.parser.jsonapi.Annotations.Types;
import top.trumeet.flarumsdk.internal.parser.jsonapi.Models.ErrorModel;
import top.trumeet.flarumsdk.internal.parser.jsonapi.Models.ErrorSource;
import top.trumeet.flarumsdk.internal.parser.jsonapi.Models.JSONApiObject;
import top.trumeet.flarumsdk.internal.parser.jsonapi.Models.JSONList;
import top.trumeet.flarumsdk.internal.parser.jsonapi.Models.Links;
import top.trumeet.flarumsdk.internal.parser.jsonapi.Models.Resource;

public class JSONApiConverter {

    private static final String DATE_FORMAT_FROM_SERVER = "yyyy-MM-dd'T'HH:mm:ssZ";
    private static final String DATE_FORMAT_TO_SERVER = "yyyy-MM-dd HH:mm:ssZ";

    private SimpleDateFormat dateFormatFromServer;
    private SimpleDateFormat dateFormatToServer;

    private HashMap<String, Class<? extends Resource>> classesIndex;

    public JSONApiConverter(Class<? extends Resource>... classes) {
        dateFormatFromServer = new SimpleDateFormat(DATE_FORMAT_FROM_SERVER, Locale.US);
        dateFormatToServer = new SimpleDateFormat(DATE_FORMAT_TO_SERVER, Locale.US);

        classesIndex = new HashMap<>();
        List<Class<? extends Resource>> classList = Arrays.asList(classes);

        for (Class<? extends Resource> c : classList) {
            if (c.getAnnotation(Type.class) != null) {
                String type = c.getAnnotation(Type.class).value();
                if (!classesIndex.containsKey(type))
                    classesIndex.put(type, c);
            } else if (c.getAnnotation(Types.class) != null) {
                String[] types = c.getAnnotation(Types.class).value();
                for (String type : types) {
                    if (!classesIndex.containsKey(type))
                        classesIndex.put(type, c);
                }
            } else {
                e("Classes", "No Annotation [" + c.getName() + "]");
            }
        }
    }

    public JSONApiConverter(HashMap<String, Class<? extends Resource>> classesIndex) {
        dateFormatFromServer = new SimpleDateFormat(DATE_FORMAT_FROM_SERVER, Locale.US);
        dateFormatToServer = new SimpleDateFormat(DATE_FORMAT_TO_SERVER, Locale.US);

        this.classesIndex = classesIndex;
    }

    public JSONApiConverter withDateFormat(SimpleDateFormat dateFormat) {
        this.dateFormatFromServer = dateFormat;
        this.dateFormatToServer = dateFormat;
        return this;
    }

    public Date parseDate(String date) throws ParseException {
        return dateFormatFromServer.parse(date);
    }

    public String toJson(Resource resource) {
        JSONObject json = toJsonObject(resource);
        if (json != null)
            return json.toString();
        return null;
    }

    public String toJson(List<? extends Resource> resources) {
        JSONObject json = toJsonArray(resources);
        if (json != null)
            return json.toString();
        return null;
    }

    public JSONApiObject fromJson(String jsonObject) {
        JSONApiObject jsonApiObject = new JSONApiObject();
        try {
            JSONObject json = new JSONObject(jsonObject);
            HashMap<String, Resource> includes = new HashMap<>();

            if (json.isNull("errors")) {
                if (!json.isNull("included")) {
                    JSONArray included = json.getJSONArray("included");
                    for (int i = 0; i < included.length(); i++) {
                        JSONObject each = included.getJSONObject(i);
                        String key = getResourceTag(each);

                        Resource resource = resourceFromJson(each, includes);
                        if (resource == null)
                            continue;
                        includes.put(key, resource);
                    }

                    //Second pass to check if there is this item on relationships
                    for (int i = 0; i < included.length(); i++) {
                        JSONObject each = included.getJSONObject(i);
                        String key = getResourceTag(each);

                        includes.remove(key);
                        Resource resource = resourceFromJson(each, includes);
                        if (resource == null)
                            continue;
                        includes.put(key, resource);
                    }
                }

                if (!json.isNull("data")) {
                    Object data = json.get("data");
                    if (data instanceof JSONObject) {
                        //Single Object
                        JSONObject objectData = (JSONObject) data;
                        Resource resource = resourceFromJson(objectData, includes);
                        if (resource != null)
                            jsonApiObject.addData(resource);
                    } else if (data instanceof JSONArray) {
                        //ListObjects
                        JSONArray objectData = (JSONArray) data;
                        for (int i = 0; i < objectData.length(); i++) {
                            Resource resource = resourceFromJson(objectData.getJSONObject(i), includes);
                            if (resource == null)
                                continue;
                            jsonApiObject.addData(resource);
                        }
                    }
                }

                if (!json.isNull("links"))
                    jsonApiObject.setLinks(linksFromJson(json.getJSONObject("links")));

                Field hasErrors = jsonApiObject.getClass().getDeclaredField("hasErrors");
                boolean oldAcessible = hasErrors.isAccessible();
                hasErrors.setAccessible(true);
                hasErrors.setBoolean(jsonApiObject, false);
                hasErrors.setAccessible(oldAcessible);
            } else {
                JSONArray errorList = json.getJSONArray("errors");
                List<ErrorModel> errorModelList = new ArrayList<>();
                for (int i = 0; i < errorList.length(); i++) {
                    ErrorModel error = new ErrorModel();
                    JSONObject eachRow = errorList.getJSONObject(i);

                    if (!eachRow.isNull("code"))
                        error.setStatus(eachRow.getString("code"));

                    if (!eachRow.isNull("detail"))
                        error.setDetail(eachRow.getString("detail"));

                    if (!eachRow.isNull("title"))
                        error.setTitle(eachRow.getString("title"));

                    if (!eachRow.isNull("source")) {
                        ErrorSource source = new ErrorSource();
                        JSONObject sourceJson = eachRow.getJSONObject("source");

                        if (!sourceJson.isNull("parameter"))
                            source.setParameter(sourceJson.getString("parameter"));

                        if (!sourceJson.isNull("pointer"))
                            source.setPointer(sourceJson.getString("pointer"));

                        error.setSource(source);
                    }

                    errorModelList.add(error);
                }

                Field hasErrors = jsonApiObject.getClass().getDeclaredField("errors");
                boolean oldAcessible = hasErrors.isAccessible();
                hasErrors.setAccessible(true);
                hasErrors.set(jsonApiObject, errorModelList);
                hasErrors.setAccessible(oldAcessible);
            }
        } catch (Exception ex) {
            e(JSONApiConverter.class.getSimpleName(), "Here _ " + ex.getMessage());
            ex.printStackTrace();
        }
        return jsonApiObject;
    }

    private Resource resourceFromJson(JSONObject jsonObject, HashMap<String, Resource> includes)
            throws JSONException, IllegalAccessException, InstantiationException, ParseException, NoSuchFieldException {
        String typeString = jsonObject.getString("type");
        if (!classesIndex.containsKey(typeString)) {
            // Not registered
            return null;
        }
        Resource resource = classesIndex.get(typeString).newInstance();

        Field idMainField = Resource.class.getDeclaredField("id");
        Field typeMainField = Resource.class.getDeclaredField("type");
        Field attrMainField = Resource.class.getDeclaredField("hasAttributes");

        boolean oldIdAccessible = idMainField.isAccessible();
        boolean oldTypeAccessible = typeMainField.isAccessible();
        boolean oldAccessibleAccessible = attrMainField.isAccessible();

        idMainField.setAccessible(true);
        if (!jsonObject.isNull("id")) {
            idMainField.set(resource, jsonObject.getString("id"));
        } else {
            idMainField.set(resource, "");
        }
        idMainField.setAccessible(oldIdAccessible);

        typeMainField.setAccessible(true);
        typeMainField.set(resource, typeString);
        typeMainField.setAccessible(oldTypeAccessible);

        attrMainField.setAccessible(true);
        attrMainField.setBoolean(resource, jsonObject.isNull("attributes"));
        attrMainField.setAccessible(oldAccessibleAccessible);

        //Pega os atributos da classe
        List<Field> fields = getFields(resource.getClass());
        HashMap<String, Field> fieldsHash = new HashMap<>();

        for (Field f : fields) {
            if (f.getAnnotation(SerialName.class) != null)
                fieldsHash.put(f.getAnnotation(SerialName.class).value(), f);
            else
                fieldsHash.put(f.getName(), f);
        }

        if (!jsonObject.isNull("attributes")) {
            //Separa os atributos do JSON
            JSONObject attributes = jsonObject.getJSONObject("attributes");
            Iterator<String> it = attributes.keys();

            while (it.hasNext()) {
                String attr = it.next();
                Object value = attributes.get(attr);

                if (fieldsHash.containsKey(attr)) {
                    Field currentField = fieldsHash.get(attr);
                    Boolean oldAccessible = currentField.isAccessible();
                    currentField.setAccessible(true);

                    try {
                        if (value instanceof String) {
                            if (currentField.getType().equals(char.class))
                                currentField.setChar(resource, String.valueOf(value).charAt(0));
                            else if (currentField.getType().equals(String.class))
                                currentField.set(resource, value);
                            else if (currentField.getType().equals(Date.class))
                                currentField.set(resource, parseDate(String.valueOf(value)));
                            else if (currentField.getType().equals(double.class) || currentField.getType().equals(Double.class))
                                currentField.set(resource, Double.valueOf(String.valueOf(value)));
                            else if (currentField.getType().equals(float.class) || currentField.getType().equals(Float.class))
                                currentField.set(resource, Float.valueOf(String.valueOf(value)));
                            else
                                e("JSONApiConverter", "Type not setted (" + currentField.getType().getName() + ")");
                        } else if (value instanceof Double || value instanceof Float) {
                            if (currentField.getType().equals(float.class) || currentField.getType().equals(Float.class))
                                currentField.setDouble(resource, (float) value);
                            else if (currentField.getType().equals(double.class) || currentField.getType().equals(Double.class))
                                currentField.setDouble(resource, (double) value);
                        } else if (value instanceof Integer) {
                            currentField.setInt(resource, (int) value);
                        } else if (value instanceof Long) {
                            currentField.setLong(resource, (long) value);
                        } else if (value instanceof Character) {
                            currentField.setChar(resource, (char) value);
                        } else if (value instanceof Boolean) {
                            currentField.setBoolean(resource, (boolean) value);
                        } else if (value instanceof JSONObject) {
                            JSONObject json = (JSONObject) value;
                            Map<String, Object> data = new HashMap<>();

                            Iterator<String> keys = json.keys();
                            while (keys.hasNext()) {
                                String k = keys.next();
                                data.put(k, json.get(k));
                            }

                            currentField.set(resource, data);
                        } else if (value instanceof JSONArray) {
                            JSONArray array = (JSONArray) value;
                            List<Object> arrayData = new ArrayList<>();
                            if (array.length() > 0) {
                                for (int i = 0; i < array.length(); i++) {
                                    arrayData.add(array.get(i));
                                }
                            }
                            currentField.set(resource, arrayData);
                        }
                    } catch (Exception ex) {
                        e("JSONApiConverter", String.format("Failed to pass attribute %s", attr));
                    }

                    currentField.setAccessible(oldAccessible);
                }
            }
        }

        //Termina os atributos
        //Inicia os relationships
        if (!jsonObject.isNull("relationships")) {
            JSONObject relationships = jsonObject.getJSONObject("relationships");
            Iterator <String> keys = relationships.keys();
            while (keys.hasNext()) {
                String key = keys.next();
                JSONObject eachRelation = relationships.getJSONObject(key);

                Object data = eachRelation.get("data");

                if (data == null || data == JSONObject.NULL) {
                    //Forcing the data to be an actual object in order to keep parsing the links on data:null
                    data = new JSONArray();
                }

                if (fieldsHash.containsKey(key)){
                    Field field = fieldsHash.get(key);
                    Boolean oldAccessible = field.isAccessible();
                    field.setAccessible(true);

                    if (data instanceof JSONObject) {
                        JSONObject dataJson = (JSONObject) data;
                        String keyRelation = getResourceTag(dataJson);
                        Object fieldValue = null;

                        if (includes.get(keyRelation) != null) {
                            fieldValue = includes.get(keyRelation);
                        } else {
                            if (dataJson.isNull("attributes")) {
                                String id = dataJson.getString("id");
                                String type = dataJson.getString("type");

                                if (!classesIndex.containsKey(type))
                                    continue;
                                fieldValue = classesIndex.get(type).newInstance();

                                Field idField = Resource.class.getDeclaredField("id");
                                boolean oldAcessible = idField.isAccessible();

                                idField.setAccessible(true);
                                idField.set(fieldValue, id);
                                idField.setAccessible(oldAcessible);
                            } else {
                                Resource r = resourceFromJson(dataJson, includes);
                                if (r == null)
                                    continue;
                                fieldValue = r;
                            }
                        }

                        if (fieldValue != null) {
                            if (!dataJson.isNull("links")) {
                                fieldValue.getClass().getDeclaredField("links")
                                        .set(fieldValue, linksFromJson(((JSONObject) data)
                                                .getJSONObject("links")));
                            }

                            field.set(resource, fieldValue);
                        }
                    } else if (data instanceof JSONArray) {
                        JSONList<Resource> relationList = new JSONList<>();
                        JSONArray dataJson = (JSONArray) data;

                        for (int i = 0; i < dataJson.length(); i++) {
                            String keyRelation = getResourceTag(dataJson.getJSONObject(i));
                            if (includes.get(keyRelation) != null) {
                                relationList.add(includes.get(keyRelation));
                            } else {
                                if (dataJson.getJSONObject(i).isNull("attributes")) {
                                    String id = dataJson.getJSONObject(i).getString("id");
                                    String type = dataJson.getJSONObject(i).getString("type");

                                    if (!classesIndex.containsKey(type))
                                        continue;
                                    Resource fieldValue = classesIndex.get(type).newInstance();

                                    Field idField = Resource.class.getDeclaredField("id");
                                    boolean oldAcessible = idField.isAccessible();

                                    idField.setAccessible(true);
                                    idField.set(fieldValue, id);
                                    idField.setAccessible(oldAcessible);

                                    relationList.add(fieldValue);
                                } else {
                                    Resource r = resourceFromJson(dataJson.getJSONObject(i), includes);
                                    if (r == null)
                                        continue;
                                    relationList.add(r);
                                }
                            }
                        }

                        if (!eachRelation.isNull("links")) {
                            relationList.setLinks(linksFromJson(eachRelation.getJSONObject("links")));
                        }

                        field.set(resource, relationList);
                    }
                    field.setAccessible(oldAccessible);
                }
            }
        }

        return resource;
    }

    private Links linksFromJson(JSONObject linksJson) throws NoSuchFieldException, JSONException, IllegalAccessException {
        Links links = new Links();

        Iterator<String> it = linksJson.keys();
        while (it.hasNext()) {
            String next = it.next();
            if (links.getClass().getDeclaredField(next) != null) {
                Field currentField = links.getClass().getDeclaredField(next);

                Boolean oldAccessible = currentField.isAccessible();
                currentField.setAccessible(true);

                currentField.set(links, linksJson.getString(next));

                currentField.setAccessible(oldAccessible);
            }
        }

        return links;
    }

    private JSONObject toJsonArray(List<? extends Resource> resources) {
        JSONObject mainNode = new JSONObject();
        try {
            JSONArray mainContent = new JSONArray();
            JSONArray include = new JSONArray();

            //IncludedIds
            List<String> includedIds = new ArrayList<>();

            //Handle Serialization
            for (Resource resource : resources) {
                JSONObject obj = toJsonObject(resource);

                //Add all data
                JSONObject data = obj.getJSONObject("data");
                mainContent.put(data);

                //Add all Included
                if (!obj.isNull("included")) {
                    JSONArray extras = obj.getJSONArray("included");
                    for (int i = 0; i < extras.length(); i++) {
                        JSONObject eachInclude = extras.getJSONObject(i);
                        String key = getResourceTag(eachInclude);

                        if (!includedIds.contains(key)) {
                            includedIds.add(key);
                            include.put(eachInclude);
                        }
                    }
                }
            }

            mainNode.put("data", mainContent);
            if (include.length() > 0)
                mainNode.put("included", include);
        } catch (Exception ex){
            ex.printStackTrace();
        }
        return mainNode;
    }

    private JSONObject toJsonObject(Resource resource) {
        try {
            Resource ninstance = resource.getClass().newInstance();
            if (resource.equals(ninstance)) {
                return new JSONObject();
            }
        } catch (Exception e) {}

        List<Field> fields = getFields(resource.getClass());
        JSONObject mainNode = new JSONObject();

        try {
            JSONObject content = new JSONObject();
            JSONObject attributes = new JSONObject();
            JSONObject relationship = new JSONObject();
            JSONArray include = new JSONArray();

            //Processar os dados
            content.put("type", resource.getType());
            for (Field field : fields) {
                String fieldName = null;
                Boolean oldAccessible = field.isAccessible();
                field.setAccessible(true);

                if (field.getAnnotation(Excluded.class) != null)
                    continue;

                if (field.get(resource) == null)
                    continue;

                if (field.getAnnotation(SerialName.class) != null) {
                    fieldName = field.getAnnotation(SerialName.class).value();
                }

                if (fieldName == null)
                    fieldName = field.getName();

                if (Collection.class.isAssignableFrom(field.getType())) {
                    JSONArray array = new JSONArray();
                    List<?> list = (List) field.get(resource);

                    if (list.size() > 0) {
                        for (Object listItem : list) {
                            try {
                                if (listItem instanceof String) {
                                    array.put(String.valueOf(listItem));
                                } else if (listItem instanceof Integer) {
                                    array.put((int) listItem);
                                } else if (listItem instanceof Double || listItem instanceof Float) {
                                    array.put(Double.valueOf(String.valueOf(listItem)));
                                } else if (listItem instanceof Long) {
                                    array.put((Long) listItem);
                                } else if (listItem instanceof Character) {
                                    array.put(Character.toString((char) listItem));
                                } else if (listItem instanceof Boolean) {
                                    array.put((boolean) listItem);
                                } else if (listItem instanceof Date) {
                                    array.put(dateFormatToServer.format((Date) field.get(resource)).replace(" ", "T"));
                                } else if (listItem instanceof Map) {
                                    JSONObject obj = new JSONObject();
                                    Map<String, Object> data = (Map) field.get(listItem);

                                    Iterator<String> it = data.keySet().iterator();
                                    while (it.hasNext()) {
                                        String key = it.next();
                                        obj.put(key, data.get(key));
                                    }
                                    array.put(obj);
                                } else if (listItem instanceof Resource) {
                                    JSONObject relationshipNode = null;
                                    JSONArray relationshipNodeData = null;

                                    if (relationship.isNull(fieldName)) {
                                        relationshipNode = new JSONObject();
                                    } else {
                                        relationshipNode = relationship.getJSONObject(fieldName);
                                    }

                                    if (relationshipNode.isNull("data")) {
                                        relationshipNodeData = new JSONArray();
                                    } else {
                                        relationshipNodeData = relationshipNode.getJSONArray("data");
                                    }

                                    relationshipNodeData.put(getNodeAsRelationship(listItem));
                                    relationshipNode.put("data", relationshipNodeData);

                                    relationship.put(fieldName, relationshipNode);

                                    JSONObject obj = getNodeAsInclude(listItem);
                                    if (obj != null)
                                        include.put(obj);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    if (array.length() > 0)
                        attributes.put(fieldName, array);
                } else {
                    try {
                        if (field.getAnnotation(Id.class) != null) {
                            content.put("id", String.valueOf(field.get(resource)));
                        } else if (field.get(resource) instanceof String) {
                            attributes.put(fieldName, String.valueOf(field.get(resource)));
                        } else if (field.get(resource) instanceof Integer) {
                            attributes.put(fieldName, field.getInt(resource));
                        } else if (field.get(resource) instanceof Double || field.get(resource) instanceof Float) {
                            attributes.put(fieldName, Double.valueOf(String.valueOf(field.get(resource))));
                        } else if (field.get(resource) instanceof Long) {
                            attributes.put(fieldName, field.getLong(resource));
                        } else if (field.get(resource) instanceof Character) {
                            attributes.put(fieldName, Character.toString(field.getChar(resource)));
                        } else if (field.get(resource) instanceof Boolean) {
                            attributes.put(fieldName, field.getBoolean(resource));
                        } else if (field.get(resource) instanceof Date) {
                            attributes.put(fieldName, dateFormatToServer.format((Date) field.get(resource)).replace(" ", "T"));
                        } else if (field.get(resource) instanceof Map) {
                            JSONObject obj = new JSONObject();
                            Map<String, Object> data = (Map) field.get(resource);

                            Iterator<String> it = data.keySet().iterator();
                            while (it.hasNext()) {
                                String key = it.next();
                                obj.put(key, data.get(key));
                            }
                            attributes.put(fieldName, obj);
                        } else if (field.get(resource) instanceof Resource) {
                            JSONObject relationshipNode = new JSONObject();
                            relationshipNode.put("data", getNodeAsRelationship(field.get(resource)));

                            relationship.put(fieldName, relationshipNode);

                            JSONObject obj = getNodeAsInclude(field.get(resource));
                            if (obj != null)
                                include.put(obj);
                        }
                    } catch (Exception ex) {
                        e("JSONApiConverter", String.format("Failed to pass attribute %s", fieldName));
                    }
                }


                field.setAccessible(oldAccessible);
            }

            if (attributes.length() > 0)
                content.put("attributes", attributes);

            if (relationship.length() > 0)
                content.put("relationships", relationship);

            if (include.length() > 0)
                mainNode.put("included", include);

            mainNode.put("data", content);

        } catch (Exception ex) {
            //Tratar erros
            ex.printStackTrace();
        }

        return mainNode;
    }

    private String getResourceTag(Resource resource) {
        String type = resource.getType();
        return (type + "|" + resource.getId());
    }

    private String getResourceTag(JSONObject json) throws JSONException {
        return (json.getString("type") + "|" + json.getString("id"));
    }

    private JSONObject getNodeAsRelationship(Object resource) throws JSONException, IllegalAccessException {
        List<Field> fields = getFields(resource.getClass());
        JSONObject jsonObject = new JSONObject();

        jsonObject.put("type", ((Resource) resource).getType());

        for (Field field : fields) {
            Boolean oldAccessible = field.isAccessible();
            field.setAccessible(true);
            if (field.getAnnotation(Id.class) != null) {
                jsonObject.put("id", field.get(resource));
            }
            field.setAccessible(oldAccessible);
        }

        return jsonObject;
    }

    private JSONObject getNodeAsInclude(Object resource) throws JSONException, IllegalAccessException, ParseException {
        List<Field> fields = getFields(resource.getClass());

        JSONObject content = new JSONObject();
        JSONObject attributes = new JSONObject();
        JSONObject relationship = new JSONObject();

        content.put("type", ((Resource) resource).getType());

        for (Field field : fields) {
            if (Collection.class.isAssignableFrom(field.getType()))
                continue;

            String fieldName = null;
            Boolean oldAccessible = field.isAccessible();
            field.setAccessible(true);

            if (field.getAnnotation(Excluded.class) != null)
                continue;

            if (field.getAnnotation(SerialName.class) != null) {
                fieldName = field.getAnnotation(SerialName.class).value();
            }

            if (fieldName == null)
                fieldName = field.getName();

            if (field.getAnnotation(Id.class) != null) {
                content.put("id", String.valueOf(field.get(resource)));
            } else if (field.get(resource) instanceof String) {
                attributes.put(fieldName, String.valueOf(field.get(resource)));
            } else if (field.get(resource) instanceof Integer) {
                attributes.put(fieldName, field.getInt(resource));
            } else if (field.get(resource) instanceof Double || field.get(resource) instanceof Float) {
                attributes.put(fieldName, Double.parseDouble(String.valueOf(field.get(resource))));
            } else if (field.get(resource) instanceof Long) {
                attributes.put(fieldName, field.getLong(resource));
            } else if (field.get(resource) instanceof Character) {
                attributes.put(fieldName, Character.toString(field.getChar(resource)));
            } else if (field.get(resource) instanceof Boolean) {
                attributes.put(fieldName, field.getBoolean(resource));
            } else if (field.get(resource) instanceof Date) {
                attributes.put(fieldName, dateFormatToServer.format((Date) field.get(resource)).replace(" ", "T"));
            }else if (field.get(resource) instanceof Resource) {
                JSONObject relationshipNode = new JSONObject();
                relationshipNode.put("data", getNodeAsRelationship(field.get(resource)));

                relationship.put(fieldName, relationshipNode);
            }

            field.setAccessible(oldAccessible);
        }

        if (attributes.length() > 0)
            content.put("attributes", attributes);

        if (relationship.length() > 0)
            content.put("relationships", relationship);

        if (attributes.length() > 0 || relationship.length() > 0)
            return content;

        return null;
    }

    private List<Field> getFields(Class<?> type) {
        List<Field> fields = new ArrayList<>();
        fields.addAll(Arrays.asList(type.getDeclaredFields()));

        if (type.getSuperclass() != null && !type.getSuperclass().getName().equals(Object.class.getName())) {
            fields.addAll(getFields(type.getSuperclass()));
        }

        return fields;
    }
    
    private static void e (String tag, String message) {
        System.err.println(tag + ": " + message);
    }

}

