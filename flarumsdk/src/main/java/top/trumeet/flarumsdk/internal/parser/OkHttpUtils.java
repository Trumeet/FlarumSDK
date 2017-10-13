package top.trumeet.flarumsdk.internal.parser;

import okhttp3.Call;
import okhttp3.Response;
import top.trumeet.flarumsdk.Callback;
import top.trumeet.flarumsdk.Flarum;
import top.trumeet.flarumsdk.FlarumException;
import top.trumeet.flarumsdk.Result;
import top.trumeet.flarumsdk.data.JSONApiObject;

import java.io.IOException;

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
                    if (response.code() == 402 && resultStr == null || resultStr.trim().equalsIgnoreCase("") &&
                            converter == null) {
                        apiManager.getPlatformExecutor()
                                .execute(new Runnable() {
                                    @Override
                                    public void run() {
                                        callback.onResponse(call, null);
                                    }
                                });
                        return;
                    }
                    final JSONApiObject object = apiManager.convert(resultStr);
                    if (object != null && object.hasErrors()) {
                        // Handle error
                        callOnFailure(call, FlarumException.create(object.getErrors()));
                        return;
                    }
                    final Result<T> result = new Result<>(response, converter.convert(object,
                            resultStr, apiManager), object);
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
                                         ObjectParser.JsonObjectConverter<T> converter) throws FlarumException {
        try {
            Response response = original.execute();
            String result = response.body().string();
            if (response.code() == 402 &&
                    result == null || result.trim().equalsIgnoreCase("") &&
                    converter == null) {
                return null;
            }
            JSONApiObject object = apiManager.convert(result);
            if (object != null && object.hasErrors()) {
                throw FlarumException.create(object.getErrors());
            }
            return new Result<>(response, converter.convert(object, result, apiManager)
                    , object);
        } catch (IOException e) {
            throw FlarumException.fromIOException(e);
        }
    }
}
