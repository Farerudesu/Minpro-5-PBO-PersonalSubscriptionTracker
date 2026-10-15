package com.pbo.fareru.minpro.pbo.view.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import com.pbo.fareru.minpro.pbo.controller.LanggananGUIController;
import com.pbo.fareru.minpro.pbo.model.Langganan;
import com.pbo.fareru.minpro.pbo.model.LanggananProduktivitas;
import com.pbo.fareru.minpro.pbo.model.LanggananStreaming;

public class MainFrame extends JFrame {
    private final LanggananGUIController controller;
    private final List<Langganan> dataTampil;

    private final JLabel lblValTotalBulanan;
    private final JLabel lblValLanggananAktif;
    private final JLabel lblValStreaming;
    private final JLabel lblValProduktivitas;

    private final JTextField txtPencarian;
    private final JComboBox<String> cmbFilterKategori;
    private final JComboBox<String> cmbFilterStatus;
    private final JLabel lblInfoJumlah;

    private final JTable tableLangganan;
    private final DefaultTableModel tableModel;

    private final JButton btnTambah;
    private final JButton btnEdit;
    private final JButton btnHapus;
    private final JButton btnDetail;
    private final JButton btnSimulasi;
    private final JButton btnRefresh;
    private final JButton btnTesOrm;

    public MainFrame() {
        super("Personal Subscription Tracker");
        this.controller = new LanggananGUIController();
        this.dataTampil = new ArrayList<>();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1050, 680);
        setMinimumSize(new Dimension(850, 500));
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel panelHeader = new JPanel(new BorderLayout());
        panelHeader.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        JLabel lblTitle = new JLabel("Personal Subscription Tracker");
        lblTitle.setFont(lblTitle.getFont().deriveFont(18.0f));
        JLabel lblStatusDb = new JLabel("Status: MySQL Terhubung");

        panelHeader.add(lblTitle, BorderLayout.WEST);
        panelHeader.add(lblStatusDb, BorderLayout.EAST);

        JPanel panelMetrics = new JPanel(new GridLayout(1, 4, 10, 0));
        panelMetrics.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(0, 15, 10, 15),
                BorderFactory.createTitledBorder("Ringkasan Pengeluaran")));

        lblValTotalBulanan = new JLabel("Rp 0,00");
        lblValLanggananAktif = new JLabel("0");
        lblValStreaming = new JLabel("Rp 0,00");
        lblValProduktivitas = new JLabel("Rp 0,00");

        panelMetrics.add(buatPanelRingkasan("Total Bulanan:", lblValTotalBulanan));
        panelMetrics.add(buatPanelRingkasan("Langganan Aktif:", lblValLanggananAktif));
        panelMetrics.add(buatPanelRingkasan("Biaya Streaming:", lblValStreaming));
        panelMetrics.add(buatPanelRingkasan("Biaya Produktivitas:", lblValProduktivitas));

        JPanel panelToolbar = new JPanel(new BorderLayout());
        panelToolbar.setBorder(BorderFactory.createEmptyBorder(0, 15, 10, 15));

        JPanel panelFilterKiri = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        panelFilterKiri.add(new JLabel("Cari:"));

        txtPencarian = new JTextField(15);
        panelFilterKiri.add(txtPencarian);

        panelFilterKiri.add(new JLabel("Kategori:"));
        cmbFilterKategori = new JComboBox<>(new String[]{"Semua", "Streaming", "Produktivitas"});
        panelFilterKiri.add(cmbFilterKategori);

        panelFilterKiri.add(new JLabel("Status:"));
        cmbFilterStatus = new JComboBox<>(new String[]{"Semua", "Aktif", "Nonaktif"});
        panelFilterKiri.add(cmbFilterStatus);

        JButton btnReset = new JButton("Reset");
        btnReset.addActionListener(e -> resetFilter());
        panelFilterKiri.add(btnReset);

        lblInfoJumlah = new JLabel("Menampilkan 0 data");
        JPanel panelFilterKanan = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        panelFilterKanan.add(lblInfoJumlah);

        panelToolbar.add(panelFilterKiri, BorderLayout.WEST);
        panelToolbar.add(panelFilterKanan, BorderLayout.EAST);

        JPanel panelAtas = new JPanel(new BorderLayout());
        panelAtas.add(panelHeader, BorderLayout.NORTH);
        panelAtas.add(panelMetrics, BorderLayout.CENTER);
        panelAtas.add(panelToolbar, BorderLayout.SOUTH);
        add(panelAtas, BorderLayout.NORTH);

        String[] columns = {
            "No", "ID", "Layanan", "Kategori / Tipe", "Harga / Bulan",
            "Tgl Tagihan", "Status", "Metode Pembayaran", "Detail Spesifik", "Kapasitas / Lisensi"
        };

        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tableLangganan = new JTable(tableModel);
        tableLangganan.setRowHeight(24);
        tableLangganan.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        konfigurasiTabel();

        tableLangganan.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 && tableLangganan.getSelectedRow() != -1) {
                    bukaDialogDetail();
                }
            }
        });

        tableLangganan.getSelectionModel().addListSelectionListener(e -> updateStatusTombolAksi());

        JScrollPane scrollPane = new JScrollPane(tableLangganan);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(0, 15, 10, 15));
        add(scrollPane, BorderLayout.CENTER);

        JPanel panelAksi = new JPanel(new BorderLayout());
        panelAksi.setBorder(BorderFactory.createEmptyBorder(5, 15, 10, 15));

        JPanel panelAksiKiri = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        btnTambah = new JButton("Tambah");
        btnTambah.addActionListener(e -> bukaDialogTambah());

        btnEdit = new JButton("Edit");
        btnEdit.addActionListener(e -> bukaDialogEdit());

        btnHapus = new JButton("Hapus");
        btnHapus.addActionListener(e -> konfirmasiHapusData());

        btnDetail = new JButton("Detail");
        btnDetail.addActionListener(e -> bukaDialogDetail());

        btnSimulasi = new JButton("Simulasi Biaya");
        btnSimulasi.addActionListener(e -> bukaDialogSimulasi());

        panelAksiKiri.add(btnTambah);
        panelAksiKiri.add(btnEdit);
        panelAksiKiri.add(btnHapus);
        panelAksiKiri.add(btnDetail);
        panelAksiKiri.add(btnSimulasi);

        JPanel panelAksiKanan = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        btnRefresh = new JButton("Refresh");
        btnRefresh.addActionListener(e -> muatDataDariDatabase());

        btnTesOrm = new JButton("Tes ORM");
        btnTesOrm.addActionListener(e -> {
            String hasil = controller.cekStatusOrmHibernate();
            JOptionPane.showMessageDialog(this, hasil, "Status ORM Hibernate", JOptionPane.INFORMATION_MESSAGE);
        });

        panelAksiKanan.add(btnRefresh);
        panelAksiKanan.add(btnTesOrm);

        panelAksi.add(panelAksiKiri, BorderLayout.WEST);
        panelAksi.add(panelAksiKanan, BorderLayout.EAST);
        add(panelAksi, BorderLayout.SOUTH);

        txtPencarian.getDocument().addDocumentListener(new DocumentListener() {
            @Override public void insertUpdate(DocumentEvent e) { terapkanFilter(); }
            @Override public void removeUpdate(DocumentEvent e) { terapkanFilter(); }
            @Override public void changedUpdate(DocumentEvent e) { terapkanFilter(); }
        });
        cmbFilterKategori.addActionListener(e -> terapkanFilter());
        cmbFilterStatus.addActionListener(e -> terapkanFilter());

        updateStatusTombolAksi();
        muatDataDariDatabase();
    }

    private JPanel buatPanelRingkasan(String judul, JLabel lblNilai) {
        JPanel p = new JPanel(new GridLayout(2, 1));
        p.add(new JLabel(judul));
        p.add(lblNilai);
        return p;
    }

    private void konfigurasiTabel() {
        DefaultTableCellRenderer center = new DefaultTableCellRenderer();
        center.setHorizontalAlignment(SwingConstants.CENTER);

        DefaultTableCellRenderer right = new DefaultTableCellRenderer();
        right.setHorizontalAlignment(SwingConstants.RIGHT);

        tableLangganan.getColumnModel().getColumn(0).setPreferredWidth(40);
        tableLangganan.getColumnModel().getColumn(0).setCellRenderer(center);

        tableLangganan.getColumnModel().getColumn(1).setPreferredWidth(70);
        tableLangganan.getColumnModel().getColumn(1).setCellRenderer(center);

        tableLangganan.getColumnModel().getColumn(2).setPreferredWidth(130);
        tableLangganan.getColumnModel().getColumn(3).setPreferredWidth(160);

        tableLangganan.getColumnModel().getColumn(4).setPreferredWidth(120);
        tableLangganan.getColumnModel().getColumn(4).setCellRenderer(right);

        tableLangganan.getColumnModel().getColumn(5).setPreferredWidth(80);
        tableLangganan.getColumnModel().getColumn(5).setCellRenderer(center);

        tableLangganan.getColumnModel().getColumn(6).setPreferredWidth(80);
        tableLangganan.getColumnModel().getColumn(6).setCellRenderer(center);

        tableLangganan.getColumnModel().getColumn(7).setPreferredWidth(140);
        tableLangganan.getColumnModel().getColumn(8).setPreferredWidth(180);

        tableLangganan.getColumnModel().getColumn(9).setPreferredWidth(100);
        tableLangganan.getColumnModel().getColumn(9).setCellRenderer(center);
    }

    public void muatDataDariDatabase() {
        try {
            controller.muatData();
            terapkanFilter();
            perbaruiRingkasan();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                    "Gagal menghubungkan ke database: " + e.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void perbaruiRingkasan() {
        List<Langganan> semua = controller.getListLangganan();
        double totalBulanan = controller.hitungTotalPengeluaranBulanan(semua);
        int aktifCount = controller.hitungTotalLanggananAktif(semua);
        double totalStreaming = controller.hitungTotalStreaming(semua);
        double totalProduktivitas = controller.hitungTotalProduktivitas(semua);

        lblValTotalBulanan.setText(LanggananGUIController.formatRupiah(totalBulanan));
        lblValLanggananAktif.setText(aktifCount + " dari " + semua.size());
        lblValStreaming.setText(LanggananGUIController.formatRupiah(totalStreaming));
        lblValProduktivitas.setText(LanggananGUIController.formatRupiah(totalProduktivitas));
    }

    private void terapkanFilter() {
        String kw = txtPencarian.getText();
        String kategori = (String) cmbFilterKategori.getSelectedItem();
        String status = (String) cmbFilterStatus.getSelectedItem();

        List<Langganan> hasil = controller.filterData(kw, kategori, status);
        dataTampil.clear();
        dataTampil.addAll(hasil);

        tableModel.setRowCount(0);
        for (int i = 0; i < dataTampil.size(); i++) {
            Langganan l = dataTampil.get(i);
            String noStr = String.valueOf(i + 1);
            String idStr = l.getIdSubscription();
            String namaLayanan = l.getLayanan() != null ? l.getLayanan().getNamaLayanan() : "-";
            String kategoriStr = l.getTipeLangganan();
            String hargaStr = LanggananGUIController.formatRupiah(l.getHargaBulanan());
            String tglStr = "Tgl " + l.getTanggalTagihan();
            String statusStr = l.getStatus();
            String metodeStr = l.getMetode() != null ? l.getMetode().getNamaMetode() : "-";

            String detailStr = "-";
            String kapasitasStr = "-";
            if (l instanceof LanggananStreaming ls) {
                detailStr = ls.getKualitasResolusi();
                kapasitasStr = ls.getBatasLayar() + " Layar";
            } else if (l instanceof LanggananProduktivitas lp) {
                detailStr = lp.getKapasitasStorage();
                kapasitasStr = lp.getLisensiUser() + " User";
            }

            tableModel.addRow(new Object[]{
                noStr, idStr, namaLayanan, kategoriStr, hargaStr,
                tglStr, statusStr, metodeStr, detailStr, kapasitasStr
            });
        }

        lblInfoJumlah.setText("Menampilkan " + dataTampil.size() + " dari " + controller.getListLangganan().size() + " data");
        updateStatusTombolAksi();
    }

    private void resetFilter() {
        txtPencarian.setText("");
        cmbFilterKategori.setSelectedIndex(0);
        cmbFilterStatus.setSelectedIndex(0);
        terapkanFilter();
    }

    private void updateStatusTombolAksi() {
        boolean adaPilihan = tableLangganan.getSelectedRow() != -1;
        btnEdit.setEnabled(adaPilihan);
        btnHapus.setEnabled(adaPilihan);
        btnDetail.setEnabled(adaPilihan);
    }

    private Langganan getLanggananTerpilih() {
        int row = tableLangganan.getSelectedRow();
        if (row >= 0 && row < dataTampil.size()) {
            return dataTampil.get(row);
        }
        return null;
    }

    private void bukaDialogTambah() {
        FormLanggananDialog dlg = new FormLanggananDialog(this, controller, FormLanggananDialog.Mode.TAMBAH, null);
        dlg.setVisible(true);
        if (dlg.isSuksesDisimpan()) {
            muatDataDariDatabase();
        }
    }

    private void bukaDialogEdit() {
        Langganan item = getLanggananTerpilih();
        if (item == null) {
            JOptionPane.showMessageDialog(this, "Pilih data langganan yang ingin diedit!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }
        FormLanggananDialog dlg = new FormLanggananDialog(this, controller, FormLanggananDialog.Mode.EDIT, item);
        dlg.setVisible(true);
        if (dlg.isSuksesDisimpan()) {
            muatDataDariDatabase();
        }
    }

    private void konfirmasiHapusData() {
        Langganan item = getLanggananTerpilih();
        if (item == null) {
            JOptionPane.showMessageDialog(this, "Pilih data langganan yang ingin dihapus!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String nama = item.getLayanan() != null ? item.getLayanan().getNamaLayanan() : item.getIdSubscription();
        int respon = JOptionPane.showConfirmDialog(this,
                "Apakah Anda yakin ingin menghapus langganan berikut?\n\n"
                + "ID      : " + item.getIdSubscription() + "\n"
                + "Layanan : " + nama + "\n"
                + "Harga   : " + LanggananGUIController.formatRupiah(item.getHargaBulanan()) + "\n",
                "Konfirmasi Hapus",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (respon == JOptionPane.YES_OPTION) {
            try {
                controller.hapusLangganan(item.getIdSubscription());
                JOptionPane.showMessageDialog(this, "Data langganan berhasil dihapus!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
                muatDataDariDatabase();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Gagal menghapus data: " + e.getMessage(), "Error Database", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void bukaDialogDetail() {
        Langganan item = getLanggananTerpilih();
        if (item == null) {
            JOptionPane.showMessageDialog(this, "Pilih data langganan terlebih dahulu!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }
        DetailLanggananDialog dlg = new DetailLanggananDialog(this, controller, item);
        dlg.setVisible(true);
        if (dlg.isDiedit()) {
            muatDataDariDatabase();
        }
    }

    private void bukaDialogSimulasi() {
        List<Langganan> semua = controller.getListLangganan();
        if (semua.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Belum ada data langganan!", "Informasi", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        Langganan terpilih = getLanggananTerpilih();
        SimulasiDiskonDialog dlg = new SimulasiDiskonDialog(this, semua, terpilih);
        dlg.setVisible(true);
    }
}
