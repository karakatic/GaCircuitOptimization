package si.feri.ga.utils;

import si.feri.ga.entity.Chromosome;
import si.feri.ga.entity.Problem;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 * @author karakatic
 */
public class TruthTableUtils {
    public static String FILE_NAME          = "";
    public static String STARTING_SOLUTIONS = "";

    public static Problem loadTruthTable() {
        return loadTruthTable(getSolutions());
    }

    // TODO: repair, not working currently
    public static LinkedList<Chromosome> getSolutions() {
        if (STARTING_SOLUTIONS == null || STARTING_SOLUTIONS.trim().isEmpty())
            return null;

        LinkedList<Chromosome> result = new LinkedList<>();

        try {
            BufferedReader in   = new BufferedReader(new FileReader(STARTING_SOLUTIONS));
            String         line = "";
            while ((line = in.readLine()) != null) {
                String[] arr = line.split("\\t");
                line = arr[arr.length - 1];
                arr = line.split(",");
                int[] genotype = new int[arr.length];

                for (int i = 0; i < arr.length; i++) {
                    genotype[i] = Integer.parseInt(arr[i]);
                }
                //result.add(new Chromosome(genotype));
            }

            in.close();
        } catch (Exception e) {
            System.err.println(e);
            return null;
        }

        return result;
    }

    public static Problem loadTruthTable(LinkedList<Chromosome> solutions) {
        Problem  p = null;
        byte[][] truth_table;

        try {
            BufferedReader in = new BufferedReader(new FileReader(FILE_NAME));
            Chromosome.INPUTS = new ArrayList<>();
            ArrayList<String> truth_row = new ArrayList<>();

            String line;
            while ((line = in.readLine()) != null) {
                if (line.isEmpty())
                    continue;

                String[] two_att = line.split("\\|");
                Chromosome.INPUTS.add(two_att[0]);
                truth_row.add(two_att[1]);
            }

            Chromosome.OUTPUT = Chromosome.INPUTS.remove(truth_row.size() - 1);
            truth_table = new byte[truth_row.size()][];
            String[] temp_row;
            for (int i = 0; i < truth_row.size(); i++) {
                temp_row = truth_row.get(i).trim().split("\\s");
                truth_table[i] = new byte[temp_row.length];
                for (int j = 0; j < temp_row.length; j++)
                    truth_table[i][j] = Byte.parseByte(temp_row[j]);
            }

            in.close();

            p = new Problem(truth_table, solutions);
        } catch (Exception e) {
            System.err.println(e);
        }

        return p;
    }

    public static String saveTruthTable() {
        return saveTruthTable("TruthTables/", Problem.INPUT_COUNT + "-");
    }

    public static String saveTruthTable(String folder, String prefix) {
        String name = prefix + System.currentTimeMillis();
        try (PrintWriter out = new PrintWriter(folder + name)) {

            for (int i = 0; i < Problem.TRUTH_TABLE.length; i++) {
                char c = (char) (i + 65);
                if (i == Problem.TRUTH_TABLE.length - 1) {
                    out.println();
                    c = 'Z';
                }

                out.print(c + " | ");
                for (int j = 0; j < Problem.TRUTH_TABLE[i].length; j++) {
                    short s = Problem.TRUTH_TABLE[i][j];
                    out.print(s + " ");
                }
                out.println();
            }
        } catch (Exception e) {
            System.err.println(e);
            return null;
        }

        return name;
    }


    public static byte[][] generateTruthTable(int inputCount) {
        byte[][] tt = new byte[inputCount + 1][];
        for (int i = 0; i < inputCount; i++) {
            tt[i] = getColumn(i, inputCount);
        }

        tt[inputCount] = getRandomColumn(inputCount);
        return tt;
    }

    private static byte[] getRandomColumn(int inputCount) {
        double rows   = Math.pow(2, inputCount);
        byte[] column = new byte[(int) rows];
        for (int i = 0; i < rows; i++) {
            column[i] = (byte) Problem.rnd.nextInt(2);
        }

        return column;
    }

    private static byte[] getColumn(int index, int inputCount) {
        int step_original = inputCount - index - 1;
        step_original = (int) Math.pow(2, step_original);

        int step = step_original;

        double rows   = Math.pow(2, inputCount);
        byte[] column = new byte[(int) rows];
        byte   fill   = 0;
        for (int i = 0; i < rows; i++) {
            column[i] = fill;
            step--;
            if (step == 0) {
                step = step_original;
                if (fill == 0)
                    fill = 1;
                else if (fill == 1)
                    fill = 0;
            }
        }

        return column;
    }
}
