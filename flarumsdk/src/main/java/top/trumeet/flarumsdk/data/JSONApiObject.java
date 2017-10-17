package top.trumeet.flarumsdk.data;

import java.util.ArrayList;
import java.util.List;

public class JSONApiObject {

    private List<Data> data;
    private Links links;
    private List<Error> errors;
    private List<Data> included;

    public List<Data> getIncluded() {
        return included;
    }

    public void setIncluded(List<Data> included) {
        this.included = included;
    }

    public JSONApiObject() {
        data = new ArrayList<>();
    }

    public List<Data> getData() {
        return data;
    }

    public Data getData(int position) {
        return data.get(position);
    }

    public void setData(List<Data> data) {
        this.data = data;
    }

    public void addData(Data resource) {
        data.add(resource);
    }

    public Links getLinks() {
        return links;
    }

    public void setLinks(Links links) {
        this.links = links;
    }

    public boolean hasErrors() {
        return getErrors() != null && !getErrors().isEmpty();
    }

    public List<Error> getErrors() {
        return errors;
    }

    @Override
    public String toString() {
        return "JSONApiObject{" +
                "data=" + data +
                ", links=" + links +
                ", errors=" + errors +
                '}';
    }

    public void setErrors(List<Error> errors) {
        this.errors = errors;
    }
}
