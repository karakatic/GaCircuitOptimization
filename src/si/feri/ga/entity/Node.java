package si.feri.ga.entity;

import java.util.ArrayList;

/**
 * @author karakatic
 */
public class Node {
    public String result;
    public Node   parent;
    public Node left  = null;
    public Node right = null;

    public static void main(String[] args) {
        Chromosome.INPUTS = new ArrayList<>();
        Chromosome.INPUTS.add("A");
        Chromosome.INPUTS.add("B");

        Node n  = new Node("AND");
        Node n1 = new Node("OR");
        Node n2 = new Node("NOT");

        n.setLeft(n1);
        n.setRight(new Node("A"));

        n1.setLeft(n2);
        n1.setRight(new Node("B"));

        n2.setLeft(new Node("A"));

        System.out.println(n);
        System.out.println(n.size());
        System.out.println(n.elementCount());
    }

    public Node(String result) {
        this.result = result;
    }

    public Node(Node n) {
        this.result = n.result;
        if (n.left != null)
            this.setLeft(new Node(n.left));
        if (n.right != null)
            this.setRight(new Node(n.right));
    }

    public void setLeft(Node n) {
        left = n;
        left.parent = this;
    }

    public void setRight(Node n) {
        right = n;
        right.parent = this;
    }

    public int depth() {
        if (parent == null)
            return 0;

        return parent.depth() + 1;
    }

    public int maxDepth() {
        return maxDepth(this) - 1;
    }

    private int maxDepth(Node n) {
        if (n == null)
            return 0;

        int lDepth = maxDepth(n.left);
        int rDepth = maxDepth(n.right);

        // use the larger + 1 
        return Math.max(lDepth, rDepth) + 1;
    }

    public int size() {
        return size(this);
    }

    private int size(Node n) {
        if (n == null)
            return 0;

        return size(n.left) + size(n.right) + 1;
    }

    public int elementCount() {
        return elementCount(this);
    }

    private int elementCount(Node n) {
        if (n == null || Chromosome.INPUTS.contains(n.result))
            return 0;

        return elementCount(n.left) + elementCount(n.right) + 1;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(result);
        if (left != null) {
            sb.append("(").append(left.toString());
            if (right != null)
                sb.append(",").append(right.toString());
            sb.append(")");
        }
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null)
            return false;
        if (!(o instanceof Node))
            return false;

        Node s = (Node) o;

        if (!result.equals(s.result))
            return false;

        Node left2  = s.left;
        Node right2 = s.right;

        if (left2 != null || left != null) {
            if (!left.equals(left2))
                return false;
        }
        if (right2 != null || right != null) {
            if (!right.equals(right2))
                return false;
        }

        return true;
    }
}
