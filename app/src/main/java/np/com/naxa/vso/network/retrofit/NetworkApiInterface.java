package np.com.naxa.vso.network.retrofit;



import np.com.naxa.vso.network.model.AskForHelpResponse;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Created by Samir on 5/7/2018.
 */

public interface NetworkApiInterface {

    @Multipart
    @POST("ask_for_help")
    Call<AskForHelpResponse> getAskForHelpResponse(@Part MultipartBody.Part file,
                                                     @Part("data") RequestBody jsonToSend);
}

