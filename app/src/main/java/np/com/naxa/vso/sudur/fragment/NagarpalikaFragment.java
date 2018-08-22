package np.com.naxa.vso.sudur.fragment;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import np.com.naxa.vso.R;
import np.com.naxa.vso.sudur.adapter.NagarpalikaBudget_Adapter;
import np.com.naxa.vso.sudur.model.NagarpalikaBudget_Model;

/**
 * A simple {@link Fragment} subclass.
 */
public class NagarpalikaFragment extends Fragment {

    public static final String MyPREFERENCES = "nagar_budget";
    SharedPreferences sharedpreferences;
    private boolean setData;

    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    ImageView imageView;

    String district_id_fromactivity = "0";

    NagarpalikaBudget_Adapter ca;
    public static List<NagarpalikaBudget_Model> resultCur = new ArrayList<>();
    public static List<NagarpalikaBudget_Model> filteredList = new ArrayList<>();

    String text = null;
    JSONArray data = null;
    String districtToFilter = "all is well";
    private List<NagarpalikaBudget_Model> FilteredBudgetWithCurrentDistrict;


    public NagarpalikaFragment() {
        // Required empty public constructor

    }


    public void setDistrictToFilter(String districtToFilter) {

        this.districtToFilter = districtToFilter;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_nagarpalika, container, false);

        sharedpreferences = getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.nagarList);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);


        FilteredBudgetWithCurrentDistrict = new ArrayList<>();

        try {
            populateBudgetAsync(districtToFilter);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return rootView;
    }


    private void populateBudgetAsync(final String distictNameEng) throws JSONException {
        String text = sharedpreferences.getString("nagar_budget", "");
        JSONObject nagarBugdgetJSON = new JSONObject(text);

        final JSONArray data = nagarBugdgetJSON.getJSONArray("data");

        FilteredBudgetWithCurrentDistrict.clear();

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < data.length(); i++) {
                    try {
                        JSONObject row = data.getJSONObject(i);
                        String EngDistrictName = row.getString("district_name_en");
                        String NepDistrictName = row.getString("district_name_np");

                        Boolean shouldThisDistBeAdded = EngDistrictName.equalsIgnoreCase(distictNameEng);




                        if (shouldThisDistBeAdded) {


                            NagarpalikaBudget_Model newData = new NagarpalikaBudget_Model();
                            newData.setDistrict_id(row.getString("district_id"));
                            newData.setNagar_title_en(row.getString("nagar_gau_palika"));
                            newData.setNagar_title_np(row.getString("nagar_gau_palika_np"));
                            newData.setNagar_budget_amount_en(row.getString("budget"));
                            newData.setNagar_budget_amount_np(row.getString("budget_np"));

                            FilteredBudgetWithCurrentDistrict.add(newData);

                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }



                fillTable(FilteredBudgetWithCurrentDistrict);


            }
        };
        new Thread(runnable).start();
    }


    public void fillTable(List<NagarpalikaBudget_Model> filteredList) {


        ca = new NagarpalikaBudget_Adapter(getActivity(), filteredList);
        recyclerView.setAdapter(ca);

    }

}
