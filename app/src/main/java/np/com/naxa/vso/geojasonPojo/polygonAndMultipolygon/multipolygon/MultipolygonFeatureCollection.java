
package np.com.naxa.vso.geojasonPojo.polygonAndMultipolygon.multipolygon;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MultipolygonFeatureCollection {

    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("features")
    @Expose
    private List<MultipolygonFeature> features = null;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<MultipolygonFeature> getFeatures() {
        return features;
    }

    public void setFeatures(List<MultipolygonFeature> features) {
        this.features = features;
    }
}
