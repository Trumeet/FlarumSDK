package top.trumeet.flarumsdk;

import java.io.IOException;
import java.util.concurrent.Executor;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import top.trumeet.flarumsdk.data.Discussion;
import top.trumeet.flarumsdk.data.Forum;
import top.trumeet.flarumsdk.data.Group;
import top.trumeet.flarumsdk.data.Link;
import top.trumeet.flarumsdk.data.Notification;
import top.trumeet.flarumsdk.data.Tag;
import top.trumeet.flarumsdk.data.User;
import top.trumeet.flarumsdk.internal.parser.OkHttpUtils;
import top.trumeet.flarumsdk.internal.parser.converter.ForumConverter;
import top.trumeet.flarumsdk.internal.parser.jsonapi.JSONApiConverter;
import top.trumeet.flarumsdk.internal.platform.Platform;

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
                new ForumConverter());
    }

    public Call getForumInfo (Callback<Forum> callback) {
        return OkHttpUtils.enqueue(apiInterface.forumInfo(), this,
                new ForumConverter(), callback);
    }

    private static class API {
        private final OkHttpClient client;
        private final String baseUrl;

        API(OkHttpClient client, String baseUrl) {
            this.client = client;
            this.baseUrl = baseUrl + "/api/";
        }

        Call forumInfo () {
            return client.newCall(new Request.Builder()
            .url(baseUrl + "forum")
            .build());
        }
    }
}
