package np.com.naxa.vso.home.model;

import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.plugins.cluster.clustering.ClusterItem;

public class MyItem implements ClusterItem {
    private final LatLng position;
    private String title;
    private String snippet;

    public MyItem(double lat, double lng) {
        position = new LatLng(lat, lng);
        title = null;
        snippet = null;
    }

    public MyItem(double lat, double lng, String title, String snippet) {
        position = new LatLng(lat, lng);
        this.title = title;
        this.snippet = snippet;
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
