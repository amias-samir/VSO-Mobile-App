package np.com.naxa.vso;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

//import com.naxa.changunaryanapp.changunaryanmunicipality.R;
//import com.naxa.changunaryanapp.changunaryanmunicipality.Utils.LocationTracker;


public class ReportActivity extends AppCompatActivity implements LocationListener {
    Button vCamera;
    TextInputLayout vName;
    TextInputLayout vMessage;
    TextInputLayout vContactNumber;
    Button vSubmit;
    Button vSave;
    LocationManager DcoLocationManager;
    Toolbar toolbar;

    double latitude;
    double longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_v2);

        initUI();

        initToolbar();

//        IcLocationTracker = new LocationTracker(getApplicationContext());
        DcoLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        // DcoLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, ReportActivity);

        vCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                startActivityForResult(intent, 0);
            }
        });
        vSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = vName.getEditText().getText().toString();
                String message = vMessage.getEditText().getText().toString();
                String contact = vMessage.getEditText().getText().toString();
//              latitude = IcLocationTracker.getLatitude();
//              longitude = IcLocationTracker.getLongitude();
                Toast.makeText(getApplicationContext(), latitude + "/n" + longitude, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initToolbar() {
        toolbar = findViewById(R.id.toolbar_general);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Ask a question");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    private void initUI() {
        vName = findViewById(R.id.editText);
        vMessage = findViewById(R.id.message);
        vContactNumber = findViewById(R.id.editText4);
        vCamera = findViewById(R.id.camera_icon);
        vSubmit = findViewById(R.id.imageButton);
//        vSave = findViewById(R.id.imageButton2);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK && requestCode == 0) {
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
        }
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    public static void start(Context context) {
        Intent intent = new Intent(context, ReportActivity.class);
        context.startActivity(intent);
    }
}
