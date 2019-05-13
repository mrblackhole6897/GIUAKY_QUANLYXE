package com.example.quanlyxe;

public class ChiTietDonDatHang {
    private String  maXe,maDDH;
    private int soLuong;

    public ChiTietDonDatHang(String maXe, int soLuong, String maDDH) {
        this.maXe = maXe;
        this.soLuong = soLuong;
        this.maDDH = maDDH;
    }

    public String getMaXe() {
        return maXe;
    }

    public void setMaXe(String maXe) {
        this.maXe = maXe;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }

    public String getMaDDH() {
        return maDDH;
    }

    public void setMaDDH(String maDDH) {
        this.maDDH = maDDH;
    }
}
