package np.com.naxa.vso;

import android.graphics.Color;

import org.osmdroid.bonuspack.kml.KmlFeature;
import org.osmdroid.bonuspack.kml.KmlLineString;
import org.osmdroid.bonuspack.kml.KmlPlacemark;
import org.osmdroid.bonuspack.kml.KmlPoint;
import org.osmdroid.bonuspack.kml.KmlPolygon;
import org.osmdroid.bonuspack.kml.KmlTrack;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Overlay;
import org.osmdroid.views.overlay.Polygon;
import org.osmdroid.views.overlay.Polyline;

import io.reactivex.annotations.Nullable;

public class OverlayPopupHiddenStyler implements KmlFeature.Styler {

    private String value;

    public OverlayPopupHiddenStyler() {
        this.value = null;
    }

    public OverlayPopupHiddenStyler(@Nullable String value) {
        this.value = value;
    }

    @Override
    public void onFeature(Overlay overlay, KmlFeature kmlFeature) {

    }

    @Override
    public void onPoint(Marker marker, KmlPlacemark kmlPlacemark, KmlPoint kmlPoint) {

    }

    @Override
    public void onLineString(Polyline polyline, KmlPlacemark kmlPlacemark, KmlLineString kmlLineString) {
        polyline.setWidth(Math.max(kmlLineString.mCoordinates.size() / 200.0f, 3.0f));
        switch (value) {
            case "river":
                polyline.setColor(Color.BLUE);
                break;
            case "road":
                polyline.setColor(Color.BLACK);
                break;
            default:
        }
    }

    @Override
    public void onPolygon(Polygon polygon, KmlPlacemark kmlPlacemark, KmlPolygon kmlPolygon) {
        polygon.setStrokeColor(Color.BLACK);
        polygon.setStrokeWidth(2.5f);
        polygon.setFillColor(0x20AA1010);
    }

    @Override
    public void onTrack(Polyline polyline, KmlPlacemark kmlPlacemark, KmlTrack kmlTrack) {

    }
}
