package np.com.naxa.vso.database.combinedentity;

import android.arch.persistence.room.Embedded;

import np.com.naxa.vso.database.entity.CommonPlacesAttrb;
import np.com.naxa.vso.database.entity.HospitalFacilities;

public class HospitalAndCommon {
    @Embedded
    HospitalFacilities hospitalFacilities;

    @Embedded
    CommonPlacesAttrb commonPlacesAttrb;


    public HospitalFacilities getHospitalFacilities() {
        return hospitalFacilities;
    }

    public void setHospitalFacilities(HospitalFacilities hospitalFacilities) {
        this.hospitalFacilities = hospitalFacilities;
    }

    public CommonPlacesAttrb getCommonPlacesAttrb() {
        return commonPlacesAttrb;
    }

    public void setCommonPlacesAttrb(CommonPlacesAttrb commonPlacesAttrb) {
        this.commonPlacesAttrb = commonPlacesAttrb;
    }
}
