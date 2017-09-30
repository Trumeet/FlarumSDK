package top.trumeet.flarumsdk.internal.parser.jsonapi.Models;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import top.trumeet.flarumsdk.internal.parser.jsonapi.Annotations.Excluded;
import top.trumeet.flarumsdk.internal.parser.jsonapi.Annotations.Id;
import top.trumeet.flarumsdk.internal.parser.jsonapi.Annotations.Type;
import top.trumeet.flarumsdk.internal.parser.jsonapi.Annotations.Types;

public class Resource {

    @Id
    private String id;

    private Links resource_api_links;

    @Excluded
    private String type;

    @Excluded
    protected boolean hasAttributes;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        if (type != null)
            return type;
        if (getClass().getAnnotation(Type.class) != null)
            return getClass().getAnnotation(Type.class).value();
        if (type == null && getClass().getAnnotation(Types.class).value() != null && getClass().getAnnotation(Types.class).value().length > 0)
            return getClass().getAnnotation(Types.class).value()[0];
        return "";
    }

    public boolean hasAttributes() {
        return hasAttributes;
    }

    public Links getLinks() {
        return resource_api_links;
    }

    public void setLinks(Links links) {
        this.resource_api_links = links;
    }

    @Override
    public boolean equals(Object o) {
        if (o.getClass() != getClass())
            return false;

        List<Field> fields = getFields(getClass());
        for (Field f : fields) {
            boolean acessible = f.isAccessible();
            try {
                f.setAccessible(true);
                Object v1 = f.get(this);
                Object v2 = f.get(o);

                if (v1 == null && v2 == null)
                    continue;

                if ((v1 != null && v2 == null) || (v1 == null && v2 != null))
                    return false;

                if (!f.get(this).equals(f.get(o)))
                    return false;
            } catch (Exception e) {
                e.printStackTrace();
            }finally {
                f.setAccessible(acessible);
            }
        }

        return true;
    }

    private static List<Field> getFields(Class<?> type) {
        List<Field> fields = new ArrayList<>();
        fields.addAll(Arrays.asList(type.getDeclaredFields()));

        if (type.getSuperclass() != null && !type.getSuperclass().getName().equals(Object.class.getName())) {
            fields.addAll(getFields(type.getSuperclass()));
        }

        return fields;
    }
}
