package np.com.naxa.vso.emergencyContacts;

import com.chad.library.adapter.base.entity.AbstractExpandableItem;
import com.chad.library.adapter.base.entity.MultiItemEntity;

public class EmergencyContactsPojo extends AbstractExpandableItem<ContactDetail> implements MultiItemEntity {

    @Override
    public int getLevel() {
        return 0;
    }

    @Override
    public int getItemType() {
        return 0;
    }

    private String header;

    public String getHeader() {
        return header;
    }



    public void setHeader(String header) {
        this.header = header;
    }



}
