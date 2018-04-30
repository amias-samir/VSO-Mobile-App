package np.com.naxa.vso.home;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;

import com.mancj.slideup.SlideUp;
import com.mancj.slideup.SlideUpBuilder;

import butterknife.BindView;
import butterknife.ButterKnife;
import np.com.naxa.vso.R;

public class HomeActivityNew extends AppCompatActivity {
    private SlideUp slideUp;

    @BindView(R.id.dim)
    public View dim;

    @BindView(R.id.slideView)
    public View sliderView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_new);
        ButterKnife.bind(this);
        setupSlider();

        slideUp.show();
    }

    private void setupSlider() {
        slideUp = new SlideUpBuilder(sliderView)
                .withStartGravity(Gravity.BOTTOM)
                .withLoggingEnabled(true)
                .withGesturesEnabled(true)
                .withStartState(SlideUp.State.HIDDEN)
                .withSlideFromOtherView(findViewById(R.id.rootView))
                .build();
    }


}
