package np.com.naxa.vso.activity;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class SplashActivity extends AppCompatActivity {

    private MapDataRepository repository;
    private List<String> assetNameList = new ArrayList<>();
    private List<String> fileContentList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spash);

        new Handler().postDelayed(() -> {
            loadDataAndCallHomeActivity();
        }, 2000);
    }

    private void loadDataAndCallHomeActivity() {

        repository = new MapDataRepository();

        int position = 0;
        repository.getGeoJsonString(position)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(readGeoJason(position++))
                .flatMap(readGeoJason(position++))
                .flatMap(readGeoJason(position))
                .subscribe(new DisposableObserver<Pair>() {
                    @Override
                    public void onNext(Pair pair) {

                    }

                    @Override
                    public void onError(Throwable e) {
                        HomeActivity.start(SplashActivity.this);
                    }

                    @Override
                    public void onComplete() {
                        HomeActivity.start(SplashActivity.this, assetNameList, fileContentList);
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
