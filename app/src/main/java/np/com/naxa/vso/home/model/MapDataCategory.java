package np.com.naxa.vso.home.model;

import java.util.ArrayList;
import java.util.List;

public class MapDataCategory {

    public int image;
    private String filename;

    public MapDataCategory(int image, String name, String filename) {
        this.image = image;
        this.name = name;
        this.filename = filename;
    }

    public String getFileName() {
        return filename;
    }

    public int getImage() {
        return image;
    }

    public String getName() {
        return name;
    }

    public String name;


}
