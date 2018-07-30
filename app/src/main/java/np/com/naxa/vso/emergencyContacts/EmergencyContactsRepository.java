package np.com.naxa.vso.emergencyContacts;

import android.content.Context;
import android.util.Pair;

import io.reactivex.Observable;
import np.com.naxa.vso.home.RawAssetLoader;
import np.com.naxa.vso.home.VSO;

public class EmergencyContactsRepository extends RawAssetLoader {
    private Context context;


    public EmergencyContactsRepository() {
        context = VSO.getInstance().getApplicationContext();
    }

    public Observable<Pair> getContactJsonString(int pos) {
        String assetName = getContactAssetName(pos);
        return loadTextFromAsset(assetName);
    }

    String assetName = null;
    private String getContactAssetName(int pos) {
        switch (pos) {
            case 0:
                assetName = "chairpersons_of_local_units.json";
                break;
            case 1:
                assetName = "chief_of_local_level_offices.json";
                break;
            case 2:
                assetName = "elected_representatives.json";
                break;
            case 3:
                assetName = "municipal_executive_members.json";
                break;
            case 4:
                assetName = "municipality_level_disaster_management_committee.json";
                break;
            case 5:
                assetName = "nntdss_executive_committee.json";
                break;
        }

        return assetName;
    }
}
