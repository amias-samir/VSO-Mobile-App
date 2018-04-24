package np.com.naxa.vso;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.Switch;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.github.zagum.expandicon.ExpandIconView;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.mapbox.mapboxsdk.Mapbox;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import org.osmdroid.api.IMapController;
import org.osmdroid.bonuspack.kml.KmlDocument;
import org.osmdroid.bonuspack.kml.Style;
import org.osmdroid.config.Configuration;
import org.osmdroid.events.MapEventsReceiver;
import org.osmdroid.events.MapListener;
import org.osmdroid.events.ScrollEvent;
import org.osmdroid.events.ZoomEvent;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.FolderOverlay;
import org.osmdroid.views.overlay.MapEventsOverlay;
import org.osmdroid.views.overlay.infowindow.InfoWindow;

import java.io.IOException;
import java.io.InputStream;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.functions.Consumer;
import np.com.naxa.vso.emergencyContacts.EmergencyContactsActivity;
import np.com.naxa.vso.home.MapDataCategory;
import np.com.naxa.vso.home.MapDataRepository;
import np.com.naxa.vso.home.MySection;
import np.com.naxa.vso.home.SectionAdapter;
import np.com.naxa.vso.home.SpacesItemDecoration;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class HomeActivity extends AppCompatActivity {
    @BindView(R.id.sliding_layout)
    SlidingUpPanelLayout slidingPanel;

    @BindView(R.id.recylcer_view_map_categories)
    RecyclerView recyclerView;

    @BindView(R.id.map)
    MapView mapView;

    @BindView(R.id.mapView)
    com.mapbox.mapboxsdk.maps.MapView  mapboxMapview;

    @BindView(R.id.expand_icon_view)
    ExpandIconView expandIconView;

    @BindView(R.id.bnve)
    BottomNavigationViewEx bnve;

    private MapDataRepository mapDataRepository;
    private FolderOverlay myOverLay;

    private final String TAG = this.getClass().getSimpleName();
    private IMapController mapController;
    GeoPoint centerPoint;


    public static void start(Context context) {
        Intent intent = new Intent(context, HomeActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        mapDataRepository = new MapDataRepository();
        centerPoint = new GeoPoint(27.657531140175244, 85.46161651611328);
        setupRecyclerView();
        setupSlidingPanel();
        setupMap();
        setupBottomBar();

        Mapbox.getInstance(this, getString(R.string.access_token));


    }

    private void setupBottomBar() {
        bnve.enableAnimation(false);
        bnve.enableShiftingMode(false);
        bnve.enableItemShiftingMode(false);


        bnve.setOnNavigationItemSelectedListener(item -> {

            switch (item.getItemId()) {
                case R.id.menu_ask_for_help:
                    ReportActivity.start(HomeActivity.this);
                    break;
                case R.id.menu_emergency_contacts:
                    EmergencyContactsActivity.start(HomeActivity.this);
                    break;
                case R.id.menu_open_spaces:
                    break;
            }
            return true;
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        //this will refresh the osmdroid configuration on resuming.
        //if you make changes to the configuration, use
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        Configuration.getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this));
//        vMapView.onResume(); //needed for compass, my location overlays, v6.0.0 and up
    }

    @Override
    public void onPause() {
        super.onPause();
        //this will refresh the osmdroid configuration on resuming.
        //if you make changes to the configuration, use
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        Configuration.getInstance().save(this, prefs);
        // vMapView.onPause();  //needed for compass, my location overlays, v6.0.0 and up
    }

    private void setupMap() {
        mapView.setTileSource(TileSourceFactory.MAPNIK);
        mapView.setBuiltInZoomControls(false);
        mapView.setMultiTouchControls(true);
        mapView.setMapListener(new MapListener() {
            @Override
            public boolean onScroll(ScrollEvent event) {
                return false;
            }

            @Override
            public boolean onZoom(ZoomEvent event) {
                return false;
            }
        });
        mapController = mapView.getController();
        mapController.setZoom(13);


        MapEventsOverlay mapEventsOverlay = new MapEventsOverlay(this, new MapEventsReceiver() {
            @Override
            public boolean singleTapConfirmedHelper(GeoPoint p) {
                InfoWindow.closeAllInfoWindowsOn(mapView);
                slidingPanel.setPanelHeight(110);
                return false;
            }

            @Override
            public boolean longPressHelper(GeoPoint p) {
                return false;
            }
        });

        mapView.getOverlays().add(0, mapEventsOverlay);
        mapController.setCenter(centerPoint);
        mapDataRepository.getMunicipalityBorder(mapView)
                .doOnNext(new Consumer<FolderOverlay>() {
                    @Override
                    public void accept(FolderOverlay folderOverlay) throws Exception {

                        mapView.getOverlays().add(folderOverlay);
                        mapView.invalidate();
                        mapController.animateTo(centerPoint);

                    }
                }).doOnError(new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                throwable.printStackTrace();
            }
        })
                .subscribe();
    }

    private void setupSlidingPanel() {
        slidingPanel.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {
                expandIconView.setFraction(slideOffset, true);
            }

            @Override
            public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {

            }
        });
    }

    private void setupRecyclerView() {
        LinearLayoutManager mLayoutManager = new GridLayoutManager(this, 3);
        recyclerView.setLayoutManager(mLayoutManager);
        SectionAdapter sectionAdapter = new SectionAdapter(R.layout.square_image_title, R.layout.list_section_header, MySection.getMapDataCatergorySections());
        recyclerView.setAdapter(sectionAdapter);

        sectionAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {

                slidingPanel.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                loadMunicipalityBoarder();
                addAdditionalLayerHospital();

//                switch (position) {
//                    case 0:
//                        addAdditionalLayerHospital();
//                        break;
//                    case 1:
//                       addAdditionalLayerOpenSpace();
//                        break;
//                    case 2:
//                        addAdditionalLayerSchool();
//                        break;
//                }


            }
        });
    }


    private void loadMunicipalityBoarder() {
        mapView.getOverlays().clear();

        new Thread(new Runnable() {
            @Override
            public void run() {


                Log.d(TAG, "loadMunicipalityBoarder: ");
//            mapView.getOverlays().clear();

                String jsonString = null;
                try {
                    InputStream jsonStream = getResources().openRawResource(R.raw.changunarayan_boundary);
                    int size = jsonStream.available();
                    byte[] buffer = new byte[size];
                    jsonStream.read(buffer);
                    jsonStream.close();
                    jsonString = new String(buffer, "UTF-8");
                } catch (IOException ex) {
                    ex.printStackTrace();
                    return;
                }

                final KmlDocument kmlDocument = new KmlDocument();
                kmlDocument.parseGeoJSON(jsonString);

                Drawable defaultMarker = getResources().getDrawable(R.drawable.marker_default);

                Bitmap defaultBitmap = ((BitmapDrawable) defaultMarker).getBitmap();

                final Style defaultStyle = new Style(defaultBitmap, 0x901010AA, 3f, 0x20AA1010);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        myOverLay = (FolderOverlay) kmlDocument.mKmlRoot.buildOverlay(mapView, defaultStyle, null, kmlDocument);
                        mapView.getOverlays().add(myOverLay);
                        mapView.invalidate();
                        mapController.animateTo(centerPoint);
                    }
                });


            }
        }).start();
    }

    private void addAdditionalLayerSchool() {
        Log.d(TAG, "addAdditionalLayerSchool: ");

        new Thread(new Runnable() {
            @Override
            public void run() {


//        mapView.getOverlays().clear();

                String jsonString = null;
                try {
                    InputStream jsonStream = getResources().openRawResource(R.raw.educational_institutes);
                    int size = jsonStream.available();
                    byte[] buffer = new byte[size];
                    jsonStream.read(buffer);
                    jsonStream.close();
                    jsonString = new String(buffer, "UTF-8");
                } catch (IOException ex) {
                    ex.printStackTrace();
                    return;
                }

                final KmlDocument kmlDocument = new KmlDocument();
                kmlDocument.parseGeoJSON(jsonString);

                Drawable defaultMarker = getResources().getDrawable(R.drawable.marker_default);

                Bitmap defaultBitmap = ((BitmapDrawable) defaultMarker).getBitmap();

                final Style defaultStyle = new Style(defaultBitmap, 0x901010AA, 5f, 0x20AA1010);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        myOverLay = (FolderOverlay) kmlDocument.mKmlRoot.buildOverlay(mapView, defaultStyle, null, kmlDocument);
                        mapView.getOverlays().add(myOverLay);
                        mapView.invalidate();
                    }
                });


            }
        }).start();
    }

    private void addAdditionalLayerHospital() {

        Log.d(TAG, "addAdditionalLayerHospital: ");
//        mapView.getOverlays().clear();
        new Thread(new Runnable() {
            @Override
            public void run() {

                String jsonString = null;
                try {
                    InputStream jsonStream = getResources().openRawResource(R.raw.health_facilities);
                    int size = jsonStream.available();
                    byte[] buffer = new byte[size];
                    jsonStream.read(buffer);
                    jsonStream.close();
                    jsonString = new String(buffer, "UTF-8");
                } catch (IOException ex) {
                    ex.printStackTrace();
                    return;
                }

                final KmlDocument kmlDocument = new KmlDocument();
                kmlDocument.parseGeoJSON(jsonString);

                Drawable defaultMarker = getResources().getDrawable(R.drawable.marker_default);

                Bitmap defaultBitmap = ((BitmapDrawable) defaultMarker).getBitmap();

                final Style defaultStyle = new Style(defaultBitmap, 0x901010AA, 5f, 0x20AA1010);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        myOverLay = (FolderOverlay) kmlDocument.mKmlRoot.buildOverlay(mapView, defaultStyle, null, kmlDocument);
                        mapView.getOverlays().add(myOverLay);
                        mapView.invalidate();
                    }
                });


            }
        }).start();
    }

    private void addAdditionalLayerOpenSpace() {

        Log.d(TAG, "addAdditionalLayerOpenSpace: ");
        new Thread(new Runnable() {
            @Override
            public void run() {


//        mapView.getOverlays().clear();

                String jsonString = null;
                try {
                    InputStream jsonStream = getResources().openRawResource(R.raw.open_space);
                    int size = jsonStream.available();
                    byte[] buffer = new byte[size];
                    jsonStream.read(buffer);
                    jsonStream.close();
                    jsonString = new String(buffer, "UTF-8");
                } catch (IOException ex) {
                    ex.printStackTrace();
                    return;
                }

                final KmlDocument kmlDocument = new KmlDocument();
                kmlDocument.parseGeoJSON(jsonString);

                Drawable defaultMarker = getResources().getDrawable(R.drawable.marker_default);

                Bitmap defaultBitmap = ((BitmapDrawable) defaultMarker).getBitmap();

                final Style defaultStyle = new Style(defaultBitmap, 0x901010AA, 5f, 0x20AA1010);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        myOverLay = (FolderOverlay) kmlDocument.mKmlRoot.buildOverlay(mapView, defaultStyle, null, kmlDocument);
                        mapView.getOverlays().add(myOverLay);
                        mapView.invalidate();
                    }
                });


            }
        }).start();
    }

    @OnClick(R.id.fab_location_toggle)
    public void turnGPSon() {
        if (!hasGPSPermissions()) {
            EasyPermissions.requestPermissions(
                    this,
                    getString(R.string.rationale_gps),
                    Permissions.RC_GPS_PERM,
                    Manifest.permission.CAMERA);
            return;
        }

        showGPSSetting();
    }

    @OnClick(R.id.fab_map_layer)
    public void showMaplayerMenu() {
        PopupMenu popup = new PopupMenu(this, findViewById(R.id.fab_map_layer));
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_map_layer, popup.getMenu());
        popup.show();
    }

    @AfterPermissionGranted(Permissions.RC_GPS_PERM)
    public void showGPSSetting() {
        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));

    }


    private boolean hasGPSPermissions() {
        return EasyPermissions.hasPermissions(this, Manifest.permission.ACCESS_FINE_LOCATION);
    }
}
