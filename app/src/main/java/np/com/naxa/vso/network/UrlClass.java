package np.com.naxa.vso.network;

/**
 * Created by Samir on 9/9/2017.
 */

public class UrlClass {

    public static final String REQUEST_OK = "200";
    public static final String REQUEST_401 = "401";
    public static final String REQUEST_400 = "400";

    public static final String BASE_URL = "http://app.naxa.com.np/";

    public static final String BASE_URL_REPORTING = "http://changu.dintnepal.org/";


    public static  String VSO_BASE_URL ;


    public static boolean isInvalidResponse(String responseCode) {
        return !responseCode.equals(UrlClass.REQUEST_OK);
    }
}
