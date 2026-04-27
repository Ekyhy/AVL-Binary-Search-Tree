public class AVLTree {

    public int getHeight(Node n) {
        return (n == null) ? 0 : n.height;
    }

    public int getBalance(Node n) {
        return (n == null) ? 0 : getHeight(n.left) - getHeight(n.right);
    }

    private Node rotateRight(Node y) {
        Node x = y.left;
        Node T2 = x.right;
        x.right = y;
        y.left = T2;
        y.height = Math.max(getHeight(y.left), getHeight(y.right)) + 1;
        x.height = Math.max(getHeight(x.left), getHeight(x.right)) + 1;
        return x;
    }

    private Node rotateLeft(Node x) {
        Node y = x.right;
        Node T2 = y.left;
        y.left = x;
        x.right = T2;
        x.height = Math.max(getHeight(x.left), getHeight(x.right)) + 1;
        y.height = Math.max(getHeight(y.left), getHeight(y.right)) + 1;
        return y;
    }

    public Node insert(Node node, int id, String nama) {
        if (node == null) return new Node(id, nama);

        if (id < node.id)
            node.left = insert(node.left, id, nama);
        else if (id > node.id)
            node.right = insert(node.right, id, nama);
        else
            return node;

        node.height = 1 + Math.max(getHeight(node.left), getHeight(node.right));
        int balance = getBalance(node);

        if (balance > 1 && id < node.left.id) return rotateRight(node);
        if (balance < -1 && id > node.right.id) return rotateLeft(node);
        if (balance > 1 && id > node.left.id) {
            node.left = rotateLeft(node.left);
            return rotateRight(node);
        }
        if (balance < -1 && id < node.right.id) {
            node.right = rotateRight(node.right);
            return rotateLeft(node);
        }
        return node;
    }

    public Node delete(Node root, int id) {
        if (root == null) return root;

        if (id < root.id)
            root.left = delete(root.left, id);
        else if (id > root.id)
            root.right = delete(root.right, id);
        else {
            if ((root.left == null) || (root.right == null)) {
                Node temp = (root.left == null) ? root.right : root.left;
                root = temp;
            } else {
                Node temp = getMinValueNode(root.right);
                root.id = temp.id;
                root.nama = temp.nama;
                root.right = delete(root.right, temp.id);
            }
        }

        if (root == null) return root;

        root.height = 1 + Math.max(getHeight(root.left), getHeight(root.right));
        int balance = getBalance(root);

        if (balance > 1 && getBalance(root.left) >= 0) return rotateRight(root);
        if (balance > 1 && getBalance(root.left) < 0) {
            root.left = rotateLeft(root.left);
            return rotateRight(root);
        }
        if (balance < -1 && getBalance(root.right) <= 0) return rotateLeft(root);
        if (balance < -1 && getBalance(root.right) > 0) {
            root.right = rotateRight(root.right);
            return rotateLeft(root);
        }
        return root;
    }

    private Node getMinValueNode(Node node) {
        Node current = node;
        while (current.left != null) current = current.left;
        return current;
    }

    // --- FUNGSI TRAVERSAL ---

    public void preorder(Node root, StringBuilder res) {
        if (root != null) {
            res.append(root.id).append(" (").append(root.nama).append("), ");
            preorder(root.left, res);
            preorder(root.right, res);
        }
    }

    public void inorder(Node root, StringBuilder res) {
        if (root != null) {
            inorder(root.left, res);
            res.append(root.id).append(" (").append(root.nama).append("), ");
            inorder(root.right, res);
        }
    }

    public void postorder(Node root, StringBuilder res) {
        if (root != null) {
            postorder(root.left, res);
            postorder(root.right, res);
            res.append(root.id).append(" (").append(root.nama).append("), ");
        }
    }
}