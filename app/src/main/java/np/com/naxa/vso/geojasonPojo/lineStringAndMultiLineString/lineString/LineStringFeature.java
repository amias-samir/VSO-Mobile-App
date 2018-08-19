
package np.com.naxa.vso.geojasonPojo.lineStringAndMultiLineString.lineString;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import np.com.naxa.vso.geojasonPojo.lineStringAndMultiLineString.LineStringAndMultiLineStringProperties;
import np.com.naxa.vso.geojasonPojo.polygonAndMultipolygon.PolygonAndMultipolygonProperties;

public class LineStringFeature {

    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("properties")
    @Expose
    private LineStringAndMultiLineStringProperties properties;
    @SerializedName("geometry")
    @Expose
    private LineStringGeometry geometry;

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

    public LineStringGeometry getGeometry() {
        return geometry;
    }

    public void setGeometry(LineStringGeometry geometry) {
        this.geometry = geometry;
    }

}
