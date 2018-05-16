package np.com.naxa.vso.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Pair;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import np.com.naxa.vso.R;
import np.com.naxa.vso.home.HomeActivity;
import np.com.naxa.vso.home.MapDataRepository;
import np.com.naxa.vso.utils.ProgressDialogUtils;
import np.com.naxa.vso.utils.ToastUtils;

public class LoadingActivity extends AppCompatActivity {

    private MapDataRepository repository;
    private List<String> assetNameList = new ArrayList<>();
    private List<String> fileContentList = new ArrayList<>();

    private ProgressDialog progressDialog;

    public static void start(Context context) {
        context.startActivity(new Intent(context, LoadingActivity.class));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        ToastUtils.showToast("Awesome Toast");

        progressDialog = new ProgressDialogUtils().getProgressDialog(LoadingActivity.this, "Loading");
        progressDialog.show();

        loadDataAndCallHomeActivity();
    }

    private void loadDataAndCallHomeActivity() {

        repository = new MapDataRepository();

        int position = 0;
        repository.getGeoJsonString(position)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(readGeoJason(position++))
                .flatMap(readGeoJason(position++))
                .flatMap(readGeoJason(position))
                .subscribe(new DisposableObserver<Pair>() {
                    @Override
                    public void onNext(Pair pair) {

                    }

                    @Override
                    public void onError(Throwable e) {
                        HomeActivity.start(LoadingActivity.this);
                    }

                    @Override
                    public void onComplete() {
                        progressDialog.dismiss();
                        HomeActivity.start(LoadingActivity.this, assetNameList, fileContentList);
                    }
                });

    }

    private Function<Pair, ObservableSource<Pair>> readGeoJason(int position) {
        return pair -> {
            String assetName = (String) pair.first;
            String fileContent = (String) pair.second;
            assetNameList.add(assetName);
            fileContentList.add(fileContent);
            Log.i("Shree", "Position is: " + position);
            return repository.getGeoJsonString(position + 1);
        };
    }
}
