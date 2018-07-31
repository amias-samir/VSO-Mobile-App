package np.com.naxa.vso.utils.maputils;

public class WardDetailsModel {
    String name;
    String ward;
    String area;
    String district;
    double latitude;
    double longitude;


    public WardDetailsModel(String name, String ward, String area, String district, double latitude, double longitude) {
        this.name = name;
        this.ward = ward;
        this.area = area;
        this.district = district;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWard() {
        return ward;
    }

    public void setWard(String ward) {
        this.ward = ward;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
