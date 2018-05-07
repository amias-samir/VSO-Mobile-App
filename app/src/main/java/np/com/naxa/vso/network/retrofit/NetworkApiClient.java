package np.com.naxa.vso.network.retrofit;




import com.github.simonpercic.oklog3.OkLogInterceptor;

import np.com.naxa.vso.network.UrlClass;
import okhttp3.Dispatcher;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Samir on 5/7/2018.
 */

public class NetworkApiClient {

    private static Retrofit retrofit = null;

    public static Retrofit getAPIClient() {

        if (retrofit == null) {
            OkLogInterceptor okLogInterceptor = OkLogInterceptor.builder().build();
            OkHttpClient.Builder okHttpBuilder = new OkHttpClient.Builder();

            Dispatcher dispatcher = new Dispatcher();
            dispatcher.setMaxRequests(3);

            okHttpBuilder.dispatcher(dispatcher);
            okHttpBuilder.addInterceptor(okLogInterceptor);

            OkHttpClient okHttpClient = okHttpBuilder.build();
            retrofit = new Retrofit.Builder()
                    .baseUrl(UrlClass.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(okHttpClient)
                    .build();
        }
        return retrofit;

    }


}
