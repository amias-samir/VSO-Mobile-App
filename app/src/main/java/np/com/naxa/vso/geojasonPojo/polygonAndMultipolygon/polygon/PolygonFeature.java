
package np.com.naxa.vso.geojasonPojo.polygonAndMultipolygon.polygon;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import np.com.naxa.vso.geojasonPojo.polygonAndMultipolygon.PolygonAndMultipolygonProperties;
import np.com.naxa.vso.osm_convertor.multipolygon.Geometry;
import np.com.naxa.vso.osm_convertor.multipolygon.Properties;

public class PolygonFeature {

    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("properties")
    @Expose
    private PolygonAndMultipolygonProperties properties;
    @SerializedName("geometry")
    @Expose
    private PolygonGeometry geometry;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public PolygonAndMultipolygonProperties getProperties() {
        return properties;
    }

    public void setProperties(PolygonAndMultipolygonProperties properties) {
        this.properties = properties;
    }

    public PolygonGeometry getGeometry() {
        return geometry;
    }

    public void setGeometry(PolygonGeometry geometry) {
        this.geometry = geometry;
    }

}
