package np.com.naxa.vso.utils.maputils;

import org.osmdroid.util.GeoPoint;

import java.util.ArrayList;
import java.util.List;

public class PolygonMapUtils {

    //
    //Ray Casting algorithm which identifies point in polygon
    //
    public static boolean isPointInPolygon(GeoPoint tap, ArrayList<GeoPoint> vertices) {
        int intersectCount = 0;
        if (vertices == null) return false;
        for (int j = 0; j < vertices.size() - 1; j++) {
            if (rayCastIntersect(tap, vertices.get(j), vertices.get(j + 1))) {
                intersectCount++;
            }
        }

        return ((intersectCount % 2) == 1); // odd = inside, even = outside;
    }

    private static boolean rayCastIntersect(GeoPoint tap, GeoPoint vertA, GeoPoint vertB) {

        double aY = vertA.getLatitude();
        double aX = vertA.getLongitude();
        double bY = vertB.getLatitude();
        double bX = vertB.getLongitude();
        double pY = tap.getLatitude();
        double pX = tap.getLongitude();

        if ((aY > pY && bY > pY) || (aY < pY && bY < pY)
                || (aX < pX && bX < pX)) {
            return false; // a and b can't both be above or below pt.y, and a or
            // b must be east of pt.x
        }

        double m = (aY - bY) / (aX - bX); // Rise over run
        double bee = (-aX) * m + aY; // y = mx + b
        double x = (pY - bee) / m; // algebra is neat!

        return x > pX;
    }

    //
    //Calculate the center of the polygon
    //
    public static GeoPoint calculateCentroidWithGeoPoint(List<GeoPoint> pointList) {
        double x = 0;
        double y = 0;
        for (GeoPoint point : pointList) {
            x += point.getLatitude();
            y += point.getLongitude();
        }
        x /= pointList.size();
        y /= pointList.size();

        return new GeoPoint(y, x);
    }

    public static GeoPoint calculateCentroidWithPointList(List<List<Double>> pointList) {
        double x = 0;
        double y = 0;
        for (List<Double> point : pointList) {
            x += point.get(0);
            y += point.get(1);
        }
        x /= pointList.size();
        y /= pointList.size();

        return new GeoPoint(y, x);
    }
}
