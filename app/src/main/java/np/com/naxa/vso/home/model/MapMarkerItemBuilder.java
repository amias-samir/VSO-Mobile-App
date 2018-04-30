package np.com.naxa.vso.home.model;

import com.google.gson.JsonElement;

import java.util.Map;
import java.util.Set;

public class MapMarkerItemBuilder {
    private double lat;
    private double lng;
    private String title;
    private String snippet;
    private Set<Map.Entry<String, JsonElement>> geoJsonProperties;

    public MapMarkerItemBuilder setLat(double lat) {
        this.lat = lat;
        return this;
    }

    public MapMarkerItemBuilder setLng(double lng) {
        this.lng = lng;
        return this;
    }

    public MapMarkerItemBuilder setTitle(String title) {
        this.title = title;
        return this;
    }

    public MapMarkerItemBuilder setSnippet(String snippet) {
        this.snippet = snippet;
        return this;
    }

    public MapMarkerItemBuilder setGeoJsonProperties(Set<Map.Entry<String, JsonElement>> geoJsonProperties) {
        this.geoJsonProperties = geoJsonProperties;
        return this;
    }

    public MapMarkerItem createMapMarkerItem() {
        return new MapMarkerItem(lat, lng, title, snippet, geoJsonProperties);
    }
}