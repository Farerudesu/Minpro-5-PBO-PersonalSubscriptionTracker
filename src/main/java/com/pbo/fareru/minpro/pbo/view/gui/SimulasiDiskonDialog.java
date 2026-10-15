package com.pbo.fareru.minpro.pbo.view.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import com.pbo.fareru.minpro.pbo.controller.LanggananGUIController;
import com.pbo.fareru.minpro.pbo.model.DapatDidiskon;
import com.pbo.fareru.minpro.pbo.model.Langganan;
import com.pbo.fareru.minpro.pbo.model.LanggananProduktivitas;
import com.pbo.fareru.minpro.pbo.model.LanggananStreaming;

public class SimulasiDiskonDialog extends JDialog {
    private final List<Langganan> listLangganan;
    private final JComboBox<LanggananComboItem> cmbLangganan;
    private final JSpinner spnDurasi;
    private final JSpinner spnPromo;

    private final JLabel lblHargaNormalBulan;
    private final JLabel lblTipeOOP;
    private final JLabel lblEstimasiNormal;
    private final JLabel lblEstimasiPolimorfik;
    private final JLabel lblDiskonInterface;
    private final JLabel lblTotalAkhirPromo;

    public SimulasiDiskonDialog(Frame owner, List<Langganan> listLangganan, Langganan itemDipilih) {
        super(owner, "Simulasi Biaya & Diskon", true);
        this.listLangganan = listLangganan;

        setSize(520, 420);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout());
        setResizable(false);

        JPanel panelContent = new JPanel(new GridBagLayout());
        panelContent.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        int row = 0;

        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0.35;
        panelContent.add(new JLabel("Pilih Langganan:"), gbc);

        gbc.gridx = 1; gbc.gridy = row; gbc.weightx = 0.65;
        cmbLangganan = new JComboBox<>();
        for (Langganan l : listLangganan) {
            LanggananComboItem item = new LanggananComboItem(l);
            cmbLangganan.addItem(item);
            if (itemDipilih != null && l.getIdSubscription().equalsIgnoreCase(itemDipilih.getIdSubscription())) {
                cmbLangganan.setSelectedItem(item);
            }
        }
        panelContent.add(cmbLangganan, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row;
        panelContent.add(new JLabel("Durasi (Bulan):"), gbc);

        gbc.gridx = 1; gbc.gridy = row;
        spnDurasi = new JSpinner(new SpinnerNumberModel(12, 1, 60, 1));
        panelContent.add(spnDurasi, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row;
        panelContent.add(new JLabel("Diskon Tambahan (%):"), gbc);

        gbc.gridx = 1; gbc.gridy = row;
        spnPromo = new JSpinner(new SpinnerNumberModel(0.0, 0.0, 50.0, 5.0));
        panelContent.add(spnPromo, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 2;
        JPanel panelHasil = new JPanel(new GridBagLayout());
        panelHasil.setBorder(BorderFactory.createTitledBorder("Hasil Perhitungan"));

        GridBagConstraints gbcH = new GridBagConstraints();
        gbcH.fill = GridBagConstraints.HORIZONTAL;
        gbcH.insets = new Insets(4, 5, 4, 5);

        int hRow = 0;
        lblHargaNormalBulan = addHasilRow(panelHasil, gbcH, hRow++, "Harga Per Bulan:", "-");
        lblTipeOOP = addHasilRow(panelHasil, gbcH, hRow++, "Tipe Subclass:", "-");
        lblEstimasiNormal = addHasilRow(panelHasil, gbcH, hRow++, "Total Biaya Normal:", "-");
        lblEstimasiPolimorfik = addHasilRow(panelHasil, gbcH, hRow++, "Estimasi Polimorfik:", "-");
        lblDiskonInterface = addHasilRow(panelHasil, gbcH, hRow++, "Diskon Interface:", "-");
        lblTotalAkhirPromo = addHasilRow(panelHasil, gbcH, hRow++, "Total Akhir (Setelah Promo):", "-");

        panelContent.add(panelHasil, gbc);

        cmbLangganan.addActionListener(e -> hitungSimulasi());
        spnDurasi.addChangeListener(e -> hitungSimulasi());
        spnPromo.addChangeListener(e -> hitungSimulasi());

        JPanel panelBtn = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnTutup = new JButton("Tutup");
        btnTutup.addActionListener(e -> dispose());
        panelBtn.add(btnTutup);

        add(new JScrollPane(panelContent), BorderLayout.CENTER);
        add(panelBtn, BorderLayout.SOUTH);

        hitungSimulasi();
    }

    private JLabel addHasilRow(JPanel panel, GridBagConstraints gbc, int row, String label, String defaultVal) {
        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0.45;
        panel.add(new JLabel(label), gbc);

        gbc.gridx = 1; gbc.gridy = row; gbc.weightx = 0.55;
        JLabel val = new JLabel(defaultVal);
        panel.add(val, gbc);

        return val;
    }

    private void hitungSimulasi() {
        LanggananComboItem item = (LanggananComboItem) cmbLangganan.getSelectedItem();
        if (item == null || item.langganan == null) return;

        Langganan sub = item.langganan;
        int durasi = (Integer) spnDurasi.getValue();
        double promo = ((Number) spnPromo.getValue()).doubleValue();

        lblHargaNormalBulan.setText(LanggananGUIController.formatRupiah(sub.getHargaBulanan()));
        lblTipeOOP.setText(sub.getTipeLangganan());

        double akumulatifNormal = sub.getHargaBulanan() * durasi;
        lblEstimasiNormal.setText(LanggananGUIController.formatRupiah(akumulatifNormal));

        double polimorfikBulan = sub.hitungEstimasiBiaya(durasi);
        lblEstimasiPolimorfik.setText(LanggananGUIController.formatRupiah(polimorfikBulan));

        if (sub instanceof DapatDidiskon dd) {
            double diskonInterface = dd.hitungDiskon(durasi);
            lblDiskonInterface.setText(LanggananGUIController.formatRupiah(diskonInterface));
        } else {
            lblDiskonInterface.setText("Rp 0,00");
        }

        double totalAkhir = sub.hitungEstimasiBiaya(durasi, promo);
        lblTotalAkhirPromo.setText(LanggananGUIController.formatRupiah(totalAkhir));
    }

    private static class LanggananComboItem {
        final Langganan langganan;
        LanggananComboItem(Langganan langganan) { this.langganan = langganan; }
        @Override
        public String toString() {
            if (langganan == null) return "-";
            String nama = langganan.getLayanan() != null ? langganan.getLayanan().getNamaLayanan() : "-";
            return "[" + langganan.getIdSubscription() + "] " + nama + " (" + LanggananGUIController.formatRupiah(langganan.getHargaBulanan()) + ")";
        }
    }
}
