package com.example.ApkPKL;

public class DataPasien {
    private int id;
    private String nama;
    private String kontak;
    private String lokasi;
    private double latitudeP;
    private double longitudeP;
    private double latitudePT;
    private double longitudePT;
    private String lainnya;
    private String waktu_sampai;

    public DataPasien(){

    }

    public DataPasien(int id, String nama, String kontak, String lokasi, double latitudeP, double longitudeP, double latitudePT,  double longitudePT, String lainnya, String waktu_sampai) {
        this.id = id;
        this.nama = nama;
        this.kontak = kontak;
        this.lokasi = lokasi;
        this.latitudeP = latitudeP;
        this.longitudeP = longitudeP;
        this.latitudePT = latitudePT;
        this.longitudePT = longitudePT;
        this.lainnya = lainnya;
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

    public String getLokasi()
    {
        return lokasi;
    }

    public void setLokasi(String Lokasi)
    {
        this.lokasi = Lokasi;
    }

    public double getLatitudeP()
    {
        return latitudeP;
    }

    public void setLatitudeP(double LatitudeP)
    {
        latitudeP = LatitudeP;
    }

    public double getLongitudeP()
    {
        return longitudeP;
    }

    public void setLongitudeP(double LongitudeP)
    {
        longitudeP = LongitudeP;
    }

    public double getLatitudePT()
    {
        return latitudePT;
    }

    public void setLatitudePT(double LatitudePT)
    {
        latitudePT = LatitudePT;
    }

    public double getLongitudePT()
    {
        return longitudePT;
    }

    public void setLongitudePT(double LongitudePT)
    {
        longitudePT = LongitudePT;
    }

    public String getLainnya()
    {
        return lainnya;
    }

    public void setLainnya(String Lainnya)
    {
        this.lainnya = Lainnya;
    }

    public String getWaktu_sampai()
    {
        return waktu_sampai;
    }

    public void setWaktu_sampai(String Waktu_sampai)
    {
        this.waktu_sampai = Waktu_sampai;
    }

    // Creating toString
    @Override
    public String toString()
    {
        return "Profil [id="
                + id
                + ", nama="
                + nama
                + ", kontak="
                + kontak
                + ", lokasi="
                + lokasi
                + ", latitudeP="
                + latitudeP
                + ", longitudeP="
                + longitudeP
                + ", latitudePT="
                + latitudePT
                + ", longitudePT="
                + longitudePT
                + ", lainnya="
                + lainnya
                + ", waktu_sampai="
                + waktu_sampai + "]";
    }
}
