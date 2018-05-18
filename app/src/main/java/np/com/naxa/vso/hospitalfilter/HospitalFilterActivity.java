package np.com.naxa.vso.hospitalfilter;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
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
import me.riddhimanadib.formmaster.model.FormHeader;
import np.com.naxa.vso.R;
import np.com.naxa.vso.activity.LoadingActivity;
import np.com.naxa.vso.activity.SplashActivity;
import np.com.naxa.vso.database.entity.HospitalFacilities;
import np.com.naxa.vso.emergencyContacts.ExpandableUseActivity;
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

    LifecycleOwner owner = this;


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

        setupForm();




    }

    private void initRoomDatabase (){

        hospitalFacilitiesVewModel = ViewModelProviders.of(this).get(HospitalFacilitiesVewModel.class);

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
        List<String> hospitalTypeList =  getDistinctValuesListFromColumn("Type");
//        List<String> hospitalTypeList =  GetDataFromDatabase.geTypeListDistinct(hospitalFacilitiesVewModel, this);
//        List<String> hospitalTypeList =new ArrayList<>();
//        hospitalTypeList.add("Type 1");
//        hospitalTypeList.add("Type 2");
//        hospitalTypeList.add("Type 3");
//        hospitalTypeList.add("Type 4");
//        hospitalTypeList.add("Type 5");
//        hospitalTypeList.add("Type 6");
        FormElementPickerSingle hospitalTypeElement = FormElementPickerSingle.createInstance().setTag(TAG_HOSPITAL_TYPE).setTitle("Hospital Type").setOptions(hospitalTypeList).setPickerTitle("Pick any type");


        FormHeader headerBedCapacity = FormHeader.createInstance("Bed Capacity");
//        FormElementSwitch bedCapacitySwitcher = FormElementSwitch.createInstance().setTag(TAG_BED).setTitle("Bed").setSwitchTexts("Yes", "No");


//        List<String> bedCapacity = getDistinctValuesListFromColumn("Number_of_Beds");
//        List<String> bedCapacity = GetDataFromDatabase.getBedListDistinct(hospitalFacilitiesVewModel, this);
        List<String> bedCapacity = new ArrayList<>();
        bedCapacity.add("0-10");
        bedCapacity.add("11-20");
        bedCapacity.add("21-30");
        bedCapacity.add("31-40");
        bedCapacity.add("41-50");
        bedCapacity.add("50+");
        FormElementPickerMulti bedCapacityElement = FormElementPickerMulti.createInstance().setTag(TAG_BED_CAPACITY).setTitle("Bed Capacity").setOptions(bedCapacity).setPickerTitle("Choose one or more bed capacity").setNegativeText("reset");

        FormHeader headerBuildingStructure = FormHeader.createInstance("Structure");
//        FormElementSwitch buildingStructureSwitcher = FormElementSwitch.createInstance().setTag(TAG_BUILDING_STRUCTURE).setTitle("Building Structure").setSwitchTexts("Yes", "No");
//        List<String> buildingStructure =  getDistinctValuesListFromColumn("Structure_Type");
//        List<String> buildingStructure =  GetDataFromDatabase.getStructureTypeListDistinct(hospitalFacilitiesVewModel, this);
        List<String> buildingStructure =new ArrayList<>();
        buildingStructure.add("Structure 1");
        buildingStructure.add("Structure 2");
        buildingStructure.add("Structure 3");
        buildingStructure.add("Structure 4");
        buildingStructure.add("Structure 5");
        buildingStructure.add("Structure 6");
        FormElementPickerMulti buildingStructureElement = FormElementPickerMulti.createInstance().setTag(TAG_BUILDING_STRUCTURE_LIST).setTitle("Building Structure Module").setOptions(buildingStructure).setPickerTitle("Choose one or more building structure").setNegativeText("reset");

        FormHeader headerAvailiableFacilities = FormHeader.createInstance("Facilities");
//        FormElementSwitch availiableFacilitiesSwitcher = FormElementSwitch.createInstance().setTag(TAG_AVAILABLE_FACILITIES).setTitle("Available Facilities").setSwitchTexts("Yes", "No");
        List<String> availiableFaclities = new ArrayList<>();
        availiableFaclities.add("Emergency_Service");
        availiableFaclities.add("ICU_Service");
        availiableFaclities.add("Ambulance_Service");
        availiableFaclities.add("Toilet_Facility");
        availiableFaclities.add("Fire_Extinguisher");
        FormElementPickerMulti availiableFaclitieseElement = FormElementPickerMulti.createInstance().setTag(TAG_AVAILABLE_FACILITIES_LIST).setTitle("Available Facilities List").setOptions(availiableFaclities).setPickerTitle("Choose one or more available facilities").setNegativeText("reset");

        FormHeader headerBuildingExcavation = FormHeader.createInstance("Building Evacuation");
//        FormElementSwitch availiableBuildingExcavationSwitcher = FormElementSwitch.createInstance().setTag(TAG_EXCAVATION_PLANS).setTitle(" Available building excavation plans").setSwitchTexts("Yes", "No");
//        List<String> excavationPlans = getDistinctValuesListFromColumn("Evacuation_Plan");
//        List<String> excavationPlans = GetDataFromDatabase.getEvacuationPlanListDistinct(hospitalFacilitiesVewModel, this);
        List<String> excavationPlans = new ArrayList<>();
        excavationPlans.add("Plan 1");
        excavationPlans.add("Plan 2");
        excavationPlans.add("Plan 3");
        excavationPlans.add("Plan 4");
        excavationPlans.add("Plan 5");
        excavationPlans.add("Plan 6");
        FormElementPickerMulti excavationPlansElement = FormElementPickerMulti.createInstance().setTag(TAG_EXCAVATION_PLANS_LIST).setTitle("Evacuation Plans List").setOptions(excavationPlans).setPickerTitle("Choose one or more evacuation plan").setNegativeText("reset");

        List<BaseFormElement> formItems = new ArrayList<>();
        formItems.add(headerWard);
        formItems.add(wardListElement);

        formItems.add(headerHospitalType);
        formItems.add(hospitalTypeElement);

        formItems.add(headerBedCapacity);
        formItems.add(bedCapacityElement);

        formItems.add(headerBuildingStructure);
        formItems.add(buildingStructureElement);

        formItems.add(headerAvailiableFacilities);
        formItems.add(availiableFaclitieseElement);

        formItems.add(headerBuildingExcavation);
        formItems.add(excavationPlansElement);

        mFormBuilder.addFormElements(formItems);
    }

    private List<String> getDistinctValuesListFromColumn(String columnName){
        List<String> distinctValuesList = new ArrayList<String>();
         Log.d(TAG, "getDistinctValuesFromColumn: "+columnName);
            hospitalFacilitiesVewModel.getAllTypeList().observe(this, new Observer<List<String>>() {
                @Override
                public void onChanged(@NonNull final List<String> distinctValuesList) {
                    distinctValuesList.addAll(distinctValuesList);
                }
            });
        return distinctValuesList;
    }


    @Override
    public void onValueChanged(BaseFormElement formElement) {
        Log.d("formListner", "onValueChanged: " + formElement.getTitle());
        Toast.makeText(this, formElement.getValue(), Toast.LENGTH_SHORT).show();


    }

    @OnClick(R.id.btn_filter)
    public void onViewClicked() {

        getFormData();
    }


    private void getFormData (){

        String ward,hospital_type, bed, bed_capacity, building_structure,building_structure_list, available_facilities, available_facilities_list,
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

        Log.d("HospitalFilter", "getFormData: "+ward +" , "+ bed_capacity + " , "+excavation_plans_list);

        int bedRange[] = QueryBuildWithSplitter.dynamicStringSplitterWithRangeQueryBuild("10-20, 20-30");
        int lowestBedValue = bedRange[0];
        int highestBedValue = bedRange[1];

        searchDataFromDatabase(ward, hospital_type,
                QueryBuildWithSplitter.dynamicStringSplitterWithColumnCheckQuery("number_of_bed", bed_capacity),
                QueryBuildWithSplitter.dynamicStringSplitterWithColumnCheckQuery("structure", building_structure_list),
                QueryBuildWithSplitter.dynamicStringSplitterWithColumnCheckQuery("available_facilities", available_facilities_list),
                QueryBuildWithSplitter.dynamicStringSplitterWithColumnCheckQuery("evacuation", excavation_plans_list));

    }


    private void searchDataFromDatabase(String ward,String hospital_type, String bedCapacity, String building_structure, String available_facilities, String excavation_plans){
try{
        hospitalFacilitiesVewModel.getFilteredList(ward, hospital_type, bedCapacity, building_structure, available_facilities, excavation_plans).observe(this, new android.arch.lifecycle.Observer<List<HospitalFacilities>>() {
            @Override
            public void onChanged(@Nullable final List<HospitalFacilities> hospitalFacilities) {
                // Update the cached copy of the words in the adapter.
//                adapter.setWords(words);

              hospitalFacilitiesList.addAll(hospitalFacilities);
                Log.d("HospitalFiltered", "onChanged: data retrieved ");
            }
        });

    } catch (NullPointerException e) {

        Log.d(TAG, "Exception: " + e.toString());
    }

    }




}