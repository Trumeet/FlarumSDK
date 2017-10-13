package top.trumeet.flarumsdk.internal.parser.converter;

import top.trumeet.flarumsdk.Flarum;
import top.trumeet.flarumsdk.data.JSONApiObject;
import top.trumeet.flarumsdk.internal.parser.ObjectParser;
import top.trumeet.flarumsdk.login.LoginResponse;

/**
 * Created by Trumeet on 2017/9/30.
 */

public class LoginResponseConverter implements ObjectParser.JsonObjectConverter<LoginResponse> {
    @Override
    public LoginResponse convert(JSONApiObject object, String responseString, Flarum apiManager) {
        try {
            return apiManager.getGson()
                    .fromJson(responseString, LoginResponse.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
