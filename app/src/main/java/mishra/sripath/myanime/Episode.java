package mishra.sripath.myanime;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.customtabs.CustomTabsIntent;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

public class Episode extends AppCompatActivity {
    private class HelloWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            return false;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_episode);
        final Intent intent = getIntent();
        String url=intent.getStringExtra("Link");
        Ion.with(getApplicationContext()).load(url).asString().setCallback(new FutureCallback<String>() {
            @Override
            public void onCompleted(Exception e, String result) {
                if(result.contains("Download")==false)
                {
                    TextView t=(TextView)findViewById(R.id.textView4);
                    t.setText("Due to the anime being old The link has died Please wait this link is reuploaded. Please try other anime till then.");
                }
                else{
                    final String res[]=result.split("<div class=\"download_feed_link\" ><a class=");
                    res[1]=res[1].substring(res[1].indexOf('=')+2);
                    String url1=res[1].substring(0,res[1].indexOf('"'));
                    Ion.with(getApplicationContext()).load(url1).asString().setCallback(new FutureCallback<String>() {
                        @Override
                        public void onCompleted(Exception e, String result) {
                            String res1[]=result.split("IFRAME SRC=\"");
                            String url2=res1[1].substring(0,res1[1].indexOf('"'));
                            CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
                            CustomTabsIntent customTabsIntent = builder.build();
                            customTabsIntent.launchUrl(Episode.this, Uri.parse(url2));
                        }
                    });
                }
            }

        });
    }
}
