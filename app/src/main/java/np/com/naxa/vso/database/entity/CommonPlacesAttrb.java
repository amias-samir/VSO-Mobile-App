package np.com.naxa.vso.database.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.Keep;

/**
 * Created by samir on 5/08/2018.
 */

@Keep
@Entity(tableName = "CommonPlacesAttrb",
        indices = {@Index(value = "latitude", unique = true),
                @Index(value = "longitude", unique = true)})
public class CommonPlacesAttrb {

    @PrimaryKey(autoGenerate = true)
    private int uid;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "address")
    private String address;

    @ColumnInfo(name = "type")
    private String type;

    @ColumnInfo(name = "latitude")
    private Double latitude;

    @ColumnInfo(name = "longitude")
    private Double longitude;

    @ColumnInfo(name = "remarks")
    private String remarks;

    public CommonPlacesAttrb(String name, String address, String type, Double latitude, Double longitude, String remarks) {
        this.name = name;
        this.address = address;
        this.type = type;
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
}
