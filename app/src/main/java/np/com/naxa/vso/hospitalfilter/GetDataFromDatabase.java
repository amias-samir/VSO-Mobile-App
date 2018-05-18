package np.com.naxa.vso.hospitalfilter;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import java.security.acl.Owner;
import java.util.ArrayList;
import java.util.List;

import np.com.naxa.vso.viewmodel.HospitalFacilitiesVewModel;

public class GetDataFromDatabase {
    private static final String TAG = "GetDataFromDatabase";


    public static List<String> getBedListDistinct(HospitalFacilitiesVewModel hospitalFacilitiesVewModel, LifecycleOwner owner){
        List<String> distinctValuesList = new ArrayList<String>();
        Log.d(TAG, "getDistinctValuesFromColumn: ");
        hospitalFacilitiesVewModel.getAllBedCapacityList().observe(owner, new Observer<List<String>>() {
            @Override
            public void onChanged(@NonNull final List<String> distinctValuesList) {
                distinctValuesList.addAll(distinctValuesList);
            }
        });
        return distinctValuesList;
    }

    public static List<String> geTypeListDistinct(HospitalFacilitiesVewModel hospitalFacilitiesVewModel, LifecycleOwner owner){
        List<String> distinctValuesList = new ArrayList<String>();
        Log.d(TAG, "getDistinctValuesFromColumn: ");
        hospitalFacilitiesVewModel.getAllTypeList().observe(owner, new Observer<List<String>>() {
            @Override
            public void onChanged(@NonNull final List<String> distinctValuesList) {
                distinctValuesList.addAll(distinctValuesList);
            }
        });
        return distinctValuesList;
    }

    public static List<String> getStructureTypeListDistinct(HospitalFacilitiesVewModel hospitalFacilitiesVewModel, LifecycleOwner owner){
        List<String> distinctValuesList = new ArrayList<String>();
        Log.d(TAG, "getDistinctValuesFromColumn: ");
        hospitalFacilitiesVewModel.getAllStructureTypeList().observe(owner, new Observer<List<String>>() {
            @Override
            public void onChanged(@NonNull final List<String> distinctValuesList) {
                distinctValuesList.addAll(distinctValuesList);
            }
        });
        return distinctValuesList;
    }


    public static List<String> getEvacuationPlanListDistinct(HospitalFacilitiesVewModel hospitalFacilitiesVewModel, LifecycleOwner owner){
        List<String> distinctValuesList = new ArrayList<String>();
        Log.d(TAG, "getDistinctValuesFromColumn: ");
        hospitalFacilitiesVewModel.getAllEvacuationPlanList().observe(owner, new Observer<List<String>>() {
            @Override
            public void onChanged(@NonNull final List<String> distinctValuesList) {
                distinctValuesList.addAll(distinctValuesList);
            }
        });
        return distinctValuesList;
    }


}
