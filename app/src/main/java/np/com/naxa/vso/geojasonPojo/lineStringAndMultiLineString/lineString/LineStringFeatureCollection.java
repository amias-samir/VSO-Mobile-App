
package np.com.naxa.vso.geojasonPojo.lineStringAndMultiLineString.lineString;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class LineStringFeatureCollection {

    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("features")
    @Expose
    private List<LineStringFeature> features = null;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<LineStringFeature> getFeatures() {
        return features;
    }

    public void setFeatures(List<LineStringFeature> features) {
        this.features = features;
    }
}
