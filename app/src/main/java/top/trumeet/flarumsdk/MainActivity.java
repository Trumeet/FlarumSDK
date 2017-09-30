package top.trumeet.flarumsdk;

import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import okhttp3.Call;
import top.trumeet.flarumsdk.data.Forum;

public class MainActivity extends Activity {
    private Flarum flarum;
    private TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = findViewById(android.R.id.text1);
        flarum = Flarum.create("https://bbs.letitfly.me");
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
        menu.add(0, 0, 0, "Actions")
        .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected (MenuItem item) {
        return true;
    }

    private void append (CharSequence charSequence) {
        textView.append(charSequence);
    }

    private void appendInfo (CharSequence charSequence) {
        append(charSequence);
    }

    private void appendError (CharSequence charSequence) {
        append(Html.fromHtml("<font color=\'red\'>" + charSequence + "</font>"));
    }
}
