package top.trumeet.flarumsdk;

import android.net.Uri;
import okhttp3.Call;
import okhttp3.RequestBody;
import top.trumeet.flarumsdk.internal.parser.ObjectParser;
import top.trumeet.flarumsdk.internal.parser.OkHttpUtils;

import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Trumeet on 2017/10/5.
 */
public class RequestBuilder<T> {
    public static class BaseRequest<TYPE> {
        public final String urlEndpoint;
        public final String method;
        public final RequestBody body;
        public final Flarum flarum;
        public final ObjectParser.JsonObjectConverter<TYPE> converter;

        public BaseRequest(String urlEndpoint, String method, RequestBody body, Flarum flarum,
                           ObjectParser.JsonObjectConverter<TYPE> converter) {
            this.urlEndpoint = urlEndpoint;
            this.method = method;
            this.body = body;
            this.flarum = flarum;
            this.converter = converter;
        }
    }

    public RequestBuilder(BaseRequest<T> baseRequest) {
        this.flarum = baseRequest.flarum;
        this.baseRequest = baseRequest;
    }

    private BaseRequest baseRequest;
    private Flarum flarum;
    private Map<String, String> queries;
    private int page;

    @SuppressWarnings("unchecked")
    public Result<T> execute () throws FlarumException {
        return OkHttpUtils.execute(flarum.makeCall(build())
                , flarum, baseRequest.converter);
    }

    @SuppressWarnings("unchecked")
    public Call enqueue (Callback<T> callback) {
        return OkHttpUtils.enqueue(flarum.makeCall(build()),
                flarum, baseRequest.converter, callback);
    }

    private okhttp3.Request build () {
        return baseBuilder().build();
    }

    public RequestBuilder addFilter (String key, String value) {
        if (queries == null) queries = new HashMap<>(1);
        queries.put(key, value) ;
        return this;
    }

    public RequestBuilder removeFilter (String key) {
        if (queries == null) return this;
        queries.remove(key);
        if (queries.isEmpty()) queries = null;
        return this;
    }

    public String getFilter (String key) {
        if (queries == null) return null;
        return queries.get(key);
    }

    public RequestBuilder setPage (int page) {
        this.page = page;
        return this;
    }

    private okhttp3.Request.Builder baseBuilder () {
        Uri.Builder uriBuilder = flarum.getBaseUrl().buildUpon()
                .appendPath("api")
                .appendPath(baseRequest.urlEndpoint);
        if (queries != null) {
            for (String key : queries.keySet()) {
                uriBuilder.appendQueryParameter("filter[" + key + "]" /* very bad */,
                        queries.get(key));
            }
        }
        if (page > 0) {
            uriBuilder.appendQueryParameter("page%5Boffset%5D" /* too bad */,
                    String.valueOf(page * 20));
        }
        String url = URLDecoder.decode(uriBuilder.build().toString());
        okhttp3.Request.Builder builder = new okhttp3.Request.Builder()
                .url(url)
                .method(baseRequest.method, baseRequest.body);
        String token = flarum.getToken();
        if (token != null)
            builder.addHeader("Authorization", "Token " + token /* Too bad */);
        return builder;
    }
}
