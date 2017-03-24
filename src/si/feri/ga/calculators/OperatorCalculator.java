package si.feri.ga.calculators;

import si.feri.ga.entity.Chromosome;
import si.feri.ga.entity.Node;
import si.feri.ga.entity.Problem;

/**
 * @author karakatic
 */
public class OperatorCalculator extends FenotypeCalculator {
    private Chromosome c;

    @Override
    public void run(Chromosome s) {
        this.c = s;
        int fit             = countOperators(c.root);
        int input_count     = (int) Math.pow(2, Chromosome.INPUTS.size());
        int number_of_wrong = getNumberOfErrors();

        c.acc = (double) (input_count - number_of_wrong) / input_count;
        fit += number_of_wrong * 100;

        c.fitness = fit;
    }

    // Return the number of operators in circuit
    private int countOperators(Node n) {
        if (n == null)
            return 0;

        int operator = 0;

        if (Chromosome.OPERATORS.contains(n.result))
            operator = 1;

        return countOperators(n.left) + countOperators(n.right) + operator;
    }

    // Returns the number of wrong outputs
    // min=0 -> all outputs are correct
    // max=2^(num of inputs) -> all outputs are wrong
    public int getNumberOfErrors() {
        int    size = Chromosome.INPUTS.size() + 1;
        byte[] ex   = new byte[size];

        int penalty = 0;

        for (int i = 0; i < Math.pow(2, Chromosome.INPUTS.size()); i++) {
            for (int j = 0; j < size; j++) {
                byte s = Problem.TRUTH_TABLE[j][i];
                ex[j] = s;
            }
            boolean result = testNode(c.root, ex);
            byte    res    = 0;
            if (result)
                res = 1;

            if (res != ex[ex.length - 1])
                penalty++;
        }

        return penalty;
    }

    // Returns correct result for combination of inputs (true=1 false=0)
    private boolean testNode(Node n, byte[] ex) {
        String op = n.result;

        if (Chromosome.OPERATORS.contains(op)) {
            boolean left  = testNode(n.left, ex);
            boolean right = false;
            if (!op.equals("NOT"))
                right = testNode(n.right, ex);

            return eval(op, left, right);
        }

        int   index = Chromosome.INPUTS.indexOf(op);
        short res   = ex[index];
        if (res == 1)
            return true;

        return false;
    }

    private boolean eval(String op, boolean a, boolean b) {
        if (op.equals("AND"))
            return and(a, b);
        else if (op.equals("OR"))
            return or(a, b);
        else if (op.equals("XOR"))
            return xor(a, b);
        else if (op.equals("XNOR"))
            return xnor(a, b);
        else if (op.equals("NOR"))
            return nor(a, b);
        else if (op.equals("NAND"))
            return nand(a, b);

        return not(a);
    }

    private boolean and(boolean a, boolean b) {
        return a && b;
    }

    private boolean or(boolean a, boolean b) {
        return a || b;
    }

    private boolean not(boolean a) {
        return !a;
    }

    private boolean nand(boolean a, boolean b) {
        return !(a && b);
    }

    private boolean nor(boolean a, boolean b) {
        return !(a || b);
    }

    private boolean xor(boolean a, boolean b) {
        if (a && !b)
            return true;
        if (!a && b)
            return true;
        return false;
    }

    private boolean xnor(boolean a, boolean b) {
        return !xor(a, b);
    }
}