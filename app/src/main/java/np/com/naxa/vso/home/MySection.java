package np.com.naxa.vso.home;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LiveDataReactiveStreams;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.support.v4.app.FragmentActivity;

import com.chad.library.adapter.base.entity.SectionEntity;

import org.reactivestreams.Publisher;

import java.util.ArrayList;
import java.util.List;

import np.com.naxa.vso.R;
import np.com.naxa.vso.database.entity.GeoJsonCategoryEntity;
import np.com.naxa.vso.database.entity.GeoJsonListEntity;
import np.com.naxa.vso.home.model.MapDataCategory;
import np.com.naxa.vso.viewmodel.CommonPlacesAttribViewModel;
import np.com.naxa.vso.viewmodel.GeoJsonCategoryViewModel;

public class MySection extends SectionEntity<MapDataCategory> {
    private boolean isMore;


    public MySection(boolean isHeader, String header) {
        super(isHeader, header);
    }

    public MySection(MapDataCategory t) {
        super(t);
    }


    public static List<MySection> getResourcesCatergorySections(List<GeoJsonCategoryEntity> geoJsonCategoryEntitiesList) {

        List<MySection> list = new ArrayList<>();

        if (geoJsonCategoryEntitiesList.size() > 0) {
            for (GeoJsonCategoryEntity geoJsonCategoryEntity : geoJsonCategoryEntitiesList
                    ) {
                list.add(new MySection(new MapDataCategory(geoJsonCategoryEntity.getCategoryPhoto(), geoJsonCategoryEntity.getCategoryName(), geoJsonCategoryEntity.getCategoryTable(), MapDataCategory.POINT, R.drawable.marker_default, geoJsonCategoryEntity.getSummaryName())));
            }
        }
        return list;
    }

    public static List<MySection> getHazardCatergorySections(List<GeoJsonCategoryEntity> geoJsonCategoryEntitiesList) {
        List<MySection> list = new ArrayList<>();

        if (geoJsonCategoryEntitiesList.size() > 0) {
            for (GeoJsonCategoryEntity geoJsonCategoryEntity : geoJsonCategoryEntitiesList
                    ) {
                list.add(new MySection(new MapDataCategory(geoJsonCategoryEntity.getCategoryPhoto(), geoJsonCategoryEntity.getCategoryName(), geoJsonCategoryEntity.getCategoryTable(), MapDataCategory.POINT, R.drawable.marker_default, geoJsonCategoryEntity.getSummaryName())));
            }
        }

        return list;
    }

    public static List<MySection> getBaseDataCatergorySections(List<GeoJsonCategoryEntity> geoJsonCategoryEntitiesList) {
        List<MySection> list = new ArrayList<>();

        if (geoJsonCategoryEntitiesList.size() > 0) {
            for (GeoJsonCategoryEntity geoJsonCategoryEntity : geoJsonCategoryEntitiesList
                    ) {
                list.add(new MySection(new MapDataCategory(geoJsonCategoryEntity.getCategoryPhoto(), geoJsonCategoryEntity.getCategoryName(), geoJsonCategoryEntity.getCategoryTable(), MapDataCategory.POINT, R.drawable.marker_default, geoJsonCategoryEntity.getSummaryName())));
            }
        }

        return list;
    }
}
