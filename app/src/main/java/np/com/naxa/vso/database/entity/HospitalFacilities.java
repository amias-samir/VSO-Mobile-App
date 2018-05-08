package np.com.naxa.vso.database.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import static android.arch.persistence.room.ForeignKey.CASCADE;

/**
 * Created by samir on 5/08/2018.
 */

@Entity(tableName = "hospital_facilities",
        foreignKeys = @ForeignKey(
                entity = CommonPlacesAttrb.class,
                parentColumns = "uid",
                childColumns = "fk_common_places",
                onDelete = CASCADE),
        indices = @Index(value = "uid"))
public class HospitalFacilities {

    @PrimaryKey(autoGenerate = true)
    private int hid;

    @ColumnInfo(name = "fk_common_place")
    private int fk_common_places;

    @ColumnInfo(name = "ambulance")
    private String ambulance;

    @ColumnInfo(name = "contact_no")
    private String contact_no;

    @ColumnInfo(name = "contact_pe")
    private String contact_pe;

    @ColumnInfo(name = "earthquake")
    private String earthquake;

    @ColumnInfo(name = "emergency")
    private String emergency;

    @ColumnInfo(name = "fire_extin")
    private String fire_extin;

    @ColumnInfo(name = "icu_service")
    private String icu_service;

    @ColumnInfo(name = "number_of")
    private String number_of;

    @ColumnInfo(name = "open_space")
    private String open_space;

    @ColumnInfo(name = "structure")
    private String structure;

    @ColumnInfo(name = "toilet_fac")
    private String toilet_fac;

    public HospitalFacilities(int fk_common_places, String ambulance, String contact_no, String contact_pe, String earthquake, String emergency, String fire_extin, String icu_service, String number_of, String open_space, String structure, String toilet_fac) {
        this.fk_common_places = fk_common_places;
        this.ambulance = ambulance;
        this.contact_no = contact_no;
        this.contact_pe = contact_pe;
        this.earthquake = earthquake;
        this.emergency = emergency;
        this.fire_extin = fire_extin;
        this.icu_service = icu_service;
        this.number_of = number_of;
        this.open_space = open_space;
        this.structure = structure;
        this.toilet_fac = toilet_fac;
    }

    public int getFk_common_places() {
        return fk_common_places;
    }

    public void setFk_common_places(int fk_common_places) {
        this.fk_common_places = fk_common_places;
    }

    public String getAmbulance() {
        return ambulance;
    }

    public void setAmbulance(String ambulance) {
        this.ambulance = ambulance;
    }

    public String getContact_no() {
        return contact_no;
    }

    public void setContact_no(String contact_no) {
        this.contact_no = contact_no;
    }

    public String getContact_pe() {
        return contact_pe;
    }

    public void setContact_pe(String contact_pe) {
        this.contact_pe = contact_pe;
    }

    public String getEarthquake() {
        return earthquake;
    }

    public void setEarthquake(String earthquake) {
        this.earthquake = earthquake;
    }

    public String getEmergency() {
        return emergency;
    }

    public void setEmergency(String emergency) {
        this.emergency = emergency;
    }

    public String getFire_extin() {
        return fire_extin;
    }

    public void setFire_extin(String fire_extin) {
        this.fire_extin = fire_extin;
    }

    public String getIcu_service() {
        return icu_service;
    }

    public void setIcu_service(String icu_service) {
        this.icu_service = icu_service;
    }

    public String getNumber_of() {
        return number_of;
    }

    public void setNumber_of(String number_of) {
        this.number_of = number_of;
    }

    public String getOpen_space() {
        return open_space;
    }

    public void setOpen_space(String open_space) {
        this.open_space = open_space;
    }

    public String getStructure() {
        return structure;
    }

    public void setStructure(String structure) {
        this.structure = structure;
    }

    public String getToilet_fac() {
        return toilet_fac;
    }

    public void setToilet_fac(String toilet_fac) {
        this.toilet_fac = toilet_fac;
    }

    public int getHid() {
        return hid;
    }

    public void setHid(int hid) {
        this.hid = hid;
    }
}
