//<<<<<<< HEAD
////package np.com.naxa.vso;
////
////import android.Manifest;
////import android.content.SharedPreferences;
////import android.content.pm.PackageManager;
////import android.graphics.Bitmap;
////import android.graphics.drawable.BitmapDrawable;
////import android.graphics.drawable.Drawable;
////import android.os.Bundle;
////import android.preference.PreferenceManager;
////import android.support.design.widget.FloatingActionButton;
////import android.support.v4.app.ActivityCompat;
////import android.support.v7.app.AppCompatActivity;
////import android.util.Log;
////import android.view.View;
////
////import org.osmdroid.api.IMapController;
////import org.osmdroid.bonuspack.kml.KmlDocument;
////import org.osmdroid.bonuspack.kml.Style;
////import org.osmdroid.config.Configuration;
////import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
////import org.osmdroid.util.BoundingBox;
////import org.osmdroid.util.GeoPoint;
////import org.osmdroid.views.MapView;
////import org.osmdroid.views.overlay.FolderOverlay;
////import org.osmdroid.views.overlay.Marker;
////import org.osmdroid.views.overlay.Polyline;
////
////import java.io.IOException;
////import java.io.InputStream;
////
////import butterknife.BindView;
////import butterknife.ButterKnife;
////import butterknife.OnClick;
////
////public class MainActivity extends AppCompatActivity {
////
////    private static final String TAG = "MainActivity";
////
////    @BindView(R.id.map)
////    MapView vMapView;
////    @BindView(R.id.floating_school)
////    FloatingActionButton floatingSchool;
////    @BindView(R.id.floating_openspaces)
////    FloatingActionButton floatingOpenspaces;
////    @BindView(R.id.floating_hospital)
////    FloatingActionButton floatingHospital;
////
////    FolderOverlay myOverLay;
////    Polyline polylineLayer;
////    IMapController mapController;
////    GeoPoint centerPoint;
////    int mapZoomLevel = 12;
////
////    @Override
////    protected void onCreate(Bundle savedInstanceState) {
////        super.onCreate(savedInstanceState);
////        setContentView(R.layout.activity_main);
////        ButterKnife.bind(this);
////
////        checkPermission();
////
////        initMap();
////
////        loadMunicipalityBoarder();
////
////        polylineLayer = new Polyline();
////    }
////
////
////
////    public void initMap(){
////
////        myOverLay = new FolderOverlay();
////
////        vMapView.setTileSource(TileSourceFactory.MAPNIK);
////
////        vMapView.setBuiltInZoomControls(true);
////        vMapView.setMultiTouchControls(true);
////        mapController = vMapView.getController();
////        mapController.setZoom((int)mapZoomLevel);
////        centerPoint = new GeoPoint(27.696268407446215, 85.462727834610064);
////        mapController.setCenter(centerPoint);
////
////        Marker startMarker = new Marker(vMapView);
////        startMarker.setPosition(centerPoint);
////        startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
////        vMapView.getOverlays().add(startMarker);
////
////        startMarker.setIcon(getResources().getDrawable(R.drawable.marker_default));
////        startMarker.setTitle("Chagunayayan Nagarpalika");
////
////    }
////
////    public void onResume(){
////        super.onResume();
////        //this will refresh the osmdroid configuration on resuming.
////        //if you make changes to the configuration, use
////        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
////        Configuration.getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this));
//////        vMapView.onResume(); //needed for compass, my location overlays, v6.0.0 and up
////    }
////
////    public void onPause(){
////        super.onPause();
////        //this will refresh the osmdroid configuration on resuming.
////        //if you make changes to the configuration, use
////        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
////        Configuration.getInstance().save(this, prefs);
////       // vMapView.onPause();  //needed for compass, my location overlays, v6.0.0 and up
////    }
////
////
////
////    @OnClick({R.id.floating_school, R.id.floating_openspaces, R.id.floating_hospital})
////    public void onViewClicked(View view) {
////        switch (view.getId()) {
////            case R.id.floating_school:
////
////                loadMunicipalityBoarder();
////                addAdditionalLayerSchool();
////                break;
////            case R.id.floating_openspaces:
////                loadMunicipalityBoarder();
////                addAdditionalLayerOpenSpace();
////                break;
////            case R.id.floating_hospital:
////                loadMunicipalityBoarder();
////                addAdditionalLayerHospital();
////                break;
////        }
////    }
////
////    public void checkPermission() {
////        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
////                || ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
////                || ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
////                || ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED
////                || ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
////                ) {
////            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
////            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 2);
////            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
////            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.INTERNET}, 4);
////            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 5);
////        }
////    }
////
////
////    private void loadMunicipalityBoarder() {
//=======
//package np.com.naxa.vso;
//
//import android.Manifest;
//import android.content.SharedPreferences;
//import android.content.pm.PackageManager;
//import android.graphics.Bitmap;
//import android.graphics.drawable.BitmapDrawable;
//import android.graphics.drawable.Drawable;
//import android.os.Bundle;
//import android.preference.PreferenceManager;
//import android.support.design.widget.FloatingActionButton;
//import android.support.v4.app.ActivityCompat;
//import android.support.v7.app.AppCompatActivity;
//import android.util.Log;
//import android.view.View;
//
//import org.osmdroid.api.IMapController;
//import org.osmdroid.bonuspack.kml.KmlDocument;
//import org.osmdroid.bonuspack.kml.Style;
//import org.osmdroid.config.Configuration;
//import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
//import org.osmdroid.util.BoundingBox;
//import org.osmdroid.util.GeoPoint;
//import org.osmdroid.views.MapView;
//import org.osmdroid.views.overlay.FolderOverlay;
//import org.osmdroid.views.overlay.Marker;
//import org.osmdroid.views.overlay.Polyline;
//
//import java.io.IOException;
//import java.io.InputStream;
//
//import butterknife.BindView;
//import butterknife.ButterKnife;
//import butterknife.OnClick;
//
//public class MainActivity extends AppCompatActivity {
//
//    private static final String TAG = "MainActivity";
//
//    @BindView(R.id.map)
//    MapView vMapView;
//    @BindView(R.id.floating_school)
//    FloatingActionButton floatingSchool;
//    @BindView(R.id.floating_openspaces)
//    FloatingActionButton floatingOpenspaces;
//    @BindView(R.id.floating_hospital)
//    FloatingActionButton floatingHospital;
//
//    FolderOverlay myOverLay;
//    Polyline polylineLayer;
//    IMapController mapController;
//    GeoPoint centerPoint;
//    int mapZoomLevel = 12;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//        ButterKnife.bind(this);
//
//        checkPermission();
//
//        initMap();
//
//        loadMunicipalityBoarder();
//
//        polylineLayer = new Polyline();
//    }
//
//
//
//    public void initMap(){
//
//        myOverLay = new FolderOverlay();
//
//        vMapView.setTileSource(TileSourceFactory.MAPNIK);
//
//        vMapView.setBuiltInZoomControls(true);
//        vMapView.setMultiTouchControls(true);
//        mapController = vMapView.getController();
//        mapController.setZoom((int)mapZoomLevel);
//        centerPoint = new GeoPoint(27.696268407446215, 85.462727834610064);
//        mapController.setCenter(centerPoint);
//
//        Marker startMarker = new Marker(vMapView);
//        startMarker.setPosition(centerPoint);
//        startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
//        vMapView.getOverlays().add(startMarker);
//
//        startMarker.setIcon(getResources().getDrawable(R.drawable.marker_default));
//        startMarker.setTitle("Chagunayayan Nagarpalika");
//
//    }
//
//    public void onResume(){
//        super.onResume();
//        //this will refresh the osmdroid configuration on resuming.
//        //if you make changes to the configuration, use
//        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
//        Configuration.getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this));
////        vMapView.onResume(); //needed for compass, my location overlays, v6.0.0 and up
//    }
//
//    public void onPause(){
//        super.onPause();
//        //this will refresh the osmdroid configuration on resuming.
//        //if you make changes to the configuration, use
//        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
//        Configuration.getInstance().save(this, prefs);
//       // vMapView.onPause();  //needed for compass, my location overlays, v6.0.0 and up
//    }
//
//
//
//    @OnClick({R.id.floating_school, R.id.floating_openspaces, R.id.floating_hospital})
//    public void onViewClicked(View view) {
//        switch (view.getId()) {
//            case R.id.floating_school:
//
//                loadMunicipalityBoarder();
//                addAdditionalLayerSchool();
//                break;
//            case R.id.floating_openspaces:
//                loadMunicipalityBoarder();
//                addAdditionalLayerOpenSpace();
//                break;
//            case R.id.floating_hospital:
//                loadMunicipalityBoarder();
//                addAdditionalLayerHospital();
//                break;
//        }
//    }
//
//    public void checkPermission() {
//        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
//                || ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
//                || ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
//                || ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED
//                || ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
//                ) {
//            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
//            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 2);
//            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
//            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.INTERNET}, 4);
//            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 5);
//        }
//    }
//
//
//    private void loadMunicipalityBoarder() {
//        vMapView.getOverlays().clear();
//
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//
//
//            Log.d(TAG, "loadMunicipalityBoarder: ");
////            vMapView.getOverlays().clear();
//
//            String jsonString = null;
//            try {
//                InputStream jsonStream = getResources().openRawResource(R.raw.changunarayan_boundary);
//                int size = jsonStream.available();
//                byte[] buffer = new byte[size];
//                jsonStream.read(buffer);
//                jsonStream.close();
//                jsonString = new String(buffer,"UTF-8");
//            } catch (IOException ex) {
//                ex.printStackTrace();
//                return;
//            }
//
//            final KmlDocument kmlDocument = new KmlDocument();
//            kmlDocument.parseGeoJSON(jsonString);
//
//            Drawable defaultMarker = getResources().getDrawable(R.drawable.marker_default);
//
//            Bitmap defaultBitmap = ((BitmapDrawable) defaultMarker).getBitmap();
//
//            final Style defaultStyle = new Style(defaultBitmap, 0x901010AA, 3f, 0x20AA1010);
//
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        myOverLay = (FolderOverlay)kmlDocument.mKmlRoot.buildOverlay(vMapView,defaultStyle,null,kmlDocument);
//                        vMapView.getOverlays().add(myOverLay );
//                        vMapView.invalidate();
//                        mapController.animateTo(centerPoint);
//                    }
//                });
//
//
//
//            }
//        }).start();
//    }
//
//    private void addAdditionalLayerSchool () {
//        Log.d(TAG, "addAdditionalLayerSchool: ");
//
//        new Thread(new Runnable() {
//    @Override
//    public void run() {
//
//
////        vMapView.getOverlays().clear();
//
//        String jsonString = null;
//        try {
//            InputStream jsonStream = getResources().openRawResource(R.raw.educational_institutes);
//            int size = jsonStream.available();
//            byte[] buffer = new byte[size];
//            jsonStream.read(buffer);
//            jsonStream.close();
//            jsonString = new String(buffer,"UTF-8");
//        } catch (IOException ex) {
//            ex.printStackTrace();
//            return;
//        }
//
//        final KmlDocument kmlDocument = new KmlDocument();
//        kmlDocument.parseGeoJSON(jsonString);
//
//        Drawable defaultMarker = getResources().getDrawable(R.drawable.marker_default);
//
//        Bitmap defaultBitmap = ((BitmapDrawable) defaultMarker).getBitmap();
//
//        final Style defaultStyle = new Style(defaultBitmap, 0x901010AA, 5f, 0x20AA1010);
//
//runOnUiThread(new Runnable() {
//    @Override
//    public void run() {
//        myOverLay = (FolderOverlay)kmlDocument.mKmlRoot.buildOverlay(vMapView,defaultStyle,null,kmlDocument);
//        vMapView.getOverlays().add(myOverLay );
//        vMapView.invalidate();
//    }
//});
//
//
//    }
//        }).start();
//    }
//
//    private void addAdditionalLayerHospital () {
//
//        Log.d(TAG, "addAdditionalLayerHospital: ");
////        vMapView.getOverlays().clear();
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//
//                String jsonString = null;
//                try {
//                    InputStream jsonStream = getResources().openRawResource(R.raw.health_facilities);
//                    int size = jsonStream.available();
//                    byte[] buffer = new byte[size];
//                    jsonStream.read(buffer);
//                    jsonStream.close();
//                    jsonString = new String(buffer,"UTF-8");
//                } catch (IOException ex) {
//                    ex.printStackTrace();
//                    return;
//                }
//
//                final KmlDocument kmlDocument = new KmlDocument();
//                kmlDocument.parseGeoJSON(jsonString);
//
//                Drawable defaultMarker = getResources().getDrawable(R.drawable.marker_default);
//
//                Bitmap defaultBitmap = ((BitmapDrawable) defaultMarker).getBitmap();
//
//                final Style defaultStyle = new Style(defaultBitmap, 0x901010AA, 5f, 0x20AA1010);
//
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        myOverLay = (FolderOverlay)kmlDocument.mKmlRoot.buildOverlay(vMapView,defaultStyle,null,kmlDocument);
//                        vMapView.getOverlays().add(myOverLay );
//                        vMapView.invalidate();
//                    }
//                });
//
//
//            }
//        }).start();
//    }
//
//    private void addAdditionalLayerOpenSpace () {
//
//        Log.d(TAG, "addAdditionalLayerOpenSpace: ");
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//
//
//>>>>>>> 327297472b416f0f684d2b6a63970bc69a55aaa3
////        vMapView.getOverlays().clear();
////
////        new Thread(new Runnable() {
////            @Override
////            public void run() {
////
////
////            Log.d(TAG, "loadMunicipalityBoarder: ");
//////            vMapView.getOverlays().clear();
////
////            String jsonString = null;
////            try {
////                InputStream jsonStream = getResources().openRawResource(R.raw.changunarayan_boundary);
////                int size = jsonStream.available();
////                byte[] buffer = new byte[size];
////                jsonStream.read(buffer);
////                jsonStream.close();
////                jsonString = new String(buffer,"UTF-8");
////            } catch (IOException ex) {
////                ex.printStackTrace();
////                return;
////            }
////
////            final KmlDocument kmlDocument = new KmlDocument();
////            kmlDocument.parseGeoJSON(jsonString);
////
////            Drawable defaultMarker = getResources().getDrawable(R.drawable.marker_default);
////
////            Bitmap defaultBitmap = ((BitmapDrawable) defaultMarker).getBitmap();
////
////            final Style defaultStyle = new Style(defaultBitmap, 0x901010AA, 3f, 0x20AA1010);
////
////                runOnUiThread(new Runnable() {
////                    @Override
////                    public void run() {
////                        myOverLay = (FolderOverlay)kmlDocument.mKmlRoot.buildOverlay(vMapView,defaultStyle,null,kmlDocument);
////                        vMapView.getOverlays().add(myOverLay );
////                        vMapView.invalidate();
////                        mapController.animateTo(centerPoint);
////                    }
////                });
////
////
////
////            }
////        }).start();
////    }
////
////    private void addAdditionalLayerSchool () {
////        Log.d(TAG, "addAdditionalLayerSchool: ");
////
////        new Thread(new Runnable() {
////    @Override
////    public void run() {
////
////
//////        vMapView.getOverlays().clear();
////
////        String jsonString = null;
////        try {
////            InputStream jsonStream = getResources().openRawResource(R.raw.educational_institutes);
////            int size = jsonStream.available();
////            byte[] buffer = new byte[size];
////            jsonStream.read(buffer);
////            jsonStream.close();
////            jsonString = new String(buffer,"UTF-8");
////        } catch (IOException ex) {
////            ex.printStackTrace();
////            return;
////        }
////
////        final KmlDocument kmlDocument = new KmlDocument();
////        kmlDocument.parseGeoJSON(jsonString);
////
////        Drawable defaultMarker = getResources().getDrawable(R.drawable.marker_default);
////
////        Bitmap defaultBitmap = ((BitmapDrawable) defaultMarker).getBitmap();
////
////        final Style defaultStyle = new Style(defaultBitmap, 0x901010AA, 5f, 0x20AA1010);
////
////runOnUiThread(new Runnable() {
////    @Override
////    public void run() {
////        myOverLay = (FolderOverlay)kmlDocument.mKmlRoot.buildOverlay(vMapView,defaultStyle,null,kmlDocument);
////        vMapView.getOverlays().add(myOverLay );
////        vMapView.invalidate();
////    }
////});
////
////
////    }
////        }).start();
////    }
////
////    private void addAdditionalLayerHospital () {
////
////        Log.d(TAG, "addAdditionalLayerHospital: ");
//////        vMapView.getOverlays().clear();
////        new Thread(new Runnable() {
////            @Override
////            public void run() {
////
////                String jsonString = null;
////                try {
////                    InputStream jsonStream = getResources().openRawResource(R.raw.health_facilities);
////                    int size = jsonStream.available();
////                    byte[] buffer = new byte[size];
////                    jsonStream.read(buffer);
////                    jsonStream.close();
////                    jsonString = new String(buffer,"UTF-8");
////                } catch (IOException ex) {
////                    ex.printStackTrace();
////                    return;
////                }
////
////                final KmlDocument kmlDocument = new KmlDocument();
////                kmlDocument.parseGeoJSON(jsonString);
////
////                Drawable defaultMarker = getResources().getDrawable(R.drawable.marker_default);
////
////                Bitmap defaultBitmap = ((BitmapDrawable) defaultMarker).getBitmap();
////
////                final Style defaultStyle = new Style(defaultBitmap, 0x901010AA, 5f, 0x20AA1010);
////
////                runOnUiThread(new Runnable() {
////                    @Override
////                    public void run() {
////                        myOverLay = (FolderOverlay)kmlDocument.mKmlRoot.buildOverlay(vMapView,defaultStyle,null,kmlDocument);
////                        vMapView.getOverlays().add(myOverLay );
////                        vMapView.invalidate();
////                    }
////                });
////
////
////            }
////        }).start();
////    }
////
////    private void addAdditionalLayerOpenSpace () {
////
////        Log.d(TAG, "addAdditionalLayerOpenSpace: ");
////        new Thread(new Runnable() {
////            @Override
////            public void run() {
////
////
//////        vMapView.getOverlays().clear();
////
////                String jsonString = null;
////                try {
////                    InputStream jsonStream = getResources().openRawResource(R.raw.open_space);
////                    int size = jsonStream.available();
////                    byte[] buffer = new byte[size];
////                    jsonStream.read(buffer);
////                    jsonStream.close();
////                    jsonString = new String(buffer,"UTF-8");
////                } catch (IOException ex) {
////                    ex.printStackTrace();
////                    return;
////                }
////
////                final KmlDocument kmlDocument = new KmlDocument();
////                kmlDocument.parseGeoJSON(jsonString);
////
////                Drawable defaultMarker = getResources().getDrawable(R.drawable.marker_default);
////
////                Bitmap defaultBitmap = ((BitmapDrawable) defaultMarker).getBitmap();
////
////                final Style defaultStyle = new Style(defaultBitmap, 0x901010AA, 5f, 0x20AA1010);
////
////                runOnUiThread(new Runnable() {
////                    @Override
////                    public void run() {
////                        myOverLay = (FolderOverlay)kmlDocument.mKmlRoot.buildOverlay(vMapView,defaultStyle,null,kmlDocument);
////                        vMapView.getOverlays().add(myOverLay );
////                        vMapView.invalidate();
////                    }
////                });
////
////
////            }
////        }).start();
////    }
////
////    private void setMunicipalityMapCamera() {
////
////        try {
////            final BoundingBox DISTRICT = new BoundingBox( 27.656397051552265, 85.396515496102595 ,27.727697867932854, 85.525113426326428);
////
//////            vMapView.setLatLngBoundsForCameraTarget(DISTRICT);
//////
//////
//////            CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(DISTRICT, 0);
////////            map.moveCamera(cu);
//////            vMapView.animateCamera(cu);
////
////
////        } catch (Exception e) {
////            e.printStackTrace();
////            Log.e(TAG, "settMapCamera: ERROR");
////        }
////    }
////
////}
