
package np.com.naxa.vso.emergencyContacts;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EmergencyContactPOJO {

    @SerializedName("S. N.")
    @Expose
    private String sN;
    @SerializedName("Photo")
    @Expose
    private String photo;
    @SerializedName("Name")
    @Expose
    private String name;
    @SerializedName("Organization")
    @Expose
    private String organization;
    @SerializedName("Post")
    @Expose
    private String post;
    @SerializedName("Address")
    @Expose
    private String address;
    @SerializedName("Phone no.")
    @Expose
    private String phoneNo;
    @SerializedName("Email")
    @Expose
    private String email;

    public String getSN() {
        return sN;
    }

    public void setSN(String sN) {
        this.sN = sN;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
