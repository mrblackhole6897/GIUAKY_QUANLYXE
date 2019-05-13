package com.example.quanlyxe;

public class LoaiXe {
    String maLoai, tenLoai, xuatXu;

    public String getMaLoai() {
        return maLoai;
    }

    public void setMaLoai(String maLoai) {
        this.maLoai = maLoai;
    }

    public String getTenLoai() {
        return tenLoai;
    }

    public void setTenLoai(String tenLoai) {
        this.tenLoai = tenLoai;
    }

    public String getXuatXu() {
        return xuatXu;
    }

    public void setXuatXu(String xuatXu) {
        this.xuatXu = xuatXu;
    }

    public LoaiXe() {
    }

    public LoaiXe(String maLoai, String tenLoai, String xuatXu) {
        this.maLoai = maLoai;
        this.tenLoai = tenLoai;
        this.xuatXu = xuatXu;

    }
}
