package np.com.naxa.vso;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;


public class ReportActivity extends AppCompatActivity implements LocationListener {
    ImageButton vCamera_Icon;
    EditText vName;
    EditText vMessage;
    EditText vContactNumber;
    ImageButton vShare;
    ImageButton vSave;
    LocationManager DcoLocationManager;
    //    LocationTracker IcLocationTracker;
    double latitude;
    double longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        vName = findViewById(R.id.editText);
        vMessage = findViewById(R.id.message);
        vContactNumber = findViewById(R.id.editText4);
        vCamera_Icon = findViewById(R.id.camera_icon);
        vShare = findViewById(R.id.imageButton);
        vSave = findViewById(R.id.imageButton2);
//        IcLocationTracker = new LocationTracker(getApplicationContext());
        DcoLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        // DcoLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, ReportActivity);

        vCamera_Icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                startActivityForResult(intent, 0);
            }
        });
        vShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = vName.getText().toString();
                String message = vMessage.getText().toString();
                String contact = vMessage.getText().toString();
//                latitude = IcLocationTracker.getLatitude();
//                longitude = IcLocationTracker.getLongitude();
                Toast.makeText(getApplicationContext(), latitude + "/n" + longitude, Toast.LENGTH_SHORT).show();
            }
        });
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
}
