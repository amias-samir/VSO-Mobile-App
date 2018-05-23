package np.com.naxa.vso.hospitalfilter;

import java.util.LinkedHashMap;
import java.util.List;

import np.com.naxa.vso.database.combinedentity.HospitalAndCommon;


public class SortedHospitalItem {

    HospitalAndCommon hospitalAndCommon;
    String distance;

    public SortedHospitalItem(HospitalAndCommon hospitalAndCommon, String distance) {
        this.hospitalAndCommon = hospitalAndCommon;
        this.distance = distance;
    }

    public HospitalAndCommon getHospitalAndCommon() {
        return hospitalAndCommon;
    }

    public void setHospitalAndCommon(HospitalAndCommon hospitalAndCommon) {
        this.hospitalAndCommon = hospitalAndCommon;
    }

    public String getDistance() {
        String distanceInKmMeter;
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }
}
