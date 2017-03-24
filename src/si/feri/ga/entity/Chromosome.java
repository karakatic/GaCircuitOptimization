package si.feri.ga.entity;

import java.util.ArrayList;
import java.util.Arrays;

public class Chromosome implements Comparable<Chromosome> {
    public Node   root;
    public double fitness;
    public double acc;

    //    public static final ArrayList<String> OPERATORS = new ArrayList<>(Arrays.asList("AND", "OR", "NAND", "NOR", "XOR", "XNOR", "NOT"));
    //    public static final ArrayList<String> OPERATORS = new ArrayList<>(Arrays.asList("NAND", "NOR"));
    public static final ArrayList<String> OPERATORS = new ArrayList<>(Arrays.asList("AND", "OR", "XOR", "NOT"));

    public static ArrayList<String> INPUTS;
    public static String OUTPUT = "Z";

    public Chromosome() {
        root = new Node(OPERATORS.get(Problem.rnd.nextInt(OPERATORS.size() - 1)));
        ArrayList<String> inputs_left;
        do {
            inputs_left = new ArrayList<>(INPUTS);
            root.setLeft(makeNode(inputs_left));
            root.setRight(makeNode(inputs_left));
        } while (!inputs_left.isEmpty());
    }

    public Chromosome(Node root) {
        this.root = new Node(root);
    }

    private Node makeNode() {
        return makeNode(new ArrayList<>());
    }

    private Node makeNode(ArrayList<String> inputs_left) {
        String op = getRandomExpression();

        if (inputs_left.contains(op))
            inputs_left.remove(op);

        Node n = new Node(op);

        if (OPERATORS.contains(op)) {
            n.setLeft(makeNode(inputs_left));
            if (!op.equals("NOT"))
                n.setRight(makeNode(inputs_left));
        }

        return n;
    }

    private String getRandomExpression() {
        if (Problem.rnd.nextBoolean()) //operator
            return OPERATORS.get(Problem.rnd.nextInt(OPERATORS.size()));

        return INPUTS.get(Problem.rnd.nextInt(INPUTS.size())); //input
    }

    private void mutateOperator(String op, Node n) {
        if (Problem.rnd.nextBoolean() || n.parent == null) { // Change operator
            String op_new = op;
            while (op_new.equals(op))
                op_new = OPERATORS.get(Problem.rnd.nextInt(OPERATORS.size() - 1));

            n.result = op_new;
        } else { // Exchange for input
            Node    parent = n.parent;
            boolean left   = false;
            if (parent.left.equals(n))
                left = true;

            n = new Node(INPUTS.get(Problem.rnd.nextInt(INPUTS.size())));

            if (left)
                parent.setLeft(n);
            else
                parent.setRight(n);
        }
    }

    private void mutateInput(String op, Node n) {
        if (Problem.rnd.nextBoolean()) { // Change input
            String op_new = op;
            while (op_new.equals(op))
                op_new = INPUTS.get(Problem.rnd.nextInt(INPUTS.size()));

            n.result = op_new;
        } else { // Exchange for new tree
            Node    parent = n.parent;
            boolean left   = false;
            if (parent.left.equals(n))
                left = true;

            n = makeNode();

            if (left)
                parent.setLeft(n);
            else
                parent.setRight(n);
        }
    }

    public void mutate(double percent) {
        if (Problem.rnd.nextFloat() > percent) {
            return;
        }

        Node n    = root;
        int  size = n.size();
        while (true) {
            String op = n.result;

            if (INPUTS.contains(op)) { // for inputs
                mutateInput(op, n);
                break;
            }

            if (Problem.rnd.nextInt(size) == 0) {
                if (op.equals("NOT")) { // for not
                    Node    parent = n.parent;
                    boolean left   = false;
                    if (parent.left.equals(n))
                        left = true;

                    if (left)
                        parent.setLeft(n.left);
                    else
                        parent.setRight(n.left);

                } else if (OPERATORS.contains(op)) { // for operator
                    mutateOperator(op, n);
                }

                break;
            }

            if (Problem.rnd.nextBoolean() || op.equals("NOT"))
                n = n.left;
            else
                n = n.right;
        }
    }

    public Chromosome crossover(Chromosome partner) {
        Node n1       = new Node(root);
        Node root_new = n1;
        Node n2       = new Node(partner.root);
        int  size     = n1.size();

        while (true) {
            String op = n1.result;
            if ((INPUTS.contains(op) || Problem.rnd.nextInt(size) == 0) && n1.parent != null)
                break;

            if (Problem.rnd.nextBoolean() || op.equals("NOT"))
                n1 = n1.left;
            else
                n1 = n1.right;
        }

        while (true) {
            String op = n2.result;
            if (INPUTS.contains(op) || Problem.rnd.nextInt(size) == 0)
                break;

            if (Problem.rnd.nextBoolean() || op.equals("NOT"))
                n2 = n2.left;
            else
                n2 = n2.right;
        }

        Node p = n1.parent; // Check if the root is selected
        if (p.left.equals(n1))
            p.setLeft(n2);
        else
            p.setRight(n2);

        return new Chromosome(root_new);
    }

    @Override
    public String toString() {
        return root.toString();
    }

    @Override
    public int compareTo(Chromosome s) {
        double fit1 = fitness;
        double fit2 = s.fitness;

        if (fit1 > fit2) {
            return 1;
        } else if (fit1 < fit2) {
            return -1;
        }

        return 0;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null)
            return false;
        if (!(o instanceof Chromosome))
            return false;

        Chromosome s = (Chromosome) o;

        if (!root.equals(s.root))
            return false;

        return true;
    }
}
