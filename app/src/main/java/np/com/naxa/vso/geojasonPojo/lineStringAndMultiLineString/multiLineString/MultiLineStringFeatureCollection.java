
package np.com.naxa.vso.geojasonPojo.lineStringAndMultiLineString.multiLineString;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MultiLineStringFeatureCollection {

    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("features")
    @Expose
    private List<MultiLineStringFeature> features = null;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<MultiLineStringFeature> getFeatures() {
        return features;
    }

    public void setFeatures(List<MultiLineStringFeature> features) {
        this.features = features;
    }
}
