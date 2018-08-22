
package np.com.naxa.vso.sudur.model.rest;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import np.com.naxa.vso.sudur.model.local.Bussiness;

public class Data {

    @SerializedName("code")
    @Expose
    private Integer code;
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("data")
    @Expose
    private List<Bussiness> data = null;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public List<Bussiness> getData() {
        return data;
    }

    public void setData(List<Bussiness> data) {
        this.data = data;
    }

}
