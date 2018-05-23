package np.com.naxa.vso.utils.maputils;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import org.osmdroid.bonuspack.clustering.RadiusMarkerClusterer;
import org.osmdroid.bonuspack.clustering.StaticCluster;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Overlay;
import org.osmdroid.views.overlay.OverlayItem;

import java.util.List;

import np.com.naxa.vso.R;

public class OsmMarkerCluster {


    public static RadiusMarkerClusterer createPointOfInterestOverlay(List<Overlay> overlays, Context context) {
        //10. Marker Clustering
        RadiusMarkerClusterer poiMarkers = new RadiusMarkerClusterer(context) {
            @Override
            public Marker buildClusterMarker(StaticCluster cluster, MapView mapView) {
                Marker result = super.buildClusterMarker(cluster,mapView);
                if (cluster.getSize() > 0) {
                    // show data of the first object in cluster
                    result.setRelatedObject(cluster.getItem(0).getRelatedObject());
                }
                return result;
            }
        };

        Drawable clusterIconD = context.getResources().getDrawable(R.drawable.mapbox_marker_icon_default);
        poiMarkers.setIcon(((BitmapDrawable) clusterIconD).getBitmap());

        //end of 10.
        //11. Customizing the clusters design
        poiMarkers.getTextPaint().setTextSize(12.0f);
        poiMarkers.mAnchorV = Marker.ANCHOR_BOTTOM;
        poiMarkers.mTextAnchorU = 0.70f;
        poiMarkers.mTextAnchorV = 0.27f;
        //end of 11.
        if (overlays != null) {
            overlays.add(poiMarkers);
        }
        return poiMarkers;
    }

}
