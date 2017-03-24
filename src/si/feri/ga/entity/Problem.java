package si.feri.ga.entity;

import si.feri.ga.calculators.FenotypeCalculator;
import si.feri.ga.calculators.OperatorCalculator;
import si.feri.ga.selectioners.*;
import si.feri.ga.utils.TruthTableUtils;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Random;

public class Problem {
    // Settings
    public static int    RUN_COUNT            = 10;
    public static int    SOLUTION_COUNT       = 10; // Solutions in one generation
    public static int    ELITE_COUNT          = 2;
    public static int    INPUT_COUNT          = 10;
    public static double MUTATION_PROBABILITY = 0.05;
    public static int    SELECTION_METHOD     = 3;
    public static int    TOURNAMENT_SIZE      = 2;
    public static int    GENERATIONS          = 1000;


    public static byte[][] TRUTH_TABLE;

    public LinkedList<Chromosome> population = new LinkedList<Chromosome>();
    public int                    generation = 1;

    private Selectioner        selectioner;
    private FenotypeCalculator fenotypeCalculator;

    public static Random rnd = new Random();

    public Problem() {
        this(null, null);
    }

    public Problem(byte[][] truth_table) {
        this(truth_table, null);
    }

    public Problem(byte[][] truth_table, LinkedList<Chromosome> solutions) {
        fenotypeCalculator = new OperatorCalculator();

        if (SELECTION_METHOD == 0)
            selectioner = new ProportionateSelectioner();
        else if (SELECTION_METHOD == 1)
            selectioner = new RankSelectioner();
        else if (SELECTION_METHOD == 2)
            selectioner = new WorstFirstOutSelectioner();
        else
            selectioner = new TournamentSelectioner();

        if (truth_table == null) {
            Problem.TRUTH_TABLE = TruthTableUtils.generateTruthTable(Problem.INPUT_COUNT);
        } else {
            Problem.INPUT_COUNT = truth_table.length - 1;
            Problem.TRUTH_TABLE = truth_table;
        }

        generateSolutions(solutions);
    }

    private void generateSolutions(LinkedList<Chromosome> solutions) {
        int count = 0;
        if (solutions != null) {
            for (Chromosome s : solutions) {
                fenotypeCalculator.run(s);
                population.add(s);
                count++;
            }
        }

        for (int i = count; i < SOLUTION_COUNT; i++) {
            Chromosome s = new Chromosome();

            // Checks if solution is unique
            if (population.contains(s)) {
                i--;
                continue;
            }

            fenotypeCalculator.run(s);
            population.add(s);
        }

        while (population.size() > SOLUTION_COUNT) {
            population.removeLast();
        }

        Collections.sort(population);
    }

    public boolean makeNewGeneration() {
        LinkedList<Chromosome> selection      = selectioner.getSelection(this);
        LinkedList<Chromosome> new_generation = new LinkedList<Chromosome>();

        generation++;

        // Elite advances automatically
        for (int j = 0; j < ELITE_COUNT; j++) {
            new_generation.add(population.get(j));
        }

        // Rest of the generation
        for (int i = 0; i < SOLUTION_COUNT - ELITE_COUNT; i++) {
            Chromosome s1 = selection.get(i);
            Chromosome s2 = selection.get(++i);

            Chromosome child1 = s1.crossover(s2);
            child1.mutate(MUTATION_PROBABILITY);

            fenotypeCalculator.run(child1);
            new_generation.add(child1);

            if (i < SOLUTION_COUNT - ELITE_COUNT) {
                Chromosome child2 = s2.crossover(s1);
                child2.mutate(MUTATION_PROBABILITY);

                fenotypeCalculator.run(child2);
                new_generation.add(child2);
            }
        }

        population = new_generation;

        // Sort population by fitness
        Collections.sort(population);

        return true;
    }

    public Chromosome getBest() {
        return population.getFirst();
    }

    public Chromosome getWorst() {
        return population.getLast();
    }

    public double getAverageFitness() {
        double result = 0;
        for (Chromosome s : population)
            result += s.fitness;

        return result / population.size();
    }

    public double getAverageAccuracy() {
        double result = 0;
        for (Chromosome s : population)
            result += s.acc;

        return result / population.size();
    }

    public double getAverageElementCount() {
        double result = 0;
        for (Chromosome s : population)
            result += s.root.elementCount();

        return result / population.size();
    }

    @Override
    public String toString() {
        return String.format("%4d\t%4.0f\t%4.0f\t%4.0f\t%s%n",
                             this.generation,
                             this.getBest().fitness,
                             this.getAverageFitness(),
                             this.getWorst().fitness,
                             this.getBest());
    }

    public void print(PrintStream stream) {
        print(new PrintWriter(stream, true));
    }

    public void print(PrintWriter pw) {
        pw.printf(this.toString());
    }

    public void printFinish(PrintStream stream, long time) {
        printFinish(new PrintWriter(stream, true), time);
    }

    public void printFinish(PrintWriter pw, long time) {
        Chromosome best = this.getBest();
        pw.printf("%4.2f\t%4.0f\t%4d\t%4.2f\t%4.0f\t%4.2f\t%8d\t%s%n",
                  best.acc,
                  best.fitness,
                  best.root.elementCount(),
                  this.getAverageAccuracy(),
                  this.getAverageFitness(),
                  this.getAverageElementCount(),
                  time,
                  best);
    }
}
