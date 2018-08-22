package np.com.naxa.vso.sudur.model;

/**
 * Created by nishon.tan on 11/3/2016.
 */

public class District {
    private String id;
    private String enName;
    private String npName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEnName() {
        return enName;
    }

    public void setEnName(String enName) {
        this.enName = enName;
    }

    public String getNpName() {
        return npName;
    }

    public void setNpName(String npName) {
        this.npName = npName;
    }

    public District(String id, String enName, String npName) {
        this.id = id;
        this.enName = enName;
        this.npName = npName;

    }
}
