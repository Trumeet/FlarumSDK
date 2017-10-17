package top.trumeet.flarumsdk.internal.parser.jsonapi;

import org.junit.Assert;
import org.junit.Test;
import top.trumeet.flarumsdk.Flarum;
import top.trumeet.flarumsdk.data.Data;
import top.trumeet.flarumsdk.data.Discussion;
import top.trumeet.flarumsdk.data.JSONApiObject;

/**
 * Created by Trumeet on 2017/10/16.
 */
public class ParserTest {
    private final String JSON = "{\n" +
            "    \"links\": {\n" +
            "        \"first\": \"https://bbs.letitfly.me/api/discussions\",\n" +
            "        \"prev\": \"https://bbs.letitfly.me/api/discussions?page%5Boffset%5D=205\"\n" +
            "    },\n" +
            "    \"data\": [\n" +
            "        {\n" +
            "            \"type\": \"discussions\",\n" +
            "            \"id\": \"9\",\n" +
            "            \"attributes\": {\n" +
            "                \"title\": \"阿里云 4.4.0\",\n" +
            "                \"slug\": \"4-4-0\",\n" +
            "                \"commentsCount\": 1,\n" +
            "                \"participantsCount\": 1,\n" +
            "                \"startTime\": \"2017-09-13T16:18:28+00:00\",\n" +
            "                \"lastTime\": \"2017-09-13T16:18:28+00:00\",\n" +
            "                \"lastPostNumber\": 1,\n" +
            "                \"canReply\": false,\n" +
            "                \"canRename\": false,\n" +
            "                \"canDelete\": false,\n" +
            "                \"canHide\": false,\n" +
            "                \"isApproved\": true,\n" +
            "                \"canSelectBestAnswer\": false,\n" +
            "                \"startUserId\": 3,\n" +
            "                \"canSplit\": false,\n" +
            "                \"isLocked\": false,\n" +
            "                \"canLock\": false,\n" +
            "                \"isSticky\": false,\n" +
            "                \"canSticky\": false,\n" +
            "                \"canTag\": false\n" +
            "            },\n" +
            "            \"relationships\": {\n" +
            "                \"startUser\": {\n" +
            "                    \"data\": {\n" +
            "                        \"type\": \"users\",\n" +
            "                        \"id\": \"3\"\n" +
            "                    }\n" +
            "                },\n" +
            "                \"lastUser\": {\n" +
            "                    \"data\": {\n" +
            "                        \"type\": \"users\",\n" +
            "                        \"id\": \"3\"\n" +
            "                    }\n" +
            "                },\n" +
            "                \"startPost\": {\n" +
            "                    \"data\": {\n" +
            "                        \"type\": \"posts\",\n" +
            "                        \"id\": \"43\"\n" +
            "                    }\n" +
            "                },\n" +
            "                \"tags\": {\n" +
            "                    \"data\": [\n" +
            "                        {\n" +
            "                            \"type\": \"tags\",\n" +
            "                            \"id\": \"3\"\n" +
            "                        },\n" +
            "                        {\n" +
            "                            \"type\": \"tags\",\n" +
            "                            \"id\": \"13\"\n" +
            "                        }\n" +
            "                    ]\n" +
            "                }\n" +
            "            }\n" +
            "        },\n" +
            "        {\n" +
            "            \"type\": \"discussions\",\n" +
            "            \"id\": \"2\",\n" +
            "            \"attributes\": {\n" +
            "                \"title\": \"(已完成)LetITFly BBS (The New MAT BBS) 正在进行最后的准备\",\n" +
            "                \"slug\": \"letitfly-bbs-the-new-mat-bbs\",\n" +
            "                \"commentsCount\": 10,\n" +
            "                \"participantsCount\": 7,\n" +
            "                \"startTime\": \"2017-09-10T06:35:08+00:00\",\n" +
            "                \"lastTime\": \"2017-09-13T15:06:14+00:00\",\n" +
            "                \"lastPostNumber\": 11,\n" +
            "                \"canReply\": false,\n" +
            "                \"canRename\": false,\n" +
            "                \"canDelete\": false,\n" +
            "                \"canHide\": false,\n" +
            "                \"isApproved\": true,\n" +
            "                \"canSelectBestAnswer\": false,\n" +
            "                \"startUserId\": 1,\n" +
            "                \"canSplit\": false,\n" +
            "                \"isLocked\": true,\n" +
            "                \"canLock\": false,\n" +
            "                \"isSticky\": false,\n" +
            "                \"canSticky\": false,\n" +
            "                \"canTag\": false\n" +
            "            },\n" +
            "            \"relationships\": {\n" +
            "                \"startUser\": {\n" +
            "                    \"data\": {\n" +
            "                        \"type\": \"users\",\n" +
            "                        \"id\": \"1\"\n" +
            "                    }\n" +
            "                },\n" +
            "                \"lastUser\": {\n" +
            "                    \"data\": {\n" +
            "                        \"type\": \"users\",\n" +
            "                        \"id\": \"1\"\n" +
            "                    }\n" +
            "                },\n" +
            "                \"startPost\": {\n" +
            "                    \"data\": {\n" +
            "                        \"type\": \"posts\",\n" +
            "                        \"id\": \"2\"\n" +
            "                    }\n" +
            "                },\n" +
            "                \"tags\": {\n" +
            "                    \"data\": [\n" +
            "                        {\n" +
            "                            \"type\": \"tags\",\n" +
            "                            \"id\": \"18\"\n" +
            "                        }\n" +
            "                    ]\n" +
            "                }\n" +
            "            }\n" +
            "        }\n" +
            "    ],\n" +
            "    \"included\": [\n" +
            "        {\n" +
            "            \"type\": \"users\",\n" +
            "            \"id\": \"3\",\n" +
            "            \"attributes\": {\n" +
            "                \"username\": \"neoFelhz\",\n" +
            "                \"avatarUrl\": \"https://bbs.letitfly.me/assets/avatars/wxkfspv8cgljvodi.png\"\n" +
            "            }\n" +
            "        },\n" +
            "        {\n" +
            "            \"type\": \"users\",\n" +
            "            \"id\": \"1\",\n" +
            "            \"attributes\": {\n" +
            "                \"username\": \"LetITFly\",\n" +
            "                \"avatarUrl\": \"https://bbs.letitfly.me/assets/avatars/xt2yf6k3bn4yzgzn.png\"\n" +
            "            }\n" +
            "        },\n" +
            "        {\n" +
            "            \"type\": \"posts\",\n" +
            "            \"id\": \"43\",\n" +
            "            \"attributes\": {\n" +
            "                \"id\": 43,\n" +
            "                \"number\": 1,\n" +
            "                \"time\": \"2017-09-13T16:18:28+00:00\",\n" +
            "                \"contentType\": \"comment\",\n" +
            "                \"contentHtml\": \"<p>禁用淘宝 agoo sdk，禁用推送，禁用所有推送。</p>\\n\\n<p><img src=\\\"https://imgproxy.nfz.yecdn.com/img/?url=https://i.loli.net/2017/09/14/59b956d8988cc.png\\\" alt=\\\"\\\"><br>\\n<img src=\\\"https://imgproxy.nfz.yecdn.com/img/?url=https://i.loli.net/2017/09/14/59b956e84f38b.png\\\" alt=\\\"\\\"></p>\\n\\n<p>Activity 和 Receiver 禁用方案如图所示。</p>\\n\\n<p>如果需要禁用 云栖 有关，在 Activity 中搜索 <code>yunqi</code> 并全部禁用即可。</p>\\n\\n<p><a href=\\\"https://nofile.io/f/NUs9H7fjysZ/20170913_AliCloud\\\" target=\\\"_blank\\\" rel=\\\"nofollow noreferrer\\\">单应用备份下载地址</a></p>\",\n" +
            "                \"isApproved\": true\n" +
            "            }\n" +
            "        },\n" +
            "        {\n" +
            "            \"type\": \"posts\",\n" +
            "            \"id\": \"2\",\n" +
            "            \"attributes\": {\n" +
            "                \"id\": 2,\n" +
            "                \"number\": 1,\n" +
            "                \"time\": \"2017-09-10T06:35:08+00:00\",\n" +
            "                \"contentType\": \"comment\",\n" +
            "                \"contentHtml\": \"<p>嗯，新站这就上线了……</p>\\n\\n<p>稍后我设置好 SMTP，各位就能够自助激活账户啦。</p>\",\n" +
            "                \"isApproved\": true\n" +
            "            }\n" +
            "        },\n" +
            "        {\n" +
            "            \"type\": \"tags\",\n" +
            "            \"id\": \"3\",\n" +
            "            \"attributes\": {\n" +
            "                \"name\": \"MAT(写轮眼)/IFW 调校心得\",\n" +
            "                \"description\": \"禁止在本板块发 求某软件的禁用方案 的帖子。求禁用方案，请去 MAT/IFW 问题答疑 板块。管理员如遇到发在本板块的求禁用帖，将直接删帖\",\n" +
            "                \"slug\": \"mat-ifw\",\n" +
            "                \"color\": \"#3F51B5\",\n" +
            "                \"backgroundUrl\": null,\n" +
            "                \"backgroundMode\": null,\n" +
            "                \"iconUrl\": null,\n" +
            "                \"discussionsCount\": 123,\n" +
            "                \"position\": 1,\n" +
            "                \"defaultSort\": null,\n" +
            "                \"isChild\": false,\n" +
            "                \"isHidden\": false,\n" +
            "                \"lastTime\": \"2017-10-16T05:18:40+00:00\",\n" +
            "                \"canStartDiscussion\": false,\n" +
            "                \"canAddToDiscussion\": false\n" +
            "            }\n" +
            "        },\n" +
            "        {\n" +
            "            \"type\": \"tags\",\n" +
            "            \"id\": \"13\",\n" +
            "            \"attributes\": {\n" +
            "                \"name\": \"禁用方案\",\n" +
            "                \"description\": \"求禁用方案的帖子请勿放到本分类！\",\n" +
            "                \"slug\": \"Disable_scheme\",\n" +
            "                \"color\": \"#616161\",\n" +
            "                \"backgroundUrl\": null,\n" +
            "                \"backgroundMode\": null,\n" +
            "                \"iconUrl\": null,\n" +
            "                \"discussionsCount\": 104,\n" +
            "                \"position\": null,\n" +
            "                \"defaultSort\": null,\n" +
            "                \"isChild\": false,\n" +
            "                \"isHidden\": false,\n" +
            "                \"lastTime\": \"2017-10-16T05:18:40+00:00\",\n" +
            "                \"canStartDiscussion\": false,\n" +
            "                \"canAddToDiscussion\": false\n" +
            "            }\n" +
            "        },\n" +
            "        {\n" +
            "            \"type\": \"tags\",\n" +
            "            \"id\": \"18\",\n" +
            "            \"attributes\": {\n" +
            "                \"name\": \"站点公告\",\n" +
            "                \"description\": \"\",\n" +
            "                \"slug\": \"site-announcement\",\n" +
            "                \"color\": \"#000000\",\n" +
            "                \"backgroundUrl\": null,\n" +
            "                \"backgroundMode\": null,\n" +
            "                \"iconUrl\": null,\n" +
            "                \"discussionsCount\": 13,\n" +
            "                \"position\": 0,\n" +
            "                \"defaultSort\": null,\n" +
            "                \"isChild\": false,\n" +
            "                \"isHidden\": false,\n" +
            "                \"lastTime\": \"2017-10-14T16:38:54+00:00\",\n" +
            "                \"canStartDiscussion\": false,\n" +
            "                \"canAddToDiscussion\": false\n" +
            "            }\n" +
            "        }\n" +
            "    ]\n" +
            "}";
    @Test
    public void testParseDiscussions () {
        JSONApiObject object = Flarum.create("")
                .convert(JSON);
        Assert.assertNotNull(object);
        //Assert.assertArrayEquals(object.getData().toArray(new Data[object.getData().size()]),
        //        new Data[]{new Data()});
        //System.out.println(object);
        Data data = object.getData(0);
        // Assert.assertNotNull(data);
        Assert.assertNotNull(data.getRelationships());
        Assert.assertEquals(data.getRelationships().size(), 4);
        Assert.assertEquals(data.getRelationships().containsKey("tags"), true);

        Data data2 = object.getData(1);
        // Assert.assertNotNull(data);
        Assert.assertEquals(((Discussion)data2).getSlug(), "letitfly-bbs-the-new-mat-bbs");

        Assert.assertNotNull(object.getIncluded());
        Assert.assertEquals(object.getIncluded().size(), 7);
        Assert.assertEquals(object.getIncluded().get(6).getType(), "tags");
    }
}
