package np.com.naxa.vso;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import np.com.naxa.vso.home.HomeActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spash);

        new Handler().postDelayed(() -> {
            finish();
            HomeActivity.start(SplashActivity.this);
        }, 2000);
    }
}
