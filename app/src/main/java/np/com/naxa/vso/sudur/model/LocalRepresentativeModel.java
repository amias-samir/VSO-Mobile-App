package np.com.naxa.vso.sudur.model;

/**
 * Created by susan on 6/26/2017.
 */

public class LocalRepresentativeModel {
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

    public LocalRepresentativeModel(String id, String enName, String npName) {
        this.id = id;
        this.enName = enName;
        this.npName = npName;

    }
}
