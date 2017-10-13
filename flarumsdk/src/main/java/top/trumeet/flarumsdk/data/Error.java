package top.trumeet.flarumsdk.data;

/**
 * Created by Gustavo Fão Valvassori on 21/04/2016.
 * Propósito: ${CURSOR}
 */
public class Error {
    private String id;
    private String status;
    private String title;
    private String detail;
    private ErrorSource source;
    private String code;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public ErrorSource getSource() {
        return source;
    }

    public void setSource(ErrorSource source) {
        this.source = source;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Error{" +
                "id='" + id + '\'' +
                ", status='" + status + '\'' +
                ", title='" + title + '\'' +
                ", detail='" + detail + '\'' +
                ", source=" + source +
                ", code='" + code + '\'' +
                '}';
    }
}
