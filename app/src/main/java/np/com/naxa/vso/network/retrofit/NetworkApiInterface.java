package np.com.naxa.vso.network.retrofit;



import io.reactivex.Observable;
import np.com.naxa.vso.network.model.AskForHelpResponse;
import np.com.naxa.vso.network.model.GeoJsonCategoryDetails;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Created by Samir on 5/7/2018.
 */

public interface NetworkApiInterface {

    @Multipart
    @POST("ReportController/report_insert_api")
    Call<AskForHelpResponse> getAskForHelpResponse(@Part MultipartBody.Part file,
                                                     @Part("data") RequestBody jsonToSend);

    @GET("MapApi/get_category")
    Observable<GeoJsonCategoryDetails> getGeoJsonCategoryDetails();

}

