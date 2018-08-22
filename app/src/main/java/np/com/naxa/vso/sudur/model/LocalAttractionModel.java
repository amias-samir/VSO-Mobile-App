package np.com.naxa.vso.sudur.model;

/**
 * Created by User on 11/15/2016.
 */

public class LocalAttractionModel {
    public String mThumbnail;

    public String place_type_id;
    public String place_title_en;
    public String place_title_np;
    public String place_desc_en ;
    public String plaece_desc_np;
    public String district_name_en;
    public String district_name_np;
    public String address_name_en;
    public String address_name_np;
    public String place_lat;
    public String place_lon;

    public String getPlace_lat() {
        return place_lat;
    }

    public void setPlace_lat(String place_lat) {
        this.place_lat = place_lat;
    }

    public String getPlace_lon() {
        return place_lon;
    }

    public void setPlace_lon(String place_lon) {
        this.place_lon = place_lon;
    }

    public String getPlace_type_id() { return place_type_id; }
    public void setPlace_type_id(String place_type_id) {
        this.place_type_id = place_type_id;
    }

    public String getPlace_title_en() {
        return place_title_en;
    }
    public void setPlace_title_en(String place_title_en) {
        this.place_title_en = place_title_en;
    }

    public String getPlace_title_np() {
        return place_title_np;
    }
    public void setPlace_title_np(String place_title_np) {
        this.place_title_np = place_title_np;
    }

    public String getPlace_desc_en() {
        return place_desc_en;
    }
    public void setPlace_desc_en(String place_desc_en) {
        this.place_desc_en = place_desc_en;
    }

    public String getPlaece_desc_np() {
        return plaece_desc_np;
    }
    public void setPlaece_desc_np(String plaece_desc_np) {
        this.plaece_desc_np = plaece_desc_np;
    }

    public String getDistrict_name_en() {
        return district_name_en;
    }
    public void setDistrict_name_en(String district_name_en) { this.district_name_en = district_name_en; }

    public String getDistrict_name_np() {
        return district_name_np;
    }
    public void setDistrict_name_np(String district_name_np) { this.district_name_np = district_name_np; }

    public String getAddress_name_en() {
        return address_name_en;
    }
    public void setAddress_name_en(String address_name_en) { this.address_name_en = address_name_en; }

    public String getAddress_name_np() {
        return address_name_np;
    }
    public void setAddress_name_np(String address_name_np) { this.address_name_np = address_name_np; }

    public String getmThumbnail() {
        return mThumbnail;
    }
    public void setmThumbnail(String thumbnail) { this.mThumbnail = thumbnail; }

}
