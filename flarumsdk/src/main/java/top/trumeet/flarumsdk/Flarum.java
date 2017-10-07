package top.trumeet.flarumsdk;

import android.net.Uri;
import okhttp3.*;
import org.json.JSONObject;
import top.trumeet.flarumsdk.data.*;
import top.trumeet.flarumsdk.internal.parser.converter.ItemConverter;
import top.trumeet.flarumsdk.internal.parser.converter.ListConverter;
import top.trumeet.flarumsdk.internal.parser.converter.LoginResponseConverter;
import top.trumeet.flarumsdk.internal.parser.jsonapi.JSONApiConverter;
import top.trumeet.flarumsdk.internal.platform.Platform;
import top.trumeet.flarumsdk.login.LoginResponse;

import javax.annotation.Nullable;
import java.util.List;
import java.util.concurrent.Executor;

@SuppressWarnings({"unused", "unchecked"})
public class Flarum {
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    public static final MediaType TEXT_PLAIN = MediaType.parse("text/plain; charset=utf-8");


    private Flarum (Uri baseUrl, API apiInterface) {
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
        Uri base = Uri.parse(baseUrl);
        if (base.getScheme() == null) {
            base = base.buildUpon()
                    .scheme("http")
                    .build();
        }
        Flarum flarum = new Flarum(base, new API(okHttpClient, baseUrl));
        flarum.converter = new JSONApiConverter(Forum.class
                , Notification.class,
                User.class,
                Tag.class,
                Link.class,
                Discussion.class,
                Group.class,
                Post.class);
        return flarum;
    }

    String getToken () {
        if (tokenGetter == null)
            return null;
        return tokenGetter.getToken();
    }

    Call makeCall (Request request) {
        return apiInterface.client.newCall(request);
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
    public Uri getBaseUrl() {
        return baseUrl;
    }

    /**
     * get information about the forum, including groups and tags
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
     * @param identification Username
     * @param password Password
     */
    public RequestBuilder<LoginResponse> login (String identification, String password) {
        JSONObject object = new JSONObject();
        object.put("identification", identification);
        object.put("password", password);
        return new RequestBuilder<>(new RequestBuilder.BaseRequest<>("token", "POST",
                createJsonBody(object.toString())
                , this, new LoginResponseConverter()));
    }

    /**
     * Get forum tags async
     * @return Tag list
     */
    public RequestBuilder<List<Tag>> getTags () {
        return new RequestBuilder<>(new RequestBuilder.BaseRequest<>("tags",
                "GET", null, this, new ListConverter<Tag>()));
    }

    /**
     * Get user groups
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
     * @return User list result
     */
    public RequestBuilder<List<User>> getUsers () {
        return new RequestBuilder<>(new RequestBuilder.BaseRequest<List<User>>("users",
                "GET", null, this, new ListConverter<User>()));
    }

    /**
     * Register a new user
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
     * @param uid User id, {@link Result#id}
     */
    public RequestBuilder deleteUser (int uid) {
        return new RequestBuilder(new RequestBuilder.BaseRequest("users/" + uid, "DELETE",
                createJsonBody(""), this, null));
    }

    /**
     * Add a new tag, may requires admin permission
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
     * @param id Discussion id, {@link Result#id}
     */
    public RequestBuilder deleteDiscussion (int id) {
        return new RequestBuilder(new RequestBuilder.BaseRequest("discussions/" + id,
                 "DELETE", createStringBody(""), this, null));
    }

    /**
     * Get a discussion by id
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

    private static class API {

        private final OkHttpClient client;
        private final String baseUrl;

        API(OkHttpClient client, String baseUrl) {
            this.client = client;
            this.baseUrl = baseUrl;
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

    private TokenGetter tokenGetter;

    public TokenGetter getTokenGetter() {
        return tokenGetter;
    }

    public void setTokenGetter(TokenGetter tokenGetter) {
        this.tokenGetter = tokenGetter;
    }

}
