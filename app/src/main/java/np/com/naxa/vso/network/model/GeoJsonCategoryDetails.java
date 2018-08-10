
package np.com.naxa.vso.network.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import np.com.naxa.vso.database.entity.GeoJsonCategoryEntity;

public class GeoJsonCategoryDetails {

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("data")
    @Expose
    private List<GeoJsonCategoryEntity> data = null;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public List<GeoJsonCategoryEntity> getData() {
        return data;
    }

    public void setData(List<GeoJsonCategoryEntity> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "GeoJsonCategoryDetails{" +
                "status=" + status +
                ", data=" + data +
                '}';
    }
}
