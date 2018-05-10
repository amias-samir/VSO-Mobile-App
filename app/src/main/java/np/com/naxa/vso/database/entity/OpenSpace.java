package np.com.naxa.vso.database.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.Keep;

import static android.arch.persistence.room.ForeignKey.CASCADE;

/**
 * Created by user on 4/23/2018.
 */

//@Keep
@Entity(tableName = "OpenSpace_table",
        foreignKeys = @ForeignKey(
                entity = CommonPlacesAttrb.class,
                parentColumns = "uid",
                childColumns = "fk_common_places",
                onDelete = CASCADE))
public class OpenSpace {

    @PrimaryKey(autoGenerate = true)
    int oid;

    @ColumnInfo(name = "fk_common_places")
    private Long fk_common_places;

    @ColumnInfo(name = "access_roa")
    String access_roa;

    @ColumnInfo(name = "accommodat")
    String accommodat;

    @ColumnInfo(name = "area_sqm")
    String area_sqm;

    @ColumnInfo(name = "high_tensi")
    String high_tensi;

    @ColumnInfo(name = "road_acces")
    String road_acces;

    @ColumnInfo(name = "shape_area")
    String shape_area;

    @ColumnInfo(name = "shape_leng")
    String shape_leng;

    @ColumnInfo(name = "terrain_ty")
    String terrain_ty;

    @ColumnInfo(name = "toilet_fac")
    String toilet_fac;

    @ColumnInfo(name = "water_faci")
    String water_faci;

    @ColumnInfo(name = "wifi_facil")
    String wifi_facil;

    public OpenSpace(Long fk_common_places, String access_roa, String accommodat, String area_sqm, String high_tensi, String road_acces, String shape_area, String shape_leng, String terrain_ty, String toilet_fac, String water_faci, String wifi_facil) {
        this.fk_common_places = fk_common_places;
        this.access_roa = access_roa;
        this.accommodat = accommodat;
        this.area_sqm = area_sqm;
        this.high_tensi = high_tensi;
        this.road_acces = road_acces;
        this.shape_area = shape_area;
        this.shape_leng = shape_leng;
        this.terrain_ty = terrain_ty;
        this.toilet_fac = toilet_fac;
        this.water_faci = water_faci;
        this.wifi_facil = wifi_facil;
    }


    public int getOid() {
        return oid;
    }

    public void setOid(int oid) {
        this.oid = oid;
    }

    public Long getFk_common_places() {
        return fk_common_places;
    }

    public void setFk_common_places(Long fk_common_places) {
        this.fk_common_places = fk_common_places;
    }

    public String getAccess_roa() {
        return access_roa;
    }

    public void setAccess_roa(String access_roa) {
        this.access_roa = access_roa;
    }

    public String getAccommodat() {
        return accommodat;
    }

    public void setAccommodat(String accommodat) {
        this.accommodat = accommodat;
    }

    public String getArea_sqm() {
        return area_sqm;
    }

    public void setArea_sqm(String area_sqm) {
        this.area_sqm = area_sqm;
    }

    public String getHigh_tensi() {
        return high_tensi;
    }

    public void setHigh_tensi(String high_tensi) {
        this.high_tensi = high_tensi;
    }

    public String getRoad_acces() {
        return road_acces;
    }

    public void setRoad_acces(String road_acces) {
        this.road_acces = road_acces;
    }

    public String getShape_area() {
        return shape_area;
    }

    public void setShape_area(String shape_area) {
        this.shape_area = shape_area;
    }

    public String getShape_leng() {
        return shape_leng;
    }

    public void setShape_leng(String shape_leng) {
        this.shape_leng = shape_leng;
    }

    public String getTerrain_ty() {
        return terrain_ty;
    }

    public void setTerrain_ty(String terrain_ty) {
        this.terrain_ty = terrain_ty;
    }

    public String getToilet_fac() {
        return toilet_fac;
    }

    public void setToilet_fac(String toilet_fac) {
        this.toilet_fac = toilet_fac;
    }

    public String getWater_faci() {
        return water_faci;
    }

    public void setWater_faci(String water_faci) {
        this.water_faci = water_faci;
    }

    public String getWifi_facil() {
        return wifi_facil;
    }

    public void setWifi_facil(String wifi_facil) {
        this.wifi_facil = wifi_facil;
    }
}
