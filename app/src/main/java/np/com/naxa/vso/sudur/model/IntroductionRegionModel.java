package np.com.naxa.vso.sudur.model;

/**
 * Created by Samir on 6/26/2017.
 */

public class IntroductionRegionModel {
    private String title_en;
    private String title_np;
    private String value_np;
    private String value_en;


    public IntroductionRegionModel() {
    }

    public IntroductionRegionModel(String title_np, String value_np) {
        this.title_np = title_np;
        this.value_np = value_np;
    }

    public String getTitle_en() {
        return title_en;
    }

    public void setTitle_en(String title_en) {
        this.title_en = title_en;
    }

    public String getTitle_np() {
        return title_np;
    }

    public void setTitle_np(String title_np) {
        this.title_np = title_np;
    }

    public String getValue_np() {
        return value_np;
    }

    public void setValue_np(String value_np) {
        this.value_np = value_np;
    }

    public String getValue_en() {
        return value_en;
    }

    public void setValue_en(String value_en) {
        this.value_en = value_en;
    }
}
