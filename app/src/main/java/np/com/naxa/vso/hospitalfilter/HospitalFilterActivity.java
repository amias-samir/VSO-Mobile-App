package np.com.naxa.vso.hospitalfilter;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.riddhimanadib.formmaster.FormBuilder;
import me.riddhimanadib.formmaster.listener.OnFormElementValueChangedListener;
import me.riddhimanadib.formmaster.model.BaseFormElement;
import me.riddhimanadib.formmaster.model.FormElementPickerMulti;
import me.riddhimanadib.formmaster.model.FormElementPickerSingle;
import me.riddhimanadib.formmaster.model.FormElementSwitch;
import me.riddhimanadib.formmaster.model.FormHeader;
import np.com.naxa.vso.R;

import np.com.naxa.vso.database.combinedentity.HospitalAndCommon;
import np.com.naxa.vso.database.dao.HospitalFacilitiesDao;
import np.com.naxa.vso.database.entity.HospitalFacilities;
import np.com.naxa.vso.home.HomeActivity;
import np.com.naxa.vso.utils.QueryBuildWithSplitter;
import np.com.naxa.vso.viewmodel.HospitalFacilitiesVewModel;


public class HospitalFilterActivity extends AppCompatActivity implements OnFormElementValueChangedListener {

    private static final String TAG = "HospitalFilterActivity";

    @BindView(R.id.btn_filter)
    Button btnFilter;
    private RecyclerView mRecyclerView;
    private FormBuilder mFormBuilder;

    private static final int TAG_WARD = 01;
    private static final int TAG_HOSPITAL_TYPE = 02;
    private static final int TAG_BED_CAPACITY = 03;
    private static final int TAG_BUILDING_STRUCTURE_LIST = 04;
    private static final int TAG_AVAILABLE_FACILITIES_LIST = 05;
    private static final int TAG_EXCAVATION_PLANS_LIST = 06;

    private static final int TAG_MEDICINE_STOCK = 11;
    private static final int TAG_BLOOD_STOCK = 12;
    private static final int TAG_DISASTERS_PLAN = 13;
    private static final int TAG_FIRST_AID = 14;
    private static final int TAG_EARTHQUAKE_RESILIENT = 15;
    private static final int TAG_ALTERNATIVE_ROUTE = 16;
    private static final int TAG_OCCUPANCY = 17;


    HospitalFacilitiesVewModel hospitalFacilitiesVewModel;
    List<HospitalFacilities> hospitalFacilitiesList = new ArrayList<>();
    List<HospitalAndCommon> hospitalFacilitiesWithCommonList = new ArrayList<>();

    HospitalFacilitiesDao hospitalFacilitiesDao;

    LifecycleOwner owner = this;

    List<String> hospitalTypeList = new ArrayList<String>();
    List<String> bedCapacityList = new ArrayList<String>();
    List<String> buildingStructureList = new ArrayList<String>();
    List<String> evacuationPlansList = new ArrayList<String>();


    public static void start(Context context) {
        Intent intent = new Intent(context, HospitalFilterActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospital_filter);
        ButterKnife.bind(this);

        setupToolBar();

        initRoomDatabase();

        initUIOptionData();


    }

    private void initRoomDatabase() {

        hospitalFacilitiesVewModel = ViewModelProviders.of(this).get(HospitalFacilitiesVewModel.class);

    }

    private void initUIOptionData() {

        InitUIOptionDataPostTask initUIOptionDataPostTask = new InitUIOptionDataPostTask();
        initUIOptionDataPostTask.execute();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupToolBar() {

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_general);
//        toolbar.setLogo(getResources().getDrawable(R.drawable.ic_close_24dp));
        setSupportActionBar(toolbar);

        final ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }

    }


    private void setupForm() {

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mFormBuilder = new FormBuilder(this, mRecyclerView, this);

        List<String> wardList = new ArrayList<>();
        wardList.add("1");
        wardList.add("2");
        wardList.add("3");
        wardList.add("4");
        wardList.add("5");
        wardList.add("6");
        wardList.add("7");
        wardList.add("8");
        wardList.add("9");
        wardList.add("10");
        wardList.add("11");
        wardList.add("12");
        FormElementPickerSingle wardListElement = FormElementPickerSingle.createInstance().setTag(TAG_WARD).setTitle("Select Ward").setOptions(wardList).setPickerTitle("Pick any ward");

        FormElementPickerSingle hospitalTypeElement = FormElementPickerSingle.createInstance().setTag(TAG_HOSPITAL_TYPE).setTitle("Hospital Type").setOptions(hospitalTypeList).setPickerTitle("Pick any type");

        FormElementPickerMulti bedCapacityElement = FormElementPickerMulti.createInstance().setTag(TAG_BED_CAPACITY).setTitle("Bed Capacity").setOptions(bedCapacityList).setPickerTitle("Choose one or more bed capacity").setNegativeText("reset");

        FormElementPickerMulti buildingStructureElement = FormElementPickerMulti.createInstance().setTag(TAG_BUILDING_STRUCTURE_LIST).setTitle("Building Structure Module").setOptions(buildingStructureList).setPickerTitle("Choose one or more building structure").setNegativeText("reset");

        List<String> availiableFaclities = new ArrayList<>();
        availiableFaclities.add("Emergency_Service");
        availiableFaclities.add("ICU_Service");
        availiableFaclities.add("Ambulance_Service");
//        availiableFaclities.add("Toilet_Facility");
        availiableFaclities.add("Fire_Extinguisher");
        FormElementPickerMulti availiableFaclitieseElement = FormElementPickerMulti.createInstance().setTag(TAG_AVAILABLE_FACILITIES_LIST).setTitle("Available Facilities List").setOptions(availiableFaclities).setPickerTitle("Choose one or more available facilities").setNegativeText("reset");

        FormElementPickerSingle excavationPlansElement = FormElementPickerSingle.createInstance().setTag(TAG_EXCAVATION_PLANS_LIST).setTitle("Evacuation Plans List").setOptions(evacuationPlansList).setPickerTitle("Choose evacuation plan");

        FormHeader headerBuildingExcavation = FormHeader.createInstance("Building Evacuation");

// switch input
        FormElementSwitch medicineStocklElement = FormElementSwitch.createInstance().setTag(TAG_MEDICINE_STOCK).setTitle("Medicine Stock").setSwitchTexts("Yes", "No");

        FormElementSwitch bloodStocklElement = FormElementSwitch.createInstance().setTag(TAG_BLOOD_STOCK).setTitle("Blood Stock").setSwitchTexts("Yes", "No");

        FormElementSwitch disasterElement = FormElementSwitch.createInstance().setTag(TAG_DISASTERS_PLAN).setTitle("Disaster Preparedness Response Plan").setSwitchTexts("Yes", "No");

        FormElementSwitch firstAidElement = FormElementSwitch.createInstance().setTag(TAG_FIRST_AID).setTitle("First Aid and Emergency Rescue").setSwitchTexts("Yes", "No");

        FormElementSwitch earthquakeElement = FormElementSwitch.createInstance().setTag(TAG_EARTHQUAKE_RESILIENT).setTitle("Earthquake Resilient").setSwitchTexts("Yes", "No");

        FormElementSwitch alterntiveRouteElement = FormElementSwitch.createInstance().setTag(TAG_ALTERNATIVE_ROUTE).setTitle("Alternative Routes").setSwitchTexts("Yes", "No");

        List<BaseFormElement> formItems = new ArrayList<>();
        formItems.add(wardListElement);
        formItems.add(hospitalTypeElement);
        formItems.add(bedCapacityElement);
        formItems.add(buildingStructureElement);
        formItems.add(availiableFaclitieseElement);
        formItems.add(excavationPlansElement);
        formItems.add(medicineStocklElement);
        formItems.add(bloodStocklElement);
        formItems.add(disasterElement);
        formItems.add(firstAidElement);
        formItems.add(earthquakeElement);
//        formItems.add(alterntiveRouteElement);

        mFormBuilder.addFormElements(formItems);
    }


    @Override
    public void onValueChanged(BaseFormElement formElement) {
//        Log.d("formListner", "onValueChanged: " + formElement.getTitle());
//        Toast.makeText(this, formElement.getValue(), Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.btn_filter)
    public void onViewClicked() {

        getFormData();

    }


    private void getFormData() {

        String ward, hospital_type, bed_capacity, building_structure_list, available_facilities_list, excavation_plans_list,
                medicine_stock = "no", blood_stock = "no", disaster_plan = "no", first_aid = "no", earthquake_resilient = "no";

        BaseFormElement wardElement = mFormBuilder.getFormElement(TAG_WARD);
        BaseFormElement hospitalTypeElement = mFormBuilder.getFormElement(TAG_HOSPITAL_TYPE);
        BaseFormElement bedCapacityElement = mFormBuilder.getFormElement(TAG_BED_CAPACITY);
        BaseFormElement buildingStructureElementList = mFormBuilder.getFormElement(TAG_BUILDING_STRUCTURE_LIST);
        BaseFormElement availableFacilitiesElementList = mFormBuilder.getFormElement(TAG_AVAILABLE_FACILITIES_LIST);
        BaseFormElement excavationPlansElementList = mFormBuilder.getFormElement(TAG_EXCAVATION_PLANS_LIST);

        BaseFormElement medicineElement = mFormBuilder.getFormElement(TAG_MEDICINE_STOCK);
        BaseFormElement bloodElement = mFormBuilder.getFormElement(TAG_BLOOD_STOCK);
        BaseFormElement disasterElement = mFormBuilder.getFormElement(TAG_DISASTERS_PLAN);
        BaseFormElement firstAidElement = mFormBuilder.getFormElement(TAG_FIRST_AID);
        BaseFormElement earthquakeElement = mFormBuilder.getFormElement(TAG_EARTHQUAKE_RESILIENT);

        ward = wardElement.getValue().trim();
        hospital_type = hospitalTypeElement.getValue();
        bed_capacity = bedCapacityElement.getValue();
        building_structure_list = buildingStructureElementList.getValue();
        available_facilities_list = availableFacilitiesElementList.getValue();

        excavation_plans_list = ((TextUtils.isEmpty(excavationPlansElementList.getValue().trim())) ? "No" : excavationPlansElementList.getValue().trim());
        medicine_stock = ((TextUtils.isEmpty(medicineElement.getValue().trim())) ? "No" : medicineElement.getValue().trim());
        blood_stock = ((TextUtils.isEmpty(bloodElement.getValue().trim())) ? "No" : bloodElement.getValue().trim());
        disaster_plan = ((TextUtils.isEmpty(disasterElement.getValue().trim())) ? "No" : disasterElement.getValue().trim());
        first_aid = ((TextUtils.isEmpty(firstAidElement.getValue().trim())) ? "No" : firstAidElement.getValue().trim());
        earthquake_resilient = ((TextUtils.isEmpty(earthquakeElement.getValue().trim())) ? "No" : earthquakeElement.getValue().trim());


        Log.d("HospitalFilter", "getFormData: " + ward + " , " + medicine_stock + " , " + blood_stock + " , " + disaster_plan + " , " + first_aid + " , " + earthquake_resilient);

        searchDataFromDatabase(ward, hospital_type,
                QueryBuildWithSplitter.dynamicStringSplitterWithColumnCheckQuery("number_of_beds", bed_capacity),
                QueryBuildWithSplitter.dynamicStringSplitterWithColumnCheckQuery("structure_type", building_structure_list),
                QueryBuildWithSplitter.availableFacilitiesQueryBuilder(available_facilities_list), excavation_plans_list,
                medicine_stock, blood_stock, disaster_plan, first_aid, earthquake_resilient);
    }


    private void searchDataFromDatabase(String ward, String hospital_type, String bedCapacity, String building_structure, String available_facilities, String excavation_plans,
                                        String medicineStock, String bloodStock, String disasterPlan, String firstAid, String earthquakeResilient) {
        try {
            hospitalFacilitiesVewModel.getFilteredList(ward, hospital_type, bedCapacity, building_structure, available_facilities, excavation_plans,
                    medicineStock, bloodStock, disasterPlan, firstAid, earthquakeResilient).observe(this, new android.arch.lifecycle.Observer<List<HospitalAndCommon>>() {
                @Override
                public void onChanged(@Nullable final List<HospitalAndCommon> hospitalFacilities) {
                    // Update the cached copy of the words in the adapter.
//                adapter.setWords(words);

                    Log.d(TAG, "onChanged: data retrieved " + hospitalFacilities.size());

//                    hospitalFacilitiesWithCommonList.addAll(hospitalFacilities);
                    HomeActivity.start(HospitalFilterActivity.this, (ArrayList) hospitalFacilities);

//                    Log.d(TAG, "onChanged: data retrieved " + hospitalFacilitiesWithCommonList.get(0).getCommonPlacesAttrb().getName());
//                    Log.d(TAG, "onChanged: data retrieved " + hospitalFacilitiesWithCommonList.get(0).getHospitalFacilities().getType());
//                    Log.d("HospitalFiltered", "onChanged: data retrieved " + hospitalFacilities.size());

                }
            });

        } catch (NullPointerException e) {

            Log.d(TAG, "Exception: " + e.toString());
        }

    }


    // Filter option initialize
    private class InitUIOptionDataPostTask extends AsyncTask<List<String>, Integer, List<String>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected List<String> doInBackground(List<String>... params) {


            hospitalFacilitiesVewModel.getAllTypeList().observe(owner, new Observer<List<String>>() {
                @Override
                public void onChanged(@NonNull final List<String> distinctValuesList) {
                    Log.d(TAG, "onChanged: " + distinctValuesList.get(0));

                    hospitalTypeList.addAll(distinctValuesList);
                }
            });


            hospitalFacilitiesVewModel.getAllStructureTypeList().observe(owner, new Observer<List<String>>() {
                @Override
                public void onChanged(@NonNull final List<String> distinctValuesList) {
                    Log.d(TAG, "onChanged: " + distinctValuesList.get(0));

                    buildingStructureList.addAll(distinctValuesList);
                }
            });

            hospitalFacilitiesVewModel.getAllBedCapacityList().observe(owner, new Observer<List<String>>() {
                @Override
                public void onChanged(@NonNull final List<String> distinctValuesList) {
                    Log.d(TAG, "onChanged: " + distinctValuesList.get(0));
                    bedCapacityList.addAll(distinctValuesList);
                }
            });


            hospitalFacilitiesVewModel.getAllEvacuationPlanList().observe(owner, new Observer<List<String>>() {
                @Override
                public void onChanged(@NonNull final List<String> distinctValuesList) {
                    Log.d(TAG, "onChanged: " + distinctValuesList.get(0));

                    evacuationPlansList.addAll(distinctValuesList);
                }
            });

            return hospitalTypeList;
        }

        @Override
        protected void onPostExecute(List<String> result) {
            setupForm();
//            super.onPostExecute(result);
        }
    }


}