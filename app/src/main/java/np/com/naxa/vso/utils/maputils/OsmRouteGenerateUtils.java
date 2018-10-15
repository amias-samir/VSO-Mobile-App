package np.com.naxa.vso.utils.maputils;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;

import org.osmdroid.bonuspack.routing.MapQuestRoadManager;
import org.osmdroid.bonuspack.routing.OSRMRoadManager;
import org.osmdroid.bonuspack.routing.Road;
import org.osmdroid.bonuspack.routing.RoadManager;
import org.osmdroid.bonuspack.routing.RoadNode;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.PathOverlay;
import org.osmdroid.views.overlay.Polyline;

import java.util.ArrayList;

import np.com.naxa.vso.R;
import np.com.naxa.vso.home.VSO;

public class OsmRouteGenerateUtils {

    Context ctx = VSO.getInstance();

    //======================================================================================================
/**
 *  Add a route overlay between two geopoints with Bubble overlays on the route points.
 *
 * @param startPoint Route start.
 * @param endPoint Route end.
 */
    //======================================================================================================
    public void addRouteOverlay(MapView map, GeoPoint startPoint, GeoPoint endPoint)
    {
        //1 Routing via road manager
        RoadManager roadManager = new MapQuestRoadManager(VSO.getInstance().getResources().getString(R.string.access_token));
        roadManager.addRequestOption("routeType=fastest");

        //Then, retreive the road between your start and end point:
        ArrayList<GeoPoint> waypoints = new ArrayList<GeoPoint>();
        waypoints.add(startPoint);
        waypoints.add(endPoint); //end point

        Road road = roadManager.getRoad(waypoints);

        // then, build an overlay with the route shape:
        Polyline roadOverlay = RoadManager.buildRoadOverlay(road);
        roadOverlay.setColor(Color.GREEN);


        //Add Route Overlays into map
        map.getOverlays().add(roadOverlay);

        map.invalidate();//refesh map

        Drawable nodeIcon  = ctx.getResources().getDrawable(R.drawable.map_marker_blue);


        for (int i=0;i<road.mNodes.size(); i++){
        RoadNode node = road.mNodes.get(i);
        Marker nodeMarker = new Marker(map); nodeMarker.setPosition(node.mLocation);
        nodeMarker.setIcon(nodeIcon);
        nodeMarker.setTitle("Step "+i);

        nodeMarker.setSnippet(node.mInstructions);
            nodeMarker.setSubDescription(road.getLengthDurationText(ctx, node.mLength, node.mDuration));
            Drawable icon = ctx.getResources().getDrawable(R.drawable.ic_circle_marker);
        nodeMarker.setImage(icon);

        map.getOverlays().add(nodeMarker);

    }//end for

        map.invalidate();//refesh map


    }//===================================================================================================



}
