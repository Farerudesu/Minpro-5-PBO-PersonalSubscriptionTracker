package com.pbo.fareru.minpro.pbo.view.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.pbo.fareru.minpro.pbo.controller.LanggananGUIController;
import com.pbo.fareru.minpro.pbo.model.Layanan;

public class TambahLayananDialog extends JDialog {
    private final LanggananGUIController controller;
    private final JTextField txtId;
    private final JTextField txtNama;
    private final JComboBox<String> cmbKategori;
    private Layanan layananBaru = null;

    public TambahLayananDialog(Frame owner, LanggananGUIController controller) {
        super(owner, "Tambah Layanan", true);
        this.controller = controller;

        setSize(400, 220);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout());
        setResizable(false);

        JPanel panelForm = new JPanel(new GridBagLayout());
        panelForm.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.3;
        panelForm.add(new JLabel("ID Layanan:"), gbc);

        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 0.7;
        txtId = new JTextField(controller.generateIdLayananBaru());
        txtId.setEditable(false);
        panelForm.add(txtId, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0.3;
        panelForm.add(new JLabel("Nama Layanan:"), gbc);

        gbc.gridx = 1; gbc.gridy = 1; gbc.weightx = 0.7;
        txtNama = new JTextField();
        panelForm.add(txtNama, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0.3;
        panelForm.add(new JLabel("Kategori:"), gbc);

        gbc.gridx = 1; gbc.gridy = 2; gbc.weightx = 0.7;
        String[] kategoriList = {
            "Hiburan Film & Serial",
            "Streaming Musik & Audio",
            "AI & Coding",
            "Cloud Computing",
            "Server & Hosting",
            "Containerization",
            "Office & Produktivitas",
            "Design & Creative",
            "Layanan Digital Lainnya"
        };
        cmbKategori = new JComboBox<>(kategoriList);
        panelForm.add(cmbKategori, gbc);

        JPanel panelBtn = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnBatal = new JButton("Batal");
        btnBatal.addActionListener(e -> dispose());

        JButton btnSimpan = new JButton("Simpan");
        btnSimpan.addActionListener(e -> simpanLayanan());

        panelBtn.add(btnBatal);
        panelBtn.add(btnSimpan);

        add(panelForm, BorderLayout.CENTER);
        add(panelBtn, BorderLayout.SOUTH);
    }

    private void simpanLayanan() {
        String nama = txtNama.getText().trim();
        String kategori = (String) cmbKategori.getSelectedItem();

        if (nama.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nama layanan tidak boleh kosong!", "Validasi Input", JOptionPane.WARNING_MESSAGE);
            txtNama.requestFocus();
            return;
        }

        try {
            layananBaru = new Layanan(txtId.getText(), nama, kategori);
            controller.tambahLayanan(layananBaru);
            JOptionPane.showMessageDialog(this, "Layanan berhasil disimpan!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Gagal menyimpan layanan: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public Layanan getLayananBaru() {
        return layananBaru;
    }
}
