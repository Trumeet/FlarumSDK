package top.trumeet.flarumsdk;

import android.net.Uri;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import okhttp3.*;
import org.json.JSONObject;
import top.trumeet.flarumsdk.data.*;
import top.trumeet.flarumsdk.internal.parser.ContentParser;
import top.trumeet.flarumsdk.internal.parser.converter.ItemConverter;
import top.trumeet.flarumsdk.internal.parser.converter.ListConverter;
import top.trumeet.flarumsdk.internal.parser.converter.LoginResponseConverter;
import top.trumeet.flarumsdk.internal.parser.jsonapi.Parser;
import top.trumeet.flarumsdk.internal.platform.Platform;
import top.trumeet.flarumsdk.login.LoginRequest;
import top.trumeet.flarumsdk.login.LoginResponse;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;

@SuppressWarnings({"unused", "unchecked"})
public class Flarum {
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    public static final MediaType TEXT_PLAIN = MediaType.parse("text/plain; charset=utf-8");


    private Flarum (Uri baseUrl, OkHttpClient client) {
        this.baseUrl = baseUrl;
        this.client = client;
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

        Map<String, Class> mapping = new HashMap<>(1);
        mapping.put("forums", Forum.class);
        mapping.put("discussions", Discussion.class);
        mapping.put("groups", Group.class);
        mapping.put("tags", Tag.class);
        mapping.put("links", Link.class);
        mapping.put("notifications", Notification.class);
        mapping.put("posts", Post.class);
        mapping.put("users", User.class);

        gson = new GsonBuilder()
                .registerTypeAdapter(JSONApiObject.class, new Parser(mapping))
                .registerTypeAdapter(Content.class, new ContentParser())
                .create();
    }

    /**
     * Create API client
     * @param baseUrl Forum base url, e.g: discuss.flarum.org, NOT INCLUDING scheme
     * @return Flarum API client
     */
    public static Flarum create (String baseUrl) {
        return create(baseUrl, new OkHttpClient());
    }

    /**
     * Create API client with custom {@link OkHttpClient}
     * @param baseUrl Forum base url, e.g: discuss.flarum.org, NOT INCLUDING scheme
     * @param okHttpClient Custom http client
     * @return Flarum API client
     */
    public static Flarum create (String baseUrl, OkHttpClient okHttpClient) {
        Uri base = Uri.parse(baseUrl);
        if (base.getScheme() == null) {
            base = base.buildUpon()
                    .scheme("http")
                    .build();
        }
        Flarum flarum = new Flarum(base, okHttpClient);
        return flarum;
    }

    String getToken () {
        if (tokenGetter == null)
            return null;
        return tokenGetter.getToken();
    }

    Call makeCall (Request request) {
        return client.newCall(request);
    }

    /**
     * Set auth token, it will override {@link Flarum#setToken(String)}.
     * @param getter dynamic interface
     */
    public void setToken (TokenGetter getter) {
        setTokenGetter(getter);
    }

    /**
     * Set auth token string, it will override {@link Flarum#setToken(TokenGetter)}
     * @param token token string
     */
    public void setToken (@Nullable final String token) {
        setTokenGetter(new TokenGetter() {
            @Nullable
            @Override
            public String getToken() {
                return token;
            }
        });
    }

    private final Uri baseUrl;
    private final OkHttpClient client;
    private final Executor platformExecutor;
    private final Gson gson;

    /**
     * Internal api, deserialize json to object
     * @return Parsed object
     */
    public JSONApiObject convert (String rawJson) {
        return gson.fromJson(rawJson, JSONApiObject.class);
    }

    /**
     * Internal api, get {@link com.google.gson.Gson} client
     * @return Gson
     */
    @Nonnull
    public Gson getGson () {
        return gson;
    }

    /**
     * Internal api, get {@link OkHttpClient} when create
     * @return client
     */
    public OkHttpClient getClient () {
        return client;
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
    public Uri getBaseUrl() {
        return baseUrl;
    }

    /**
     * get information about the forum, including groups and tags
     * Available filters: none
     * Paging: not support
     * @return Forum info
     */
    public RequestBuilder<Forum> getForumInfo () {
        return new RequestBuilder<>(new RequestBuilder.BaseRequest<>("forum", "GET", null, this,
                new ItemConverter<Forum>()));
    }

    /**
     * get all notifications
     * Available filters: none
     * Paging: support
     * @return notifications list
     */
    public RequestBuilder<List<Notification>> getMessageList () {
        return new RequestBuilder<>(new RequestBuilder.BaseRequest<>("notifications",
                "GET", null, this, new ListConverter<Notification>()));
    }

    /**
     * mark a notification as read
     * Available filters: none
     * Paging: not support
     * @param id Notification id, {@link Result#id}
     * @return notification object
     */
    public RequestBuilder<Notification> markNotificationAsRead (int id) {
        return new RequestBuilder<>(new RequestBuilder.BaseRequest<>("notifications/" + id,
                "PATCH", createJsonBody(""), this, new ItemConverter<Notification>()));
    }

    /**
     * Login to forum
     * NOTICE: SDK will NOT save these info, please use {@link Flarum#setToken(String)} or {@link Flarum#setToken(TokenGetter)}
     * pass it to sdk.
     * Available filters: none
     * Paging: not support
     * @param identification Username
     * @param password Password
     */
    public RequestBuilder<LoginResponse> login (String identification, String password) {
        return new RequestBuilder<>(new RequestBuilder.BaseRequest<>("token", "POST",
                createJsonBody(gson.toJson(new LoginRequest(password, identification)))
                , this, new LoginResponseConverter()));
    }

    /**
     * Get forum tags async
     * Available filters: none
     * Paging: support
     * @return Tag list
     */
    public RequestBuilder<List<Tag>> getTags () {
        return new RequestBuilder<>(new RequestBuilder.BaseRequest<>("tags",
                "GET", null, this, new ListConverter<Tag>()));
    }

    /**
     * Get user groups
     * Available filters: none
     * Paging: support
     * @return user groups list result
     */
    public RequestBuilder<List<Group>> getGroups () {
        return new RequestBuilder<>(new RequestBuilder.BaseRequest<List<Group>>("groups",
                "GET", null, this, new ListConverter<Group>()));
    }

    /**
     * Query users by username/gambits, may requires admin permission
     * Available filters:
     * q - filter by username/gambits
     * Paging: not support
     * @return User list result
     */
    public RequestBuilder<List<User>> getUsers () {
        return new RequestBuilder<>(new RequestBuilder.BaseRequest<List<User>>("users",
                "GET", null, this, new ListConverter<User>()));
    }

    /**
     * Register a new user
     * Available filters: none
     * Paging: not support
     * @param username Username
     * @param password Password
     * @param email Email
     * @return User object after register
     */
    public RequestBuilder<User> registerUser (String username, String password, String email) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("username", username);
        jsonObject.put("password", password);
        jsonObject.put("email", email);
        return new RequestBuilder<>(new RequestBuilder.BaseRequest<User>("users", "POST",
                createJsonBody(createRequestJson(jsonObject).toString()), this,
                new ItemConverter<User>()));
    }

    /**
     * Delete a user, may requires admin permission
     * Available filters: none
     * Paging: not support
     * @param uid User id, {@link Result#id}
     */
    public RequestBuilder deleteUser (int uid) {
        return new RequestBuilder(new RequestBuilder.BaseRequest("users/" + uid, "DELETE",
                createJsonBody(""), this, null));
    }

    /**
     * Add a new tag, may requires admin permission
     * Available filters: none
     * Paging: not support
     * @param name tag name
     * @param slug tag slug
     * @return Tag object if operation successfully executed
     */
    public RequestBuilder<Tag> addTag (String name, String slug) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", name);
        jsonObject.put("slug", slug);
        return new RequestBuilder<>(new RequestBuilder.BaseRequest<>("tags",
                "POST", createJsonBody(createRequestJson(jsonObject).toString()), this,
                new ItemConverter<Tag>()));
    }

    /**
     * Delete a tag, may requires admin permission
     * Available filters: none
     * Paging: not support
     * @param id Tag id, see {@link Result#id}
     */
    public RequestBuilder deleteTag (int id) {
        return new RequestBuilder(new RequestBuilder.BaseRequest("tags/" + id, "DELETE",
                createStringBody(""), null, null));
    }

    /**
     * Search user by uid or username
     * Available filters: none
     * Paging support: false
     * @param uidOrName UID or Username
     * @return User object
     */
    public RequestBuilder<User> searchUserByIdOrName (String uidOrName) {
        return new RequestBuilder<>(new RequestBuilder.BaseRequest<>("users/" + uidOrName,
                "GET", null, this, new ItemConverter<User>()));
    }

    /**
     * Get discussion list
     * Available filters:
     * q - filter by username/gambits
     * Paging: support
     * @return Discussion list result
     */
    public RequestBuilder<List<Discussion>> getDiscussionList () {
        return new RequestBuilder<>(new RequestBuilder.BaseRequest<List<Discussion>>("discussions",
                "GET", null, this, new ListConverter<Discussion>()));
    }

    /**
     * Create a new discussion
     * Available filters: none
     * Paging: not support
     * @param title Discussion title
     * @param content Discussion content
     * @return Discussion object
     */
    public RequestBuilder<Discussion> createDiscussion (String title, String content) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("title", title);
        jsonObject.put("content", content);
        return new RequestBuilder<>(new RequestBuilder.BaseRequest<Discussion>("discussions", "POST",
                createJsonBody(createRequestJson(jsonObject).toString()), this,
                new ItemConverter<Discussion>()));
    }

    /**
     * Delete a discussion
     * Available filters: none
     * Paging: not support
     * @param id Discussion id, {@link Result#id}
     */
    public RequestBuilder deleteDiscussion (int id) {
        return new RequestBuilder(new RequestBuilder.BaseRequest("discussions/" + id,
                 "DELETE", createStringBody(""), this, null));
    }

    /**
     * Get a discussion by id
     * Available filters: none
     * Paging: not support
     * @param id Discussion id, {@link Result#id}
     */
    public RequestBuilder<Discussion> getDiscussionById (int id) {
        return new RequestBuilder<>(new RequestBuilder.BaseRequest<>("discussions/" + id,
                "GET", null, this, new ItemConverter<Discussion>()));
    }

    /**
     * Get all posts (reply).
     * Available filters:
     * discussion - filter by discussion ID
     * user - filter by user ID
     * number - filter by number (position within the discussion)
     * type - filter by post type
     * Paging: support
     * @return Post list
     */
    public RequestBuilder<List<Post>> getAllPosts () {
        return new RequestBuilder<>(new RequestBuilder.BaseRequest<>("posts", "GET",
                null, this, new ListConverter<Post>()));
    }

    /**
     * Delete a user avatar
     * Available filters: none
     * Paging: not support
     * @param uid User {@link Result#id}
     */
    public RequestBuilder<User> deleteUserAvatar (int uid) {
        return new RequestBuilder<>(new RequestBuilder.BaseRequest<>("users/" + uid + "/avatar",
                "DELETE", createStringBody(""), this, new ItemConverter<User>()));
    }

    /**
     * Delete a post (reply)
     * Available filters: none
     * Paging: not support
     * @param id Post {@link Result#id}
     */
    public RequestBuilder deletePost (int id) {
        return new RequestBuilder(new RequestBuilder.BaseRequest("posts/" + id, "DELETE",
                createStringBody(""), this, null));
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

    private TokenGetter tokenGetter;

    public TokenGetter getTokenGetter() {
        return tokenGetter;
    }

    public void setTokenGetter(TokenGetter tokenGetter) {
        this.tokenGetter = tokenGetter;
    }

}
