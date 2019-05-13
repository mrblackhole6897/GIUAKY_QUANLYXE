package com.example.quanlyxe;

public class Xe {
    private String maXe, tenXe, maLoai;
    private int dungTich, donGia;

    public String getMaXe() {
        return maXe;
    }

    public void setMaXe(String maXe) {
        this.maXe = maXe;
    }

    public String getTenXe() {
        return tenXe;
    }

    public void setTenXe(String tenXe) {
        this.tenXe = tenXe;
    }

    public String getMaLoai() {
        return maLoai;
    }

    public void setMaLoai(String maLoai) {
        this.maLoai = maLoai;
    }

    public int getDungTich() {
        return dungTich;
    }

    public void setDungTich(int dungTich) {
        this.dungTich = dungTich;
    }

    public int getDonGia() {
        return donGia;
    }

    public void setDonGia(int donGia) {
        this.donGia = donGia;
    }

    public Xe() {
    }

    public Xe(String maXe, String tenXe, String maLoai, int dungTich, int donGia) {
        this.maXe = maXe;
        this.tenXe = tenXe;
        this.maLoai = maLoai;
        this.dungTich = dungTich;
        this.donGia = donGia;
    }
}
