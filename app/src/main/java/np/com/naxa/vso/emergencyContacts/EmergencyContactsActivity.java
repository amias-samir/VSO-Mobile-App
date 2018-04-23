package np.com.naxa.vso.emergencyContacts;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import np.com.naxa.vso.R;

public class EmergencyContactsActivity extends AppCompatActivity {


    @BindView(R.id.rv_emergency_contacts_list)
    RecyclerView recyclerView;

    @BindView(R.id.toolbar_general)
    Toolbar toolbar;

    private EmergencyContactsRecyclerViewAdapter adapter;
    private List<EmergencyContactsPojo> pojos;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency_contacts);
        ButterKnife.bind(this);

        initToolbar();

        setupRecyclerView();

//        removeDataAndNotify();

    }

    private void removeDataAndNotify() {
        final Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                handler.postDelayed(this, 500);
                if (!pojos.isEmpty()) {
                    pojos.remove(pojos.size() - 1);
                    adapter.setNewData(pojos);
                    adapter.notifyDataSetChanged();
                } else {
                    adapter.setNewData(dummyContactlist());
                    adapter.notifyDataSetChanged();
                }

            }
        };
        handler.post(runnable);
    }

    private void initToolbar() {
        toolbar.setTitle("Emergency Contacts");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new EmergencyContactsRecyclerViewAdapter(dummyContactlist());
        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                Toast.makeText(EmergencyContactsActivity.this, "You clicked on position " + position, Toast.LENGTH_SHORT).show();
            }
        });
        recyclerView.setAdapter(adapter);
    }

    private List<EmergencyContactsPojo> dummyContactlist() {
        pojos = new ArrayList<>();

        pojos.add(new EmergencyContactsPojo("Hospital", "123"));
        pojos.add(new EmergencyContactsPojo("School", "234"));
        pojos.add(new EmergencyContactsPojo("Heritage", "345"));
        pojos.add(new EmergencyContactsPojo("Restaurant", "456"));
        pojos.add(new EmergencyContactsPojo("Colleges", "567"));

        return pojos;
    }
}
