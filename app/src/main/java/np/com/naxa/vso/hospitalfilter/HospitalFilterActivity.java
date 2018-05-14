package np.com.naxa.vso.hospitalfilter;

import android.os.Bundle;
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
import me.riddhimanadib.formmaster.model.FormElementSwitch;
import me.riddhimanadib.formmaster.model.FormHeader;
import np.com.naxa.vso.R;

public class HospitalFilterActivity extends AppCompatActivity implements OnFormElementValueChangedListener {

    @BindView(R.id.btn_filter)
    Button btnFilter;
    private RecyclerView mRecyclerView;
    private FormBuilder mFormBuilder;

    private static final int TAG_WARD = 01;
    private static final int TAG_HOSPITAL_TYPE = 02;
    private static final int TAG_BED = 03;
    private static final int TAG_BED_CAPACITY = 04;
    private static final int TAG_BUILDING_STRUCTURE = 05;
    private static final int TAG_AVAILABLE_FACILITIES = 06;
    private static final int TAG_EXCAVATION_PLANS = 07;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospital_filter);
        ButterKnife.bind(this);

        setupToolBar();

        setupForm();


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
        List<String> hospitalTypeList = new ArrayList<>();
        hospitalTypeList.add("Type 1");
        hospitalTypeList.add("Type 2");
        hospitalTypeList.add("Type 3");
        hospitalTypeList.add("Type 4");
        hospitalTypeList.add("Type 5");
        hospitalTypeList.add("Type 6");
        FormElementPickerSingle hospitalTypeElement = FormElementPickerSingle.createInstance().setTag(TAG_HOSPITAL_TYPE).setTitle("Hospital Type").setOptions(hospitalTypeList).setPickerTitle("Pick any type");


        FormHeader headerBedCapacity = FormHeader.createInstance("Bed Capacity");
        FormElementSwitch bedCapacitySwitcher = FormElementSwitch.createInstance().setTag(TAG_BED).setTitle("Bed").setSwitchTexts("Yes", "No");
        List<String> bedCapacity = new ArrayList<>();
        bedCapacity.add("0-10");
        bedCapacity.add("11-20");
        bedCapacity.add("21-30");
        bedCapacity.add("31-40");
        bedCapacity.add("41-50");
        bedCapacity.add("50+");
        FormElementPickerMulti bedCapacityElement = FormElementPickerMulti.createInstance().setTag(TAG_BED_CAPACITY).setTitle("Bed Capacity").setOptions(bedCapacity).setPickerTitle("Choose one or more bed capacity").setNegativeText("reset");

        FormHeader headerBuildingStructure = FormHeader.createInstance("Structure");
        FormElementSwitch buildingStructureSwitcher = FormElementSwitch.createInstance().setTag(TAG_BUILDING_STRUCTURE).setTitle("Building Structure").setSwitchTexts("Yes", "No");

        FormHeader headerAvailiableFacilities = FormHeader.createInstance("Facilities");
        FormElementSwitch availiableFacilitiesSwitcher = FormElementSwitch.createInstance().setTag(TAG_AVAILABLE_FACILITIES).setTitle("Available Facilities").setSwitchTexts("Yes", "No");

        FormHeader headerBuildingExcavation = FormHeader.createInstance("Building Excavation");
        FormElementSwitch availiableBuildingExcavationSwitcher = FormElementSwitch.createInstance().setTag(TAG_EXCAVATION_PLANS).setTitle(" Available building excavation plans").setSwitchTexts("Yes", "No");


        List<BaseFormElement> formItems = new ArrayList<>();
        formItems.add(headerWard);
        formItems.add(wardListElement);

        formItems.add(headerHospitalType);
        formItems.add(hospitalTypeElement);

        formItems.add(headerBedCapacity);
        formItems.add(bedCapacitySwitcher);
        formItems.add(bedCapacityElement);

        formItems.add(headerBuildingStructure);
        formItems.add(buildingStructureSwitcher);

        formItems.add(headerAvailiableFacilities);
        formItems.add(availiableFacilitiesSwitcher);

        formItems.add(headerBuildingExcavation);
        formItems.add(availiableBuildingExcavationSwitcher);

        mFormBuilder.addFormElements(formItems);
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

        String ward,hospital_type, bed, bed_capacity, building_structure, available_facilities, excavation_plans;

        BaseFormElement wardElement = mFormBuilder.getFormElement(TAG_WARD);
        BaseFormElement hospitalTypeElement = mFormBuilder.getFormElement(TAG_HOSPITAL_TYPE);
        BaseFormElement bedElement = mFormBuilder.getFormElement(TAG_BED);
        BaseFormElement bedCapacityElement = mFormBuilder.getFormElement(TAG_BED_CAPACITY);
        BaseFormElement buildingStructureElement = mFormBuilder.getFormElement(TAG_BUILDING_STRUCTURE);
        BaseFormElement availableFacilitiesElement = mFormBuilder.getFormElement(TAG_AVAILABLE_FACILITIES);
        BaseFormElement excavationPlansElement = mFormBuilder.getFormElement(TAG_EXCAVATION_PLANS);

        ward = wardElement.getValue();
        hospital_type = hospitalTypeElement.getValue();
        bed = bedElement.getValue();
        bed_capacity = bedCapacityElement.getValue();
        building_structure = buildingStructureElement.getValue();
        available_facilities = availableFacilitiesElement.getValue();
        excavation_plans = excavationPlansElement.getValue();

        Log.d("HospitalFilter", "getFormData: "+ward +" , "+ bed_capacity + " , "+excavation_plans);
    }


}