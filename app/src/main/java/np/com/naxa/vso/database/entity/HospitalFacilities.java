package np.com.naxa.vso.database.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Keep;

import static android.arch.persistence.room.ForeignKey.CASCADE;

/**
 * Created by samir on 5/08/2018.
 */

@Entity(tableName = "hospital_facilities",
        foreignKeys = @ForeignKey(
                entity = CommonPlacesAttrb.class,
                parentColumns = "uid",
                childColumns = "fk_common_places",
                onDelete = CASCADE))
public class HospitalFacilities implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    private int hid;

    @ColumnInfo(name = "fk_common_places")
    private Long fk_common_places;

    @ColumnInfo(name = "category")
    private String Category;

    @ColumnInfo(name = "type")
    private String Type;

    @ColumnInfo(name = "open_space")
    private String Open_Space;

    @ColumnInfo(name = "contact_number")
    private String Contact_Number;

    @ColumnInfo(name = "contact_person")
    private String Contact_Person;

    @ColumnInfo(name = "emergency_service")
    private String Emergency_Service;

    @ColumnInfo(name = "icu_service")
    private String ICU_Service;

    @ColumnInfo(name = "ambulance_service")
    private String Ambulance_Service;

    @ColumnInfo(name = "number_of_beds")
    private String Number_of_Beds;

    @ColumnInfo(name = "structure_type")
    private String Structure_Type;

    @ColumnInfo(name = "earthquake_damage")
    private String Earthquake_Damage;

    @ColumnInfo(name = "toilet_facility")
    private String Toilet_Facility;

    @ColumnInfo(name = "fire_extinguisher")
    private String Fire_Extinguisher;

    @ColumnInfo(name = "evacuation_plan")
    private String Evacuation_Plan;

    @ColumnInfo(name = "alternatice_route")
    private String Alternatice_Route;

    @ColumnInfo(name = "no_of_doctors")
    private String No_of_Doctors;

    @ColumnInfo(name = "no_of_nurses")
    private String No_of_Nurses;

    @ColumnInfo(name = "no_of_health_assistant")
    private String No_of_Health_Assistant;

    @ColumnInfo(name = "total_no_of_employees")
    private String Total_No_of_Employees;

    @ColumnInfo(name = "water_storage_capacity_litre_")
    private String Water_Storage_Capacity_Litre_;

    @ColumnInfo(name = "emergency_stock_capacity")
    private String Emergency_Stock_Capacity;

    @ColumnInfo(name = "ict_grading_a_b_c_d")
    private String ICT_Grading_A_B_C_D;

    public HospitalFacilities(Long fk_common_places, String Category, String Type, String Open_Space, String Contact_Number,
                              String Contact_Person, String Emergency_Service, String ICU_Service, String Ambulance_Service,
                              String Number_of_Beds, String Structure_Type, String Earthquake_Damage, String Toilet_Facility,
                              String Fire_Extinguisher, String Evacuation_Plan, String Alternatice_Route, String No_of_Doctors,
                              String No_of_Nurses, String No_of_Health_Assistant, String Total_No_of_Employees,
                              String Water_Storage_Capacity_Litre_, String Emergency_Stock_Capacity, String ICT_Grading_A_B_C_D) {
        this.fk_common_places = fk_common_places;
        this.Category = Category;
        this.Type = Type;
        this.Open_Space = Open_Space;
        this.Contact_Number = Contact_Number;
        this.Contact_Person = Contact_Person;
        this.Emergency_Service = Emergency_Service;
        this.ICU_Service = ICU_Service;
        this.Ambulance_Service = Ambulance_Service;
        this.Number_of_Beds = Number_of_Beds;
        this.Structure_Type = Structure_Type;
        this.Earthquake_Damage = Earthquake_Damage;
        this.Toilet_Facility = Toilet_Facility;
        this.Fire_Extinguisher = Fire_Extinguisher;
        this.Evacuation_Plan = Evacuation_Plan;
        this.Alternatice_Route = Alternatice_Route;
        this.No_of_Doctors = No_of_Doctors;
        this.No_of_Nurses = No_of_Nurses;
        this.No_of_Health_Assistant = No_of_Health_Assistant;
        this.Total_No_of_Employees = Total_No_of_Employees;
        this.Water_Storage_Capacity_Litre_ = Water_Storage_Capacity_Litre_;
        this.Emergency_Stock_Capacity = Emergency_Stock_Capacity;
        this.ICT_Grading_A_B_C_D = ICT_Grading_A_B_C_D;
    }

    public int getHid() {
        return hid;
    }

    public void setHid(int hid) {
        this.hid = hid;
    }

    public Long getFk_common_places() {
        return fk_common_places;
    }

    public void setFk_common_places(Long fk_common_places) {
        this.fk_common_places = fk_common_places;
    }

    public String getCategory() {
        return Category;
    }

    public void setCategory(String category) {
        Category = category;
    }

    public String getOpen_Space() {
        return Open_Space;
    }

    public void setOpen_Space(String open_Space) {
        Open_Space = open_Space;
    }

    public String getContact_Number() {
        return Contact_Number;
    }

    public void setContact_Number(String contact_Number) {
        Contact_Number = contact_Number;
    }

    public String getContact_Person() {
        return Contact_Person;
    }

    public void setContact_Person(String contact_Person) {
        Contact_Person = contact_Person;
    }

    public String getEmergency_Service() {
        return Emergency_Service;
    }

    public void setEmergency_Service(String emergency_Service) {
        Emergency_Service = emergency_Service;
    }

    public String getICU_Service() {
        return ICU_Service;
    }

    public void setICU_Service(String ICU_Service) {
        this.ICU_Service = ICU_Service;
    }

    public String getAmbulance_Service() {
        return Ambulance_Service;
    }

    public void setAmbulance_Service(String ambulance_Service) {
        Ambulance_Service = ambulance_Service;
    }

    public String getNumber_of_Beds() {
        return Number_of_Beds;
    }

    public void setNumber_of_Beds(String number_of_Beds) {
        Number_of_Beds = number_of_Beds;
    }

    public String getStructure_Type() {
        return Structure_Type;
    }

    public void setStructure_Type(String structure_Type) {
        Structure_Type = structure_Type;
    }

    public String getEarthquake_Damage() {
        return Earthquake_Damage;
    }

    public void setEarthquake_Damage(String earthquake_Damage) {
        Earthquake_Damage = earthquake_Damage;
    }

    public String getToilet_Facility() {
        return Toilet_Facility;
    }

    public void setToilet_Facility(String toilet_Facility) {
        Toilet_Facility = toilet_Facility;
    }

    public String getFire_Extinguisher() {
        return Fire_Extinguisher;
    }

    public void setFire_Extinguisher(String fire_Extinguisher) {
        Fire_Extinguisher = fire_Extinguisher;
    }

    public String getEvacuation_Plan() {
        return Evacuation_Plan;
    }

    public void setEvacuation_Plan(String evacuation_Plan) {
        Evacuation_Plan = evacuation_Plan;
    }

    public String getAlternatice_Route() {
        return Alternatice_Route;
    }

    public void setAlternatice_Route(String alternatice_Route) {
        Alternatice_Route = alternatice_Route;
    }

    public String getNo_of_Doctors() {
        return No_of_Doctors;
    }

    public void setNo_of_Doctors(String no_of_Doctors) {
        No_of_Doctors = no_of_Doctors;
    }

    public String getNo_of_Nurses() {
        return No_of_Nurses;
    }

    public void setNo_of_Nurses(String no_of_Nurses) {
        No_of_Nurses = no_of_Nurses;
    }

    public String getNo_of_Health_Assistant() {
        return No_of_Health_Assistant;
    }

    public void setNo_of_Health_Assistant(String no_of_Health_Assistant) {
        No_of_Health_Assistant = no_of_Health_Assistant;
    }

    public String getTotal_No_of_Employees() {
        return Total_No_of_Employees;
    }

    public void setTotal_No_of_Employees(String total_No_of_Employees) {
        Total_No_of_Employees = total_No_of_Employees;
    }

    public String getWater_Storage_Capacity_Litre_() {
        return Water_Storage_Capacity_Litre_;
    }

    public void setWater_Storage_Capacity_Litre_(String water_Storage_Capacity_Litre_) {
        Water_Storage_Capacity_Litre_ = water_Storage_Capacity_Litre_;
    }

    public String getEmergency_Stock_Capacity() {
        return Emergency_Stock_Capacity;
    }

    public void setEmergency_Stock_Capacity(String emergency_Stock_Capacity) {
        Emergency_Stock_Capacity = emergency_Stock_Capacity;
    }

    public String getICT_Grading_A_B_C_D() {
        return ICT_Grading_A_B_C_D;
    }

    public void setICT_Grading_A_B_C_D(String ICT_Grading_A_B_C_D) {
        this.ICT_Grading_A_B_C_D = ICT_Grading_A_B_C_D;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.hid);
        dest.writeValue(this.fk_common_places);
        dest.writeString(this.Category);
        dest.writeString(this.Type);
        dest.writeString(this.Open_Space);
        dest.writeString(this.Contact_Number);
        dest.writeString(this.Contact_Person);
        dest.writeString(this.Emergency_Service);
        dest.writeString(this.ICU_Service);
        dest.writeString(this.Ambulance_Service);
        dest.writeString(this.Number_of_Beds);
        dest.writeString(this.Structure_Type);
        dest.writeString(this.Earthquake_Damage);
        dest.writeString(this.Toilet_Facility);
        dest.writeString(this.Fire_Extinguisher);
        dest.writeString(this.Evacuation_Plan);
        dest.writeString(this.Alternatice_Route);
        dest.writeString(this.No_of_Doctors);
        dest.writeString(this.No_of_Nurses);
        dest.writeString(this.No_of_Health_Assistant);
        dest.writeString(this.Total_No_of_Employees);
        dest.writeString(this.Water_Storage_Capacity_Litre_);
        dest.writeString(this.Emergency_Stock_Capacity);
        dest.writeString(this.ICT_Grading_A_B_C_D);
    }

    protected HospitalFacilities(Parcel in) {
        this.hid = in.readInt();
        this.fk_common_places = (Long) in.readValue(Long.class.getClassLoader());
        this.Category = in.readString();
        this.Type = in.readString();
        this.Open_Space = in.readString();
        this.Contact_Number = in.readString();
        this.Contact_Person = in.readString();
        this.Emergency_Service = in.readString();
        this.ICU_Service = in.readString();
        this.Ambulance_Service = in.readString();
        this.Number_of_Beds = in.readString();
        this.Structure_Type = in.readString();
        this.Earthquake_Damage = in.readString();
        this.Toilet_Facility = in.readString();
        this.Fire_Extinguisher = in.readString();
        this.Evacuation_Plan = in.readString();
        this.Alternatice_Route = in.readString();
        this.No_of_Doctors = in.readString();
        this.No_of_Nurses = in.readString();
        this.No_of_Health_Assistant = in.readString();
        this.Total_No_of_Employees = in.readString();
        this.Water_Storage_Capacity_Litre_ = in.readString();
        this.Emergency_Stock_Capacity = in.readString();
        this.ICT_Grading_A_B_C_D = in.readString();
    }

    public static final Parcelable.Creator<HospitalFacilities> CREATOR = new Parcelable.Creator<HospitalFacilities>() {
        @Override
        public HospitalFacilities createFromParcel(Parcel source) {
            return new HospitalFacilities(source);
        }

        @Override
        public HospitalFacilities[] newArray(int size) {
            return new HospitalFacilities[size];
        }
    };
}
