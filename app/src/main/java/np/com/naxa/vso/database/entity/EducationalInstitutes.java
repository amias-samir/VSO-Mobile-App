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

@Entity(tableName = "educational_institutes",
        foreignKeys = @ForeignKey(
                entity = CommonPlacesAttrb.class,
                parentColumns = "uid",
                childColumns = "fk_common_places",
                onDelete = CASCADE),
        indices = @Index(value = "uid"))
public class EducationalInstitutes {
    @PrimaryKey(autoGenerate = true)
    private int eid;

    @ColumnInfo(name = "fk_common_places")
    private int fk_common_places;

    @ColumnInfo(name = "ambulance")
    private String college_he;

    @ColumnInfo(name = "ambulance")
    private String contact_no;

    @ColumnInfo(name = "ambulance")
    private String contact_pe;

    @ColumnInfo(name = "ambulance")
    private String earthquake;

    @ColumnInfo(name = "ambulance")
    private String evacuation;

    @ColumnInfo(name = "ambulance")
    private String female_stu;

    @ColumnInfo(name = "ambulance")
    private String male_stu;

    @ColumnInfo(name = "ambulance")
    private String total_stu;

    @ColumnInfo(name = "ambulance")
    private String fire_extin;

    @ColumnInfo(name = "ambulance")
    private String level;

    @ColumnInfo(name = "ambulance")
    private String open_space;

    @ColumnInfo(name = "ambulance")
    private String structure;

    public EducationalInstitutes(int fk_common_places, String college_he, String contact_no, String contact_pe, String earthquake, String evacuation, String female_stu, String male_stu, String total_stu, String fire_extin, String level, String open_space, String structure) {
        this.fk_common_places = fk_common_places;
        this.college_he = college_he;
        this.contact_no = contact_no;
        this.contact_pe = contact_pe;
        this.earthquake = earthquake;
        this.evacuation = evacuation;
        this.female_stu = female_stu;
        this.male_stu = male_stu;
        this.total_stu = total_stu;
        this.fire_extin = fire_extin;
        this.level = level;
        this.open_space = open_space;
        this.structure = structure;
    }

    public int getEid() {
        return eid;
    }

    public void setEid(int eid) {
        this.eid = eid;
    }

    public int getFk_common_places() {
        return fk_common_places;
    }

    public void setFk_common_places(int fk_common_places) {
        this.fk_common_places = fk_common_places;
    }

    public String getCollege_he() {
        return college_he;
    }

    public void setCollege_he(String college_he) {
        this.college_he = college_he;
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

    public String getEvacuation() {
        return evacuation;
    }

    public void setEvacuation(String evacuation) {
        this.evacuation = evacuation;
    }

    public String getFemale_stu() {
        return female_stu;
    }

    public void setFemale_stu(String female_stu) {
        this.female_stu = female_stu;
    }

    public String getMale_stu() {
        return male_stu;
    }

    public void setMale_stu(String male_stu) {
        this.male_stu = male_stu;
    }

    public String getTotal_stu() {
        return total_stu;
    }

    public void setTotal_stu(String total_stu) {
        this.total_stu = total_stu;
    }

    public String getFire_extin() {
        return fire_extin;
    }

    public void setFire_extin(String fire_extin) {
        this.fire_extin = fire_extin;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
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
}
