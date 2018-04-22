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

        removeDataAndNotify();

    }

    private void removeDataAndNotify() {
        final Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (!pojos.isEmpty()) {
                    handler.postDelayed(this, 2000);
                    pojos.remove(pojos.size() - 1);
                    adapter.setNewData(pojos);
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(EmergencyContactsActivity.this, "Data finished!!", Toast.LENGTH_SHORT).show();
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

        pojos.add(new EmergencyContactsPojo("Ram", "123"));
        pojos.add(new EmergencyContactsPojo("Shyam", "234"));
        pojos.add(new EmergencyContactsPojo("Hari", "345"));
        pojos.add(new EmergencyContactsPojo("Gita", "456"));
        pojos.add(new EmergencyContactsPojo("Sita", "567"));

        return pojos;
    }
}
