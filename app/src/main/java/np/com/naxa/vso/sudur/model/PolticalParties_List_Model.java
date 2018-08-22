package np.com.naxa.vso.sudur.model;

/**
 * Created by Samir on 11/6/2016.
 */

public class PolticalParties_List_Model {
    public String mThumbnail;

    public String poltical_party_name_en;
    public String poltical_party_name_np;
    public String poltical_party_seat;


    public String getPoltical_party_name_en() { return poltical_party_name_en; }
    public void setPoltical_party_name_en(String poltical_party_name_en) {this.poltical_party_name_en = poltical_party_name_en;}

    public String getPoltical_party_name_np() { return poltical_party_name_np; }
    public void setPoltical_party_name_np(String poltical_party_name_np) {this.poltical_party_name_np = poltical_party_name_np;}

    public String getThumbnail() { return mThumbnail; }
    public void setThumbnail(String thumbnail) { this.mThumbnail = thumbnail; }

    public String getPoltical_party_seat() {
        return poltical_party_seat;
    }

    public void setPoltical_party_seat(String poltical_party_seat) {
        this.poltical_party_seat = poltical_party_seat;
    }
}
