
package np.com.naxa.vso.osm_convertor.polygon;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Properties_ {

    @SerializedName("OBJECTID_1")
    @Expose
    private Integer oBJECTID1;
    @SerializedName("OBJECTID")
    @Expose
    private Integer oBJECTID;
    @SerializedName("Centroid_X")
    @Expose
    private Double centroidX;
    @SerializedName("Centroid_Y")
    @Expose
    private Double centroidY;
    @SerializedName("Perimeter")
    @Expose
    private Double perimeter;
    @SerializedName("Area_Sqm")
    @Expose
    private Integer areaSqm;
    @SerializedName("Shape_Leng")
    @Expose
    private Double shapeLeng;
    @SerializedName("DCODE")
    @Expose
    private Integer dCODE;
    @SerializedName("District")
    @Expose
    private Object district;
    @SerializedName("GaPa_NaPa_")
    @Expose
    private String gaPaNaPa;
    @SerializedName("Centre")
    @Expose
    private Object centre;
    @SerializedName("State")
    @Expose
    private Object state;
    @SerializedName("Remarks")
    @Expose
    private Object remarks;
    @SerializedName("Shape_Le_1")
    @Expose
    private Double shapeLe1;
    @SerializedName("Shape_Area")
    @Expose
    private Double shapeArea;

    public Integer getOBJECTID1() {
        return oBJECTID1;
    }

    public void setOBJECTID1(Integer oBJECTID1) {
        this.oBJECTID1 = oBJECTID1;
    }

    public Integer getOBJECTID() {
        return oBJECTID;
    }

    public void setOBJECTID(Integer oBJECTID) {
        this.oBJECTID = oBJECTID;
    }

    public Double getCentroidX() {
        return centroidX;
    }

    public void setCentroidX(Double centroidX) {
        this.centroidX = centroidX;
    }

    public Double getCentroidY() {
        return centroidY;
    }

    public void setCentroidY(Double centroidY) {
        this.centroidY = centroidY;
    }

    public Double getPerimeter() {
        return perimeter;
    }

    public void setPerimeter(Double perimeter) {
        this.perimeter = perimeter;
    }

    public Integer getAreaSqm() {
        return areaSqm;
    }

    public void setAreaSqm(Integer areaSqm) {
        this.areaSqm = areaSqm;
    }

    public Double getShapeLeng() {
        return shapeLeng;
    }

    public void setShapeLeng(Double shapeLeng) {
        this.shapeLeng = shapeLeng;
    }

    public Integer getDCODE() {
        return dCODE;
    }

    public void setDCODE(Integer dCODE) {
        this.dCODE = dCODE;
    }

    public Object getDistrict() {
        return district;
    }

    public void setDistrict(Object district) {
        this.district = district;
    }

    public String getGaPaNaPa() {
        return gaPaNaPa;
    }

    public void setGaPaNaPa(String gaPaNaPa) {
        this.gaPaNaPa = gaPaNaPa;
    }

    public Object getCentre() {
        return centre;
    }

    public void setCentre(Object centre) {
        this.centre = centre;
    }

    public Object getState() {
        return state;
    }

    public void setState(Object state) {
        this.state = state;
    }

    public Object getRemarks() {
        return remarks;
    }

    public void setRemarks(Object remarks) {
        this.remarks = remarks;
    }

    public Double getShapeLe1() {
        return shapeLe1;
    }

    public void setShapeLe1(Double shapeLe1) {
        this.shapeLe1 = shapeLe1;
    }

    public Double getShapeArea() {
        return shapeArea;
    }

    public void setShapeArea(Double shapeArea) {
        this.shapeArea = shapeArea;
    }

}
