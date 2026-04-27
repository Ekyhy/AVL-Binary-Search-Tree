import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import javax.swing.*;

public class AVLVisualizer extends JFrame {
    private AVLTree tree = new AVLTree();
    private Node root = null; 
    
    private JTextField idField = new JTextField(5);
    private JTextField nameField = new JTextField(10);
    private TreePanel paintArea = new TreePanel();

    public AVLVisualizer() {
        setTitle("Visualisator AVL Tree - Ekyhy");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Panel Input (Atas)
        JPanel inputPanel = new JPanel();
        inputPanel.add(new JLabel("ID:"));
        inputPanel.add(idField);
        inputPanel.add(new JLabel("Nama:"));
        inputPanel.add(nameField);

        // 1. Definisikan tombol
        JButton addBtn = new JButton("Tambah");
        JButton delBtn = new JButton("Hapus");
        JButton clearBtn = new JButton("Reset");
        JButton loadBtn = new JButton("Muat File CSV"); 
        JButton preBtn = new JButton("Pre-Order");
        JButton inBtn = new JButton("In-Order");
        JButton postBtn = new JButton("Post-Order");

        // 2. Tambahkan tombol ke panel
        inputPanel.add(addBtn);
        inputPanel.add(delBtn);
        inputPanel.add(clearBtn);
        inputPanel.add(loadBtn); 
        inputPanel.add(new JLabel(" | Traversal:"));
        inputPanel.add(preBtn);
        inputPanel.add(inBtn);
        inputPanel.add(postBtn);

        // --- LOGIKA TOMBOL TRAVERSAL ---
        preBtn.addActionListener(e -> {
            if (root == null) {
                JOptionPane.showMessageDialog(this, "Tree masih kosong!");
                return;
            }
            StringBuilder sb = new StringBuilder();
            tree.preorder(root, sb);
            tampilkanDialogScroll("Hasil Pre-Order", sb.toString());
        });

        inBtn.addActionListener(e -> {
            if (root == null) {
                JOptionPane.showMessageDialog(this, "Tree masih kosong!");
                return;
            }
            StringBuilder sb = new StringBuilder();
            tree.inorder(root, sb);
            tampilkanDialogScroll("Hasil In-Order", sb.toString());
        });

        postBtn.addActionListener(e -> {
            if (root == null) {
                JOptionPane.showMessageDialog(this, "Tree masih kosong!");
                return;
            }
            StringBuilder sb = new StringBuilder();
            tree.postorder(root, sb);
            tampilkanDialogScroll("Hasil Post-Order", sb.toString());
        });

        // --- LOGIKA TOMBOL MUAT FILE CSV ---
        loadBtn.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Pilih File CSV Data Barang");
            
            int result = fileChooser.showOpenDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                try (BufferedReader br = new BufferedReader(new FileReader(selectedFile))) {
                    String line;
                    boolean isHeader = true; 
                    int count = 0; 

                    while ((line = br.readLine()) != null) {
                        if (isHeader) {
                            isHeader = false;
                            continue;
                        }

                        String[] parts = line.split(",");
                        if (parts.length >= 2) {
                            try {
                                int id = Integer.parseInt(parts[0].trim());
                                String nama = parts[1].trim();
                                
                                root = tree.insert(root, id, nama);
                                count++;
                            } catch (NumberFormatException ex) {
                                System.out.println("Melewati baris tidak valid: " + line);
                            }
                        }
                    }
                    
                    paintArea.setRoot(root);
                    JOptionPane.showMessageDialog(this, "Berhasil memuat " + count + " data ke dalam AVL Tree!");
                    
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Terjadi kesalahan saat membaca file:\n" + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // --- LOGIKA TOMBOL TAMBAH, HAPUS, RESET ---
        addBtn.addActionListener(e -> {
            try {
                int id = Integer.parseInt(idField.getText());
                String nama = nameField.getText();
                root = tree.insert(root, id, nama);
                paintArea.setRoot(root);
                idField.setText("");
                nameField.setText("");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Input ID harus angka!");
            }
        });

        delBtn.addActionListener(e -> {
            try {
                int id = Integer.parseInt(idField.getText());
                root = tree.delete(root, id);
                paintArea.setRoot(root);
                idField.setText("");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Pilih ID yang ingin dihapus");
            }
        });

        clearBtn.addActionListener(e -> {
            root = null;
            paintArea.setRoot(null);
        });

        // Layout Utama
        add(inputPanel, BorderLayout.NORTH);
        // Membungkus area gambar dengan JScrollPane agar bisa digeser jika data banyak
        add(new JScrollPane(paintArea), BorderLayout.CENTER);
    }

    // --- METHOD BANTUAN UNTUK POP-UP SCROLL TRAVERSAL ---
    private void tampilkanDialogScroll(String judul, String isiTeks) {
        JTextArea textArea = new JTextArea(isiTeks);
        textArea.setLineWrap(true);       // Teks akan turun ke baris baru jika mentok ke kanan
        textArea.setWrapStyleWord(true);  // Memotong teks berdasarkan kata, bukan huruf
        textArea.setEditable(false);      // Agar teks tidak bisa diedit oleh pengguna
        textArea.setCaretPosition(0);     // Memastikan scroll bar selalu mulai dari paling atas

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(400, 300)); // Ukuran jendela pop-up: Lebar 400, Tinggi 300

        JOptionPane.showMessageDialog(this, scrollPane, judul, JOptionPane.INFORMATION_MESSAGE);
    }

    // --- INNER CLASS UNTUK AREA GAMBAR ---
    class TreePanel extends JPanel {
        private Node root;
        private int radius = 20;
        private int vGap = 50;

        public void setRoot(Node root) {
            this.root = root;
            // Area diperbesar agar visualisasi banyak data dari file CSV bisa muat
            setPreferredSize(new Dimension(4000, 2000));
            revalidate();
            repaint(); 
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (root != null) {
                drawTree((Graphics2D) g, root, getWidth() / 2, 50, getWidth() / 4);
            }
        }

        private void drawTree(Graphics2D g, Node node, int x, int y, int hGap) {
            if (node.left != null) {
                g.drawLine(x, y, x - hGap, y + vGap);
                drawTree(g, node.left, x - hGap, y + vGap, hGap / 2);
            }

            if (node.right != null) {
                g.drawLine(x, y, x + hGap, y + vGap);
                drawTree(g, node.right, x + hGap, y + vGap, hGap / 2);
            }

            g.setColor(new Color(100, 149, 237)); 
            g.fillOval(x - radius, y - radius, 2 * radius, 2 * radius);
            g.setColor(Color.BLACK);
            g.drawOval(x - radius, y - radius, 2 * radius, 2 * radius);

            String text = String.valueOf(node.id);
            FontMetrics fm = g.getFontMetrics();
            int tx = x - fm.stringWidth(text) / 2;
            int ty = y + fm.getAscent() / 2 - 2;
            g.drawString(text, tx, ty);
            
            g.setFont(new Font("Arial", Font.PLAIN, 10));
            g.drawString(node.nama, x - radius, y + radius + 12);
            g.setFont(new Font("Arial", Font.BOLD, 12));
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new AVLVisualizer().setVisible(true);
        });
    }
}