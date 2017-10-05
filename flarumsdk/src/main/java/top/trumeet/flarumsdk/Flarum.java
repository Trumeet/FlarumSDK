package top.trumeet.flarumsdk;

import android.net.Uri;
import okhttp3.*;
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
     * @param baseUrl Forum base url, e.g: discuss.flarum.org, NOT INCLUDING scheme, TODO
     * @return Flarum API client
     */
    public static Flarum create (String baseUrl) {
        return create(baseUrl, new OkHttpClient());
    }

    /**
     * Create API client with custom {@link OkHttpClient}
     * @param baseUrl Forum base url, e.g: discuss.flarum.org, NOT INCLUDING scheme, TODO
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

    /**
     * Internal api, get json api converter, you can deserialize json to object use it
     * @return Json api converter
     */
    public JSONApiConverter getConverter() {
        return converter;
    }

    /**
     * Internal api, get platform executor. In android, it will execute on main thread
     * @return executor
     */
    public Executor getPlatformExecutor() {
        return platformExecutor;
    }

    /**
     * Get forum base url
     * @return base url
     */
    public String getBaseUrl() {
        return baseUrl;
    }

    /**
     * get information about the forum, including groups and tags
     * @return Forum info
     */
    public Result<Forum> getForumInfo () throws FlarumException {
        return OkHttpUtils.execute(apiInterface.forumInfo(), this,
                new ItemConverter<Forum>());
    }

    /**
     * get information about the forum async, including groups and tags
     * @param callback Callback
     * @return Call
     */
    public Call getForumInfo (Callback<Forum> callback) {
        return OkHttpUtils.enqueue(apiInterface.forumInfo(), this,
                new ItemConverter<Forum>(), callback);
    }

    /**
     * get all notifications
     * @param page page
     * @return notifications list
     */
    public Result<List<Notification>> getMessageList (int page) throws FlarumException {
        return OkHttpUtils.execute(apiInterface.notifications(page), this,
                new ListConverter<Notification>());
    }

    /**
     * get all notifications async
     * @param page page
     * @param callback Callback
     * @return Call
     */
    public Call getMessageList (int page, Callback<List<Notification>> callback) {
        return OkHttpUtils.enqueue(apiInterface.notifications(page), this,
                new ListConverter<Notification>(), callback);
    }

    /**
     * mark a notification as read
     * @param id Notification id, {@link Result#id}
     * @return notification object
     */
    public Result<Notification> markNotificationAsRead (int id) throws FlarumException {
        return OkHttpUtils.execute(apiInterface.markNotificationAsRead(id),
                this, new ItemConverter<Notification>());
    }

    /**
     * mark a notification as read async
     * @param id Notification id, {@link Result#id}
     * @param callback Callback
     * @return Call
     */
    public Call markNotificationAsRead (int id, Callback<Notification> callback) {
        return OkHttpUtils.enqueue(apiInterface.markNotificationAsRead(id),
                this, new ItemConverter<Notification>(), callback);
    }

    /**
     * Login to forum
     * NOTICE: SDK will NOT save these info, please use {@link Flarum#setToken(String)} or {@link Flarum#setToken(TokenGetter)}
     * pass it to sdk.
     * @param request Login request, username and password
     * @return Login response: token and uid
     */
    public Result<LoginResponse> login (LoginRequest request) throws FlarumException {
        Call call = apiInterface.login(request.getIdentification(),
                request.getPassword());
        return OkHttpUtils.execute(call, this, new LoginResponseConverter());
    }

    /**
     * Login to forum async
     * NOTICE: SDK will NOT save these info, please use {@link Flarum#setToken(String)} or {@link Flarum#setToken(TokenGetter)}
     * pass it to sdk.
     * @param request Login request, username and password
     * @param callback Callback
     * @return Call
     */
    public Call login (LoginRequest request, final Callback<LoginResponse> callback) {
        return OkHttpUtils.enqueue(apiInterface.login(request.getIdentification(), request.getPassword()),
                this, new LoginResponseConverter(), callback);
    }

    /**
     * Get forum tags async
     * @param page page
     * @param callback Callback
     * @return Call
     */
    public Call getTags (int page, Callback<List<Tag>> callback) {
        return OkHttpUtils.enqueue(apiInterface.tags(page), this,
                new ListConverter<Tag>(), callback);
    }

    /**
     * Get forum tags
     * @param page page
     * @return Tags list result
     */
    public Result<List<Tag>> getTags (int page) throws FlarumException {
        return OkHttpUtils.execute(apiInterface.tags(page), this,
                new ListConverter<Tag>());
    }

    /**
     * Get user groups async
     * @param page page
     * @param callback Callback
     * @return Call
     */
    public Call getGroups (int page, Callback<List<Group>> callback) {
        return OkHttpUtils.enqueue(apiInterface.groups(page), this,
                new ListConverter<Group>(), callback);
    }

    /**
     * Get user groups
     * @param page page
     * @return user groups list result
     */
    public Result<List<Group>> getGroups (int page) throws FlarumException {
        return OkHttpUtils.execute(apiInterface.groups(page), this,
                new ListConverter<Group>());
    }

    /**
     * Get all users, may requires admin permission
     * @param page page
     * @return User list result
     */
    public Result<List<User>> getUsers (int page) throws FlarumException {
        return getUsers(null, page);
    }

    /**
     * Get all users async, may requires admin permission
     * @param page page
     * @param callback Callback
     * @return Call
     */
    public Call getUsers (int page, Callback<List<User>> callback) {
        return getUsers(null, page, callback);
    }

    /**
     * Query users by username/gambits, may requires admin permission
     * @param query filter by username/gambits
     * @param page page
     * @return User list result
     */
    public Result<List<User>> getUsers (String query, int page) throws FlarumException {
        return OkHttpUtils.execute(apiInterface.users(page, query), this,
                new ListConverter<User>());
    }

    /**
     * Query users by username/gambits async, may requires admin permission
     * @param query filter by username/gambits
     * @param page page
     * @param callback Callback
     * @return Call
     */
    public Call getUsers (String query, int page, Callback<List<User>> callback) {
        return OkHttpUtils.enqueue(apiInterface.users(page, query), this,
                new ListConverter<User>(), callback);
    }

    /**
     * Register a new user
     * @param username Username
     * @param password Password
     * @param email Email
     * @return User object after register
     */
    public Result<User> registerUser (String username, String password, String email) throws FlarumException {
        return OkHttpUtils.execute(apiInterface.registerUser(username, password, email), this,
                new ItemConverter<User>());
    }

    /**
     * Register a new user async
     * @param username Username
     * @param password Password
     * @param email Email
     * @param callback Callback
     * @return Call
     */
    public Call registerUser (String username, String password, String email, Callback<User> callback) {
        return OkHttpUtils.enqueue(apiInterface.registerUser(username, password, email),
        this, new ItemConverter<User>(), callback);
    }

    /**
     * Delete a user, may requires admin permission
     * @param uid User id, {@link Result#id}
     */
    public void deleteUser (int uid) throws FlarumException {
        OkHttpUtils.execute(apiInterface.deleteUser(uid), this,
                null);
    }

    /**
     * Delete a user async, may requires admin permission
     * @param uid User id, {@link Result#id}
     * @param callback Callback
     * @return Call
     */
    public Call deleteUser (int uid, Callback callback) {
        return OkHttpUtils.enqueue(apiInterface.deleteUser(uid),
                this, null, callback);
    }

    /**
     * Add a new tag, may requires admin permission
     * @param name tag name
     * @param slug tag slug
     * @return Tag object if operation successfully executed
     */
    public Result<Tag> addTag (String name, String slug) throws FlarumException {
        return OkHttpUtils.execute(apiInterface.addTag(name, slug), this,
                new ItemConverter<Tag>());
    }

    /**
     * Add a new tag async, may requires admin permission
     * @param name tag name
     * @param slug tag slug
     * @return Call
     */
    public Call addTag (String name, String slug, Callback<Tag> callback) {
        return OkHttpUtils.enqueue(apiInterface.addTag(name, slug), this,
                new ItemConverter<Tag>(), callback);
    }

    /**
     * Delete a tag, may requires admin permission
     * @param id Tag id, see {@link Result#id}
     */
    public void deleteTag (int id) throws FlarumException {
        OkHttpUtils.execute(apiInterface.deleteTag(id), this,
                null);
    }

    /**
     * Delete a tag async, may requires admin permission
     * @param id Tag id, see {@link Result#id}
     * @param callback Callback
     * @return Call
     */
    public Call deleteTag (int id, Callback callback) {
        return OkHttpUtils.enqueue(apiInterface.deleteTag(id), this,
                null, callback);
    }

    /**
     * Get discussion list
     * @param page page
     * @return Discussion list result
     */
    public Result<List<Discussion>> getDiscussionList (int page) throws FlarumException {
        return getDiscussionList(null, page);
    }

    /**
     * Get discussion list async
     * @param page page
     * @param callback Callback
     * @return Call
     */
    public Call getDiscussionList (int page, Callback<List<Discussion>> callback) {
        return getDiscussionList(null, page, callback);
    }

    /**
     * Search user by uid or username
     * @param uidOrName UID or Username
     * @return User object
     */
    public Result<User> searchUserByIdOrName (String uidOrName) throws FlarumException {
        return OkHttpUtils.execute(apiInterface.getUserByIdOrName(uidOrName), this,
                new ItemConverter<User>());
    }

    /**
     * Search user by uid or username
     * @param uidOrName UID or Username
     * @param callback Callback
     * @return Call
     */
    public Call searchUserByIdOrName (String uidOrName, Callback<User> callback) {
        return OkHttpUtils.enqueue(apiInterface.getUserByIdOrName(uidOrName), this,
                new ItemConverter<User>(), callback);
    }

    /**
     * Get discussion list, filter by username/gambits
     * @param query filter by username/gambits
     * @param page page
     * @return Discussion list result
     */
    public Result<List<Discussion>> getDiscussionList (String query, int page) throws FlarumException {
        return OkHttpUtils.execute(apiInterface.discussions(query, page), this,
                new ListConverter<Discussion>());
    }

    /**
     * Get discussion list async, filter by username/gambits
     * @param query filter by username/gambits
     * @param page page
     * @param callback Callback
     * @return Call
     */
    public Call getDiscussionList (String query, int page, Callback<List<Discussion>> callback) {
        return OkHttpUtils.enqueue(apiInterface.discussions(query, page), this,
                new ListConverter<Discussion>(), callback);
    }

    /**
     * Create a new discussion
     * @param title Discussion title
     * @param content Discussion content
     * @return Discussion object
     */
    public Result<Discussion> createDiscussion (String title, String content) throws FlarumException {
        return OkHttpUtils.execute(apiInterface.createDiscussion(title, content), this,
                new ItemConverter<Discussion>());
    }

    /**
     * Create a new discussion
     * @param title Discussion title
     * @param content Discussion content
     * @param callback Callback
     * @return Call
     */
    public Call createDiscussion (String title, String content, Callback<Discussion> callback) {
        return OkHttpUtils.enqueue(apiInterface.createDiscussion(title, content), this,
                new ItemConverter<Discussion>(), callback);
    }

    /**
     * Delete a discussion
     * @param id Discussion id, {@link Result#id}
     */
    public void deleteDiscussion (int id) throws FlarumException {
        OkHttpUtils.execute(apiInterface.deleteDiscussion(id), this, null);
    }

    /**
     * Delete a discussion async
     * @param id Discussion id, {@link Result#id}
     * @param callback Callback
     * @return Call
     */
    public Call deleteDiscussion (int id, Callback callback) {
        return OkHttpUtils.enqueue(apiInterface.deleteDiscussion(id), this, null,
                callback);
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

        Call deleteUser (int uid) {
            return client.newCall(baseBuilder("users/" + uid, "DELETE",
                    createStringBody(""), null, 0).build());
        }

        Call addTag (String name, String slug) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("name", name);
            jsonObject.put("slug", slug);
            return client.newCall(baseBuilder("tags", "POST",
                    createJsonBody(createRequestJson(jsonObject).toString())
                    , null, 0).build());
        }

        Call deleteTag (int id) {
            return client.newCall(baseBuilder("tags/" + id, "DELETE",
                    createStringBody(""), null, 0).build());
        }

        Call discussions (@Nullable String query, int id) {
            return client.newCall(baseBuilder("discussions", "GET",
                    null, query != null ? new Query("q", query) :
                            null, id).build());
        }

        Call getUserByIdOrName (String query) {
            return client.newCall(baseBuilder("users/" + query
                    , "GET", null, null, 0)
            .build());
        }

        Call createDiscussion (String title, String content) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("title", title);
            jsonObject.put("content", content);
            return client.newCall(baseBuilder("discussions", "POST",
                    createJsonBody(createRequestJson(jsonObject).toString()), null, 0)
            .build());
        }

        Call deleteDiscussion (int id) {
            return client.newCall(baseBuilder("discussions/" + id, "DELETE",
                    createStringBody(""), null, 0).build());
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
