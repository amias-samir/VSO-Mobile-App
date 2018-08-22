package np.com.naxa.vso.sudur.model;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by nishon.tan on 10/26/2016.
 *
 * A class to store locaiton of interest to plot in the map
 *
 * Type
 * 1. Schools
 * 2. Hospitals
 * 3. Parks
 * 4. Temples
 */

public class InterestLocation {
  LatLng latLng;

    public LatLng getLatLng() {
        return latLng;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }

    public InterestLocation(LatLng latLng, String type, String title, String desc) {
        this.latLng = latLng;
        this.type = type;
        this.title = title;
        this.desc = desc;
    }

    String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }



    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    String title;
    String desc;
}
