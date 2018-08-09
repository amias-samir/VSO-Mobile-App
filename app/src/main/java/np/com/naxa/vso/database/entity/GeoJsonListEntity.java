package np.com.naxa.vso.database.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity(tableName = "GeoJsonListEntity",
        indices = {@Index(value = "category_table",
                unique = true)})
public class GeoJsonListEntity {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @SerializedName("category_table")
    @ColumnInfo(name = "category_table")
    @Expose
    private String categoryName;

    @SerializedName("category_json")
    @ColumnInfo(name = "category_json")
    @Expose
    private String categoryTable;

    public GeoJsonListEntity(String categoryName, String categoryTable) {
        this.categoryName = categoryName;
        this.categoryTable = categoryTable;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryTable() {
        return categoryTable;
    }

    public void setCategoryTable(String categoryTable) {
        this.categoryTable = categoryTable;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
