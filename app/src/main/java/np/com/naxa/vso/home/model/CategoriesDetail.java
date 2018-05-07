package np.com.naxa.vso.home.model;

public class CategoriesDetail {

    int image;
    String name, address, geojsonProperties;

    public CategoriesDetail(int image, String name, String address, String desciption) {
        this.image = image;
        this.name = name;
        this.address = address;
        this.geojsonProperties = desciption;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
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

    public String getGeojsonProperties() {
        return geojsonProperties;
    }

    public void setGeojsonProperties(String geojsonProperties) {
        this.geojsonProperties = geojsonProperties;
    }
}
