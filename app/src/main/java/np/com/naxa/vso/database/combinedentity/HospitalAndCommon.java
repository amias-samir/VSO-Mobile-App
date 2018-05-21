package np.com.naxa.vso.database.combinedentity;

import android.arch.persistence.room.Embedded;
import android.os.Parcel;
import android.os.Parcelable;

import np.com.naxa.vso.database.entity.CommonPlacesAttrb;
import np.com.naxa.vso.database.entity.HospitalFacilities;

public class HospitalAndCommon implements Parcelable {
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


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.hospitalFacilities, flags);
        dest.writeParcelable(this.commonPlacesAttrb, flags);
    }

    public HospitalAndCommon() {
    }

    protected HospitalAndCommon(Parcel in) {
        this.hospitalFacilities = in.readParcelable(HospitalFacilities.class.getClassLoader());
        this.commonPlacesAttrb = in.readParcelable(CommonPlacesAttrb.class.getClassLoader());
    }

    public static final Parcelable.Creator<HospitalAndCommon> CREATOR = new Parcelable.Creator<HospitalAndCommon>() {
        @Override
        public HospitalAndCommon createFromParcel(Parcel source) {
            return new HospitalAndCommon(source);
        }

        @Override
        public HospitalAndCommon[] newArray(int size) {
            return new HospitalAndCommon[size];
        }
    };
}
