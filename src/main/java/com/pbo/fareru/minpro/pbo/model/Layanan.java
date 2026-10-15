package com.pbo.fareru.minpro.pbo.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "layanan")
public class Layanan {
    @Id
    @Column(name = "id_layanan")
    private String idLayanan;

    @Column(name = "nama_layanan")
    private String namaLayanan;

    @Column(name = "kategori")
    private String kategori;

    protected Layanan() { }

    public Layanan(String idLayanan, String namaLayanan, String kategori) {
        setIdLayanan(idLayanan);
        setNamaLayanan(namaLayanan);
        setKategori(kategori);
    }

    public String getIdLayanan() {
        return idLayanan;
    }

    public void setIdLayanan(String idLayanan) {
        if (idLayanan != null && !idLayanan.trim().isEmpty()) {
            this.idLayanan = idLayanan;
        }
    }

    public String getNamaLayanan() {
        return namaLayanan;
    }

    public void setNamaLayanan(String namaLayanan) {
        if (namaLayanan != null && !namaLayanan.trim().isEmpty()) {
            this.namaLayanan = namaLayanan;
        }
    }

    public String getKategori() {
        return kategori;
    }

    public void setKategori(String kategori) {
        if (kategori != null && !kategori.trim().isEmpty()) {
            this.kategori = kategori;
        }
    }

    @Override
    public String toString() {
        return namaLayanan != null ? namaLayanan + " (" + kategori + ")" : "-";
    }
}