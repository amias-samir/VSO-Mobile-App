package np.com.naxa.vso.home;

import java.util.ArrayList;
import java.util.List;

public class MapDataCategory {

    public String image;

    public MapDataCategory(String image, String name) {
        this.image = image;
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public String getName() {
        return name;
    }

    public String name;

    public static List<MapDataCategory> getCategories() {
        List<MapDataCategory> list = new ArrayList<>();
        MapDataCategory category;

        list.add(new MapDataCategory("","Hospital"));
        list.add(new MapDataCategory("","Hospital"));
        list.add(new MapDataCategory("","Hospital"));
        list.add(new MapDataCategory("","Hospital"));
        list.add(new MapDataCategory("","Hospital"));

        return list;
    }
}
