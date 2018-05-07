package np.com.naxa.vso.network.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AskForHelpResponse {
    @SerializedName("status")
    @Expose
    String status;

    @SerializedName("data")
    @Expose
    String data;

    @SerializedName("msg")
    @Expose
    String msg;

    public AskForHelpResponse(String status, String data, String msg) {
        this.status = status;
        this.data = data;
        this.msg = msg;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
