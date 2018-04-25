package np.com.naxa.vso.home.model;

import java.util.ArrayList;
import java.util.List;

public class MapDataCategory {

    public int image;

    public MapDataCategory(int image, String name) {
        this.image = image;
        this.name = name;
    }

    public int getImage() {
        return image;
    }

    public String getName() {
        return name;
    }

    public String name;


}
