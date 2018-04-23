package np.com.naxa.vso.emergencyContacts;

import com.chad.library.adapter.base.entity.AbstractExpandableItem;
import com.chad.library.adapter.base.entity.MultiItemEntity;

public class EmergencyContactsPojo extends AbstractExpandableItem<EmergencyContactsPojo.ContactSingle>{

    @Override
    public int getLevel() {
        return 0;
    }

    public class ContactSingle{
        private int pic;
        private String name,phone;

        public ContactSingle(String name, String phone) {
            this.name = name;
            this.phone = phone;
        }

        public int getPic() {
            return pic;
        }

        public void setPic(int pic) {
            this.pic = pic;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }
    }

}
