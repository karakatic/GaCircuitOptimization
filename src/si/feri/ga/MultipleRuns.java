package si.feri.ga;

import si.feri.ga.entity.Chromosome;
import si.feri.ga.entity.Problem;
import si.feri.ga.utils.Utils;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

/**
 * @author karakatic
 */
public class MultipleRuns {
    public static void main(String[] args) {
        boolean sysout = true;
        String  folder = "MultiRun";

        Utils.readSettings(folder);

        int max_input = Problem.INPUT_COUNT;
        int min_input = 2;

        for (int i = min_input; i < max_input; i++) {
            PrintWriter out = null;
            try {
                out = new PrintWriter(folder + "/results/" + i + "-" + ".log");
                out.println();
                out.println(
                        "R-run I-number of inputs G-generation when finished ACC-accuracy of the best MA-average acuracy W-worst of the last generation A-average of last generation B-best of the last generation S-solution of the best EC-element count for the best EA-element count average in generation");

                for (int k = 0; k < Problem.RUN_COUNT; k++) {
                    Problem.INPUT_COUNT = i;
                    Chromosome.INPUTS = new ArrayList<>();
                    for (int j = 0; j < Problem.INPUT_COUNT; j++) {
                        char c = (char) (j + 65);
                        Chromosome.INPUTS.add(c + "");
                    }

                    Problem p = new Problem();

                    System.out.println(k + "====== INPUT COUNT: " + Problem.INPUT_COUNT);
                    System.out.println(Problem.TRUTH_TABLE[0].length + " " + Problem.TRUTH_TABLE.length);
                    long time = System.currentTimeMillis();

                    while (p.generation < Problem.GENERATIONS) {
                        p.makeNewGeneration();

                        out.println("R:" + k + ";I:" + Problem.INPUT_COUNT + ";G:" + p.generation + ";ACC:" + p
                                .getBest().acc + ";MA:" + p.getAverageAccuracy() + ";EC:" + p.getBest().root
                                .elementCount() + ";EA:" + p.getAverageElementCount());
                        System.out.println(
                                "R:" + k + ";I:" + Problem.INPUT_COUNT + ";G:" + p.generation + ";ACC:" + p
                                        .getBest().acc + ";MA:" + p.getAverageAccuracy() + ";EC:" + p.getBest().root
                                        .elementCount() + ";EA:" + p.getAverageElementCount());
                        out.flush();
                    }

                    int fitnessBest    = (int) p.getBest().fitness;
                    int fitnessAverage = (int) p.getAverageFitness();
                    int fitnessWorst   = (int) p.getWorst().fitness;
//                    System.out.println("Finished in " + (System.currentTimeMillis() - time) + " ms");
//                    System.out.println("Best: " + p.generation + ";" + fitnessWorst + ";" + fitnessAverage + ";" + fitnessBest + ";" + p.getBest().toString());
//                out.println("R:" + k + ";I:" + Problem.INPUT_COUNT + ";G:" + p.generation + ";ACC:" + p.getBest().acc + ";MA:" + p.getAverageAccuracy() + ";W:" + fitnessWorst + ";A:" + fitnessAverage + ";B:" + fitnessBest + ";S:" + p.getBest().toString());
                    System.out.println("R:" + k + ";I:" + Problem.INPUT_COUNT + ";G:" + p.generation + ";ACC:" + p
                            .getBest().acc + ";MA:" + p
                            .getAverageAccuracy() + ";W:" + fitnessWorst + ";A:" + fitnessAverage + ";B:" + fitnessBest + ";S:" + p
                            .getBest().toString());
                }
            } catch (IOException e) {
                System.err.println(e);
            } finally {
                out.close();
            }
        }
    }
}
