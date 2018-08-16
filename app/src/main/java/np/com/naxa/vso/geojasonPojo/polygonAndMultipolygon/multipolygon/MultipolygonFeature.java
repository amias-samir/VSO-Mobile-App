
package np.com.naxa.vso.geojasonPojo.polygonAndMultipolygon.multipolygon;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import np.com.naxa.vso.osm_convertor.multipolygon.Geometry;
import np.com.naxa.vso.osm_convertor.multipolygon.Properties;

public class MultipolygonFeature {

    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("properties")
    @Expose
    private Properties properties;
    @SerializedName("geometry")
    @Expose
    private MultipolygonGeometry geometry;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Properties getProperties() {
        return properties;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }

    public MultipolygonGeometry getGeometry() {
        return geometry;
    }

    public void setGeometry(MultipolygonGeometry geometry) {
        this.geometry = geometry;
    }
}
