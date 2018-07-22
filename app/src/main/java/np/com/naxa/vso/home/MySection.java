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


    public static List<MySection> getMapDataCatergorySections() {

        List<MySection> list = new ArrayList<>();
        list.add(new MySection(new MapDataCategory(R.drawable.ic_local_hospital_black_24dp, "Hospital", "government_agencies.geojson")));
        list.add(new MySection(new MapDataCategory(R.drawable.ic_transfer_within_a_station_black_24dp, "Open Spaces", "demo_openspace_changu.geojson")));
        list.add(new MySection(new MapDataCategory(R.drawable.ic_school_black_24dp, "Education", "educational_Institution_geojson.geojson")));
        return list;
    }

    public static List<MySection> getResourcesCatergorySections() {
        List<MySection> list = new ArrayList<>();
        list.add(new MySection(new MapDataCategory(R.drawable.ic_school_black_24dp, "Educational Institution", "educational_Institution_geojson.geojson")));
        return list;
    }

    public static List<MySection> getHazardCatergorySections() {
        List<MySection> list = new ArrayList<>();
        list.add(new MySection(new MapDataCategory(R.drawable.ic_school_black_24dp, "Financial Institution", "financial_institution.geojson")));
        list.add(new MySection(new MapDataCategory(R.drawable.ic_school_black_24dp, "Industries", "industries_updated_geojson.geojson")));
        list.add(new MySection(new MapDataCategory(R.drawable.ic_school_black_24dp, "Public Toilets", null)));
        list.add(new MySection(new MapDataCategory(R.drawable.ic_school_black_24dp, "Transportation Utilities", null)));
        list.add(new MySection(new MapDataCategory(R.drawable.ic_school_black_24dp, "Water Bodies", null)));
        return list;
    }

    public static List<MySection> getBaseDataCatergorySections() {
        List<MySection> list = new ArrayList<>();
        list.add(new MySection(new MapDataCategory(R.drawable.ic_school_black_24dp, "Changunarayan Municipality Boundary", "changunarayan_municipality_boundary.geojson")));
        list.add(new MySection(new MapDataCategory(R.drawable.ic_school_black_24dp, "Changunarayan Municipality Wards", "changunarayan_new_wards.geojson")));
        list.add(new MySection(new MapDataCategory(R.drawable.ic_school_black_24dp, "Road Network", "road_network.geojson")));
        list.add(new MySection(new MapDataCategory(R.drawable.ic_school_black_24dp, "River Network", "river_network.geojson")));
        return list;
    }
}
