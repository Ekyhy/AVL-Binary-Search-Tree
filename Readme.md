🌳 AVL Tree Project: Self-Balancing Binary Search Tree
======================================================

Proyek ini merupakan implementasi struktur data **AVL Tree** yang dirancang untuk menjaga keseimbangan pohon secara otomatis. AVL Tree memastikan bahwa perbedaan tinggi antara sub-pohon kiri dan kanan tidak lebih dari 1, sehingga menjamin kompleksitas waktu operasi dasar tetap dalam rentang **$O(\\log n)$**.

Proyek ini tersedia dalam dua varian implementasi:

1.  **Python (Web-Based)**: Menggunakan Flask dan visualisasi Mermaid.js.
    
2.  **Java (Desktop-GUI)**: Menggunakan Java Swing (JFrame) untuk visualisasi grafis interaktif.
    

🚀 Fitur Utama
--------------

*   **Self-Balancing**: Implementasi otomatis rotasi LL, RR, LR, dan RL.
    
*   **CRUD Operations**: Tambah data, Cari data, dan Hapus data secara dinamis.
    
*   **Data Traversal**: Mendukung penelusuran _In-order_, _Pre-order_, dan _Post-order_.
    
*   **Visualisasi Real-Time**:
    
    *   Versi Python: Render grafik via Mermaid.js di browser.
        
    *   Versi Java: Render koordinat dinamis via Graphics2D di JFrame.
        
*   **Input Fleksibel**: Mendukung pembacaan data massal dari file **Excel (.xlsx)** maupun input manual.
    

🛠️ Tech Stack
--------------

### Varian Python

*   **Language**: Python 3.10+
    
*   **Framework**: Flask (Backend)
    
*   **Libraries**: Pandas, Openpyxl (Excel Processing)
    
*   **Frontend**: Mermaid.js, Bootstrap 5
    

### Varian Java

*   **Language**: Java 17+ (JDK)
    
*   **Build Tool**: Maven (Dependency Management)
    
*   **Libraries**: Apache POI (Excel Processing), Swing/AWT (Visualisasi)
    
*   **IDE**: VS Code (Extension Pack for Java)
    


📖 Logika Algoritma AVL
-----------------------

Setiap simpul (_node_) menyimpan nilai **Height** (Tinggi). Faktor Keseimbangan (_Balance Factor_) dihitung dengan rumus:

$$BF = \\text{height}(left) - \\text{height}(right)$$

Pohon akan melakukan rotasi jika $|BF| > 1$:

1.  **Left-Left (LL)**: Dilakukan rotateRight pada node yang tidak seimbang.
    
2.  **Right-Right (RR)**: Dilakukan rotateLeft pada node yang tidak seimbang.
    
3.  **Left-Right (LR)**: Dilakukan rotateLeft pada anak kiri, kemudian rotateRight pada node utama.
    
4.  **Right-Left (RL)**: Dilakukan rotateRight pada anak kanan, kemudian rotateLeft pada node utama.
    

⚙️ Cara Menjalankan
-------------------

### Menjalankan Versi Python

1.  Bashpip install flask pandas openpyxl
    
2.  Bashpython app.py
    
3.  Akses di browser: http://127.0.0.1:5000
    

### Menjalankan Versi Java (VS Code)

1.  Buka folder proyek Java di VS Code.
    
2.  Pastikan **Extension Pack for Java** aktif.
    
3.  Jika menggunakan Maven, biarkan VS Code mengunduh library Apache POI secara otomatis.
    
4.  Buka file AVLVisualizer.java atau App.java.
    
5.  Klik tombol **Run** (di atas metode main).
    

📊 Visualisasi
--------------

> **Catatan Implementasi:**
> 
> *   Pada **Python**, visualisasi bersifat statis/semi-dinamis dengan merefresh halaman untuk mengirimkan string Mermaid baru ke browser.
>     
> *   Pada **Java**, visualisasi bersifat dinamis murni. Setiap kali data ditambah/dihapus, fungsi repaint() dipanggil untuk menggambar ulang pohon berdasarkan koordinat $x$ dan $y$ yang dihitung secara rekursif.
>     

👀 Youtube
