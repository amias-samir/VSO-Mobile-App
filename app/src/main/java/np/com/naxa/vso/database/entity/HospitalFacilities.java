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

    @ColumnInfo(name = "ward")
    private String Ward;

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

    @ColumnInfo(name = "no_of_rooms")
    private String No_of_Rooms;

    @ColumnInfo(name = "no_of_stories")
    private String No_of_Stories;

    @ColumnInfo(name = "emergency_phone_number")
    private String Emergency_Phone_Number;

    @ColumnInfo(name = "male_toilet")
    private String Male_Toilet;

    @ColumnInfo(name = "female_toilet")
    private String Female_Toilet;

    @ColumnInfo(name = "differently_abled_toilet_facility")
    private String Differently_abled_Toilet_Facility;

    @ColumnInfo(name = "disaster_preparedness_response_plan")
    private String Disaster_Preparedness_Response_Plan;

    @ColumnInfo(name = "first_aid_and_emergency_rescue")
    private String First_Aid_and_Emergency_Rescue;

    @ColumnInfo(name = "national_building_code")
    private String National_Building_Code;

    @ColumnInfo(name = "building_age_and_state")
    private String Building_Age_and_State;

    @ColumnInfo(name = "occupancy")
    private String Occupancy;

    @ColumnInfo(name = "area_in_sq_m")
    private String Area_in_Sq_m;

    @ColumnInfo(name = "built_up_area_in_sq_m")
    private String Built_up_Area_in_Sq_m;

    @ColumnInfo(name = "built_up_area_in_hectare")
    private String Built_up_Area_in_Hectare;

    @ColumnInfo(name = "area_in_hectare")
    private String Area_in_Hectare;

    @ColumnInfo(name = "open_area_in_sq_m")
    private String Open_Area_in_Sq_m;

    @ColumnInfo(name = "open_area_in_hectare")
    private String Open_Area_in_Hectare;

    @ColumnInfo(name = "email")
    private String Email;

    @ColumnInfo(name = "web")
    private String Web;

    @ColumnInfo(name = "medicine_in_stock")
    private String Medicine_in_Stock;

    @ColumnInfo(name = "blood_in_stock")
    private String Blood_in_Stock;

    public HospitalFacilities(Long fk_common_places, String Ward, String Category, String Type, String Open_Space, String Contact_Number,
                              String Contact_Person, String Emergency_Service, String ICU_Service, String Ambulance_Service,
                              String Number_of_Beds, String Structure_Type, String Earthquake_Damage, String Toilet_Facility,
                              String Fire_Extinguisher, String Evacuation_Plan, String Alternatice_Route, String No_of_Doctors,
                              String No_of_Nurses, String No_of_Health_Assistant, String Total_No_of_Employees,
                              String Water_Storage_Capacity_Litre_, String Emergency_Stock_Capacity, String ICT_Grading_A_B_C_D,
                              String No_of_Rooms, String No_of_Stories, String Emergency_Phone_Number, String Male_Toilet,
                              String Female_Toilet, String Differently_abled_Toilet_Facility, String Disaster_Preparedness_Response_Plan,
                              String First_Aid_and_Emergency_Rescue, String National_Building_Code, String Building_Age_and_State,
                              String Occupancy, String Area_in_Sq_m, String Built_up_Area_in_Sq_m, String Built_up_Area_in_Hectare,
                              String Area_in_Hectare, String Open_Area_in_Sq_m, String Open_Area_in_Hectare, String Email,
                              String Web, String Medicine_in_Stock, String Blood_in_Stock) {
        this.fk_common_places = fk_common_places;
        this.Ward = Ward;
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
        this.ICT_Grading_A_B_C_D = ICT_Grading_A_B_C_D;
        this.No_of_Rooms = No_of_Rooms;
        this.No_of_Stories = No_of_Stories;
        this.Emergency_Phone_Number = Emergency_Phone_Number;
        this.Male_Toilet = Male_Toilet;
        this.Female_Toilet = Female_Toilet;
        this.Differently_abled_Toilet_Facility = Differently_abled_Toilet_Facility;
        this.Disaster_Preparedness_Response_Plan = Disaster_Preparedness_Response_Plan;
        this.First_Aid_and_Emergency_Rescue = First_Aid_and_Emergency_Rescue;
        this.National_Building_Code = National_Building_Code;
        this.Building_Age_and_State = Building_Age_and_State;
        this.Occupancy = Occupancy;
        this.Area_in_Sq_m = Area_in_Sq_m;
        this.Built_up_Area_in_Sq_m = Built_up_Area_in_Sq_m;
        this.Built_up_Area_in_Hectare = Built_up_Area_in_Hectare;
        this.Area_in_Hectare = Area_in_Hectare;
        this.Open_Area_in_Sq_m = Open_Area_in_Sq_m;
        this.Open_Area_in_Hectare = Open_Area_in_Hectare;
        this.Email = Email;
        this.Web = Web;
        this.Medicine_in_Stock = Medicine_in_Stock;
        this.Blood_in_Stock = Blood_in_Stock;
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

    public String getNo_of_Rooms() {
        return No_of_Rooms;
    }

    public void setNo_of_Rooms(String no_of_Rooms) {
        No_of_Rooms = no_of_Rooms;
    }

    public String getNo_of_Stories() {
        return No_of_Stories;
    }

    public void setNo_of_Stories(String no_of_Stories) {
        No_of_Stories = no_of_Stories;
    }

    public String getEmergency_Phone_Number() {
        return Emergency_Phone_Number;
    }

    public void setEmergency_Phone_Number(String emergency_Phone_Number) {
        Emergency_Phone_Number = emergency_Phone_Number;
    }

    public String getMale_Toilet() {
        return Male_Toilet;
    }

    public void setMale_Toilet(String male_Toilet) {
        Male_Toilet = male_Toilet;
    }

    public String getFemale_Toilet() {
        return Female_Toilet;
    }

    public void setFemale_Toilet(String female_Toilet) {
        Female_Toilet = female_Toilet;
    }

    public String getDifferently_abled_Toilet_Facility() {
        return Differently_abled_Toilet_Facility;
    }

    public void setDifferently_abled_Toilet_Facility(String differently_abled_Toilet_Facility) {
        Differently_abled_Toilet_Facility = differently_abled_Toilet_Facility;
    }

    public String getDisaster_Preparedness_Response_Plan() {
        return Disaster_Preparedness_Response_Plan;
    }

    public void setDisaster_Preparedness_Response_Plan(String disaster_Preparedness_Response_Plan) {
        Disaster_Preparedness_Response_Plan = disaster_Preparedness_Response_Plan;
    }

    public String getFirst_Aid_and_Emergency_Rescue() {
        return First_Aid_and_Emergency_Rescue;
    }

    public void setFirst_Aid_and_Emergency_Rescue(String first_Aid_and_Emergency_Rescue) {
        First_Aid_and_Emergency_Rescue = first_Aid_and_Emergency_Rescue;
    }

    public String getNational_Building_Code() {
        return National_Building_Code;
    }

    public void setNational_Building_Code(String national_Building_Code) {
        National_Building_Code = national_Building_Code;
    }

    public String getBuilding_Age_and_State() {
        return Building_Age_and_State;
    }

    public void setBuilding_Age_and_State(String building_Age_and_State) {
        Building_Age_and_State = building_Age_and_State;
    }

    public String getOccupancy() {
        return Occupancy;
    }

    public void setOccupancy(String occupancy) {
        Occupancy = occupancy;
    }

    public String getArea_in_Sq_m() {
        return Area_in_Sq_m;
    }

    public void setArea_in_Sq_m(String area_in_Sq_m) {
        Area_in_Sq_m = area_in_Sq_m;
    }

    public String getBuilt_up_Area_in_Sq_m() {
        return Built_up_Area_in_Sq_m;
    }

    public void setBuilt_up_Area_in_Sq_m(String built_up_Area_in_Sq_m) {
        Built_up_Area_in_Sq_m = built_up_Area_in_Sq_m;
    }

    public String getBuilt_up_Area_in_Hectare() {
        return Built_up_Area_in_Hectare;
    }

    public void setBuilt_up_Area_in_Hectare(String built_up_Area_in_Hectare) {
        Built_up_Area_in_Hectare = built_up_Area_in_Hectare;
    }

    public String getArea_in_Hectare() {
        return Area_in_Hectare;
    }

    public void setArea_in_Hectare(String area_in_Hectare) {
        Area_in_Hectare = area_in_Hectare;
    }

    public String getOpen_Area_in_Sq_m() {
        return Open_Area_in_Sq_m;
    }

    public void setOpen_Area_in_Sq_m(String open_Area_in_Sq_m) {
        Open_Area_in_Sq_m = open_Area_in_Sq_m;
    }

    public String getOpen_Area_in_Hectare() {
        return Open_Area_in_Hectare;
    }

    public void setOpen_Area_in_Hectare(String open_Area_in_Hectare) {
        Open_Area_in_Hectare = open_Area_in_Hectare;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getWeb() {
        return Web;
    }

    public void setWeb(String web) {
        Web = web;
    }

    public String getMedicine_in_Stock() {
        return Medicine_in_Stock;
    }

    public void setMedicine_in_Stock(String medicine_in_Stock) {
        Medicine_in_Stock = medicine_in_Stock;
    }

    public String getBlood_in_Stock() {
        return Blood_in_Stock;
    }

    public void setBlood_in_Stock(String blood_in_Stock) {
        Blood_in_Stock = blood_in_Stock;
    }

    public String getWard() {
        return Ward;
    }

    public void setWard(String ward) {
        Ward = ward;
    }



    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.hid);
        dest.writeValue(this.fk_common_places);
        dest.writeString(this.Ward);
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

        dest.writeString(this.No_of_Rooms);
        dest.writeString(this.No_of_Stories);
        dest.writeString(this.Emergency_Phone_Number);
        dest.writeString(this.Male_Toilet);
        dest.writeString(this.Female_Toilet);
        dest.writeString(this.Differently_abled_Toilet_Facility);
        dest.writeString(this.Disaster_Preparedness_Response_Plan);
        dest.writeString(this.First_Aid_and_Emergency_Rescue);
        dest.writeString(this.National_Building_Code);
        dest.writeString(this.Building_Age_and_State);
        dest.writeString(this.Occupancy);
        dest.writeString(this.Area_in_Sq_m);
        dest.writeString(this.Built_up_Area_in_Sq_m);
        dest.writeString(this.Built_up_Area_in_Hectare);
        dest.writeString(this.Area_in_Hectare);
        dest.writeString(this.Open_Area_in_Sq_m);
        dest.writeString(this.Open_Area_in_Hectare);
        dest.writeString(this.Email);
        dest.writeString(this.Web);
        dest.writeString(this.Medicine_in_Stock);
        dest.writeString(this.Blood_in_Stock);
    }

    protected HospitalFacilities(Parcel in) {
        this.hid = in.readInt();
        this.fk_common_places = (Long) in.readValue(Long.class.getClassLoader());
        this.Ward = in.readString();
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

        this.No_of_Rooms = in.readString();
        this.No_of_Stories = in.readString();
        this.Emergency_Phone_Number = in.readString();
        this.Male_Toilet = in.readString();
        this.Female_Toilet = in.readString();
        this.Differently_abled_Toilet_Facility = in.readString();
        this.Disaster_Preparedness_Response_Plan = in.readString();
        this.First_Aid_and_Emergency_Rescue = in.readString();
        this.National_Building_Code = in.readString();
        this.Building_Age_and_State = in.readString();
        this.Occupancy = in.readString();
        this.Area_in_Sq_m = in.readString();
        this.Built_up_Area_in_Sq_m = in.readString();
        this.Built_up_Area_in_Hectare = in.readString();
        this.Area_in_Hectare = in.readString();
        this.Open_Area_in_Sq_m = in.readString();
        this.Open_Area_in_Hectare = in.readString();
        this.Email = in.readString();
        this.Web = in.readString();
        this.Medicine_in_Stock = in.readString();
        this.Blood_in_Stock = in.readString();
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
