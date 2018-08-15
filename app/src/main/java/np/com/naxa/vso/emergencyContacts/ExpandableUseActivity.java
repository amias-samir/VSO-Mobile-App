package np.com.naxa.vso.emergencyContacts;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import np.com.naxa.vso.R;
import np.com.naxa.vso.activity.ReportActivity;
import np.com.naxa.vso.home.HomeActivity;
import np.com.naxa.vso.utils.ToastUtils;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

import static np.com.naxa.vso.Permissions.RC_PHONE;

/**
 * https://github.com/CymChad/BaseRecyclerViewAdapterHelper
 */
public class ExpandableUseActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks {
    RecyclerView mRecyclerView;
    ExpandableItemAdapter adapter;
    ArrayList<MultiItemEntity> list;

    EmergencyContactsRepository repository;

    String currentNumber = null;
    int jsonPosition;
    private static final String TAG = "ExpandableUseActivity";

    public static void start(Context context) {
        Intent intent = new Intent(context, ExpandableUseActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_expandable_item_use);

        mRecyclerView = (RecyclerView) findViewById(R.id.rv);

        repository = new EmergencyContactsRepository();

        getContactList(0);

//        list = generateData();
//
//        adapter = new ExpandableItemAdapter(list);

        final GridLayoutManager manager = new GridLayoutManager(this, 3);
        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return adapter.getItemViewType(position) == ExpandableItemAdapter.TYPE_PERSON ? 1 : manager.getSpanCount();
            }
        });

        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                Level1Item item = (Level1Item) adapter.getItem(position);
                currentNumber = item.subTitle;
                checkPhonePermissionAndGo();
            }
        });

        mRecyclerView.setAdapter(adapter);
        // important! setLayoutManager should be called after setAdapter
        mRecyclerView.setLayoutManager(manager);


        initToolbar();
    }

    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar_general);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(ExpandableUseActivity.this.getResources().getString(R.string.emergency_contacts));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @SuppressLint("MissingPermission")
    private void makeCall() {
        if (currentNumber == null) {
            return;
        }
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + currentNumber));
        startActivity(intent);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @AfterPermissionGranted(RC_PHONE)
    private void checkPhonePermissionAndGo() {
        String phonePerm = Manifest.permission.CALL_PHONE;
        if (EasyPermissions.hasPermissions(this, phonePerm)) {
            makeCall();
        } else {
            EasyPermissions.requestPermissions(this, "Allow phone permission to make call.",
                    RC_PHONE, phonePerm);
        }
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        if (requestCode == RC_PHONE) {
            makeCall();
        }

    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        if (requestCode == RC_PHONE) {
            if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
                new AppSettingsDialog.Builder(this).build().show();
            } else {
                ToastUtils.showToast("Phone permission is required to make call.");
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==RC_PHONE){
            checkPhonePermissionAndGo();
        }
    }

    ArrayList<MultiItemEntity> res = new ArrayList<>();
    Level0Item lv0 = null;
    Level1Item lv1 = null;
    private Level0Item getContactList(int position){
        Log.d(TAG, "onNext: "+position);

        repository.getContactJsonString(position)
                .subscribe(new Observer<Pair>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }
                    @Override
                    public void onNext(Pair pair) {
                        String assetName = (String) pair.first;
                        String fileContent = (String) pair.second;
//                        Log.d(TAG, "onNext: "+fileContent);

                        lv0 = new Level0Item(getContactCategoryName(position), "");
                        Gson gson = new Gson();
                        try {
                            JSONArray jsonArray = new JSONArray(fileContent);
                            for (int i =0; i < jsonArray.length() ; i++){
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                lv1 = new Level1Item(jsonObject.getString("Name"),
                                        (jsonObject.getString("Phone no.") == null) ? (" ") : (jsonObject.getString("Phone no.")),
                                        jsonObject.getString("Post")+", "+jsonObject.getString("Organization"),
                                        jsonObject.getString("Address"));
                                lv0.addSubItem(lv1);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(ExpandableUseActivity.this, "An error occurred while loading json", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "onComplete: "+jsonPosition);
                        jsonPosition++;
                        if (jsonPosition > 6){
                            list = res;
                            adapter = new ExpandableItemAdapter(list);
                            return;
                        }
                        res.add(lv0);
                        getContactList(jsonPosition);

                    }
                });
        return lv0 ;
    }

    private String getContactCategoryName(int position){
        String categoryName = "";
        switch (position) {
            case 0:
                categoryName = getApplicationContext().getResources().getString(R.string.chairpersons_of_local_units);
                break;
            case 1:
                categoryName = getApplicationContext().getResources().getString(R.string.chief_of_local_level_offices);
                break;
            case 2:
                categoryName = getApplicationContext().getString(R.string.elected_representatives);
                break;
            case 3:
                categoryName = getApplicationContext().getString(R.string.municipal_executive_members);
                break;

            case 4:
                categoryName = getApplicationContext().getString(R.string.municipality_level_disaster_management);
                break;
            case 5:
                categoryName = getApplicationContext().getString(R.string.nntds_executive_commitee);
                break;
        }
        return categoryName;
    }


}
