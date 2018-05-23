package np.com.naxa.vso.database.combinedentity;

import android.arch.persistence.room.Embedded;

import np.com.naxa.vso.database.entity.CommonPlacesAttrb;
import np.com.naxa.vso.database.entity.EducationalInstitutes;
import np.com.naxa.vso.database.entity.HospitalFacilities;

public class EducationAndCommon {

    @Embedded
    EducationalInstitutes educationalInstitutes;

    @Embedded
    CommonPlacesAttrb commonPlacesAttrb;

    public EducationalInstitutes getEducationalInstitutes() {
        return educationalInstitutes;
    }

    public void setEducationalInstitutes(EducationalInstitutes educationalInstitutes) {
        this.educationalInstitutes = educationalInstitutes;
    }

    public CommonPlacesAttrb getCommonPlacesAttrb() {
        return commonPlacesAttrb;
    }

    public void setCommonPlacesAttrb(CommonPlacesAttrb commonPlacesAttrb) {
        this.commonPlacesAttrb = commonPlacesAttrb;
    }
}
