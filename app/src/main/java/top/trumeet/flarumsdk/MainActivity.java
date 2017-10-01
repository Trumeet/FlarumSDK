package top.trumeet.flarumsdk;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;

import okhttp3.Call;
import top.trumeet.flarumsdk.data.Forum;
import top.trumeet.flarumsdk.data.Notification;
import top.trumeet.flarumsdk.internal.parser.jsonapi.Models.ErrorModel;
import top.trumeet.flarumsdk.internal.parser.jsonapi.Models.JSONApiObject;
import top.trumeet.flarumsdk.login.LoginRequest;
import top.trumeet.flarumsdk.login.LoginResponse;

public class MainActivity extends Activity {
    // Change it to your target forum url
    private static final String BASE_URL = "https://bbs.letitfly.me";

    private Flarum flarum;
    private TextView textView;
    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = findViewById(android.R.id.text1);
        flarum = Flarum.create(BASE_URL);
        flarum.setToken(new Flarum.TokenGetter() {
            @Override
            public String getToken() {
                return token;
            }
        });
        flarum.getForumInfo(new Callback<Forum>() {
            @Override
            public void onResponse(Call call, Result<Forum> result) {
                System.out.println(Thread.currentThread().toString());
                appendInfo("Forum Info: " + result.mainAttr.getTitle());
            }

            @Override
            public void onFailure(Call call, Throwable e) {
                e.printStackTrace();
                appendError(e.getMessage());
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu (Menu menu) {
        menu.add(0, 0, 0, "Login");
        menu.add(0, 1, 0, "Message Box");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected (MenuItem item) {
        switch (item.getItemId()) {
            case 0 :
                login();
                return true;
            case 1 :
                notifications();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void notifications () {
        appendInfo("Getting notifications using token " + token);
        flarum.getMessageList(new Callback<List<Notification>>() {
            @Override
            public void onResponse(Call call, Result<List<Notification>> result) {
                if (result.mainAttr == null) {
                    appendErrors(result.object);
                } else {
                    appendInfo("Notifications: ");
                    for (Notification notification : result.mainAttr) {
                        appendInfo(notification.toString());
                    }
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                appendError(t.toString());
            }
        });
    }

    private void append (CharSequence charSequence) {
        textView.append(charSequence);
        textView.append("\n");
    }

    private void appendInfo (CharSequence charSequence) {
        append(charSequence);
    }

    private void appendError (CharSequence charSequence) {
        append(Html.fromHtml("<font color=\'red\'>" + charSequence + "</font>"));
    }

    private void appendErrors (JSONApiObject object) {
        appendError("Fail!");
        if (object.getErrors() != null) {
            for (ErrorModel errorModel : object.getErrors()) {
                appendError(errorModel.getStatus() + ", " + errorModel.getCode());
            }
        }
    }

    private void login () {
        View view = LayoutInflater.from(this)
                .inflate(R.layout.dialog_login, null);
        TextView textView = view.findViewById(android.R.id.text1);
        textView.setText(Html.fromHtml("Login to <b>" + BASE_URL + "</b>, AFTER LOGIN, YOUR OLD TOKEN WILL EXPIRED!"));
        final EditText username = view.findViewById(R.id.username);
        final EditText password = view.findViewById(R.id.password);
        Button ok = view.findViewById(R.id.button_ok);
        final AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(view)
                .setTitle("Login")
                .create();
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                appendInfo(Html.fromHtml("Logging in to <b>" + BASE_URL + "</b>, username is: " + username.getText().toString()));
                dialog.dismiss();
                flarum.login(new LoginRequest(password.getText().toString(),
                                username.getText().toString()),
                        new Callback<LoginResponse>() {
                            @Override
                            public void onResponse(Call call, Result<LoginResponse> result) {
                                if (result.mainAttr == null) {
                                    appendErrors(result.object);
                                    return;
                                }
                                appendInfo("Success! Token is " + result.mainAttr.getToken() + ", uid is " + result.mainAttr.getUserId());
                                token = result.mainAttr.getToken();
                            }

                            @Override
                            public void onFailure(Call call, Throwable t) {
                                appendError("Fail! " + t.toString());
                            }
                        });
            }
        });
        dialog.show();
    }
}
