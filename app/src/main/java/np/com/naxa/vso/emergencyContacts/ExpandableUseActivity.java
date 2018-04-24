package np.com.naxa.vso.emergencyContacts;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.util.ArrayList;
import java.util.Random;

import np.com.naxa.vso.R;
import np.com.naxa.vso.ReportActivity;

/**
 * https://github.com/CymChad/BaseRecyclerViewAdapterHelper
 */
public class ExpandableUseActivity extends AppCompatActivity {
    RecyclerView mRecyclerView;
    ExpandableItemAdapter adapter;
    ArrayList<MultiItemEntity> list;


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

        mRecyclerView.setAdapter(adapter);
        // important! setLayoutManager should be called after setAdapter
        mRecyclerView.setLayoutManager(manager);


        Toolbar toolbar = findViewById(R.id.toolbar_general);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("Emergency contacts");
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
        lv1 = new Level1Item("Putali Hospital" , "01-4251551");
        lv0.addSubItem(lv1);

        lv1 = new Level1Item("Sita ram Hospital" , "01-4251551");
        lv0.addSubItem(lv1);

        lv1 = new Level1Item("Sumeru Hospital" , "01-4251551");
        lv0.addSubItem(lv1);

        lv1 = new Level1Item("New Hospital" , "01-4251551");
        lv0.addSubItem(lv1);

        res.add(lv0);

        lv0 = new Level0Item("Police", "");
        lv1 = new Level1Item("Police HQ" , "01-4251551");
        lv0.addSubItem(lv1);

        lv1 = new Level1Item("Armed Police HQ" , "01-4251551");
        lv0.addSubItem(lv1);

        lv1 = new Level1Item("Civil Police HQ" , "01-4251551");
        lv0.addSubItem(lv1);


        res.add(lv0);

        return res;
    }
}
