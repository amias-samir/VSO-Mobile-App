package np.com.naxa.vso.sudur.model.rest;

import com.github.simonpercic.oklog3.OkLogInterceptor;

import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class ApiClient {

    private static final String BASE_URL = "http://naxa.com.np/sudurapi/";
    private static Retrofit retrofit = null;
//    private static RxJavaCallAdapterFactory rxAdapter = RxJavaCallAdapterFactory.createWithScheduler(Schedulers.io());

    public static Retrofit getClient() {


        OkHttpClient okHttpClient = buildOkLogInterceptor();

        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL).client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }


    private static OkHttpClient buildOkLogInterceptor() {
        OkLogInterceptor okLogInterceptor = OkLogInterceptor.builder().build();
        OkHttpClient.Builder okHttpBuilder = new OkHttpClient.Builder();
        okHttpBuilder.addInterceptor(okLogInterceptor);
        OkHttpClient okHttpClient = okHttpBuilder.build();
        return okHttpClient;
    }


}