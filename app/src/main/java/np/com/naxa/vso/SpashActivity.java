package np.com.naxa.vso;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SpashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
                HomeActivity.start(SpashActivity.this);
            }
        }, 2000);
    }
}
