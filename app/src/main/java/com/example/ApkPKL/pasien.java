package com.example.ApkPKL;

public class pasien {
    private int id;
    private String nama;
    private String kontak;
    private String lokasi_pasien;
    private String lokasi_tujuan;
    private String detail;
    private String waktu_penjemputan;
    private String waktu_sampai;

    public pasien(int id, String nama, String kontak, String lokasi_pasien, String lokasi_tujuan, String detail, String waktu_penjemputan, String waktu_sampai) {
        this.id = id;
        this.nama = nama;
        this.kontak = kontak;
        this.lokasi_pasien = lokasi_pasien;
        this.lokasi_tujuan = lokasi_tujuan;
        this.detail = detail;
        this.waktu_penjemputan = waktu_penjemputan;
        this.waktu_sampai = waktu_sampai;
    }


    // Calling getters and setters
    public int getId()
    {
        return id;
    }

    public void setId(int Id)
    {
        id = Id;
    }

    public String getNama()
    {
        return nama;
    }

    public void setNama(String Nama)
    {
        this.nama = Nama;
    }

    public String getKontak()
    {
        return kontak;
    }

    public void setKontak(String Kontak)
    {
        this.kontak = Kontak;
    }

    public String getLokasi_Pasien()
    {
        return lokasi_pasien;
    }

    public void setLokasi_Pasien(String Lokasi_pasien)
    {
        this.lokasi_pasien = Lokasi_pasien;
    }

    public String getLokasi_Tujuan()
    {
        return lokasi_tujuan;
    }

    public void setLokasi_Tujuan(String Lokasi_tujuan)
    {
        this.lokasi_pasien = Lokasi_tujuan;
    }

    public String getDetail()
    {
        return detail;
    }

    public void setDetail(String Detail)
    {
        this.detail = Detail;
    }

    public String getWaktu_penjemputan(){
        return waktu_penjemputan;
    }

    public void setWaktu_penjemputan(String Waktu_penjemputan){
        this.waktu_penjemputan = Waktu_penjemputan;
    }

    public String getWaktu_sampai(){
        return waktu_sampai;
    }

    public void setWaktu_sampai(String Waktu_sampai){
        this.waktu_sampai = Waktu_sampai;
    }
}
