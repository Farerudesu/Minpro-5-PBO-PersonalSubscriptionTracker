package com.pbo.fareru.minpro.pbo.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "metode_pembayaran")
public class MetodePembayaran {
    @Id
    @Column(name = "id_metode")
    private String idMetode;

    @Column(name = "nama_metode")
    private String namaMetode;

    @Column(name = "jenis")
    private String jenis;

    protected MetodePembayaran() { }

    public MetodePembayaran(String idMetode, String namaMetode, String jenis) {
        setIdMetode(idMetode);
        setNamaMetode(namaMetode);
        setJenis(jenis);
    }

    public String getIdMetode() {
        return idMetode;
    }

    public void setIdMetode(String idMetode) {
        if (idMetode != null && !idMetode.trim().isEmpty()) {
            this.idMetode = idMetode;
        }
    }

    public String getNamaMetode() {
        return namaMetode;
    }

    public void setNamaMetode(String namaMetode) {
        if (namaMetode != null && !namaMetode.trim().isEmpty()) {
            this.namaMetode = namaMetode;
        }
    }

    public String getJenis() {
        return jenis;
    }

    public void setJenis(String jenis) {
        if (jenis != null && !jenis.trim().isEmpty()) {
            this.jenis = jenis;
        }
    }

    @Override
    public String toString() {
        return namaMetode != null ? namaMetode + " [" + jenis + "]" : "-";
    }
}
