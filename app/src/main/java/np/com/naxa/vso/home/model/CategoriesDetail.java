package np.com.naxa.vso.home.model;

public class CategoriesDetail {

    int image;
    String name,location,desciption;

    public CategoriesDetail(int image, String name, String location, String desciption) {
        this.image = image;
        this.name = name;
        this.location = location;
        this.desciption = desciption;
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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDesciption() {
        return desciption;
    }

    public void setDesciption(String desciption) {
        this.desciption = desciption;
    }
}
