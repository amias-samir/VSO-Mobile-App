package np.com.naxa.vso.network.retrofit;

import android.util.Log;


import java.lang.reflect.Field;

import np.com.naxa.vso.network.UrlClass;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Samir on 5/7/2018.
 */

public class ErrorSupportCallback<T> implements Callback<T> {

    private static final String TAG = "ErrorSupportCallback";
    private Callback<T> callback;

    public ErrorSupportCallback(Callback<T> callback) {
        this.callback = callback;
    }

    @Override
    public void onResponse(Call<T> call, Response<T> response) {

        String url = response.raw().request().url().toString();

        if (response.body() == null) {

            callback.onFailure(call, new NullPointerException("Empty response"));

        } else if (UrlClass.isInvalidResponse(getResponseCode(response.body()))) {


            callback.onFailure(call, new Exception("Server did not return 200 status"));

        } else {


            callback.onResponse(call, response);
        }
    }

    @Override
    public void onFailure(Call<T> call, Throwable t) {
        Log.e(TAG,"Request Failed "+ t.toString());

        callback.onFailure(call, t);
    }

    private String getResponseCode(Object someObject) {
        String responseCode = "";

        try {
            responseCode = parseResponseCode(someObject);
        } catch (IllegalAccessException | NullPointerException e) {
            e.printStackTrace();
        }

        return responseCode;

    }

    private String parseResponseCode(Object someObject) throws NullPointerException, IllegalAccessException {

        String responseCode = "500";

        for (Field field : someObject.getClass().getDeclaredFields()) {

            field.setAccessible(true);
            Object value;

            value = field.get(someObject);

            if (field.getName().equalsIgnoreCase("status")) {
                responseCode = value.toString();
            }
        }

        return responseCode;
    }

}