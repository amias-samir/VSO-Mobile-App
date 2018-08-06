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

        list.add(new MySection(new MapDataCategory(R.drawable.ic_hospital, "Hospital", "health_facilities.geojson", MapDataCategory.POINT, R.drawable.marker_health)));
        list.add(new MySection(new MapDataCategory(R.drawable.ic_open_space, "Open Spaces", "demo_openspace_changu.geojson", MapDataCategory.BOUNDARY, R.drawable.marker_openspace)));
        list.add(new MySection(new MapDataCategory(R.drawable.ic_eductional, "Educational Institution", "educational_Institution_geojson.geojson", MapDataCategory.POINT, R.drawable.marker_education)));
        list.add(new MySection(new MapDataCategory(R.drawable.ic_financial, "Financial Institution", "financial_institution.geojson", MapDataCategory.POINT, R.drawable.marker_bank)));
        list.add(new MySection(new MapDataCategory(R.drawable.ic_public_toilets, "Public Toilets", null, MapDataCategory.POINT, R.drawable.marker_toilets)));
        list.add(new MySection(new MapDataCategory(R.drawable.ic_industry, "Industries", "industries_updated_geojson.geojson", MapDataCategory.POINT, R.drawable.marker_industry)));
        list.add(new MySection(new MapDataCategory(R.drawable.ic_transportation, "Transportation Utilities", null, MapDataCategory.POINT, R.drawable.marker_transportaion)));
        list.add(new MySection(new MapDataCategory(R.drawable.ic_water_bodies, "Water Bodies", null, MapDataCategory.BOUNDARY, R.drawable.marker_water)));
        return list;
    }

    public static List<MySection> getHazardCatergorySections() {
        List<MySection> list = new ArrayList<>();
        list.add(new MySection(new MapDataCategory(R.drawable.ic_earthquake, "Earthquake", null, MapDataCategory.POINT, R.drawable.marker_earthquake)));
        list.add(new MySection(new MapDataCategory(R.drawable.ic_landslide, "Land Slides", null, MapDataCategory.POINT, R.drawable.marker_landslide)));
        list.add(new MySection(new MapDataCategory(R.drawable.ic_fire, "Fire Hazards", null, MapDataCategory.POINT, R.drawable.marker_fire)));
        list.add(new MySection(new MapDataCategory(R.drawable.ic_floods, "Flood", null, MapDataCategory.POINT, R.drawable.marker_flood)));
        list.add(new MySection(new MapDataCategory(R.drawable.ic_fault_lines, "Fault Lines", null, MapDataCategory.POINT, R.drawable.marker_faultlines)));
        list.add(new MySection(new MapDataCategory(R.drawable.ic_road_accident, "Road Accidents", null, MapDataCategory.POINT, R.drawable.marker_accident)));
        list.add(new MySection(new MapDataCategory(R.drawable.ic_epidemic, "Epidemic", null, MapDataCategory.POINT, R.drawable.marker_epidemic)));
        list.add(new MySection(new MapDataCategory(R.drawable.ic_animal_attack, "Animal Attacks", null, MapDataCategory.POINT, R.drawable.marker_animalattack)));

        return list;
    }

    public static List<MySection> getBaseDataCatergorySections() {
        List<MySection> list = new ArrayList<>();
        list.add(new MySection(new MapDataCategory(R.drawable.ic_boundry, "Changunarayan Municipality Boundary", "changunarayan_municipality_boundary.geojson", MapDataCategory.BOUNDARY, R.drawable.marker_default)));
        list.add(new MySection(new MapDataCategory(R.drawable.ic_wards, "Changunarayan Municipality Wards", "changunarayan_new_wards.geojson", MapDataCategory.BOUNDARY, R.drawable.marker_default)));
        list.add(new MySection(new MapDataCategory(R.drawable.ic_road_netwoks, "Road Network", "road_network.geojson", MapDataCategory.ROAD, R.drawable.marker_roads)));
        list.add(new MySection(new MapDataCategory(R.drawable.ic_river_network, "River Network", "river_network.geojson", MapDataCategory.RIVER, R.drawable.marker_river)));
        return list;
    }
}
