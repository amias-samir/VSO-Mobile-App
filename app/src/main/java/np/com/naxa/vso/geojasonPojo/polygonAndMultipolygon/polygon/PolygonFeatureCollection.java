
package np.com.naxa.vso.geojasonPojo.polygonAndMultipolygon.polygon;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PolygonFeatureCollection {

    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("features")
    @Expose
    private List<PolygonFeature> features = null;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<PolygonFeature> getFeatures() {
        return features;
    }

    public void setFeatures(List<PolygonFeature> features) {
        this.features = features;
    }
}
