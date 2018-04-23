package np.com.naxa.vso;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.github.zagum.expandicon.ExpandIconView;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.FolderOverlay;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.functions.Consumer;
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

    @BindView(R.id.expand_icon_view)
    ExpandIconView expandIconView;

    @BindView(R.id.bnve)
    BottomNavigationViewEx bnve;

    private MapDataRepository mapDataRepository;

    int rotationAngle = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        mapDataRepository = new MapDataRepository();

        setupRecyclerView();
        setupSlidingPanel();
        setupMap();

        bnve.enableAnimation(false);
        bnve.enableShiftingMode(false);
        bnve.enableItemShiftingMode(false);
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
        mapView.setBuiltInZoomControls(true);
        mapView.setMultiTouchControls(true);
        final IMapController mapController = mapView.getController();
        mapController.setZoom(12);
        final GeoPoint startPoint = new GeoPoint(27.716278, 85.427889);
        mapController.setCenter(startPoint);

        mapDataRepository.getMunicipalityBorder(mapView)
                .doOnNext(new Consumer<FolderOverlay>() {
                    @Override
                    public void accept(FolderOverlay folderOverlay) throws Exception {

                        mapView.getOverlays().add(folderOverlay);
                        mapView.invalidate();
                        mapController.animateTo(startPoint);

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
                new MapDataRepository().getMunicipalityBorder(mapView);
                slidingPanel.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
            }
        });
    }

    @OnClick(R.id.fab_location_toggle)
    public void turnGPSon() {
      }



    private boolean hasGPSPermissions() {
        return EasyPermissions.hasPermissions(this, Manifest.permission.ACCESS_FINE_LOCATION);
    }
}
