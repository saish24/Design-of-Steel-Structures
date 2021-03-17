package com.example.designofsteelstructures;

import androidx.annotation.NonNull;

public class Design {
    private int length;
    private int width;
    private int thickness;
    private Double sectionalArea, weightPerMeter, Cxx, Cyy, Exx, Eyy, Ixx, Iyy, Iuu, Ivv, Rxx, Ryy, Ruu, Rvv, Zxx, Zyy, Ixy, Rzz;


    @NonNull
    @Override
    public String toString() {
        return length + " length \n" +
                        width + " width \n" +
                        thickness + " thickness \n" +
                        sectionalArea + " sectionalArea \n" +
                        weightPerMeter + " weightPerMeter \n" +
                        Cxx + " Cxx \n" +
                        Cyy + " Cyy \n" +
                        Exx + " Exx \n" +
                        Eyy + " Eyy \n" +
                        Ixx + " Ixx \n" +
                        Iyy + " Iyy \n" +
                        Iuu + " Iuu \n" +
                        Ivv + " Ivv \n" +
                        Rxx + " Rxx \n" +
                        Ryy + " Ryy \n" +
                        Ruu + " Ruu \n" +
                        Rvv + " Rvv \n" +
                        Zxx + " Zxx \n" +
                        Zyy + " Zyy \n" +
                        Ixy + " Ixy \n\n\n";
    }

    public int getLength() {
        return length;
    }

    public int getWidth() {
        return width;
    }

    public int getThickness() {
        return thickness;
    }

    public Double getSectionalArea() {
        return sectionalArea;
    }

    public Double getWeightPerMeter() {
        return weightPerMeter;
    }

    public Double getCxx() {
        return Cxx;
    }

    public Double getCyy() {
        return Cyy;
    }

    public Double getExx() {
        return Exx;
    }

    public Double getEyy() {
        return Eyy;
    }

    public Double getIxx() {
        return Ixx;
    }

    public Double getIyy() {
        return Iyy;
    }

    public Double getIuu() {
        return Iuu;
    }

    public Double getIvv() {
        return Ivv;
    }

    public Double getRxx() {
        return Rxx;
    }

    public Double getRzz() {
        return Rzz;
    }

    public Double getRyy() {
        return Ryy;
    }

    public Double getRuu() {
        return Ruu;
    }

    public Double getRvv() {
        return Rvv;
    }

    public Double getZxx() {
        return Zxx;
    }

    public Double getZyy() {
        return Zyy;
    }

    public Double getIxy() {
        return Ixy;
    }

    public Design(int length, int width, int thickness, Double sectionalArea, Double weightPerMeter, Double cxx, Double cyy, Double exx, Double eyy, Double ixx, Double iyy, Double iuu, Double ivv, Double rxx, Double ryy, Double ruu, Double rvv, Double zxx, Double zyy, Double ixy, Double rzz) {
        this.length = length;
        this.width = width;
        this.thickness = thickness;
        this.sectionalArea = sectionalArea;
        this.weightPerMeter = weightPerMeter;
        Cxx = cxx;
        Cyy = cyy;
        Exx = exx;
        Eyy = eyy;
        Ixx = ixx;
        Iyy = iyy;
        Iuu = iuu;
        Ivv = ivv;
        Rxx = rxx;
        Ryy = ryy;
        Ruu = ruu;
        Rzz = rzz;
        Rvv = rvv;
        Zxx = zxx;
        Zyy = zyy;
        Ixy = ixy;
    }

    public Design(int length, int width, int thickness, Double sectionalArea, Double weightPerMeter, Double cxx, Double cyy, Double ixx, Double iyy, Double iuu, Double ivv, Double rxx, Double ryy, Double ruu, Double rvv, Double zxx, Double zyy, Double ixy, Double rzz) {
        this.length = length;
        this.width = width;
        this.thickness = thickness;
        this.sectionalArea = sectionalArea;
        this.weightPerMeter = weightPerMeter;
        Cxx = cxx;
        Cyy = cyy;
        Exx = 0d;
        Eyy = 0d;
        Ixx = ixx;
        Iyy = iyy;
        Iuu = iuu;
        Ivv = ivv;
        Rxx = rxx;
        Ryy = ryy;
        Rzz = rzz;
        Ruu = ruu;
        Rvv = rvv;
        Zxx = zxx;
        Zyy = zyy;
        Ixy = ixy;
    }
}
