package si.feri.ga;

import si.feri.ga.entity.Problem;
import si.feri.ga.utils.TruthTableUtils;
import si.feri.ga.utils.Utils;

import java.io.IOException;
import java.io.PrintWriter;

import static si.feri.ga.utils.TruthTableUtils.saveTruthTable;

/**
 * @author karakatic
 */
public class OneRun {
    public static void main(String[] args) {
        boolean sysout = true;
        String  folder = "OneRun";

        Utils.readSettings(folder);

        try (PrintWriter outAll = new PrintWriter(folder + "/results-" + System.currentTimeMillis() + ".log")) {
            outAll.println(" Acc\t Fit\t Elm\tAccM\tFitM\tElmM\tTotaTime\tSolution");
            for (int i = 0; i < Problem.RUN_COUNT; i++) {
                Problem p;
                String  name;

                // Check if you use existing truth table, or to generate a new random one
                if (TruthTableUtils.FILE_NAME.equals("")) { // Generate a random truth table
                    p = new Problem();

                    // Save random truth table
                    name = saveTruthTable(folder + "/TruthTables/", Problem.INPUT_COUNT + "-");
                    if (name == null && sysout) {
                        System.out.println("Error saving. Continuing nonetheless.");
                    } else if (sysout) {
                        System.out.println("Truth table saved to \"" + name + "\"");
                    }
                } else { // Use existing truth table
                    p = TruthTableUtils.loadTruthTable();
                    name = TruthTableUtils.FILE_NAME + "-" + System.currentTimeMillis();
                }

                try (PrintWriter out = new PrintWriter(folder + "/results/" + name + ".log")) {
                    if (sysout) {
                        System.out.println(" Gen\tBest\t Avg\tWrst\tSolution");
                        p.print(System.out);
                    }
                    out.println(" Gen\tBest\t Avg\tWrst\tSolution");
                    p.print(out);

                    long startTime = System.currentTimeMillis(); // Start stopwatch

                    // Start the evolution
                    while (p.generation < Problem.GENERATIONS) {
                        p.makeNewGeneration();

                        if (sysout) {
                            p.print(System.out);
                        }
                        p.print(out);
                        out.flush();
                    }
                    // Evolution finished

                    long totalTime = System.currentTimeMillis() - startTime;
                    System.out.println("Finished in " + totalTime + " ms"); // Stop stopwatch

                    if (sysout) {
                        System.out.println(" Acc\t Fit\t Elm\tAccM\tFitM\tElmM\tTotaTime\tSolution");
                        p.printFinish(System.out, totalTime);
                    }
                    p.printFinish(outAll, totalTime);
                    outAll.flush();
                } catch (IOException e) {
                    System.err.println(e);
                }
            }
        } catch (IOException e) {
            System.err.println(e);
        }
    }
}
