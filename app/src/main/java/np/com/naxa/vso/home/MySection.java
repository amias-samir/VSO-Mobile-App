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
        list.add(new MySection(new MapDataCategory(R.drawable.ic_local_hospital_black_24dp, "Hospital")));
        list.add(new MySection(new MapDataCategory(R.drawable.ic_transfer_within_a_station_black_24dp, "Open Spaces")));
        list.add(new MySection(new MapDataCategory(R.drawable.ic_school_black_24dp, "Education")));
        return list;
    }

    public static List<MySection> getResourcesCatergorySections() {
        List<MySection> list = new ArrayList<>();
        list.add(new MySection(new MapDataCategory(R.drawable.ic_school_black_24dp, "Educational Institution")));
        return list;
    }

    public static List<MySection> getHazardCatergorySections() {
        List<MySection> list = new ArrayList<>();
        list.add(new MySection(new MapDataCategory(R.drawable.ic_school_black_24dp, "Financial Institution")));
        list.add(new MySection(new MapDataCategory(R.drawable.ic_school_black_24dp, "Industries")));
        list.add(new MySection(new MapDataCategory(R.drawable.ic_school_black_24dp, "Public Toilets")));
        list.add(new MySection(new MapDataCategory(R.drawable.ic_school_black_24dp, "Transportation Utilities")));
        list.add(new MySection(new MapDataCategory(R.drawable.ic_school_black_24dp, "Water Bodies")));
        return list;
    }

    public static List<MySection> getBaseDataCatergorySections() {
        List<MySection> list = new ArrayList<>();
        list.add(new MySection(new MapDataCategory(R.drawable.ic_school_black_24dp, "Changunarayan Municipality Boundary")));
        list.add(new MySection(new MapDataCategory(R.drawable.ic_school_black_24dp, "Changunarayan Municipality Wards")));
        list.add(new MySection(new MapDataCategory(R.drawable.ic_school_black_24dp, "Road Network")));
        list.add(new MySection(new MapDataCategory(R.drawable.ic_school_black_24dp, "River Network")));
        return list;
    }
}
