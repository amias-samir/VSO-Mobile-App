package np.com.naxa.vso.home.model;

import com.google.gson.JsonElement;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.plugins.cluster.clustering.ClusterItem;

import java.util.Map;
import java.util.Set;

public class MapMarkerItem implements ClusterItem {
    private final LatLng position;
    private String title;
    private String snippet;
    private Set<Map.Entry<String, JsonElement>> geoJsonProperties;



    public MapMarkerItem(double lat, double lng, String title, String snippet,Set<Map.Entry<String, JsonElement>> geoJsonProperties) {
        position = new LatLng(lat, lng);
        this.title = title;
        this.snippet = snippet;
        this.geoJsonProperties = geoJsonProperties;
    }


    @Override
    public LatLng getPosition() {
        return position;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public String getSnippet() {
        return snippet;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setSnippet(String snippet) {
        this.snippet = snippet;
    }
}
