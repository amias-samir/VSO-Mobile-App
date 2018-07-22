package np.com.naxa.vso.home;

import com.chad.library.adapter.base.entity.SectionEntity;

import java.util.ArrayList;
import java.util.List;

import np.com.naxa.vso.R;
import np.com.naxa.vso.home.model.MapDataCategory;

public class MySection extends SectionEntity<MapDataCategory> {
    private boolean isMore;

    public MySection(boolean isHeader, String header) {
        super(isHeader, header);
    }

    public MySection(MapDataCategory t) {
        super(t);
    }


    public static List<MySection> getResourcesCatergorySections() {
        List<MySection> list = new ArrayList<>();
        list.add(new MySection(new MapDataCategory(R.drawable.ic_local_hospital_black_24dp, "Hospital", "government_agencies.geojson", MapDataCategory.POINT)));
        list.add(new MySection(new MapDataCategory(R.drawable.ic_open_space, "Open Spaces", "demo_openspace_changu.geojson", MapDataCategory.BOUNDARY)));

        list.add(new MySection(new MapDataCategory(R.drawable.ic_school_black_24dp, "Educational Institution", "educational_Institution_geojson.geojson", MapDataCategory.POINT)));
        list.add(new MySection(new MapDataCategory(R.drawable.ic_school_black_24dp, "Financial Institution", "financial_institution.geojson", MapDataCategory.POINT)));
        list.add(new MySection(new MapDataCategory(R.drawable.ic_public_toilets, "Public Toilets", null, MapDataCategory.POINT)));
        list.add(new MySection(new MapDataCategory(R.drawable.ic_industry, "Industries", "industries_updated_geojson.geojson", MapDataCategory.POINT)));
        list.add(new MySection(new MapDataCategory(R.drawable.ic_transportation, "Transportation Utilities", null, MapDataCategory.POINT)));
        list.add(new MySection(new MapDataCategory(R.drawable.ic_water_bodies, "Water Bodies", null, MapDataCategory.BOUNDARY)));
        return list;
    }

    public static List<MySection> getHazardCatergorySections() {
        List<MySection> list = new ArrayList<>();
        list.add(new MySection(new MapDataCategory(R.drawable.ic_industry, "Industries", "industries_updated_geojson.geojson", MapDataCategory.POINT)));
        list.add(new MySection(new MapDataCategory(R.drawable.ic_transportation, "Transportation Utilities", null, MapDataCategory.POINT)));
        list.add(new MySection(new MapDataCategory(R.drawable.ic_water_bodies, "Water Bodies", null, MapDataCategory.BOUNDARY)));

        return list;
    }

    public static List<MySection> getBaseDataCatergorySections() {
        List<MySection> list = new ArrayList<>();
        list.add(new MySection(new MapDataCategory(R.drawable.ic_boundry, "Changunarayan Municipality Boundary", "changunarayan_municipality_boundary.geojson", MapDataCategory.BOUNDARY)));
        list.add(new MySection(new MapDataCategory(R.drawable.ic_wards, "Changunarayan Municipality Wards", "changunarayan_new_wards.geojson", MapDataCategory.BOUNDARY)));
        list.add(new MySection(new MapDataCategory(R.drawable.ic_road_netwoks, "Road Network", "road_network.geojson", MapDataCategory.ROAD)));
        list.add(new MySection(new MapDataCategory(R.drawable.ic_river_network, "River Network", "river_network.geojson", MapDataCategory.RIVER)));
        return list;
    }
}
