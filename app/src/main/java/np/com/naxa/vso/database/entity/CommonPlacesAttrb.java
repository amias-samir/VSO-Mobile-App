package np.com.naxa.vso.database.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Keep;

/**
 * Created by samir on 5/08/2018.
 */

@Keep
@Entity(tableName = "CommonPlacesAttrb",
        indices = {@Index(value = "latitude", unique = true),
                @Index(value = "longitude", unique = true)})
public class CommonPlacesAttrb implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    private int uid;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "address")
    private String address;

    @ColumnInfo(name = "places_type")
    private String type;

    @ColumnInfo(name = "detailed_properties")
    private String detailed_properties;

    @ColumnInfo(name = "latitude")
    private Double latitude;

    @ColumnInfo(name = "longitude")
    private Double longitude;

    @ColumnInfo(name = "remarks")
    private String remarks;

    public CommonPlacesAttrb(String name, String address, String type,  Double latitude, Double longitude, String remarks, String detailed_properties) {
        this.name = name;
        this.address = address;
        this.type = type;
        this.detailed_properties = detailed_properties;
        this.latitude = latitude;
        this.longitude = longitude;
        this.remarks = remarks;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

//    public String getCategory_type() {
//        return category_type;
//    }
//
//    public void setCategory_type(String category_type) {
//        this.category_type = category_type;
//    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.uid);
        dest.writeString(this.name);
        dest.writeString(this.address);
        dest.writeString(this.type);
        dest.writeString(this.detailed_properties);
        dest.writeValue(this.latitude);
        dest.writeValue(this.longitude);
        dest.writeString(this.remarks);
    }

    protected CommonPlacesAttrb(Parcel in) {
        this.uid = in.readInt();
        this.name = in.readString();
        this.address = in.readString();
        this.type = in.readString();
        this.detailed_properties = in.readString();
        this.latitude = (Double) in.readValue(Double.class.getClassLoader());
        this.longitude = (Double) in.readValue(Double.class.getClassLoader());
        this.remarks = in.readString();
    }

    public static final Parcelable.Creator<CommonPlacesAttrb> CREATOR = new Parcelable.Creator<CommonPlacesAttrb>() {
        @Override
        public CommonPlacesAttrb createFromParcel(Parcel source) {
            return new CommonPlacesAttrb(source);
        }

        @Override
        public CommonPlacesAttrb[] newArray(int size) {
            return new CommonPlacesAttrb[size];
        }
    };


    public String getDetailed_properties() {
        return detailed_properties;
    }

    public void setDetailed_properties(String detailed_properties) {
        this.detailed_properties = detailed_properties;
    }
}
