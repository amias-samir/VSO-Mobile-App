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
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import np.com.naxa.vso.R;
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

    String currentNumber = null;

    public static void start(Context context) {
        Intent intent = new Intent(context, ExpandableUseActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_expandable_item_use);

        mRecyclerView = (RecyclerView) findViewById(R.id.rv);

        list = generateData();
        adapter = new ExpandableItemAdapter(list);

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


        Toolbar toolbar = findViewById(R.id.toolbar_general);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("Emergency contacts");
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

    private ArrayList<MultiItemEntity> generateData() {
        int lv0Count = 2;
        int lv1Count = 3;
        int personCount = 5;

        String[] nameList = {"Bob", "Andy", "Lily", "Brown", "Bruce"};
        Random random = new Random();


        ArrayList<MultiItemEntity> res = new ArrayList<>();
        Level0Item lv0 = null;
        Level1Item lv1 = null;

        lv0 = new Level0Item("Hospital", "");
        lv1 = new Level1Item("Putali Hospital", "01-4251551");
        lv0.addSubItem(lv1);

        lv1 = new Level1Item("Sita ram Hospital", "01-4251551");
        lv0.addSubItem(lv1);

        lv1 = new Level1Item("Sumeru Hospital", "01-4251551");
        lv0.addSubItem(lv1);

        lv1 = new Level1Item("New Hospital", "01-4251551");
        lv0.addSubItem(lv1);

        res.add(lv0);

        lv0 = new Level0Item("Police", "");
        lv1 = new Level1Item("Police HQ", "01-4251551");
        lv0.addSubItem(lv1);

        lv1 = new Level1Item("Armed Police HQ", "01-4251551");
        lv0.addSubItem(lv1);

        lv1 = new Level1Item("Civil Police HQ", "01-4251551");
        lv0.addSubItem(lv1);

        res.add(lv0);

        return res;
    }
}
