package np.com.naxa.vso.sudur.model;

public class INGO_NGO_Model {
    private String id;
    private String name;
    private String work;
    private String desc;
    private String type;
    private String email;
    public String mThumbnail;

//    public INGO_NGO_Model(String name, String desc, String email) {
//        this.name = name;
//        this.desc = desc;
//        this.email = email;
//    }

    public INGO_NGO_Model() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWork() {
        return work;
    }

    public void setWork(String work) {
        this.work = work;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getmThumbnail() {
        return mThumbnail;
    }

    public void setmThumbnail(String mThumbnail) {
        this.mThumbnail = mThumbnail;
    }
}
