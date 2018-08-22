package np.com.naxa.vso.sudur.model.rest;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ApiInterface {


    @FormUrlEncoded
    @POST("selectBusiness")
    Call<Data> getMenu(@Field("last_sync_date_time") String data);


}
