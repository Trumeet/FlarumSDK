package top.trumeet.flarumsdk;

import java.util.List;

import top.trumeet.flarumsdk.internal.parser.jsonapi.Models.ErrorModel;

/**
 * Created by Trumeet on 2017/10/1.
 */

public class FlarumException extends Exception {
    public FlarumException() {
        super();
    }

    public FlarumException(String s) {
        super(s);
    }

    public FlarumException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public FlarumException(Throwable throwable) {
        super(throwable);
    }

    private int status;
    private String code;
    private List<ErrorModel> allErrors;

    public FlarumException (int status, String code) {
        super(status + ": " + code);
        this.status = status;
        this.code = code;
    }

    public static FlarumException create (ErrorModel errorModel) {
        return new FlarumException(Integer.parseInt(errorModel.getStatus()),
                errorModel.getCode());
    }

    public static FlarumException create (List<ErrorModel> errorModels) {
        if (errorModels == null || errorModels.size() <= 0) {
            return new FlarumException();
        }
        FlarumException exception = create(errorModels.get(0));
        exception.setAllErrors(errorModels);
        return exception;
    }

    public List<ErrorModel> getAllErrors() {
        return allErrors;
    }

    public void setAllErrors(List<ErrorModel> allErrors) {
        this.allErrors = allErrors;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return "FlarumException{" +
                "status=" + status +
                ", code='" + code + '\'' +
                ", allErrors=" + allErrors +
                '}';
    }
}
