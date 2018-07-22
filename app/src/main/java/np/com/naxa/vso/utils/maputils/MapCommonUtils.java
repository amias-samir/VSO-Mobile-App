package np.com.naxa.vso.utils.maputils;

import org.osmdroid.util.BoundingBox;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;

public class MapCommonUtils {

    public static void zoomToMapBoundary(final MapView mapView,final GeoPoint centerPoint){
        BoundingBox boundingBox = new BoundingBox(27.728708, 85.525139, 27.656069, 85.396133);
        mapView.zoomToBoundingBox(boundingBox, true);
        mapView.setScrollableAreaLimitDouble(boundingBox);
        mapView.getController().animateTo(centerPoint);
        mapView.invalidate();
    }
}
