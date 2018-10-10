package np.com.naxa.vso.firebase;

public class NotificationData {

    public static final String TEXT = "TEXT";

    private String imageName;
    private int id; // identificador da notificação
    private String title;
    private String textMessage;
    private String sound;
    private String imageUrl;
    private String thumbUrl;

    public NotificationData(String question, String message) {}

    public NotificationData(String title, String textMessage, String imageUrl, String thumbUrl) {
        this.imageName = imageName;
        this.title = title;
        this.textMessage = textMessage;
        this.imageUrl = imageUrl;
        this.thumbUrl = thumbUrl;
    }

    public NotificationData() {

    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTextMessage() {
        return textMessage;
    }

    public void setTextMessage(String textMessage) {
        this.textMessage = textMessage;
    }

    public String getSound() {
        return sound;
    }

    public void setSound(String sound) {
        this.sound = sound;
    }

    public static String getTEXT() {
        return TEXT;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getThumbUrl() {
        return thumbUrl;
    }

    public void setThumbUrl(String thumbUrl) {
        this.thumbUrl = thumbUrl;
    }
}
