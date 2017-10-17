package top.trumeet.flarumsdk;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import org.junit.Before;
import org.junit.Test;
import top.trumeet.flarumsdk.data.Discussion;
import top.trumeet.flarumsdk.data.Forum;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by Trumeet on 2017/10/16.
 */
public class FlarumTest {
    private static final String BASE_URL = "https://bbs.letitfly.me";

    private Flarum flarum;
    private CountDownLatch asyncLatch;
    
    @Before
    public void setUp () {
        flarum = Flarum.create(BASE_URL);
        asyncLatch = new CountDownLatch(1);
    }
    
    @Test
    public void shouldCreateFlarum () {
        assertNotNull(flarum);
        Flarum flarum = Flarum.create(BASE_URL, new OkHttpClient());
        assertNotNull(flarum);
        assertEquals(flarum.getBaseUrl().toString(), BASE_URL);
        assertNotNull(flarum.getGson());
    }

    private void checkForumResult (Result<Forum> result) {
        assertNotNull(result);
        assertNotNull(result.mainAttr);
        assertNotNull(result.object);
        Forum forum = result
                .mainAttr;
        assertNotNull(forum.getTitle());
    }

    @Test
    public void shouldGetForumInfo () throws Exception {
        RequestBuilder<Forum> builder = flarum
                .getForumInfo();
        assertNotNull(builder);
        checkForumResult(builder.execute());
    }

    @Test
    public void shouldGetAllDiscussionList () throws FlarumException {
        assertNotNull(flarum
                .getDiscussionList()
                .execute().mainAttr);
    }

    @Test
    public void shouldGetAllDiscussionListWithPage () throws FlarumException {
        assertNotNull(flarum
                .getDiscussionList()
                .setPage(1)
                .execute().mainAttr);
    }

    @Test
    public void shouldSearchDiscussion () throws FlarumException {
        assertNotNull(flarum
                .getDiscussionList()
                .addFilter("q", "LetITFly")
                .execute().mainAttr);
    }

    @Test
    public void shouldGetDiscussionById  () throws FlarumException {
        Result<Discussion> result = flarum
                .getDiscussionById(59)
                .execute();
        Discussion discussion = result.mainAttr;
        assertNotNull(discussion);
        assertEquals(discussion.getTitle(), "如果喜欢，请随(duo)意(duo)捐赠");
        assertNotNull(result.object.getIncluded());
        assertNotNull(discussion.getRelationships());
    }

    @Test
    public void shouldRunAsync () throws InterruptedException {
        flarum.getForumInfo()
                .enqueue(new Callback<Forum>() {
                    @Override
                    public void onResponse(Call call, Result<Forum> result) {
                        checkForumResult(result);
                        asyncLatch.countDown();
                    }

                    @Override
                    public void onFailure(Call call, Throwable t) {
                        t.printStackTrace();
                        asyncLatch.countDown();
                    }
                });
        asyncLatch.await(flarum.getClient().connectTimeoutMillis()
                , TimeUnit.MILLISECONDS);
    }
}
