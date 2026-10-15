package com.pbo.fareru.minpro.pbo.controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.pbo.fareru.minpro.pbo.config.HibernateUtil;
import com.pbo.fareru.minpro.pbo.config.Koneksi;
import com.pbo.fareru.minpro.pbo.dao.LanggananDAO;
import com.pbo.fareru.minpro.pbo.dao.LanggananDAOImpl;
import com.pbo.fareru.minpro.pbo.model.Langganan;
import com.pbo.fareru.minpro.pbo.model.LanggananProduktivitas;
import com.pbo.fareru.minpro.pbo.model.LanggananStreaming;
import com.pbo.fareru.minpro.pbo.model.Layanan;
import com.pbo.fareru.minpro.pbo.model.MetodePembayaran;
import org.hibernate.Session;

public class LanggananGUIController {
    private final LanggananDAO langgananDAO;
    private final List<Langganan> listLangganan;
    private final List<Layanan> listLayanan;
    private final List<MetodePembayaran> listMetode;

    public LanggananGUIController() {
        this.langgananDAO = new LanggananDAOImpl();
        this.listLangganan = new ArrayList<>();
        this.listLayanan = new ArrayList<>();
        this.listMetode = new ArrayList<>();
    }

    public void muatData() throws SQLException {
        listLayanan.clear();
        listMetode.clear();
        listLangganan.clear();

        try (Connection c = Koneksi.getConnection()) {
            try (PreparedStatement ps = c.prepareStatement(
                    "SELECT id_layanan, nama_layanan, kategori FROM layanan ORDER BY nama_layanan ASC");
                 ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    listLayanan.add(new Layanan(
                            rs.getString("id_layanan"),
                            rs.getString("nama_layanan"),
                            rs.getString("kategori")));
                }
            }

            try (PreparedStatement ps = c.prepareStatement(
                    "SELECT id_metode, nama_metode, jenis FROM metode_pembayaran ORDER BY nama_metode ASC");
                 ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    listMetode.add(new MetodePembayaran(
                            rs.getString("id_metode"),
                            rs.getString("nama_metode"),
                            rs.getString("jenis")));
                }
            }

            List<Langganan> dariDao = langgananDAO.findAll();
            listLangganan.addAll(dariDao);
        }
    }

    public List<Langganan> getListLangganan() {
        return new ArrayList<>(listLangganan);
    }

    public List<Layanan> getListLayanan() {
        return new ArrayList<>(listLayanan);
    }

    public List<MetodePembayaran> getListMetode() {
        return new ArrayList<>(listMetode);
    }

    public void tambahLangganan(Langganan langganan) throws SQLException {
        langgananDAO.save(langganan);
        muatData();
    }

    public void ubahLangganan(Langganan langganan) throws SQLException {
        langgananDAO.update(langganan);
        muatData();
    }

    public void hapusLangganan(String id) throws SQLException {
        langgananDAO.delete(id);
        muatData();
    }

    public void tambahLayanan(Layanan layanan) throws SQLException {
        try (Connection c = Koneksi.getConnection();
             PreparedStatement ps = c.prepareStatement(
                     "INSERT INTO layanan (id_layanan, nama_layanan, kategori) VALUES (?, ?, ?)")) {
            ps.setString(1, layanan.getIdLayanan());
            ps.setString(2, layanan.getNamaLayanan());
            ps.setString(3, layanan.getKategori());
            ps.executeUpdate();
        }
        muatData();
    }

    public String generateIdLanggananBaru() {
        int max = 0;
        for (Langganan l : listLangganan) {
            String id = l.getIdSubscription();
            if (id != null && id.toUpperCase().startsWith("SUB")) {
                try {
                    int num = Integer.parseInt(id.substring(3).trim());
                    if (num > max) {
                        max = num;
                    }
                } catch (NumberFormatException ignored) { }
            }
        }
        return String.format("SUB%02d", max + 1);
    }

    public String generateIdLayananBaru() {
        int max = 0;
        for (Layanan l : listLayanan) {
            String id = l.getIdLayanan();
            if (id != null && id.toUpperCase().startsWith("LYN")) {
                try {
                    int num = Integer.parseInt(id.substring(3).trim());
                    if (num > max) {
                        max = num;
                    }
                } catch (NumberFormatException ignored) { }
            }
        }
        return String.format("LYN%02d", max + 1);
    }

    public List<Langganan> filterData(String keyword, String filterKategori, String filterStatus) {
        List<Langganan> hasil = new ArrayList<>();
        String kw = (keyword != null) ? keyword.trim().toLowerCase(Locale.ROOT) : "";

        for (Langganan l : listLangganan) {
            if (filterKategori != null && !filterKategori.equalsIgnoreCase("Semua")) {
                if (filterKategori.equalsIgnoreCase("Streaming") && !(l instanceof LanggananStreaming)) {
                    continue;
                }
                if (filterKategori.equalsIgnoreCase("Produktivitas") && !(l instanceof LanggananProduktivitas)) {
                    continue;
                }
            }

            if (filterStatus != null && !filterStatus.equalsIgnoreCase("Semua")) {
                if (l.getStatus() == null || !l.getStatus().equalsIgnoreCase(filterStatus)) {
                    continue;
                }
            }

            if (!kw.isEmpty()) {
                boolean cocokId = l.getIdSubscription() != null && l.getIdSubscription().toLowerCase().contains(kw);
                boolean cocokNama = l.getLayanan() != null && l.getLayanan().getNamaLayanan().toLowerCase().contains(kw);
                boolean cocokKategori = l.getLayanan() != null && l.getLayanan().getKategori().toLowerCase().contains(kw);
                boolean cocokMetode = l.getMetode() != null && l.getMetode().getNamaMetode().toLowerCase().contains(kw);
                boolean cocokTipe = l.getTipeLangganan() != null && l.getTipeLangganan().toLowerCase().contains(kw);

                String detail = "";
                if (l instanceof LanggananStreaming ls) {
                    detail = ls.getKualitasResolusi();
                } else if (l instanceof LanggananProduktivitas lp) {
                    detail = lp.getKapasitasStorage();
                }
                boolean cocokDetail = detail != null && detail.toLowerCase().contains(kw);

                if (!cocokId && !cocokNama && !cocokKategori && !cocokMetode && !cocokTipe && !cocokDetail) {
                    continue;
                }
            }

            hasil.add(l);
        }
        return hasil;
    }

    public double hitungTotalPengeluaranBulanan(List<Langganan> list) {
        if (list == null) return 0.0;
        double total = 0.0;
        for (Langganan l : list) {
            if ("Aktif".equalsIgnoreCase(l.getStatus())) {
                total += l.getHargaBulanan();
            }
        }
        return total;
    }

    public int hitungTotalLanggananAktif(List<Langganan> list) {
        if (list == null) return 0;
        int count = 0;
        for (Langganan l : list) {
            if ("Aktif".equalsIgnoreCase(l.getStatus())) {
                count++;
            }
        }
        return count;
    }

    public double hitungTotalStreaming(List<Langganan> list) {
        if (list == null) return 0.0;
        double total = 0.0;
        for (Langganan l : list) {
            if (l instanceof LanggananStreaming && "Aktif".equalsIgnoreCase(l.getStatus())) {
                total += l.getHargaBulanan();
            }
        }
        return total;
    }

    public double hitungTotalProduktivitas(List<Langganan> list) {
        if (list == null) return 0.0;
        double total = 0.0;
        for (Langganan l : list) {
            if (l instanceof LanggananProduktivitas && "Aktif".equalsIgnoreCase(l.getStatus())) {
                total += l.getHargaBulanan();
            }
        }
        return total;
    }

    public String cekStatusOrmHibernate() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            List<Layanan> daftar = session.createQuery("from Layanan", Layanan.class).list();
            return "ORM Hibernate 6 Terhubung Sukses! Ditemukan " + daftar.size() + " entitas Layanan di database.";
        } catch (Exception e) {
            return "Koneksi Hibernate ORM: " + e.getMessage();
        }
    }

    public static String formatRupiah(double nominal) {
        return "Rp " + String.format(Locale.GERMANY, "%,.2f", nominal);
    }
}
