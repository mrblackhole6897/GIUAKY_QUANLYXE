package com.example.quanlyxe;

import java.util.Date;

public class DonDatHang {
    private String maDDH;
    private String ngayLap;

    public String getMaDDH() {
        return maDDH;
    }

    public void setMaDDH(String maDDH) {
        this.maDDH = maDDH;
    }

    public String getNgayDat() {
        return ngayLap;
    }

    public void setNgayDat(String ngayDat) {
        this.ngayLap = ngayDat;
    }

    public DonDatHang(String maDDH, String ngayDat) {
        this.maDDH = maDDH;
        this.ngayLap = ngayDat;
    }

    public DonDatHang() {
    }
}
