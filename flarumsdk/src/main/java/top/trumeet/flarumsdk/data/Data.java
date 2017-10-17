package top.trumeet.flarumsdk.data;

import java.util.List;
import java.util.Map;

/**
 * Created by Trumeet on 2017/10/10.
 */
public class Data {
    private String type;
    private String id;

    private Map<String, List<Data>>
            relationships;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Map<String, List<Data>> getRelationships() {
        return relationships;
    }

    public void setRelationships(Map<String, List<Data>> relationships) {
        this.relationships = relationships;
    }
}
