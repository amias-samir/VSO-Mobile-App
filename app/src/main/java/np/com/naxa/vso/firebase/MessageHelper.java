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

    @ColumnInfo(name = "question")
    private String question;

    @ColumnInfo(name = "time")
    private String time;

    public MessageHelper() {
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

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
