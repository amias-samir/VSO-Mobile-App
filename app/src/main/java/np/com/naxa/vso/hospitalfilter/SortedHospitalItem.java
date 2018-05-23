package np.com.naxa.vso.hospitalfilter;

import java.util.LinkedHashMap;


public class SortedHospitalItem {

    LinkedHashMap linkedHashMap ;

    public SortedHospitalItem(LinkedHashMap linkedHashMap) {
        this.linkedHashMap = linkedHashMap;
    }

    public LinkedHashMap getLinkedHashMap() {
        return linkedHashMap;
    }

    public void setLinkedHashMap(LinkedHashMap linkedHashMap) {
        this.linkedHashMap = linkedHashMap;
    }
}
