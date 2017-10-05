package top.trumeet.flarumsdk;

import okhttp3.Response;
import top.trumeet.flarumsdk.internal.parser.jsonapi.Models.JSONApiObject;

/**
 * Created by Trumeet on 2017/9/26.
 */

public class Result<T> {
    /**
     * Id, TODO
     */
    public final int id = 0;


    /**
     * Raw response from OkHttp
     */
    public final Response rawResponse;

    /**
     * Main information
     */
    public final T mainAttr;

    /**
     * Other api result, it include all data, such as include and relationships.
     */
    public final JSONApiObject object;

    public Result(Response rawResponse, T mainAttr, JSONApiObject object) {
        this.rawResponse = rawResponse;
        this.mainAttr = mainAttr;
        this.object = object;
    }
}
