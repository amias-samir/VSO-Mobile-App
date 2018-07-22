package np.com.naxa.vso.home.model;

import java.util.ArrayList;
import java.util.List;

public class MapDataCategory {

    public int image;
    private String filename;
    private String type;

    public static final String ROAD = "ROAD";
    public static  final String RIVER = "RIVER";
    public  static final String BOUNDARY = "BOUNDARY";
    public  static final String POINT = "POINT";

    public MapDataCategory(int image, String name, String filename, String type) {
        this.image = image;
        this.name = name;
        this.filename = filename;
        this.type = type;
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


    public String getType() {
        return type;
    }
}
