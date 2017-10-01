package top.trumeet.flarumsdk.internal.parser;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Response;
import top.trumeet.flarumsdk.Callback;
import top.trumeet.flarumsdk.Flarum;
import top.trumeet.flarumsdk.FlarumException;
import top.trumeet.flarumsdk.Result;
import top.trumeet.flarumsdk.internal.parser.jsonapi.Models.JSONApiObject;

/**
 * Created by Trumeet on 2017/9/26.
 */

public class OkHttpUtils {
    public static <T> Call enqueue (final Call original,
                                    final Flarum apiManager,
                                                   final ObjectParser.JsonObjectConverter<T> converter
            , final Callback<T> callback) {
        original.enqueue(new okhttp3.Callback() {
            @Override
            public void onResponse(final Call call, final Response response) {
                try {
                    String resultStr = response.body().string();
                    final JSONApiObject object = apiManager.getConverter().fromJson(resultStr);
                    if (object.hasErrors()) {
                        // Handle error
                        callOnFailure(call, FlarumException.create(object.getErrors()));
                        return;
                    }
                    final Result<T> result = new Result<>(response, converter.convert(object,
                            resultStr), object);
                    apiManager.getPlatformExecutor()
                            .execute(new Runnable() {
                                @Override
                                public void run() {
                                    callback.onResponse(call, result);
                                }
                            });
                } catch (IOException e) {
                    onFailure(call, e);
                }
            }

            @Override
            public void onFailure(final Call call, final IOException t) {
                callOnFailure(call, t);
            }

            private void callOnFailure (final Call call, final Throwable t) {
                apiManager.getPlatformExecutor().execute(new Runnable() {
                    @Override
                    public void run() {
                        callback.onFailure(call, t);
                    }
                });
            }
        });
        return original;
    }

    public static <T> Result<T> execute (Call original, Flarum apiManager,
                                         ObjectParser.JsonObjectConverter<T> converter) throws IOException {
        Response response = original.execute();
        JSONApiObject object = apiManager.getConverter().fromJson(response.body().string());
        return new Result<>(response, converter.convert(object, response.body().string()), object);
    }
}
