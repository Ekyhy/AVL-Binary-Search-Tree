import java.awt.*;
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

        JButton addBtn = new JButton("Tambah");
        JButton delBtn = new JButton("Hapus");
        JButton clearBtn = new JButton("Reset");

        inputPanel.add(addBtn);
        inputPanel.add(delBtn);
        inputPanel.add(clearBtn);

        // Logika Tombol
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
        add(new JScrollPane(paintArea), BorderLayout.CENTER);
    }

    // --- INNER CLASS UNTUK AREA GAMBAR ---
    class TreePanel extends JPanel {
        private Node root;
        private int radius = 20;
        private int vGap = 50;

        public void setRoot(Node root) {
            this.root = root;
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
            // Gambar Garis ke Anak Kiri
            if (node.left != null) {
                g.drawLine(x, y, x - hGap, y + vGap);
                drawTree(g, node.left, x - hGap, y + vGap, hGap / 2);
            }

            // Gambar Garis ke Anak Kanan
            if (node.right != null) {
                g.drawLine(x, y, x + hGap, y + vGap);
                drawTree(g, node.right, x + hGap, y + vGap, hGap / 2);
            }

            // Gambar Bulatan Node
            g.setColor(new Color(100, 149, 237)); // Biru muda
            g.fillOval(x - radius, y - radius, 2 * radius, 2 * radius);
            g.setColor(Color.BLACK);
            g.drawOval(x - radius, y - radius, 2 * radius, 2 * radius);

            // Gambar Teks ID
            String text = String.valueOf(node.id);
            FontMetrics fm = g.getFontMetrics();
            int tx = x - fm.stringWidth(text) / 2;
            int ty = y + fm.getAscent() / 2 - 2;
            g.drawString(text, tx, ty);
            
            // Gambar Nama di bawah node (opsional)
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
