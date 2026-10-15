package com.pbo.fareru.minpro.pbo.view.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.pbo.fareru.minpro.pbo.controller.LanggananGUIController;
import com.pbo.fareru.minpro.pbo.model.DapatDidiskon;
import com.pbo.fareru.minpro.pbo.model.Langganan;
import com.pbo.fareru.minpro.pbo.model.LanggananProduktivitas;
import com.pbo.fareru.minpro.pbo.model.LanggananStreaming;

public class DetailLanggananDialog extends JDialog {
    private final Frame owner;
    private final LanggananGUIController controller;
    private final Langganan data;
    private boolean diedit = false;

    public DetailLanggananDialog(Frame owner, LanggananGUIController controller, Langganan data) {
        super(owner, "Detail Langganan - " + data.getIdSubscription(), true);
        this.owner = owner;
        this.controller = controller;
        this.data = data;

        setSize(460, 480);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout());
        setResizable(false);

        JPanel panelContent = new JPanel(new GridBagLayout());
        panelContent.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        int row = 0;
        addRow(panelContent, gbc, row++, "ID Langganan:", data.getIdSubscription());
        addRow(panelContent, gbc, row++, "Nama Layanan:", data.getLayanan() != null ? data.getLayanan().getNamaLayanan() : "-");
        addRow(panelContent, gbc, row++, "Kategori Layanan:", data.getLayanan() != null ? data.getLayanan().getKategori() : "-");
        addRow(panelContent, gbc, row++, "Tipe Langganan:", data.getTipeLangganan());
        addRow(panelContent, gbc, row++, "Harga Bulanan:", LanggananGUIController.formatRupiah(data.getHargaBulanan()));
        addRow(panelContent, gbc, row++, "Tanggal Tagihan:", "Tanggal " + data.getTanggalTagihan());
        addRow(panelContent, gbc, row++, "Status:", data.getStatus());
        addRow(panelContent, gbc, row++, "Metode Pembayaran:", data.getMetode() != null ? data.getMetode().getNamaMetode() + " (" + data.getMetode().getJenis() + ")" : "-");

        if (data instanceof LanggananStreaming ls) {
            addRow(panelContent, gbc, row++, "Resolusi / Audio:", ls.getKualitasResolusi());
            addRow(panelContent, gbc, row++, "Batas Layar:", ls.getBatasLayar() + " Perangkat");
        } else if (data instanceof LanggananProduktivitas lp) {
            addRow(panelContent, gbc, row++, "Storage / Fitur:", lp.getKapasitasStorage());
            addRow(panelContent, gbc, row++, "Lisensi User:", lp.getLisensiUser() + " User");
        }

        addRow(panelContent, gbc, row++, "Biaya Tahunan (OOP):", LanggananGUIController.formatRupiah(data.hitungBiayaTahunan()));
        if (data instanceof DapatDidiskon dd) {
            addRow(panelContent, gbc, row++, "Diskon Tahunan:", LanggananGUIController.formatRupiah(dd.hitungDiskon(12)));
        }

        JPanel panelBtn = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnUbah = new JButton("Edit");
        btnUbah.addActionListener(e -> {
            FormLanggananDialog form = new FormLanggananDialog(owner, controller, FormLanggananDialog.Mode.EDIT, data);
            form.setVisible(true);
            if (form.isSuksesDisimpan()) {
                diedit = true;
                dispose();
            }
        });

        JButton btnTutup = new JButton("Tutup");
        btnTutup.addActionListener(e -> dispose());

        panelBtn.add(btnUbah);
        panelBtn.add(btnTutup);

        add(new JScrollPane(panelContent), BorderLayout.CENTER);
        add(panelBtn, BorderLayout.SOUTH);
    }

    private void addRow(JPanel panel, GridBagConstraints gbc, int row, String label, String value) {
        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0.4;
        panel.add(new JLabel(label), gbc);

        gbc.gridx = 1; gbc.gridy = row; gbc.weightx = 0.6;
        panel.add(new JLabel(value != null ? value : "-"), gbc);
    }

    public boolean isDiedit() {
        return diedit;
    }
}
