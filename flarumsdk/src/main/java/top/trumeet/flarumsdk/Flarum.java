package top.trumeet.flarumsdk;

import android.net.Uri;
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
import java.net.URLDecoder;
import java.util.List;
import java.util.concurrent.Executor;

@SuppressWarnings({"unused", "unchecked"})
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

    public Result<Forum> getForumInfo () throws FlarumException {
        return OkHttpUtils.execute(apiInterface.forumInfo(), this,
                new ItemConverter<Forum>());
    }

    public Call getForumInfo (Callback<Forum> callback) {
        return OkHttpUtils.enqueue(apiInterface.forumInfo(), this,
                new ItemConverter<Forum>(), callback);
    }

    public Result<List<Notification>> getMessageList (int page) throws FlarumException {
        return OkHttpUtils.execute(apiInterface.notifications(page), this,
                new ListConverter<Notification>());
    }

    public Call getMessageList (int page, Callback<List<Notification>> callback) {
        return OkHttpUtils.enqueue(apiInterface.notifications(page), this,
                new ListConverter<Notification>(), callback);
    }

    public Result<Notification> markNotificationAsRead (int id) throws FlarumException {
        return OkHttpUtils.execute(apiInterface.markNotificationAsRead(id),
                this, new ItemConverter<Notification>());
    }

    public Call markNotificationAsRead (int id, Callback<Notification> callback) {
        return OkHttpUtils.enqueue(apiInterface.markNotificationAsRead(id),
                this, new ItemConverter<Notification>(), callback);
    }

    public Result<LoginResponse> login (LoginRequest request) throws FlarumException, JSONException {
        Call call = apiInterface.login(request.getIdentification(),
                request.getPassword());
        return OkHttpUtils.execute(call, this, new LoginResponseConverter());
    }

    public Call login (LoginRequest request, final Callback<LoginResponse> callback) {
        return OkHttpUtils.enqueue(apiInterface.login(request.getIdentification(), request.getPassword()),
                this, new LoginResponseConverter(), callback);
    }

    public Call getTags (int page, Callback<List<Tag>> callback) {
        return OkHttpUtils.enqueue(apiInterface.tags(page), this,
                new ListConverter<Tag>(), callback);
    }

    public Result<List<Tag>> getTags (int page) throws FlarumException {
        return OkHttpUtils.execute(apiInterface.tags(page), this,
                new ListConverter<Tag>());
    }

    public Call getGroups (int page, Callback<List<Group>> callback) {
        return OkHttpUtils.enqueue(apiInterface.groups(page), this,
                new ListConverter<Group>(), callback);
    }

    public Result<List<Group>> getGroups (int page) throws FlarumException {
        return OkHttpUtils.execute(apiInterface.groups(page), this,
                new ListConverter<Group>());
    }

    public Result<List<User>> getUsers (int page) throws FlarumException {
        return OkHttpUtils.execute(apiInterface.users(page, null), this,
                new ListConverter<User>());
    }

    public Call getUsers (int page, Callback<List<User>> callback) {
        return OkHttpUtils.enqueue(apiInterface.users(page, null), this,
                new ListConverter<User>(), callback);
    }

    public Result<List<User>> getUsers (String query, int page) throws FlarumException {
        return OkHttpUtils.execute(apiInterface.users(page, query), this,
                new ListConverter<User>());
    }

    public Call getUsers (String query, int page, Callback<List<User>> callback) {
        return OkHttpUtils.enqueue(apiInterface.users(page, query), this,
                new ListConverter<User>(), callback);
    }

    public Result<User> registerUser (String username, String password, String email) throws FlarumException {
        return OkHttpUtils.execute(apiInterface.registerUser(username, password, email), this,
                new ItemConverter<User>());
    }

    public Call registerUser (String username, String password, String email, Callback<User> callback) {
        return OkHttpUtils.enqueue(apiInterface.registerUser(username, password, email),
        this, new ItemConverter<User>(), callback);
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
            this.baseUrl = baseUrl;
        }

        Call forumInfo () {
            return client.newCall(baseBuilder("forum", "GET", null,
                    null, 0)
            .build());
        }

        Call login (String identification, String password) {
            JSONObject object = new JSONObject();
            object.put("identification", identification);
            object.put("password", password);
            return client.newCall(baseBuilder("token", "POST",
                    createJsonBody(object.toString()),
                    null, 0)
            .build());
        }

        Call markNotificationAsRead (int id) {
            return client.newCall(baseBuilder("notifications/" + id
                    , "PATCH",
                    createStringBody(""), null, 0).build());
        }

        Call notifications (int page) {
            return client.newCall(baseBuilder("notifications", "GET",
                    null, null, page).build());
        }

        Call tags (int page) {
            return client.newCall(baseBuilder("tags", "GET",
                    null, null, page).build());
        }

        Call groups (int page) {
            return client.newCall(baseBuilder("groups", "GET",
                    null, null, page).build());
        }

        Call users (int page, String query) {
            return client.newCall(baseBuilder("users", "GET",
                    null, query != null ? new Query("q", query) :
                    null, page).build());
        }

        Call registerUser (String username, String password, String email) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("username", username);
            jsonObject.put("password", password);
            jsonObject.put("email", email);
            return client.newCall(baseBuilder("users", "POST",
                    createJsonBody(createRequestJson(jsonObject).toString())
                    , null, 0).build());
        }

        private Request.Builder baseBuilder (@Nonnull String urlPoint, @Nonnull String method,
                                             @Nullable RequestBody requestBody,
                                             @Nullable Query filter,
                                             int page) {

            Uri.Builder uriBuilder = new Uri.Builder()
                    .scheme("https" /* TODO */)
                    .authority(baseUrl)
                    .appendPath("api")
                    .appendPath(urlPoint);
            if (filter != null) {
                uriBuilder.appendQueryParameter("filter[" + filter.key + "]" /* very bad */,
                        filter.value);
            }
            if (page > 0) {
                uriBuilder.appendQueryParameter("page%5Boffset%5D" /* too bad */,
                        String.valueOf(page * 20));
            }
            String url = URLDecoder.decode(uriBuilder.build().toString());
            Request.Builder builder = new Request.Builder()
                    .url(url)
                    .method(method, requestBody);
            String token = tokenGetter != null ? tokenGetter.getToken() : null;
            if (token != null)
                builder.addHeader("Authorization", "Token " + token /* Too bad */);
            return builder;
        }

        private static class Query {
            public final String key;
            public final String value;

            public Query(String key, String value) {
                this.key = key;
                this.value = value;
            }
        }

        private static RequestBody createJsonBody (String json) {
            return RequestBody.create(JSON, json);
        }

        private static RequestBody createStringBody (String body) {
            return RequestBody.create(TEXT_PLAIN, body);
        }

        private static JSONObject createRequestJson (JSONObject attributes) {
            JSONObject object = new JSONObject();
            JSONObject data = new JSONObject();
            data.put("attributes", attributes);
            object.put("data", data);
            return object;
        }
    }
}
