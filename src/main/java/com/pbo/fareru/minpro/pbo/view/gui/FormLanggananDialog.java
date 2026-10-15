package com.pbo.fareru.minpro.pbo.view.gui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ItemEvent;
import java.sql.SQLException;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.pbo.fareru.minpro.pbo.controller.LanggananGUIController;
import com.pbo.fareru.minpro.pbo.model.Langganan;
import com.pbo.fareru.minpro.pbo.model.LanggananProduktivitas;
import com.pbo.fareru.minpro.pbo.model.LanggananStreaming;
import com.pbo.fareru.minpro.pbo.model.Layanan;
import com.pbo.fareru.minpro.pbo.model.MetodePembayaran;

public class FormLanggananDialog extends JDialog {
    public enum Mode { TAMBAH, EDIT }

    private final Frame owner;
    private final LanggananGUIController controller;
    private final Mode mode;
    private final Langganan dataAwal;
    private boolean suksesDisimpan = false;

    private final JTextField txtId;
    private final JComboBox<Layanan> cmbLayanan;
    private final JComboBox<String> cmbTipe;
    private final JTextField txtHarga;
    private final JLabel lblHargaPreview;
    private final JComboBox<MetodePembayaran> cmbMetode;
    private final JSpinner spnTanggal;
    private final JComboBox<String> cmbStatus;

    private final JComboBox<String> cmbResolusi;
    private final JSpinner spnLayar;

    private final JTextField txtStorage;
    private final JSpinner spnLisensi;

    private final CardLayout cardLayoutTipe;
    private final JPanel panelDinamis;
    private final JLabel lblPreviewTahunan;

    public FormLanggananDialog(Frame owner, LanggananGUIController controller, Mode mode, Langganan dataAwal) {
        super(owner, mode == Mode.TAMBAH ? "Tambah Langganan" : "Edit Langganan", true);
        this.owner = owner;
        this.controller = controller;
        this.mode = mode;
        this.dataAwal = dataAwal;

        setSize(520, 600);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout());
        setResizable(false);

        JPanel panelUtama = new JPanel(new GridBagLayout());
        panelUtama.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        int row = 0;

        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0.35;
        panelUtama.add(new JLabel("ID Subscription:"), gbc);

        gbc.gridx = 1; gbc.gridy = row; gbc.weightx = 0.65;
        String idInit = (mode == Mode.EDIT && dataAwal != null)
                ? dataAwal.getIdSubscription()
                : controller.generateIdLanggananBaru();
        txtId = new JTextField(idInit);
        txtId.setEditable(false);
        panelUtama.add(txtId, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row;
        panelUtama.add(new JLabel("Layanan:"), gbc);

        gbc.gridx = 1; gbc.gridy = row;
        JPanel panelLayananRow = new JPanel(new BorderLayout(5, 0));
        cmbLayanan = new JComboBox<>();
        muatComboLayanan();
        panelLayananRow.add(cmbLayanan, BorderLayout.CENTER);

        JButton btnTambahLayanan = new JButton("+");
        btnTambahLayanan.addActionListener(e -> bukaDialogTambahLayanan());
        panelLayananRow.add(btnTambahLayanan, BorderLayout.EAST);
        panelUtama.add(panelLayananRow, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row;
        panelUtama.add(new JLabel("Kategori Tipe:"), gbc);

        gbc.gridx = 1; gbc.gridy = row;
        cmbTipe = new JComboBox<>(new String[]{"STREAMING", "PRODUCTIVITY"});
        if (mode == Mode.EDIT) {
            cmbTipe.setEnabled(false);
        }
        panelUtama.add(cmbTipe, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 2;
        cardLayoutTipe = new CardLayout();
        panelDinamis = new JPanel(cardLayoutTipe);
        panelDinamis.setBorder(BorderFactory.createTitledBorder("Detail Khusus Tipe"));

        JPanel panelStreaming = new JPanel(new GridBagLayout());
        GridBagConstraints gbcS = new GridBagConstraints();
        gbcS.fill = GridBagConstraints.HORIZONTAL;
        gbcS.insets = new Insets(4, 5, 4, 5);

        gbcS.gridx = 0; gbcS.gridy = 0; gbcS.weightx = 0.35;
        panelStreaming.add(new JLabel("Resolusi/Audio:"), gbcS);

        gbcS.gridx = 1; gbcS.gridy = 0; gbcS.weightx = 0.65;
        String[] resolusiOpts = {
            "4K UHD + HDR",
            "1080p Full HD",
            "720p HD",
            "Very High Quality",
            "High Quality Audio",
            "Dolby Atmos"
        };
        cmbResolusi = new JComboBox<>(resolusiOpts);
        cmbResolusi.setEditable(true);
        panelStreaming.add(cmbResolusi, gbcS);

        gbcS.gridx = 0; gbcS.gridy = 1;
        panelStreaming.add(new JLabel("Batas Layar:"), gbcS);

        gbcS.gridx = 1; gbcS.gridy = 1;
        spnLayar = new JSpinner(new SpinnerNumberModel(1, 1, 10, 1));
        panelStreaming.add(spnLayar, gbcS);

        JPanel panelProduktivitas = new JPanel(new GridBagLayout());
        GridBagConstraints gbcP = new GridBagConstraints();
        gbcP.fill = GridBagConstraints.HORIZONTAL;
        gbcP.insets = new Insets(4, 5, 4, 5);

        gbcP.gridx = 0; gbcP.gridy = 0; gbcP.weightx = 0.35;
        panelProduktivitas.add(new JLabel("Fitur/Storage:"), gbcP);

        gbcP.gridx = 1; gbcP.gridy = 0; gbcP.weightx = 0.65;
        txtStorage = new JTextField("2 TB Cloud Storage");
        panelProduktivitas.add(txtStorage, gbcP);

        gbcP.gridx = 0; gbcP.gridy = 1;
        panelProduktivitas.add(new JLabel("Lisensi User:"), gbcP);

        gbcP.gridx = 1; gbcP.gridy = 1;
        spnLisensi = new JSpinner(new SpinnerNumberModel(1, 1, 100, 1));
        panelProduktivitas.add(spnLisensi, gbcP);

        panelDinamis.add(panelStreaming, "STREAMING");
        panelDinamis.add(panelProduktivitas, "PRODUCTIVITY");
        panelUtama.add(panelDinamis, gbc);

        row++;
        gbc.gridwidth = 1;
        gbc.gridx = 0; gbc.gridy = row;
        panelUtama.add(new JLabel("Harga Bulanan (Rp):"), gbc);

        gbc.gridx = 1; gbc.gridy = row;
        JPanel panelHargaGroup = new JPanel(new BorderLayout(0, 2));
        txtHarga = new JTextField();
        lblHargaPreview = new JLabel("Rp 0,00");
        panelHargaGroup.add(txtHarga, BorderLayout.CENTER);
        panelHargaGroup.add(lblHargaPreview, BorderLayout.SOUTH);
        panelUtama.add(panelHargaGroup, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row;
        panelUtama.add(new JLabel("Tanggal Tagihan (1-31):"), gbc);

        gbc.gridx = 1; gbc.gridy = row;
        spnTanggal = new JSpinner(new SpinnerNumberModel(1, 1, 31, 1));
        panelUtama.add(spnTanggal, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row;
        panelUtama.add(new JLabel("Metode Pembayaran:"), gbc);

        gbc.gridx = 1; gbc.gridy = row;
        cmbMetode = new JComboBox<>();
        muatComboMetode();
        panelUtama.add(cmbMetode, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row;
        panelUtama.add(new JLabel("Status:"), gbc);

        gbc.gridx = 1; gbc.gridy = row;
        cmbStatus = new JComboBox<>(new String[]{"Aktif", "Nonaktif"});
        panelUtama.add(cmbStatus, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 2;
        JPanel panelPreviewCard = new JPanel(new BorderLayout());
        panelPreviewCard.setBorder(BorderFactory.createTitledBorder("Estimasi Biaya Tahunan"));
        lblPreviewTahunan = new JLabel("Rp 0,00 / tahun");
        panelPreviewCard.add(lblPreviewTahunan, BorderLayout.CENTER);
        panelUtama.add(panelPreviewCard, gbc);

        cmbTipe.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                String tipe = (String) cmbTipe.getSelectedItem();
                cardLayoutTipe.show(panelDinamis, tipe);
                updateLivePreview();
            }
        });

        txtHarga.getDocument().addDocumentListener(new DocumentListener() {
            @Override public void insertUpdate(DocumentEvent e) { updateLivePreview(); }
            @Override public void removeUpdate(DocumentEvent e) { updateLivePreview(); }
            @Override public void changedUpdate(DocumentEvent e) { updateLivePreview(); }
        });

        JPanel panelBtn = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnBatal = new JButton("Batal");
        btnBatal.addActionListener(e -> dispose());

        JButton btnSimpan = new JButton(mode == Mode.TAMBAH ? "Simpan" : "Perbarui");
        btnSimpan.addActionListener(e -> simpanData());

        panelBtn.add(btnBatal);
        panelBtn.add(btnSimpan);

        add(panelUtama, BorderLayout.CENTER);
        add(panelBtn, BorderLayout.SOUTH);

        if (mode == Mode.EDIT && dataAwal != null) {
            isiDataUntukEdit(dataAwal);
        } else {
            txtHarga.setText("50000");
        }
        updateLivePreview();
    }

    private void muatComboLayanan() {
        cmbLayanan.removeAllItems();
        List<Layanan> daftar = controller.getListLayanan();
        for (Layanan l : daftar) {
            cmbLayanan.addItem(l);
        }
    }

    private void muatComboMetode() {
        cmbMetode.removeAllItems();
        List<MetodePembayaran> daftar = controller.getListMetode();
        for (MetodePembayaran m : daftar) {
            cmbMetode.addItem(m);
        }
    }

    private void bukaDialogTambahLayanan() {
        TambahLayananDialog dlg = new TambahLayananDialog(owner, controller);
        dlg.setVisible(true);
        if (dlg.getLayananBaru() != null) {
            muatComboLayanan();
            for (int i = 0; i < cmbLayanan.getItemCount(); i++) {
                if (cmbLayanan.getItemAt(i).getIdLayanan().equalsIgnoreCase(dlg.getLayananBaru().getIdLayanan())) {
                    cmbLayanan.setSelectedIndex(i);
                    break;
                }
            }
        }
    }

    private void isiDataUntukEdit(Langganan data) {
        txtId.setText(data.getIdSubscription());
        txtHarga.setText(String.valueOf((long) data.getHargaBulanan()));
        spnTanggal.setValue(data.getTanggalTagihan());
        cmbStatus.setSelectedItem(data.getStatus());

        if (data.getLayanan() != null) {
            for (int i = 0; i < cmbLayanan.getItemCount(); i++) {
                if (cmbLayanan.getItemAt(i).getIdLayanan().equalsIgnoreCase(data.getLayanan().getIdLayanan())) {
                    cmbLayanan.setSelectedIndex(i);
                    break;
                }
            }
        }

        if (data.getMetode() != null) {
            for (int i = 0; i < cmbMetode.getItemCount(); i++) {
                if (cmbMetode.getItemAt(i).getIdMetode().equalsIgnoreCase(data.getMetode().getIdMetode())) {
                    cmbMetode.setSelectedIndex(i);
                    break;
                }
            }
        }

        if (data instanceof LanggananStreaming ls) {
            cmbTipe.setSelectedItem("STREAMING");
            cardLayoutTipe.show(panelDinamis, "STREAMING");
            cmbResolusi.setSelectedItem(ls.getKualitasResolusi());
            spnLayar.setValue(ls.getBatasLayar());
        } else if (data instanceof LanggananProduktivitas lp) {
            cmbTipe.setSelectedItem("PRODUCTIVITY");
            cardLayoutTipe.show(panelDinamis, "PRODUCTIVITY");
            txtStorage.setText(lp.getKapasitasStorage());
            spnLisensi.setValue(lp.getLisensiUser());
        }
    }

    private void updateLivePreview() {
        try {
            double harga = Double.parseDouble(txtHarga.getText().trim());
            lblHargaPreview.setText(LanggananGUIController.formatRupiah(harga) + " / bulan");

            String tipe = (String) cmbTipe.getSelectedItem();
            double estimasiTahunan;
            if ("STREAMING".equalsIgnoreCase(tipe)) {
                estimasiTahunan = harga * 12 * 0.95;
                lblPreviewTahunan.setText(LanggananGUIController.formatRupiah(estimasiTahunan) + " (Diskon 5%)");
            } else {
                estimasiTahunan = harga * 12 * 0.90;
                lblPreviewTahunan.setText(LanggananGUIController.formatRupiah(estimasiTahunan) + " (Diskon 10%)");
            }
        } catch (NumberFormatException e) {
            lblHargaPreview.setText("Format angka belum sesuai");
            lblPreviewTahunan.setText("Rp 0,00");
        }
    }

    private void simpanData() {
        Layanan layanan = (Layanan) cmbLayanan.getSelectedItem();
        MetodePembayaran metode = (MetodePembayaran) cmbMetode.getSelectedItem();
        String strHarga = txtHarga.getText().trim();
        int tanggal = (Integer) spnTanggal.getValue();
        String status = (String) cmbStatus.getSelectedItem();
        String tipe = (String) cmbTipe.getSelectedItem();

        if (layanan == null) {
            JOptionPane.showMessageDialog(this, "Silakan pilih layanan!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (metode == null) {
            JOptionPane.showMessageDialog(this, "Silakan pilih metode pembayaran!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        double harga;
        try {
            harga = Double.parseDouble(strHarga);
            if (harga <= 0) {
                JOptionPane.showMessageDialog(this, "Harga bulanan harus lebih besar dari 0!", "Peringatan", JOptionPane.WARNING_MESSAGE);
                txtHarga.requestFocus();
                return;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Harga bulanan harus berupa angka!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            txtHarga.requestFocus();
            return;
        }

        Langganan modelBaru;
        String idSub = txtId.getText().trim();

        if ("STREAMING".equalsIgnoreCase(tipe)) {
            String resolusi = cmbResolusi.getSelectedItem() != null ? cmbResolusi.getSelectedItem().toString().trim() : "1080p Full HD";
            int layar = (Integer) spnLayar.getValue();
            if (resolusi.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Kualitas resolusi tidak boleh kosong!", "Peringatan", JOptionPane.WARNING_MESSAGE);
                return;
            }
            modelBaru = new LanggananStreaming(idSub, layanan, harga, metode, tanggal, resolusi, layar);
        } else {
            String storage = txtStorage.getText().trim();
            int lisensi = (Integer) spnLisensi.getValue();
            if (storage.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Fitur/Storage tidak boleh kosong!", "Peringatan", JOptionPane.WARNING_MESSAGE);
                txtStorage.requestFocus();
                return;
            }
            modelBaru = new LanggananProduktivitas(idSub, layanan, harga, metode, tanggal, storage, lisensi);
        }

        modelBaru.setStatus(status);

        try {
            if (mode == Mode.TAMBAH) {
                controller.tambahLangganan(modelBaru);
                JOptionPane.showMessageDialog(this, "Data langganan berhasil disimpan!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
            } else {
                controller.ubahLangganan(modelBaru);
                JOptionPane.showMessageDialog(this, "Data langganan berhasil diubah!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
            }
            suksesDisimpan = true;
            dispose();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Gagal menyimpan ke database: " + ex.getMessage(), "Error Database", JOptionPane.ERROR_MESSAGE);
        }
    }

    public boolean isSuksesDisimpan() {
        return suksesDisimpan;
    }
}
