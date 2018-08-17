
package np.com.naxa.vso.geojasonPojo.lineStringAndMultiLineString.multiLineString;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import np.com.naxa.vso.geojasonPojo.lineStringAndMultiLineString.LineStringAndMultiLineStringProperties;
import np.com.naxa.vso.osm_convertor.multipolygon.Properties;

public class MultiLineStringFeature {

    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("properties")
    @Expose
    private LineStringAndMultiLineStringProperties properties;
    @SerializedName("geometry")
    @Expose
    private MultiLineStringGeometry geometry;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public LineStringAndMultiLineStringProperties getProperties() {
        return properties;
    }

    public void setProperties(LineStringAndMultiLineStringProperties properties) {
        this.properties = properties;
    }

    public MultiLineStringGeometry getGeometry() {
        return geometry;
    }

    public void setGeometry(MultiLineStringGeometry geometry) {
        this.geometry = geometry;
    }
}
