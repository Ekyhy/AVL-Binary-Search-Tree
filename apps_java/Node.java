public class Node {
    int id;
    String nama;
    Node left, right;
    int height;

    public Node(int id, String nama) {
        this.id = id;
        this.nama = nama;
        this.height = 1;
    }
}