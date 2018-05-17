package np.com.naxa.vso.activity;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import np.com.naxa.vso.R;
import np.com.naxa.vso.database.databaserepository.CommonPlacesAttrbRepository;
import np.com.naxa.vso.database.entity.CommonPlacesAttrb;
import np.com.naxa.vso.database.entity.EducationalInstitutes;
import np.com.naxa.vso.database.entity.HospitalFacilities;
import np.com.naxa.vso.database.entity.OpenSpace;
import np.com.naxa.vso.home.HomeActivity;
import np.com.naxa.vso.home.MapDataRepository;
import np.com.naxa.vso.utils.ProgressDialogUtils;
import np.com.naxa.vso.viewmodel.CommonPlacesAttribViewModel;
import np.com.naxa.vso.viewmodel.EducationalInstitutesViewModel;
import np.com.naxa.vso.viewmodel.HospitalFacilitiesVewModel;
import np.com.naxa.vso.viewmodel.OpenSpaceViewModel;

public class SplashActivity extends AppCompatActivity {

    private static final String TAG = "SplashActivity";
    private MapDataRepository repository;
    private CommonPlacesAttribViewModel commonPlacesAttribViewModel;
    private HospitalFacilitiesVewModel hospitalFacilitiesVewModel;
    private EducationalInstitutesViewModel educationalInstitutesViewModel;
    private OpenSpaceViewModel openSpaceViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spash);

        try {
            // Get a new or existing ViewModel from the ViewModelProvider.
            commonPlacesAttribViewModel = ViewModelProviders.of(this).get(CommonPlacesAttribViewModel.class);
            hospitalFacilitiesVewModel = ViewModelProviders.of(this).get(HospitalFacilitiesVewModel.class);
            educationalInstitutesViewModel = ViewModelProviders.of(this).get(EducationalInstitutesViewModel.class);
            openSpaceViewModel = ViewModelProviders.of(this).get(OpenSpaceViewModel.class);
        } catch (NullPointerException e) {

            Log.d(TAG, "Exception: " + e.toString());
        }

        new Handler().postDelayed(() -> {
            loadDataAndCallHomeActivity();
        }, 2000);
    }

    private void loadDataAndCallHomeActivity() {

        repository = new MapDataRepository();

        int position = 0;
        repository.getGeoJsonString(position)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(readGeoJason(position++))
                .flatMap(readGeoJason(position++))
                .flatMap(readGeoJason(position))
                .subscribe(new DisposableObserver<Pair>() {
                    @Override
                    public void onNext(Pair pair) {

                    }

                    @Override
                    public void onError(Throwable e) {
                        HomeActivity.start(SplashActivity.this);
                    }

                    @Override
                    public void onComplete() {
                        HomeActivity.start(SplashActivity.this);
                    }
                });

    }

    private Function<Pair, ObservableSource<Pair>> readGeoJason(int position) {
        return pair -> {
            String assetName = (String) pair.first;
            String fileContent = (String) pair.second;
            saveGeoJsonDataToDatabase(position, fileContent);
            Log.i("Shree", "Position is: " + position);
            return repository.getGeoJsonString(position + 1);
        };
    }

    private void saveGeoJsonDataToDatabase(int pos, String geoJson) {
        if (pos == 0) {
//            save hospital data
            new Thread(new Runnable() {
                @Override
                public void run() {
                    saveHospitalData(geoJson);
                }
            }).start();
        }

        if (pos == 1) {
//            save openspace data
            new Thread(new Runnable() {
                @Override
                public void run() {
                    saveOpenSpaces(geoJson);
                }
            }).start();
        }

        if (pos == 2) {
//            save school data
            new Thread(new Runnable() {
                @Override
                public void run() {
                    saveEducationalInstitutes(geoJson);
                }
            }).start();
        }
    }

    private void saveHospitalData(String geoJsonString) {

        CommonPlacesAttrbRepository.pID.clear();
        JSONObject jsonObject = null;
        String name = null, address = null, type = null, remarks = null, ambulance = null, contact_no = null, contact_pe = null,
                earthquake = null, emergency = null, fire_extin = null, icu_service = null, number_of = null, open_space = null,
                structure = null, toilet_fac;
        Long fk_common_places = null;
        Double latitude = 0.0, longitude = 0.0;
        try {
            jsonObject = new JSONObject(geoJsonString);
            JSONArray jsonarray = new JSONArray(jsonObject.getString("features"));

            for (int i = 0; i < jsonarray.length(); i++) {
                JSONObject properties = new JSONObject(jsonarray.getJSONObject(i).getString("properties"));
                name = properties.getString("name");
                address = properties.getString("Address");
                type = properties.getString("Type");
                latitude = Double.parseDouble(properties.getString("Y"));
                longitude = Double.parseDouble(properties.getString("X"));
                remarks = properties.getString("Remarks");

                CommonPlacesAttrb commonPlacesAttrb = new CommonPlacesAttrb(name, address, type, latitude, longitude, remarks);

                fk_common_places = commonPlacesAttribViewModel.insert(commonPlacesAttrb);
                Log.d(TAG, "saveHospitalData: " + fk_common_places);

                ambulance = properties.getString("Ambulance_");
                contact_no = properties.getString("Contact_Nu");
                contact_pe = properties.getString("Contact_Pe");
                earthquake = properties.getString("Earthquake");
                emergency = properties.getString("Emergency_");
                fire_extin = properties.getString("Fire_Extin");
                icu_service = properties.getString("ICU_Servic");
                number_of = properties.getString("Number_of_");
//                int bedCapacity = Integer.parseInt(number_of);
                open_space = properties.getString("Open_Space");
                structure = properties.getString("Structure_");
                toilet_fac = properties.getString("Toilet_Fac");

                HospitalFacilities hospitalFacilities = new HospitalFacilities(fk_common_places, ambulance, contact_no, contact_pe, earthquake, emergency,
                        fire_extin, icu_service, number_of, open_space, structure, toilet_fac);
                hospitalFacilitiesVewModel.insert(hospitalFacilities);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    private void saveEducationalInstitutes(String geoJsonString) {

        CommonPlacesAttrbRepository.pID.clear();

        JSONObject jsonObject = null;
        Double latitude = 0.0, longitude = 0.0;
        String name = null, address = null, type = null, remarks = null;

        Long fk_common_places = null;
        String college_he = null, contact_no = null, contact_pe = null, earthquake = null, evacuation = null, fire_extin = null,
                female_student = null, male_student = null, total_student = null, level = null, structure = null, open_space = null;
        try {
            jsonObject = new JSONObject(geoJsonString);
            JSONArray jsonarray = new JSONArray(jsonObject.getString("features"));

            for (int i = 0; i < jsonarray.length(); i++) {
                JSONObject properties = new JSONObject(jsonarray.getJSONObject(i).getString("properties"));
                name = properties.getString("name");
                address = properties.getString("Address");
                type = properties.getString("Type");
                latitude = Double.parseDouble(properties.getString("Y"));
                longitude = Double.parseDouble(properties.getString("X"));
                remarks = properties.getString("Remarks");

                CommonPlacesAttrb commonPlacesAttrb = new CommonPlacesAttrb(name, address, type, latitude, longitude, remarks);

                fk_common_places = commonPlacesAttribViewModel.insert(commonPlacesAttrb);
                Log.d(TAG, "saveEducationalInstitutes: " + fk_common_places);

                college_he = properties.getString("College_He");
                contact_no = properties.getString("Contact_Nu");
                contact_pe = properties.getString("Contact_Pe");
                earthquake = properties.getString("Earthquake");
                evacuation = properties.getString("Evacuation");
                fire_extin = properties.getString("Fire_Extin");
                male_student = properties.getString("Male_Stude");
                female_student = properties.getString("Female_Stu");
                total_student = properties.getString("Total_Stud");
                open_space = properties.getString("Open_Space");
                structure = properties.getString("Structure_");

                EducationalInstitutes educationalInstitutes = new EducationalInstitutes(fk_common_places, college_he, contact_no, contact_pe, earthquake,
                        evacuation, female_student, male_student, total_student, fire_extin, level, open_space, structure);
                educationalInstitutesViewModel.insert(educationalInstitutes);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void saveOpenSpaces(String geoJsonString) {
        CommonPlacesAttrbRepository.pID.clear();

        JSONObject jsonObject = null;
        Double latitude = 0.0, longitude = 0.0;
        String name = null, address = null, type = null, remarks = null;

        Long fk_common_places = null;
        String access_roa = null, accommodat = null, area_sqm = null, hign_tensi = null, road_access = null, shape_area = null,
                shape_leng = null, terrain_ty = null, toilet_fac = null, water_faci = null, wifi_facil = null;
        try {
            jsonObject = new JSONObject(geoJsonString);
            JSONArray jsonarray = new JSONArray(jsonObject.getString("features"));

            for (int i = 0; i < jsonarray.length(); i++) {
                JSONObject properties = new JSONObject(jsonarray.getJSONObject(i).getString("properties"));
                name = properties.getString("name");
                address = properties.getString("Address");
                type = properties.getString("Type");
                latitude = Double.parseDouble(properties.getString("Y"));
                longitude = Double.parseDouble(properties.getString("X"));
                remarks = properties.getString("Remarks");

                CommonPlacesAttrb commonPlacesAttrb = new CommonPlacesAttrb(name, address, type, latitude, longitude, remarks);

                fk_common_places = commonPlacesAttribViewModel.insert(commonPlacesAttrb);
                Log.d(TAG, "saveOpenSpaces: " + fk_common_places);

                access_roa = properties.getString("Access_Roa");
                accommodat = properties.getString("Accommodat");
                area_sqm = properties.getString("Area_SqM");
                hign_tensi = properties.getString("High_Tensi");
                road_access = properties.getString("Road_Acces");
                shape_area = properties.getString("Shape_Area");
                shape_leng = properties.getString("Shape_Leng");
                terrain_ty = properties.getString("Terrain_Ty");
                toilet_fac = properties.getString("Toilet_Fac");
                water_faci = properties.getString("Water_Faci");
                wifi_facil = properties.getString("Wifi_Facil");

                OpenSpace openSpace = new OpenSpace(fk_common_places, access_roa, accommodat, area_sqm, hign_tensi, road_access, shape_area,
                        shape_leng, terrain_ty, toilet_fac, water_faci, wifi_facil);
                openSpaceViewModel.insert(openSpace);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
