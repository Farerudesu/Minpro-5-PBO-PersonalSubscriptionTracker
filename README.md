# Minpro-5-PBO-PersonalSubscriptionTracker

```
+-------------------------------------------------------------+
| Nama  : Muhammad Fahriel                                    |
| NIM   : 2509116050                                          |
| Kelas : Sistem Informasi A 2024                             |
| Tema  : Personal Subscription Tracker (Desktop GUI App)     |
+-------------------------------------------------------------+
```

Aplikasi desktop interaktif berbasis **Java Swing GUI** untuk mencatat, mengelola, memantau, dan menghitung pengeluaran biaya langganan digital pribadi. Proyek **Mini Project 5 (Minpro 5)** ini melanjutkan pengembangan dari **Mini Project 4** dengan menambahkan antarmuka grafis (**Graphical User Interface / GUI**) menggunakan komponen bawaan Java Swing seperti `JFrame`, `JPanel`, `JButton`, `JTextField`, dan `JTable`. Seluruh fungsionalitas CRUD terhubung langsung ke **Database Relasional MySQL** melalui **JDBC Prepared Statement** dan arsitektur **Data Access Object (DAO)**, dengan data awal yang dimuat secara otomatis dari database tanpa harus memasukkan data secara manual. Proyek ini juga tetap mempertahankan konsep **OOP (Encapsulation, Inheritance, Polymorphism, Abstraction, Interface)**, implementasi **Framework ORM (Hibernate 6 & Jakarta Persistence)**, serta arsitektur **Model-View-Controller (MVC)**.

---

## Daftar Isi

1. [Deskripsi Singkat Program](#1-deskripsi-singkat-program)
2. [Penjelasan Struktur Package](#2-penjelasan-struktur-package)
3. [Penjelasan Alur Program](#3-penjelasan-alur-program)
4. [Penjelasan Penerapan Encapsulation dan Inheritance](#4-penjelasan-penerapan-encapsulation-dan-inheritance)
   - [A. Encapsulation & Data Hiding](#a-encapsulation--data-hiding)
   - [B. Inheritance (Pewarisan Kelas)](#b-inheritance-pewarisan-kelas)
5. [Penjelasan Penerapan Polymorphism dan Abstraction](#5-penjelasan-penerapan-polymorphism-dan-abstraction)
   - [A. Abstraction (Abstract Class & Abstract Method)](#a-abstraction-abstract-class--abstract-method)
   - [B. Polymorphism (Overriding & Overloading)](#b-polymorphism-overriding--overloading)
   - [C. Interface `DapatDidiskon` & Keyword `final`](#c-interface-dapatdidiskon--keyword-final)
6. [Penjelasan Penerapan JDBC](#6-penjelasan-penerapan-jdbc)
   - [A. Koneksi Database Relasional MySQL](#a-koneksi-database-relasional-mysql)
   - [B. Keamanan Query via Prepared Statement](#b-keamanan-query-via-prepared-statement)
   - [C. Arsitektur Data Access Object (DAO)](#c-arsitektur-data-access-object-dao)
   - [D. Inisialisasi Data Otomatis via Database (.sql)](#d-inisialisasi-data-otomatis-via-database-sql)
7. [Penjelasan Implementasi GUI Menggunakan Java Swing](#7-penjelasan-implementasi-gui-menggunakan-java-swing)
   - [A. Komponen-Komponen Utama Swing](#a-komponen-komponen-utama-swing)
   - [B. Tata Letak Antarmuka (Layout Management)](#b-tata-letak-antarmuka-layout-management)
   - [C. Penanganan Aksi Pengguna (Event Handling)](#c-penanganan-aksi-pengguna-event-handling)
8. [Penjelasan Hubungan Antara GUI, Controller, DAO, dan Database](#8-penjelasan-hubungan-antara-gui-controller-dao-dan-database)
9. [Penjelasan Letak Penerapan Nilai Tambah](#9-penjelasan-letak-penerapan-nilai-tambah)
   - [A. Desain GUI yang Rapi, Sederhana, dan Konsisten](#a-desain-gui-yang-rapi-sederhana-dan-konsisten)
   - [B. Penggunaan JTable Terstruktur & Pemformatan Data](#b-penggunaan-jtable-terstruktur--pemformatan-data)
   - [C. Fitur Pencarian dan Penyaringan Data (Search & Filter)](#c-fitur-pencarian-dan-penyaringan-data-search--filter)
   - [D. Integrasi Framework ORM (Hibernate 6)](#d-integrasi-framework-orm-hibernate-6)
   - [E. Dialog Simulasi Estimasi Biaya & Diskon Polimorfik](#e-dialog-simulasi-estimasi-biaya--diskon-polimorfik)
10. [Panduan Menjalankan Program](#10-panduan-menjalankan-program)

---

## 1. Deskripsi Singkat Program

**Personal Subscription Tracker** adalah aplikasi yang dirancang untuk membantu pengguna melacak berbagai layanan digital berbayar yang digunakan, seperti layanan hiburan (*Netflix, Spotify, Disney+*) maupun layanan produktivitas dan pengembangan (*Claude Code, GitHub Copilot, AWS, VPS Hosting, Docker*).

Fitur-fitur utama pada aplikasi ini meliputi:
- **Pengelolaan Data Lengkap (CRUD via GUI)**:
  - **Create**: Menambah data langganan baru melalui form dialog modal dengan ID otomatis, pemilihan master layanan dan metode pembayaran, serta field dinamis sesuai jenis langganan.
  - **Read**: Menampilkan seluruh catatan langganan dari tabel database ke dalam `JTable` secara terstruktur dan rapi.
  - **Update**: Mengubah rincian data langganan yang dipilih pada tabel tanpa mengubah ID uniknya.
  - **Delete**: Menghapus data langganan dari database dengan konfirmasi dialog peringatan terlebih dahulu.
- **Pemuatan Data Otomatis**: Data langsung diambil dari database MySQL saat aplikasi pertama kali dibuka tanpa perlu input dari awal.
- **Pencarian & Penyaringan Real-Time**: Memudahkan pencarian data langganan berdasarkan kata kunci nama layanan/ID, filter kategori (Streaming / Produktivitas), dan filter status (Aktif / Nonaktif).
- **Ringkasan Finansial**: Menampilkan ringkasan total tagihan bulanan, jumlah langganan aktif, serta perbandingan biaya streaming dan produktivitas pada panel dashboard.
- **Simulasi Biaya & Diskon**: Menyediakan dialog khusus untuk mensimulasikan estimasi biaya jangka panjang berbasis perhitungan polimorfik subclass dan interface diskon.
- **Dukungan Dua Antarmuka**: Mode utama aplikasi berjalan menggunakan antarmuka grafis (**GUI**), dan tetap menyediakan opsi mode konsol (**CLI**) melalui parameter argumen `--cli`.

---

## 2. Penjelasan Struktur Package

Proyek ini menggunakan struktur package yang memisahkan tanggung jawab antara antarmuka, logika pengendali, pemrosesan data, dan konfigurasi database:

```
src/main/java/com/pbo/fareru/minpro/pbo/
├── config/                                  <- Konfigurasi koneksi & persistensi
│   ├── HibernateUtil.java                  <- Konfigurasi SessionFactory ORM Hibernate 6
│   ├── Koneksi.java                        <- Pengelola koneksi JDBC DriverManager MySQL
│   ├── TesKoneksi.java                     <- Utilitas pengujian koneksi JDBC
│   └── TesORM.java                         <- Utilitas pengujian pemetaan ORM Hibernate
├── controller/                              <- Pengendali logika dan jembatan data (Controller)
│   ├── InputValidator.java                 <- Validasi input pada mode CLI
│   ├── LanggananController.java            <- Controller alur menu pada mode CLI
│   └── LanggananGUIController.java         <- Controller penghubung GUI Swing ke DAO & Database
├── dao/                                     <- Lapisan akses data (Data Access Object)
│   ├── LanggananDAO.java                   <- Interface kontrak operasi CRUD
│   ├── LanggananDAOImpl.java               <- Implementasi SQL Prepared Statement JDBC
│   └── LanggananMapper.java                <- Pemetaan ResultSet SQL ke objek model
├── model/                                   <- Entitas data & domain bisnis (Model)
│   ├── DapatDidiskon.java                  <- Interface kontrak perhitungan diskon
│   ├── Langganan.java                      <- Abstract class induk model langganan
│   ├── LanggananProduktivitas.java         <- Subclass langganan produktivitas & cloud
│   ├── LanggananStreaming.java             <- Subclass langganan streaming media
│   ├── Layanan.java                        <- Entitas master data layanan digital
│   └── MetodePembayaran.java               <- Entitas master data metode pembayaran
├── personalsubscriptiontracker/             <- Titik masuk aplikasi (Main Entry Point)
│   └── Main.java                           <- Peluncur aplikasi utama (Swing GUI / CLI)
└── view/                                    <- Antarmuka pengguna (View)
    ├── LanggananView.java                  <- Antarmuka berbasis teks/terminal (CLI)
    └── gui/                                <- Antarmuka grafis desktop Java Swing (GUI)
        ├── DetailLanggananDialog.java      <- Dialog rincian detail data langganan
        ├── FormLanggananDialog.java        <- Dialog form tambah dan ubah data (Create/Update)
        ├── MainFrame.java                  <- Window utama aplikasi (JFrame, JTable, Action)
        ├── SimulasiDiskonDialog.java       <- Dialog simulasi estimasi biaya & diskon
        └── TambahLayananDialog.java        <- Dialog modal penambahan master layanan baru
```

Berkas pendukung database:
- `database/personal_subcrition_tracker.sql`: Skrip SQL pembuatan tabel dan data dummy awal.
- `src/main/resources/hibernate.cfg.xml`: Berkas konfigurasi Hibernate ORM dan mapping entitas.

---

## 3. Penjelasan Alur Program

Berikut bagan alur proses berjalannya aplikasi dari mulai dijalankan hingga operasi database:

```mermaid
flowchart TD
    Start([Aplikasi Dimulai / Main.java]) --> SetLAF[Inisialisasi Look and Feel Sistem]
    SetLAF --> CekArg{Cek Argumen CLI?}
    CekArg -- Ada flag --cli --> ModeCLI[Jalankan LanggananController / CLI]
    CekArg -- Default --> ModeGUI[SwingUtilities.invokeLater / Buka MainFrame]
    ModeGUI --> PanggilMuatData[LanggananGUIController.muatData]
    PanggilMuatData --> KoneksiJDBC[Koneksi JDBC ke Database MySQL]
    KoneksiJDBC --> QueryMaster[SELECT Master Layanan & Metode Pembayaran]
    KoneksiJDBC --> QueryLangganan[LanggananDAOImpl.findAll via Prepared Statement]
    QueryLangganan --> IsiTabel[Tampilkan Data ke JTable & Update Panel Ringkasan]
    IsiTabel --> InteraksiUser{Aksi Pengguna pada GUI}
    
    InteraksiUser -- Ketik di Kolom Cari / Filter --> FilterData[Penyaringan Real-Time pada JTable]
    InteraksiUser -- Klik Tambah --> BukaFormTambah[Buka FormLanggananDialog Mode Tambah]
    InteraksiUser -- Klik Edit --> BukaFormEdit[Buka FormLanggananDialog Mode Edit]
    InteraksiUser -- Klik Hapus --> DialogKonfirmasi[Tampilkan Dialog Konfirmasi Hapus]
    InteraksiUser -- Klik Detail --> BukaDetail[Buka DetailLanggananDialog]
    InteraksiUser -- Klik Simulasi Biaya --> BukaSimulasi[Buka SimulasiDiskonDialog]
    InteraksiUser -- Klik Tes ORM --> JalankanORM[Eksekusi Query HQL via HibernateUtil]

    BukaFormTambah --> ValidasiForm[Validasi Input Pengguna]
    BukaFormEdit --> ValidasiForm
    ValidasiForm --> EksekusiDAO[Simpan/Ubah Data via LanggananDAOImpl]
    DialogKonfirmasi -- Konfirmasi Ya --> HapusDAO[Hapus Data via LanggananDAOImpl]
    
    EksekusiDAO --> RefreshTampilan[Muat Ulang JTable & Ringkasan Dashboard]
    HapusDAO --> RefreshTampilan
```

### Tahapan Alur Program:
1. **Titik Masuk (`Main.java`)**: Aplikasi mengatur tema antarmuka sesuai sistem operasi (`UIManager.getSystemLookAndFeelClassName()`), lalu membuka `MainFrame` melalui antrean *Event Dispatch Thread* (`SwingUtilities.invokeLater`).
2. **Pemuatan Awal Data**: Saat `MainFrame` terbuka, controller secara otomatis memanggil `muatData()`. Data dibaca langsung dari database MySQL menggunakan JDBC dan DAO, lalu ditampilkan ke komponen `JTable` dan panel ringkasan biaya.
3. **Operasi Pengguna**:
   - Pencarian atau pengubahan filter kategori/status langsung memperbarui baris pada `JTable` secara seketika (*real-time*).
   - Tombol **Tambah** membuka formulir modal untuk memasukkan data baru dengan nomor ID otomatis (`SUBxx`).
   - Tombol **Edit** membuka data yang sedang dipilih pada baris tabel ke dalam formulir penyuntingan.
   - Tombol **Hapus** memicu dialog konfirmasi untuk mencegah data terhapus secara tidak sengaja.
   - Tombol **Detail** menampilkan informasi lengkap langganan beserta atribut spesifik subclass.
   - Tombol **Simulasi Biaya** menghitung proyeksi biaya bertahap menggunakan method polimorfik.
   - Tombol **Tes ORM** menguji fungsionalitas Hibernate ORM langsung dari antarmuka desktop.

---

## 4. Penjelasan Penerapan Encapsulation dan Inheritance

### A. Encapsulation & Data Hiding
Prinsip enkapsulasi diterapkan pada seluruh kelas model:
- **Hak Akses Privat (`private`)**: Atribut instans dideklarasikan secara `private` agar tidak bisa diubah langsung secara bebas dari luar kelas.
- **Getter dan Setter dengan Validasi**: Pengaksesan dan pengubahan nilai atribut dilakukan lewat method publik yang menyertakan validasi logika bisnis.
  - Pada [`Langganan.java`](file:///c:/Users/Fareru/Documents/NetBeansProjects/Minpro-3-PBO-PersonalSubcriptionTracker/src/main/java/com/pbo/fareru/minpro/pbo/model/Langganan.java), setter harga memastikan nilai tidak bernilai negatif:
    ```java
    public void setHargaBulanan(double hargaBulanan) {
        if (hargaBulanan >= 0) {
            this.hargaBulanan = hargaBulanan;
        } else {
            this.hargaBulanan = 0;
        }
    }
    ```
  - Setter tanggal tagihan memastikan nilai berada pada rentang hari yang sah (1 sampai 31):
    ```java
    public void setTanggalTagihan(int tanggalTagihan) {
        if (tanggalTagihan >= 1 && tanggalTagihan <= 31) {
            this.tanggalTagihan = tanggalTagihan;
        } else {
            this.tanggalTagihan = 1;
        }
    }
    ```

### B. Inheritance (Pewarisan Kelas)
Struktur pewarisan digunakan untuk mengelompokkan karakteristik umum dan membedakan kebutuhan khusus:
- **Superclass**: Kelas abstrak [`Langganan`](file:///c:/Users/Fareru/Documents/NetBeansProjects/Minpro-3-PBO-PersonalSubcriptionTracker/src/main/java/com/pbo/fareru/minpro/pbo/model/Langganan.java) menyimpan atribut umum seperti `idSubscription`, `layanan`, `hargaBulanan`, `metode`, `tanggalTagihan`, dan `status`.
- **Subclass Streaming**: [`LanggananStreaming`](file:///c:/Users/Fareru/Documents/NetBeansProjects/Minpro-3-PBO-PersonalSubcriptionTracker/src/main/java/com/pbo/fareru/minpro/pbo/model/LanggananStreaming.java) mewarisi `Langganan` dan menambahkan atribut khusus `kualitasResolusi` serta `batasLayar`.
- **Subclass Produktivitas**: [`LanggananProduktivitas`](file:///c:/Users/Fareru/Documents/NetBeansProjects/Minpro-3-PBO-PersonalSubcriptionTracker/src/main/java/com/pbo/fareru/minpro/pbo/model/LanggananProduktivitas.java) mewarisi `Langganan` dan menambahkan atribut khusus `kapasitasStorage` serta `lisensiUser`.
- Pemanggilan `super(...)` pada konstruktor subclass digunakan untuk meneruskan inisialisasi atribut dasar ke kelas induk secara konsisten.

---

## 5. Penjelasan Penerapan Polymorphism dan Abstraction

### A. Abstraction (Abstract Class & Abstract Method)
- Kelas [`Langganan`](file:///c:/Users/Fareru/Documents/NetBeansProjects/Minpro-3-PBO-PersonalSubcriptionTracker/src/main/java/com/pbo/fareru/minpro/pbo/model/Langganan.java) berstatus `abstract class` karena merupakan konsep umum yang perlu dispesifikasikan oleh kelas turunannya.
- Mendeklarasikan *abstract method*:
  ```java
  public abstract String getTipeLangganan();
  public abstract double hitungBiayaTahunan();
  ```
- Subclass wajib mengimplementasikan kedua method tersebut sesuai kebutuhan masing-masing domain layanan.

### B. Polymorphism (Overriding & Overloading)
1. **Method Overriding (Dynamic Polymorphism)**:
   - `hitungBiayaTahunan()`:
     - Pada `LanggananStreaming`, dihitung dengan potongan 5% setahun (`hargaBulanan * 12 * 0.95`).
     - Pada `LanggananProduktivitas`, dihitung dengan potongan 10% setahun (`hargaBulanan * 12 * 0.90`).
   - `tampilkanDetail()`:
     - Ditulis ulang pada masing-masing subclass untuk menambahkan rincian resolusi/layar atau kapasitas storage/lisensi.
2. **Method Overloading (Static Polymorphism)**:
   - Kelas `Langganan` memiliki dua varian method:
     - `hitungEstimasiBiaya(int bulan)`: Menghitung total biaya berdasarkan jumlah bulan.
     - `hitungEstimasiBiaya(int bulan, double diskonPersen)`: Menghitung total biaya dengan pengurangan diskon promo tambahan.

### C. Interface `DapatDidiskon` & Keyword `final`
- Interface [`DapatDidiskon.java`](file:///c:/Users/Fareru/Documents/NetBeansProjects/Minpro-3-PBO-PersonalSubcriptionTracker/src/main/java/com/pbo/fareru/minpro/pbo/model/DapatDidiskon.java) menetapkan kontrak method:
  ```java
  public interface DapatDidiskon {
      double hitungDiskon(int bulan);
  }
  ```
  Diimplementasikan oleh kedua subclass untuk menghitung nominal potongan jika periode berlangganan mencapai minimal 12 bulan.
- Keyword `final`: Diterapkan pada kelas utilitas konfigurasi seperti [`Koneksi.java`](file:///c:/Users/Fareru/Documents/NetBeansProjects/Minpro-3-PBO-PersonalSubcriptionTracker/src/main/java/com/pbo/fareru/minpro/pbo/config/Koneksi.java) dan pemetaan [`LanggananMapper.java`](file:///c:/Users/Fareru/Documents/NetBeansProjects/Minpro-3-PBO-PersonalSubcriptionTracker/src/main/java/com/pbo/fareru/minpro/pbo/dao/LanggananMapper.java) untuk mencegah pewarisan kelas yang tidak diperlukan.

---

## 6. Penjelasan Penerapan JDBC

### A. Koneksi Database Relasional MySQL
Koneksi ke database MySQL dikelola secara terpusat oleh kelas [`Koneksi.java`](file:///c:/Users/Fareru/Documents/NetBeansProjects/Minpro-3-PBO-PersonalSubcriptionTracker/src/main/java/com/pbo/fareru/minpro/pbo/config/Koneksi.java) menggunakan `DriverManager`:
```java
jdbc:mysql://localhost:3306/personal_subcrition_tracker?serverTimezone=Asia/Makassar
```

### B. Keamanan Query via Prepared Statement
Seluruh manipulasi data (SELECT, INSERT, UPDATE, DELETE) menggunakan objek `PreparedStatement` dengan parameter tanda tanya (`?`), sehingga aman dari risiko SQL Injection:
- Contoh query INSERT pada [`LanggananDAOImpl.java`](file:///c:/Users/Fareru/Documents/NetBeansProjects/Minpro-3-PBO-PersonalSubcriptionTracker/src/main/java/com/pbo/fareru/minpro/pbo/dao/LanggananDAOImpl.java):
  ```sql
  INSERT INTO langganan (id_subscription, id_layanan, harga_bulanan, id_metode, tanggal_tagihan, status, tipe, detail, jumlah)
  VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
  ```
- Contoh query UPDATE:
  ```sql
  UPDATE langganan SET id_metode=?, harga_bulanan=?, tanggal_tagihan=?, status=?, detail=?, jumlah=? WHERE id_subscription=?
  ```
- Contoh query DELETE:
  ```sql
  DELETE FROM langganan WHERE id_subscription=?
  ```

### C. Arsitektur Data Access Object (DAO)
- **Interface [`LanggananDAO.java`](file:///c:/Users/Fareru/Documents/NetBeansProjects/Minpro-3-PBO-PersonalSubcriptionTracker/src/main/java/com/pbo/fareru/minpro/pbo/dao/LanggananDAO.java)**: Menyediakan deklarasi kontrak operasi data: `findAll()`, `findById()`, `save()`, `update()`, dan `delete()`.
- **Implementasi [`LanggananDAOImpl.java`](file:///c:/Users/Fareru/Documents/NetBeansProjects/Minpro-3-PBO-PersonalSubcriptionTracker/src/main/java/com/pbo/fareru/minpro/pbo/dao/LanggananDAOImpl.java)**: Mengelola eksekusi query JDBC dengan pola *try-with-resources* agar objek `Connection`, `PreparedStatement`, dan `ResultSet` tertutup secara otomatis setelah selesai digunakan.
- **Penerjemah Objek [`LanggananMapper.java`](file:///c:/Users/Fareru/Documents/NetBeansProjects/Minpro-3-PBO-PersonalSubcriptionTracker/src/main/java/com/pbo/fareru/minpro/pbo/dao/LanggananMapper.java)**: Memetakan baris `ResultSet` SQL menjadi instance objek model yang tepat (`LanggananStreaming` atau `LanggananProduktivitas`).

### D. Inisialisasi Data Otomatis via Database (.sql)
Data awal tidak di-*hardcode* di dalam kode program Java, melainkan langsung disiapkan melalui berkas skrip SQL [`database/personal_subcrition_tracker.sql`](file:///c:/Users/Fareru/Documents/NetBeansProjects/Minpro-3-PBO-PersonalSubcriptionTracker/database/personal_subcrition_tracker.sql). Begitu aplikasi GUI dibuka, data langsung dibaca dari MySQL dan tampil pada antarmuka.

---

## 7. Penjelasan Implementasi GUI Menggunakan Java Swing

### A. Komponen-Komponen Utama Swing
1. **`JFrame` ([`MainFrame.java`](file:///c:/Users/Fareru/Documents/NetBeansProjects/Minpro-3-PBO-PersonalSubcriptionTracker/src/main/java/com/pbo/fareru/minpro/pbo/view/gui/MainFrame.java))**: Jendela utama aplikasi dengan ukuran proporsional (1050x680 piksel), penempatan di tengah layar, dan konfigurasi operasi penutupan `EXIT_ON_CLOSE`.
2. **`JPanel`**: Mengorganisasi area antarmuka menjadi beberapa bagian: panel judul atas, panel ringkasan biaya, panel bilah pencarian/filter, panel kontainer tabel, dan panel tombol aksi bawah.
3. **`JTable`**: Menampilkan kumpulan data langganan dalam format baris dan kolom yang terstruktur.
4. **`JScrollPane`**: Memberikan kemampuan gulir vertikal dan horizontal pada komponen tabel dan dialog.
5. **`JTextField`**: Input teks untuk kata kunci pencarian, pengisian harga bulanan, dan nama layanan.
6. **`JComboBox`**: Pilihan dropdown untuk data layanan, metode pembayaran, filter kategori, dan status langganan.
7. **`JSpinner`**: Input angka dengan batasan nilai tertentu untuk tanggal tagihan (1–31), durasi bulan, dan batas kuota pengguna.
8. **`JButton`**: Tombol pemicu aksi untuk operasi CRUD (*Tambah*, *Edit*, *Hapus*, *Detail*, *Simulasi Biaya*, *Refresh*, dan *Tes ORM*).
9. **`JDialog`**: Dialog modal khusus untuk form input (`FormLanggananDialog`), rincian data (`DetailLanggananDialog`), simulasi polimorfik (`SimulasiDiskonDialog`), dan penambahan master layanan (`TambahLayananDialog`).

### B. Tata Letak Antarmuka (Layout Management)
- **`BorderLayout`**: Digunakan sebagai layout utama pada `MainFrame` dan dialog untuk memisahkan area atas (NORTH), tengah (CENTER), dan bawah (SOUTH).
- **`GridBagLayout`**: Diterapkan pada panel form dialog untuk memastikan letak label dan field input tersusun rapi secara dua kolom.
- **`GridLayout`**: Digunakan pada panel ringkasan biaya agar keempat kartu statistik memiliki ukuran pembagian yang sama rata.
- **`CardLayout`**: Diterapkan pada panel atribut dinamis formulir agar input field berganti secara otomatis saat pengguna memilih tipe *Streaming* atau *Produktivitas*.
- **`FlowLayout`**: Mengatur tata letak tombol aksi agar berjejer rapi di sisi kiri dan kanan.

### C. Penanganan Aksi Pengguna (Event Handling)
- **`ActionListener`**: Merespons penekanan tombol (*button click*) dan pilihan dropdown (*combobox change*).
- **`DocumentListener`**: Mendeteksi perubahan teks pada kolom input pencarian secara seketika (*real-time search*) tanpa perlu menekan tombol tambahan.
- **`ListSelectionListener`**: Mendeteksi pemilihan baris pada `JTable` untuk mengaktifkan atau menonaktifkan tombol *Edit*, *Hapus*, dan *Detail*.
- **`MouseAdapter`**: Menangkap aksi klik ganda (*double click*) pada baris `JTable` untuk langsung membuka jendela detail langganan.

---

## 8. Penjelasan Hubungan Antara GUI, Controller, DAO, dan Database

Aplikasi menerapkan pola arsitektur **Model-View-Controller (MVC)** yang terintegrasi dengan lapisan akses data (DAO):

```
+-------------------------------------------------------------------+
|                        VIEW (Java Swing)                          |
|   MainFrame, FormLanggananDialog, DetailLanggananDialog, dll.     |
+-------------------------------------------------------------------+
                               │
               1. Aksi Pengguna (Klik, Ketik, Form Submit)
                               ▼
+-------------------------------------------------------------------+
|                           CONTROLLER                              |
|                    LanggananGUIController                         |
+-------------------------------------------------------------------+
                               │
               2. Pemanggilan Logika Bisnis & CRUD
                               ▼
+-------------------------------------------------------------------+
|                           DAO LAYER                               |
|                 LanggananDAOImpl & HibernateUtil                  |
+-------------------------------------------------------------------+
                               │
               3. Eksekusi SQL Prepared Statement / Query HQL
                               ▼
+-------------------------------------------------------------------+
|                        DATABASE (MySQL)                           |
|                  personal_subcrition_tracker                      |
+-------------------------------------------------------------------+
                               │
               4. Pengembalian Data ResultSet
                               ▼
+-------------------------------------------------------------------+
|                          MODEL (POJO)                             |
|          Langganan, Layanan, MetodePembayaran, dll.               |
+-------------------------------------------------------------------+
                               │
               5. Update Tampilan JTable & Panel Dashboard
                               ▼
+-------------------------------------------------------------------+
|                    VIEW (Swing GUI Terupdate)                     |
+-------------------------------------------------------------------+
```

1. **Pengguna berinteraksi dengan View**: Pengguna melakukan tindakan seperti menambah data, memilih baris, atau mencari data pada antarmuka GUI.
2. **View memanggil Controller**: Kelas antarmuka seperti `MainFrame` dan `FormLanggananDialog` meneruskan input ke [`LanggananGUIController.java`](file:///c:/Users/Fareru/Documents/NetBeansProjects/Minpro-3-PBO-PersonalSubcriptionTracker/src/main/java/com/pbo/fareru/minpro/pbo/controller/LanggananGUIController.java).
3. **Controller berkoordinasi dengan DAO**: Controller mengelola validasi, membuat ID baru, dan memanggil method pada [`LanggananDAOImpl.java`](file:///c:/Users/Fareru/Documents/NetBeansProjects/Minpro-3-PBO-PersonalSubcriptionTracker/src/main/java/com/pbo/fareru/minpro/pbo/dao/LanggananDAOImpl.java).
4. **DAO menjalankan query ke Database**: DAO mengeksekusi perintah SQL melalui koneksi JDBC ke server MySQL.
5. **Data dikembalikan ke Model dan View**: Hasil query dibentuk menjadi objek model oleh `LanggananMapper`, disimpan di dalam memori controller, lalu model tabel (`DefaultTableModel`) diperbarui untuk memperlihatkan data terbaru kepada pengguna.

---

## 9. Penjelasan Letak Penerapan Nilai Tambah

Berikut rincian letak dan penerapan fitur **Nilai Tambah** pada aplikasi:

### A. Desain GUI yang Rapi, Sederhana, dan Konsisten
- **Letak Kode**: [`view/gui/MainFrame.java`](file:///c:/Users/Fareru/Documents/NetBeansProjects/Minpro-3-PBO-PersonalSubcriptionTracker/src/main/java/com/pbo/fareru/minpro/pbo/view/gui/MainFrame.java#L45-L105) & seluruh dialog di package [`view/gui/`](file:///c:/Users/Fareru/Documents/NetBeansProjects/Minpro-3-PBO-PersonalSubcriptionTracker/src/main/java/com/pbo/fareru/minpro/pbo/view/gui/).
- **Penerapan**:
  - Menggunakan komponen standar Java Swing murni dengan *Look and Feel* bawaan sistem operasi.
  - Tata letak terstruktur rapi menggunakan *BorderLayout*, *GridBagLayout*, *GridLayout*, dan border berlabel (*TitledBorder*) sehingga mudah dipahami.
  - Dilengkapi panel ringkasan informasi pengeluaran bulanan di bagian atas jendela utama.

### B. Penggunaan JTable Terstruktur & Pemformatan Data
- **Letak Kode**: [`view/gui/MainFrame.java`](file:///c:/Users/Fareru/Documents/NetBeansProjects/Minpro-3-PBO-PersonalSubcriptionTracker/src/main/java/com/pbo/fareru/minpro/pbo/view/gui/MainFrame.java#L110-L165).
- **Penerapan**:
  - `JTable` dikonfigurasi menggunakan `DefaultTableModel` berstatus *read-only* agar data tidak tersunting secara keliru tanpa melalui validasi form.
  - Memiliki 10 kolom terstruktur: `No`, `ID`, `Layanan`, `Kategori / Tipe`, `Harga / Bulan`, `Tgl Tagihan`, `Status`, `Metode Pembayaran`, `Detail Spesifik`, dan `Kapasitas / Lisensi`.
  - Format angka pada kolom harga bulanan diformat rapi ke dalam bentuk mata uang Rupiah (`Rp xx.xxx,00`) rata kanan.
  - Kolom nomor urut, ID, tanggal, dan status diposisikan rata tengah (*center-aligned*).

### C. Fitur Pencarian dan Penyaringan Data (Search & Filter)
- **Letak Kode**: [`view/gui/MainFrame.java`](file:///c:/Users/Fareru/Documents/NetBeansProjects/Minpro-3-PBO-PersonalSubcriptionTracker/src/main/java/com/pbo/fareru/minpro/pbo/view/gui/MainFrame.java#L70-L100) & [`controller/LanggananGUIController.java`](file:///c:/Users/Fareru/Documents/NetBeansProjects/Minpro-3-PBO-PersonalSubcriptionTracker/src/main/java/com/pbo/fareru/minpro/pbo/controller/LanggananGUIController.java#L140-L180).
- **Penerapan**:
  - **Pencarian Real-Time**: Kolom input `txtPencarian` menggunakan `DocumentListener` sehingga tabel langsung terfilter seketika saat pengguna mengetik kata kunci.
  - **Filter Kategori**: Dropdown untuk menyaring kategori *Semua*, *Streaming*, atau *Produktivitas*.
  - **Filter Status**: Dropdown untuk menyaring status *Semua*, *Aktif*, atau *Nonaktif*.
  - **Tombol Reset**: Mengembalikan seluruh filter ke kondisi semula.
  - **Label Indikator Jumlah Data**: Menampilkan informasi jumlah data yang sedang tampil (contoh: *"Menampilkan 8 dari 8 data"*).

### D. Integrasi Framework ORM (Hibernate 6)
- **Letak Kode**: [`config/HibernateUtil.java`](file:///c:/Users/Fareru/Documents/NetBeansProjects/Minpro-3-PBO-PersonalSubcriptionTracker/src/main/java/com/pbo/fareru/minpro/pbo/config/HibernateUtil.java), [`model/Layanan.java`](file:///c:/Users/Fareru/Documents/NetBeansProjects/Minpro-3-PBO-PersonalSubcriptionTracker/src/main/java/com/pbo/fareru/minpro/pbo/model/Layanan.java), [`controller/LanggananGUIController.java`](file:///c:/Users/Fareru/Documents/NetBeansProjects/Minpro-3-PBO-PersonalSubcriptionTracker/src/main/java/com/pbo/fareru/minpro/pbo/controller/LanggananGUIController.java#L215-L225).
- **Penerapan**:
  - Konfigurasi Hibernate 6 dan pemetaan JPA (`@Entity`, `@Table`, `@Id`, `@Column`) dari Mini Project 4 tetap dipertahankan.
  - Disediakan tombol **`Tes ORM`** pada antarmuka utama yang menjalankan query HQL untuk menguji koneksi ORM secara langsung saat aplikasi desktop berjalan.

### E. Dialog Simulasi Estimasi Biaya & Diskon Polimorfik
- **Letak Kode**: [`view/gui/SimulasiDiskonDialog.java`](file:///c:/Users/Fareru/Documents/NetBeansProjects/Minpro-3-PBO-PersonalSubcriptionTracker/src/main/java/com/pbo/fareru/minpro/pbo/view/gui/SimulasiDiskonDialog.java).
- **Penerapan**:
  - Fitur simulasi dari Minpro sebelumnya diintegrasikan ke dalam dialog visual GUI.
  - Memanfaatkan polimorfisme overriding (`hitungBiayaTahunan`) dan interface `DapatDidiskon` untuk menghitung proyeksi biaya jangka panjang sesuai durasi bulan dan persentase promo tambahan yang dimasukkan pengguna.

---

## 10. Panduan Menjalankan Program

### Prasyarat:
- **Java Development Kit (JDK)**: Versi 17 atau yang lebih baru.
- **Apache Maven**: Versi 3.8+ (atau menggunakan Maven yang terpasang pada NetBeans).
- **MySQL Database Server**: Port `3306` (misal via XAMPP).

### Langkah Menjalankan:
1. Pastikan service MySQL sedang berjalan.
2. Impor berkas database:
   ```bash
   mysql -u root -p < database/personal_subcrition_tracker.sql
   ```
3. Kompilasi program via Maven:
   ```bash
   mvn clean compile
   ```
4. Jalankan aplikasi mode GUI Desktop (Default):
   ```bash
   mvn exec:java
   ```
5. *(Opsional)* Menjalankan mode CLI Terminal:
   ```bash
   mvn exec:java -Dexec.args="--cli"
   ```

---

```
+-------------------------------------------------------------+
| Mini Project 5 PBO - Universitas Mulawarman                 |
| Dikembangkan oleh Muhammad Fahriel (NIM: 2509116050)        |
+-------------------------------------------------------------+
```
