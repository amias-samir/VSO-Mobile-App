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

        list.add(new MySection(new MapDataCategory(R.drawable.ic_hospital, VSO.getInstance().getResources().getString(R.string.hospital), "health_facilities", MapDataCategory.POINT, R.drawable.marker_health)));
        list.add(new MySection(new MapDataCategory(R.drawable.ic_open_space, VSO.getInstance().getResources().getString(R.string.open_space), "open_spaces", MapDataCategory.BOUNDARY, R.drawable.marker_openspace)));
        list.add(new MySection(new MapDataCategory(R.drawable.ic_eductional, VSO.getInstance().getResources().getString(R.string.educational_institute), "educatinal_institutions", MapDataCategory.POINT, R.drawable.marker_education)));
        list.add(new MySection(new MapDataCategory(R.drawable.ic_financial, VSO.getInstance().getResources().getString(R.string.financial_institute), "financial_institute", MapDataCategory.POINT, R.drawable.marker_bank)));
//        list.add(new MySection(new MapDataCategory(R.drawable.ic_public_toilets, VSO.getInstance().getResources().getString(R.string.public_toilets), null, MapDataCategory.POINT, R.drawable.marker_toilets)));
        list.add(new MySection(new MapDataCategory(R.drawable.ic_industry, VSO.getInstance().getResources().getString(R.string.industries), "industries", MapDataCategory.POINT, R.drawable.marker_industry)));
        list.add(new MySection(new MapDataCategory(R.drawable.ic_transportation, VSO.getInstance().getResources().getString(R.string.transportation_utilities), "transportation_utilities", MapDataCategory.POINT, R.drawable.marker_transportaion)));
//        list.add(new MySection(new MapDataCategory(R.drawable.ic_water_bodies, VSO.getInstance().getResources().getString(R.string.water_bodies), null, MapDataCategory.BOUNDARY, R.drawable.marker_water)));

        list.add(new MySection(new MapDataCategory(R.drawable.ic_house, VSO.getInstance().getResources().getString(R.string.community_centres), "community_centres", MapDataCategory.POINT, R.drawable.marker_default)));
        list.add(new MySection(new MapDataCategory(R.drawable.ic_religious, VSO.getInstance().getResources().getString(R.string.religious_and_heritage), "religious_and_heritage_", MapDataCategory.POINT, R.drawable.marker_religious)));
        list.add(new MySection(new MapDataCategory(R.drawable.ic_atm, VSO.getInstance().getResources().getString(R.string.atm), "atm", MapDataCategory.POINT, R.drawable.marker_default)));
        list.add(new MySection(new MapDataCategory(R.drawable.ic_security, VSO.getInstance().getResources().getString(R.string.security_force), "security_force", MapDataCategory.POINT, R.drawable.marker_security)));
        list.add(new MySection(new MapDataCategory(R.drawable.ic_house, VSO.getInstance().getResources().getString(R.string.gas_depo), "gas_depo", MapDataCategory.POINT, R.drawable.marker_default)));
        list.add(new MySection(new MapDataCategory(R.drawable.ic_urban_utilites, VSO.getInstance().getResources().getString(R.string.urban_utilities), "urban_utilities", MapDataCategory.POINT, R.drawable.marker_default)));
        list.add(new MySection(new MapDataCategory(R.drawable.ic_pharmacies, VSO.getInstance().getResources().getString(R.string.pharmacies_and_health_clinics), "pharmacies_and_health_clinics", MapDataCategory.POINT, R.drawable.marker_health)));
        list.add(new MySection(new MapDataCategory(R.drawable.ic_house, VSO.getInstance().getResources().getString(R.string.government_agencies), "government_agencies", MapDataCategory.POINT, R.drawable.marker_government)));
        return list;
    }

    public static List<MySection> getHazardCatergorySections() {
        List<MySection> list = new ArrayList<>();
//        list.add(new MySection(new MapDataCategory(R.drawable.ic_earthquake, VSO.getInstance().getResources().getString(R.string.earthquake), null, MapDataCategory.POINT, R.drawable.marker_earthquake)));
        list.add(new MySection(new MapDataCategory(R.drawable.ic_landslide, VSO.getInstance().getResources().getString(R.string.land_slides), "landslides", MapDataCategory.POINT, R.drawable.marker_landslide)));
        list.add(new MySection(new MapDataCategory(R.drawable.ic_fire, VSO.getInstance().getResources().getString(R.string.fire_hazards), "fire_hazards", MapDataCategory.POINT, R.drawable.marker_fire)));
//        list.add(new MySection(new MapDataCategory(R.drawable.ic_floods, VSO.getInstance().getResources().getString(R.string.flood), null, MapDataCategory.POINT, R.drawable.marker_flood)));
        list.add(new MySection(new MapDataCategory(R.drawable.ic_fault_lines, VSO.getInstance().getResources().getString(R.string.fault_lines), "fault_lines", MapDataCategory.POINT, R.drawable.marker_faultlines)));
        list.add(new MySection(new MapDataCategory(R.drawable.ic_road_accident, VSO.getInstance().getResources().getString(R.string.road_accidents), "road_accidents", MapDataCategory.POINT, R.drawable.marker_accident)));
        list.add(new MySection(new MapDataCategory(R.drawable.ic_epidemic, VSO.getInstance().getResources().getString(R.string.epidemic), "epidemics", MapDataCategory.POINT, R.drawable.marker_epidemic)));
//        list.add(new MySection(new MapDataCategory(R.drawable.ic_animal_attack, VSO.getInstance().getResources().getString(R.string.animal_attacks), null, MapDataCategory.POINT, R.drawable.marker_animalattack)));

        return list;
    }

    public static List<MySection> getBaseDataCatergorySections() {
        List<MySection> list = new ArrayList<>();
        list.add(new MySection(new MapDataCategory(R.drawable.ic_boundry, VSO.getInstance().getResources().getString(R.string.municipality_boundary_name), "changunarayan_municipality_boundary.geojson", MapDataCategory.BOUNDARY, R.drawable.marker_default)));
        list.add(new MySection(new MapDataCategory(R.drawable.ic_wards, VSO.getInstance().getResources().getString(R.string.municipality_ward_name), "wards", MapDataCategory.BOUNDARY, R.drawable.marker_default)));
//        list.add(new MySection(new MapDataCategory(R.drawable.ic_road_netwoks, VSO.getInstance().getResources().getString(R.string.road_network), "road", MapDataCategory.ROAD, R.drawable.marker_roads)));
        list.add(new MySection(new MapDataCategory(R.drawable.ic_river_network, VSO.getInstance().getResources().getString(R.string.river_network), "river", MapDataCategory.RIVER, R.drawable.marker_river)));
        return list;
    }
}
