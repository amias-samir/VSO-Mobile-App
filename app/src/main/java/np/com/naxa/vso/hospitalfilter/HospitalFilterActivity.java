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
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;

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
    private static final int TAG_BED = 03;
    private static final int TAG_BED_CAPACITY = 04;
    private static final int TAG_BUILDING_STRUCTURE = 05;
    private static final int TAG_BUILDING_STRUCTURE_LIST = 15;
    private static final int TAG_AVAILABLE_FACILITIES = 06;
    private static final int TAG_AVAILABLE_FACILITIES_LIST = 16;
    private static final int TAG_EXCAVATION_PLANS = 07;
    private static final int TAG_EXCAVATION_PLANS_LIST = 17;


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

        FormHeader headerWard = FormHeader.createInstance("Ward");
        List<String> wardList = new ArrayList<>();
        wardList.add("1");
        wardList.add("2");
        wardList.add("3");
        wardList.add("4");
        wardList.add("5");
        wardList.add("6");
        FormElementPickerSingle wardListElement = FormElementPickerSingle.createInstance().setTag(TAG_WARD).setTitle("Select Ward").setOptions(wardList).setPickerTitle("Pick any ward");

        FormHeader headerHospitalType = FormHeader.createInstance("Hospital Facilities");
        FormElementPickerSingle hospitalTypeElement = FormElementPickerSingle.createInstance().setTag(TAG_HOSPITAL_TYPE).setTitle("Hospital Type").setOptions(hospitalTypeList).setPickerTitle("Pick any type");


        FormHeader headerBedCapacity = FormHeader.createInstance("Bed Capacity");
        FormElementPickerMulti bedCapacityElement = FormElementPickerMulti.createInstance().setTag(TAG_BED_CAPACITY).setTitle("Bed Capacity").setOptions(bedCapacityList).setPickerTitle("Choose one or more bed capacity").setNegativeText("reset");

        FormHeader headerBuildingStructure = FormHeader.createInstance("Structure");
        FormElementPickerMulti buildingStructureElement = FormElementPickerMulti.createInstance().setTag(TAG_BUILDING_STRUCTURE_LIST).setTitle("Building Structure Module").setOptions(buildingStructureList).setPickerTitle("Choose one or more building structure").setNegativeText("reset");

        FormHeader headerAvailiableFacilities = FormHeader.createInstance("Facilities");
        List<String> availiableFaclities = new ArrayList<>();
        availiableFaclities.add("Emergency_Service");
        availiableFaclities.add("ICU_Service");
        availiableFaclities.add("Ambulance_Service");
        availiableFaclities.add("Toilet_Facility");
        availiableFaclities.add("Fire_Extinguisher");
        FormElementPickerMulti availiableFaclitieseElement = FormElementPickerMulti.createInstance().setTag(TAG_AVAILABLE_FACILITIES_LIST).setTitle("Available Facilities List").setOptions(availiableFaclities).setPickerTitle("Choose one or more available facilities").setNegativeText("reset");

        FormHeader headerBuildingExcavation = FormHeader.createInstance("Building Evacuation");
        FormElementPickerMulti excavationPlansElement = FormElementPickerMulti.createInstance().setTag(TAG_EXCAVATION_PLANS_LIST).setTitle("Evacuation Plans List").setOptions(evacuationPlansList).setPickerTitle("Choose one or more evacuation plan").setNegativeText("reset");

        List<BaseFormElement> formItems = new ArrayList<>();
//        formItems.add(headerWard);
        formItems.add(wardListElement);

//        formItems.add(headerHospitalType);
        formItems.add(hospitalTypeElement);

//        formItems.add(headerBedCapacity);
        formItems.add(bedCapacityElement);

//        formItems.add(headerBuildingStructure);
        formItems.add(buildingStructureElement);

//        formItems.add(headerAvailiableFacilities);
        formItems.add(availiableFaclitieseElement);

//        formItems.add(headerBuildingExcavation);
        formItems.add(excavationPlansElement);

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

        String ward, hospital_type, bed, bed_capacity, building_structure, building_structure_list, available_facilities, available_facilities_list,
                excavation_plans, excavation_plans_list;

        BaseFormElement wardElement = mFormBuilder.getFormElement(TAG_WARD);
        BaseFormElement hospitalTypeElement = mFormBuilder.getFormElement(TAG_HOSPITAL_TYPE);
        BaseFormElement bedCapacityElement = mFormBuilder.getFormElement(TAG_BED_CAPACITY);
        BaseFormElement buildingStructureElementList = mFormBuilder.getFormElement(TAG_BUILDING_STRUCTURE_LIST);
        BaseFormElement availableFacilitiesElementList = mFormBuilder.getFormElement(TAG_AVAILABLE_FACILITIES_LIST);
        BaseFormElement excavationPlansElementList = mFormBuilder.getFormElement(TAG_EXCAVATION_PLANS_LIST);

        ward = wardElement.getValue();
        hospital_type = hospitalTypeElement.getValue();
        bed_capacity = bedCapacityElement.getValue();
        building_structure_list = buildingStructureElementList.getValue();
        available_facilities_list = availableFacilitiesElementList.getValue();
        excavation_plans_list = excavationPlansElementList.getValue();

        Log.d("HospitalFilter", "getFormData: " + ward + " , " + bed_capacity + " , " + excavation_plans_list);

        searchDataFromDatabase(ward, hospital_type,
                QueryBuildWithSplitter.dynamicStringSplitterWithColumnCheckQuery("number_of_beds", bed_capacity),
                QueryBuildWithSplitter.dynamicStringSplitterWithColumnCheckQuery("structure_type", building_structure_list),
                QueryBuildWithSplitter.availableFacilitiesQueryBuilder(available_facilities_list),
                QueryBuildWithSplitter.dynamicStringSplitterWithColumnCheckQuery("evacuation_plan", excavation_plans_list));
    }


    private void searchDataFromDatabase(String ward, String hospital_type, String bedCapacity, String building_structure, String available_facilities, String excavation_plans) {
        try {
            hospitalFacilitiesVewModel.getFilteredList(ward, hospital_type, bedCapacity, building_structure, available_facilities, excavation_plans).observe(this, new android.arch.lifecycle.Observer<List<HospitalAndCommon>>() {
                @Override
                public void onChanged(@Nullable final List<HospitalAndCommon> hospitalFacilities) {
                    // Update the cached copy of the words in the adapter.
//                adapter.setWords(words);

                    hospitalFacilitiesWithCommonList.addAll(hospitalFacilities);
                    HomeActivity.start(HospitalFilterActivity.this, (ArrayList) hospitalFacilities);

                    Log.d(TAG, "onChanged: data retrieved " + hospitalFacilitiesWithCommonList.get(0).getCommonPlacesAttrb().getName());
                    Log.d(TAG, "onChanged: data retrieved " + hospitalFacilitiesWithCommonList.get(0).getHospitalFacilities().getType());
                    Log.d("HospitalFiltered", "onChanged: data retrieved " + hospitalFacilities.size());
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