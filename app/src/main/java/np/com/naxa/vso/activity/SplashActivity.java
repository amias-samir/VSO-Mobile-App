package np.com.naxa.vso.activity;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Handler;
import android.support.annotation.MainThread;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import np.com.naxa.vso.R;
import np.com.naxa.vso.DatabaseDataSPClass;
import np.com.naxa.vso.database.databaserepository.CommonPlacesAttrbRepository;
import np.com.naxa.vso.database.entity.CommonPlacesAttrb;
import np.com.naxa.vso.database.entity.EducationalInstitutes;
import np.com.naxa.vso.database.entity.HospitalFacilities;
import np.com.naxa.vso.database.entity.OpenSpace;
import np.com.naxa.vso.home.HomeActivity;
import np.com.naxa.vso.home.MapDataRepository;
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
    private DatabaseDataSPClass sharedpref = new DatabaseDataSPClass(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spash);
        repository = new MapDataRepository();

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
            if (sharedpref.checkIfDataPresent()) {
                HomeActivity.start(SplashActivity.this);
            } else {
                loadDataAndCallHomeActivity();
            }
        }, 2000);


    }

    private void loadDataAndCallHomeActivity() {
        int pos = 0;
        repository.getGeoJsonString(pos)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(readGeoJason(0))
                .flatMap(readGeoJason(1))
                .flatMap(readGeoJason(2))
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
                        sharedpref.saveDataPresent();
                        HomeActivity.start(SplashActivity.this);
                    }
                });
    }

    private Function<Pair, ObservableSource<Pair>> readGeoJason(int position) {
        return pair -> {
            String assetName = (String) pair.first;
            String fileContent = (String) pair.second;
            Log.i(TAG, fileContent + "");
            saveGeoJsonDataToDatabase(position, fileContent);
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
        String name = null, address = null, remarks = null;

        String category = null, type = null, open_space = null, contact_no = null, contact_pe = null, emergency_service = null, icu_service = null,
                ward = null, ambulance = null, number_of_beds = null, structure_type = null, earthquake_damage = null, toilet_facility = null,
                fire_extingiusher = null, evacuation_plan = null, alternative_route = null, no_of_doctors = null, no_of_nurse = null,
                no_of_health_assistent = null, total_no_of_employees = null, water_storage = null, emergency_stock_capcity = null, ict_grading = null,
                No_of_Rooms = null, No_of_Stories = null, Emergency_Phone_Number = null, Male_Toilet = null, Female_Toilet = null,
                Differently_abled_Toilet_Facility = null, Disaster_Preparedness_Response_Plan = null, First_Aid_and_Emergency_Rescue = null,
                National_Building_Code = null, Building_Age_and_State = null, Occupancy = null, Area_in_Sq_m = null, Built_up_Area_in_Sq_m = null,
                Built_up_Area_in_Hectare = null, Area_in_Hectare = null, Open_Area_in_Sq_m = null, Open_Area_in_Hectare = null, Email = null,
                Web = null, Medicine_in_Stock = null, Blood_in_Stock = null;

        Long fk_common_places = null;
        Double latitude = 0.0, longitude = 0.0;
        try {
            jsonObject = new JSONObject(geoJsonString);
            JSONArray jsonarray = new JSONArray(jsonObject.getString("features"));

            for (int i = 0; i < jsonarray.length(); i++) {
                JSONObject properties = new JSONObject(jsonarray.getJSONObject(i).getString("properties"));
                name = properties.getString("name");
                address = properties.getString("Address");
                latitude = Double.parseDouble(properties.getString("Y"));
                longitude = Double.parseDouble(properties.getString("X"));
                remarks = properties.getString("Remarks");

                CommonPlacesAttrb commonPlacesAttrb = new CommonPlacesAttrb(name, address, "hospital", latitude, longitude, remarks);

                fk_common_places = commonPlacesAttribViewModel.insert(commonPlacesAttrb);
                Log.d(TAG, "saveHospitalData: " + fk_common_places);

                ward = properties.getString("Ward no.");
                category = properties.getString("Category");
                type = properties.getString("Type");
                open_space = properties.getString("Open_Space");
                contact_no = properties.getString("Contact_Number");
                contact_pe = properties.getString("Contact_Person");
                emergency_service = properties.getString("Emergency_Service");
                icu_service = properties.getString("ICU_Service");
                ambulance = properties.getString("Ambulance_Service");
                number_of_beds = properties.getString("Number_of_Beds");
                structure_type = properties.getString("Structure_Type");
                earthquake_damage = properties.getString("Earthquake_Damage");
                toilet_facility = properties.getString("Toilet_Facility");
                fire_extingiusher = properties.getString("Fire_Extinguisher");
                evacuation_plan = properties.getString("Evacuation_Plan");
                alternative_route = properties.getString("Alternatice_Route");
                no_of_doctors = properties.getString("No_of_Doctors");
                no_of_nurse = properties.getString("No_of_Nurses");
                no_of_health_assistent = properties.getString("No_of_Health_Assistant");
                total_no_of_employees = properties.getString("Total_No_of_Employees");
                water_storage = properties.getString("Water_Storage_Capacity_Litre_");
                emergency_stock_capcity = properties.getString("Emergency_Stock_Capacity");
                ict_grading = properties.getString("ICT_Grading_A_B_C_D");

                No_of_Rooms = properties.getString("No_of_Rooms");
                No_of_Stories = properties.getString("No_of_Stories");
                Emergency_Phone_Number = properties.getString("Emergency_Phone_Number");
                Male_Toilet = properties.getString("Male_Toilet");
                Female_Toilet = properties.getString("Female_Toilet");
                Differently_abled_Toilet_Facility = properties.getString("Differently_abled_Toilet_Facility");
                Disaster_Preparedness_Response_Plan = properties.getString("Disaster_Preparedness_Response_Plan");
                First_Aid_and_Emergency_Rescue = properties.getString("First_Aid_and_Emergency_Rescue");
                National_Building_Code = properties.getString("National_Building_Code");
                Building_Age_and_State = properties.getString("Building_Age_and_State");
                Occupancy = properties.getString("Occupancy");
                Area_in_Sq_m = properties.getString("Area_in_Sq_m");
                Built_up_Area_in_Sq_m = properties.getString("Built_up_Area_in_Sq_m");
                Built_up_Area_in_Hectare = properties.getString("Built_up_Area_in_Hectare");
                Area_in_Hectare = properties.getString("ICT_Grading_A_B_C_D");
                Open_Area_in_Sq_m = properties.getString("Open_Area_in_Sq_m");
                Open_Area_in_Hectare = properties.getString("Open_Area_in_Hectare");
                Email = properties.getString("Email");
                Web = properties.getString("Web");
                Medicine_in_Stock = properties.getString("Medicine_in_Stock");
                Blood_in_Stock = properties.getString("Blood_in_Stock");

                HospitalFacilities hospitalFacilities = new HospitalFacilities(fk_common_places, ward, category, type, open_space, contact_no,
                        contact_pe, emergency_service, icu_service, ambulance, number_of_beds, structure_type, earthquake_damage, toilet_facility,
                        fire_extingiusher, evacuation_plan, alternative_route, no_of_doctors, no_of_nurse, no_of_health_assistent, total_no_of_employees,
                        water_storage, emergency_stock_capcity, ict_grading, No_of_Rooms, No_of_Stories, Emergency_Phone_Number, Male_Toilet,
                        Female_Toilet, Differently_abled_Toilet_Facility, Disaster_Preparedness_Response_Plan, First_Aid_and_Emergency_Rescue,
                        National_Building_Code, Building_Age_and_State, Occupancy, Area_in_Sq_m, Built_up_Area_in_Sq_m, Built_up_Area_in_Hectare,
                        Area_in_Hectare,Open_Area_in_Sq_m, Open_Area_in_Hectare, Email, Web, Medicine_in_Stock, Blood_in_Stock);

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
