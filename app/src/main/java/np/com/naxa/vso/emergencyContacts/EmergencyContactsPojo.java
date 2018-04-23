package np.com.naxa.vso.emergencyContacts;

public class EmergencyContactsPojo {
    private int pic;
    private String name,phone;

    public EmergencyContactsPojo(String name, String phone) {
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
