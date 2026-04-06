from flask import Flask, render_template, request, redirect, url_for
import pandas as pd
import re

app = Flask(__name__)

# Struktur data BST menggunakan AVL
# Membuat class node
class Node:
    def __init__(self, id, nama):
        self.id = id
        self.nama = nama
        self.left = None
        self.right = None
        self.height = 1

# Membuat class AVLTree
class AVLTree:

    def get_height(self, node):
        return node.height if node else 0
    
    def get_balance(self, node):
        return self.get_height(node.left) - self.get_height(node.right) if node else 0
    
    def rotate_right(self, y):
        x = y.left
        T2 = x.right
        x.right = y
        y.left = T2
        y.height = 1 + max(self.get_height(y.left), self.get_height(y.right))
        x.height = 1 + max(self.get_height(x.left), self.get_height(x.right))
        return x
    
    def rotate_left(self, x):
        y  = x.right
        T2 = y.left
        y.left = x
        x.right = T2
        x.height = 1 + max(self.get_height(x.left), self.get_height(x.right))
        y.height = 1 + max(self.get_height(y.left), self.get_height(y.right))
        return y
    
    # Menambahkan fitur tambah data

    def insert(self, root, id, nama):
        if not root:
            return Node(id, nama)
        if id < root.id:
            root.left = self.insert(root.left, id, nama)  
        elif id > root.id:
            root.right = self.insert(root.right, id, nama)    
        else:
            return root
        
        root.height = 1 + max(self.get_height(root.left), self.get_height(root.right))
        balance = self.get_balance(root)

    # Kasus balancing data
        if balance > 1 and id < root.left.id: return self.rotate_right(root)
        if balance < -1 and id > root.right.id: return self.rotate_left(root)
        if balance > 1 and id > root.left.id:
            root.left = self.rotate_left(root.left)
            return self.rotate_right(root)
        if balance < -1 and id < root.right.id:
            root.right = self.rotate_right(root.right)
            return self.rotate_left(root)
        return root
    

    def get_min_value_node(self, node):
        if node is None or node.left is None: return node
        return self.get_min_value_node(node.left)
    
    def delete(self, root, id):
        if not root: return root
        if id < root.id:
            root.left = self.delete(root.left, id)
        elif id > root.id:
            root.right = self.delete(root.right, id)
        else:
            if root.left is None:
                return root.right
            elif root.right is None:
                return root.left
            temp = self.get_min_value_node(root.right)
            root.id = temp.id
            root.nama =  temp.nama
            root.right = self.delete(root.right, temp.id)
        
        if not root: return root
        root.height = 1 + max(self.get_height(root.left), self.get_height(root.right))
        balance = self.get_balance(root)
        if balance > 1 and self.get_balance(root.left) >= 0: return self.rotate_right(root)
        if balance < -1 and self.get_balance(root.right) <= 0: return self.rotate_left(root)
        if balance > 1 and self.get_balance(root.left) < 0:
            root.left = self.rotate_left(root.left)
            return self.rotate_right(root)
        if balance < -1 and self.get_balance(root.right) > 0:
            root.right = self.rotate_right(root.right)
            return self.rotate_left(root)
        return root
    
    def inorder(self, root, res):
        if root : 
            self.inorder(root.left, res)
            res.append({'id' : root.id, 'nama': root.nama})
            self.inorder(root.right,res)

    def preorder(self, root, res):
        if root:
            res.append(str(root.id))
            self.preorder(root.left, res)
            self.preorder(root.right, res)

    def postorder(self, root, res):
        if root:
            self.postorder(root.left, res)
            self.postorder(root.right, res)
            res.append(str(root.id))

    def generate_mermaid(self, root, lines=None):
        if lines is None: lines = []
        if root:

            c_nama = re.sub(r'[^a-zA-Z0-9\s]', '', str(root.nama))
            curr = f'n{root.id}["ID: {root.id} | {c_nama}"]'
            if root.left:
                l_nama = re.sub(r'[^a-zA-Z0-9\s]', '', str(root.left.nama))
                lines.append(f'{curr} --> n{root.left.id}["ID: {root.left.id} | {l_nama}"]')
                self.generate_mermaid(root.left, lines)
            if root.right:
                r_nama = re.sub(r'[^a-zA-Z0-9\s]', '', str(root.right.nama))
                lines.append(f'{curr} --> n{root.right.id}["ID: {root.right.id} | {r_nama}"]')
                self.generate_mermaid(root.right, lines)
        return "\n".join(lines)
    

tree =  AVLTree()
root = None

try:
    df = pd.read_excel('data100.xlsx')
    for _, row in df.iterrows():
        root = tree.insert(root, int(row['id']), str(row['nama']))
except Exception as e:
    print(f"Gagal memuat Excel: {e}")

@app.route('/')
def index():
    in_order, pre_order, post_order = [], [], []
    tree.inorder(root, in_order)
    tree.preorder(root, pre_order)
    tree.postorder(root, post_order)

    graph = " graph TD\n " + tree.generate_mermaid(root)
    return render_template( 'index.html',
                            data = in_order,
                            pre=", ".join(pre_order),
                            post=", ".join(post_order),
                            mermaid_graph =  graph,
                            height= tree.get_height(root))

@app.route('/add', methods=['POST'])

def add():
    global root
    id_in = int(request.form.get('id'))
    nama_in = request.form.get('nama')
    root = tree.insert(root, id_in, nama_in)
    return redirect('/')

@app.route('/delete/<int:id>')
def delete(id):
    global root
    root =  tree.delete(root, id)
    return redirect('/')

if __name__ == '__main__':
    app.run(debug=True)