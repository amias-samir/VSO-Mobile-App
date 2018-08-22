package np.com.naxa.vso.sudur.activities;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ProgressBar;

import np.com.naxa.vso.R;


public class AboutFWDCpdfDetailsWebActivity extends AppCompatActivity {

    private WebView webView;
    private ProgressBar progressBar;
    private String postUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_fwdcpdf_details_web);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("बिकाश गतिबिधि");
        setSupportActionBar(toolbar);

//        "http://docs.google.com/gview?embedded=true&url="+
        Intent i = getIntent();
        postUrl = i.getStringExtra("url");

        toolbar.setTitleTextColor(getResources().getColor(R.color.accent));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
//        getSupportActionBar().setDisplayShowTitleEnabled(false);
        final Drawable upArrow = getResources().getDrawable(R.mipmap.ic_back_icon);
        upArrow.setColorFilter(getResources().getColor(R.color.accent), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);

        webView = (WebView) findViewById(R.id.webView);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setPluginState(WebSettings.PluginState.ON);
        webView.loadUrl(postUrl);

        Log.e("WEB_ACTIVITY", "onCreate: "+ postUrl );
        webView.setHorizontalScrollBarEnabled(false);

        progressBar.setEnabled(false);

    }
}

