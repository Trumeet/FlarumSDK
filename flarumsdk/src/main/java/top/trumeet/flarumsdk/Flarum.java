package top.trumeet.flarumsdk;

import okhttp3.*;
import org.json.JSONException;
import org.json.JSONObject;
import top.trumeet.flarumsdk.data.*;
import top.trumeet.flarumsdk.internal.parser.OkHttpUtils;
import top.trumeet.flarumsdk.internal.parser.converter.ItemConverter;
import top.trumeet.flarumsdk.internal.parser.converter.ListConverter;
import top.trumeet.flarumsdk.internal.parser.converter.LoginResponseConverter;
import top.trumeet.flarumsdk.internal.parser.jsonapi.JSONApiConverter;
import top.trumeet.flarumsdk.internal.platform.Platform;
import top.trumeet.flarumsdk.login.LoginRequest;
import top.trumeet.flarumsdk.login.LoginResponse;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.Executor;

public class Flarum {
    private Flarum (String baseUrl, API apiInterface) {
        this.baseUrl = baseUrl;
        this.apiInterface = apiInterface;
        Executor executor = Platform.get().defaultCallbackExecutor();
        if (executor == null) {
            executor = new Executor() {
                @Override
                public void execute(Runnable runnable) {
                    runnable.run();
                }
            };
        }
        this.platformExecutor = executor;
    }

    /**
     * Create API client
     * @param baseUrl Forum base url, e.g: https://discuss.flarum.org
     * @return Flarum API client
     */
    public static Flarum create (String baseUrl) {
        return create(baseUrl, new OkHttpClient());
    }

    /**
     * Create API client with custom {@link OkHttpClient}
     * @param baseUrl Forum base url, e.g: https://discuss.flarum.org
     * @param okHttpClient Custom http client
     * @return Flarum API client
     */
    public static Flarum create (String baseUrl, OkHttpClient okHttpClient) {
        Flarum flarum = new Flarum(baseUrl, new API(okHttpClient, baseUrl));
        flarum.converter = new JSONApiConverter(Forum.class
                , Notification.class,
                User.class,
                Tag.class,
                Link.class,
                Discussion.class,
                Group.class);
        return flarum;
    }

    /**
     * Set auth token, it will override {@link Flarum#setToken(String)}.
     * @param getter dynamic interface
     */
    public void setToken (TokenGetter getter) {
        apiInterface.setTokenGetter(getter);
    }

    /**
     * Set auth token string, it will override {@link Flarum#setToken(TokenGetter)}
     * @param token token string
     */
    public void setToken (@Nullable final String token) {
        apiInterface.setTokenGetter(new TokenGetter() {
            @Nullable
            @Override
            public String getToken() {
                return token;
            }
        });
    }

    private final String baseUrl;
    private final API apiInterface;
    private final Executor platformExecutor;

    private JSONApiConverter converter;

    public JSONApiConverter getConverter() {
        return converter;
    }

    public Executor getPlatformExecutor() {
        return platformExecutor;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public Result<Forum> getForumInfo () throws IOException {
        return OkHttpUtils.execute(apiInterface.forumInfo(), this,
                new ItemConverter<Forum>());
    }

    public Call getForumInfo (Callback<Forum> callback) {
        return OkHttpUtils.enqueue(apiInterface.forumInfo(), this,
                new ItemConverter<Forum>(), callback);
    }

    public Result<List<Notification>> getMessageList () throws IOException {
        return OkHttpUtils.execute(apiInterface.notifications(), this,
                new ListConverter<Notification>());
    }

    public Call getMessageList (Callback<List<Notification>> callback) {
        return OkHttpUtils.enqueue(apiInterface.notifications(), this,
                new ListConverter<Notification>(), callback);
    }

    public Result<Notification> markNotificationAsRead (int id) throws IOException {
        return OkHttpUtils.execute(apiInterface.markNotificationAsRead(id),
                this, new ItemConverter<Notification>());
    }

    public Call markNotificationAsRead (int id, Callback<Notification> callback) {
        return OkHttpUtils.enqueue(apiInterface.markNotificationAsRead(id),
                this, new ItemConverter<Notification>(), callback);
    }

    public Result<LoginResponse> login (LoginRequest request) throws IOException, JSONException {
        Call call = apiInterface.login(request.getIdentification(),
                request.getPassword());
        return OkHttpUtils.execute(call, this, new LoginResponseConverter());
    }

    public Call login (LoginRequest request, final Callback<LoginResponse> callback) {
        return OkHttpUtils.enqueue(apiInterface.login(request.getIdentification(), request.getPassword()),
                this, new LoginResponseConverter(), callback);
    }

    public Call getTags (Callback<List<Tag>> callback) {
        return OkHttpUtils.enqueue(apiInterface.tags(), this,
                new ListConverter<Tag>(), callback);
    }

    public Result<List<Tag>> getTags () throws IOException {
        return OkHttpUtils.execute(apiInterface.tags(), this,
                new ListConverter<Tag>());
    }

    public Call getGroups (Callback<List<Group>> callback) {
        return OkHttpUtils.enqueue(apiInterface.groups(), this,
                new ListConverter<Group>(), callback);
    }

    public Result<List<Group>> getGroups () throws IOException {
        return OkHttpUtils.execute(apiInterface.groups(), this,
                new ListConverter<Group>());
    }

    /**
     * A dynamic getter for token
     */
    public interface TokenGetter {
        /**
         * Get current token. SDK will <strong>NOT</strong> save your token after login.
         * If you pass NULL, we will not pass Authorization header.
         * @return Token
         */
        @Nullable String getToken ();
    }

    private static class API {
        public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        public static final MediaType TEXT_PLAIN = MediaType.parse("text/plain; charset=utf-8");

        private final OkHttpClient client;
        private final String baseUrl;
        private TokenGetter tokenGetter;

        public TokenGetter getTokenGetter() {
            return tokenGetter;
        }

        public void setTokenGetter(TokenGetter tokenGetter) {
            this.tokenGetter = tokenGetter;
        }

        API(OkHttpClient client, String baseUrl) {
            this.client = client;
            this.baseUrl = baseUrl + "/api/";
        }

        Call forumInfo () {
            return client.newCall(baseBuilder("forum", "GET", null)
            .build());
        }

        Call login (String identification, String password) {
            JSONObject object = new JSONObject();
            object.put("identification", identification);
            object.put("password", password);
            return client.newCall(baseBuilder("token", "POST",
                    createJsonBody(object.toString()))
            .build());
        }

        Call markNotificationAsRead (int id) {
            return client.newCall(baseBuilder("notifications/" + id
                    , "PATCH",
                    createStringBody("")).build());
        }

        Call notifications () {
            return client.newCall(baseBuilder("notifications", "GET",
                    null).build());
        }

        Call tags () {
            return client.newCall(baseBuilder("tags", "GET",
                    null).build());
        }

        Call groups () {
            return client.newCall(baseBuilder("groups", "GET",
                    null).build());
        }

        private Request.Builder baseBuilder (@Nonnull String urlPoint, @Nonnull String method,
                                             @Nullable RequestBody requestBody) {
            Request.Builder builder = new Request.Builder()
                    .url(baseUrl + urlPoint)
                    .method(method, requestBody);
            String token = tokenGetter != null ? tokenGetter.getToken() : null;
            if (token != null)
                builder.addHeader("Authorization", "Token " + token /* Too bad */);
            return builder;
        }

        private static RequestBody createJsonBody (String json) {
            return RequestBody.create(JSON, json);
        }

        private static RequestBody createStringBody (String body) {
            return RequestBody.create(TEXT_PLAIN, body);
        }
    }
}
