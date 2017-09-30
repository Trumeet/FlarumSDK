package top.trumeet.flarumsdk.internal.parser.converter;

import org.json.JSONException;

import top.trumeet.flarumsdk.internal.parser.ObjectParser;
import top.trumeet.flarumsdk.internal.parser.jsonapi.Models.JSONApiObject;
import top.trumeet.flarumsdk.login.LoginResponse;

/**
 * Created by Trumeet on 2017/9/30.
 */

public class LoginResponseConverter implements ObjectParser.JsonObjectConverter<LoginResponse> {
    @Override
    public LoginResponse convert(JSONApiObject object, String responseString) {
        try {
            return LoginResponse.createFromJson(responseString);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
