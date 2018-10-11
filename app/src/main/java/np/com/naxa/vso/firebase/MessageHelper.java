package np.com.naxa.vso.firebase;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "MessageHelper")
public class MessageHelper implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int mid;

    @ColumnInfo(name = "message")
    private String message;

    @ColumnInfo(name = "date")
    private String date;

    @ColumnInfo(name = "message_type")
    private int message_type;

    @ColumnInfo(name = "time")
    private String time;

    public MessageHelper() {
    }

    public MessageHelper( String date,String time, String message, int message_type) {
        this.message = message;
        this.date = date;
        this.message_type = message_type;
        this.time = time;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getMessage_type() {
        return message_type;
    }

    public void setMessage_type(int message_type) {
        this.message_type = message_type;
    }

    public int getMid() {
        return mid;
    }

    public void setMid(int mid) {
        this.mid = mid;
    }
}
