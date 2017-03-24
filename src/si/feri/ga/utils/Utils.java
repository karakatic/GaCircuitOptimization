package si.feri.ga.utils;

import si.feri.ga.entity.Chromosome;
import si.feri.ga.entity.Problem;

import java.io.*;
import java.util.ArrayList;
import java.util.Properties;

/**
 * @author karakatic
 */
public class Utils {
    public static boolean problem(Chromosome s1, Chromosome s2) {
        if (s1.equals(s2))
            return true;
        return false;
    }

    public static void readSettings() {
        readSettings("OneRun");
    }

    public static void readSettings(String folder) {
        Properties p = new Properties();

        try {
            p.load(new FileInputStream(folder + "/settings.ini"));
            Problem.INPUT_COUNT = Integer.parseInt(p.getProperty("INPUT_COUNT"));
            Problem.ELITE_COUNT = Integer.parseInt(p.getProperty("ELITE_COUNT"));
            Problem.SOLUTION_COUNT = Integer.parseInt(p.getProperty("SOLUTION_COUNT"));
            Problem.MUTATION_PROBABILITY = Double.parseDouble(p.getProperty("MUTATION_PROBABILITY"));
            Problem.SELECTION_METHOD = Integer.parseInt(p.getProperty("SELECTION_METHOD"));
            Problem.GENERATIONS = Integer.parseInt(p.getProperty("GENERATIONS"));
            Problem.TOURNAMENT_SIZE = Integer.parseInt(p.getProperty("TOURNAMENT_SIZE"));
            Problem.RUN_COUNT = Integer.parseInt(p.getProperty("RUN_COUNT"));

            TruthTableUtils.STARTING_SOLUTIONS = p.getProperty("STARTING_SOLUTIONS");
            TruthTableUtils.FILE_NAME = p.getProperty("LOAD_FIELD");

            Chromosome.INPUTS = new ArrayList<String>();
            for (int i = 0; i < Problem.INPUT_COUNT; i++) {
                char c = (char) (i + 65);
                Chromosome.INPUTS.add(c + "");
            }
        } catch (FileNotFoundException e) {
            System.out.println("No settings found. Running on default mode.");
            System.out.println("Insert settings in \"settings.ini\" file.");
            Utils.makeDefaultSettings();
        } catch (Exception e) {
            System.out.println("File \"settings.ini\" has errors. Edit and try again.");
            System.exit(0);
        }
    }

    public static void makeDefaultSettings() {
        Properties p = new Properties();

        try {
            File f = new File("OneRun/settings.ini");
            if (f.exists())
                f.delete();

            f.createNewFile();

            p.load(new FileInputStream(f));
            p.put("INPUT_COUNT", "" + Problem.INPUT_COUNT);
            p.put("ELITE_COUNT", "" + Problem.ELITE_COUNT);
            p.put("SOLUTION_COUNT", "" + Problem.SOLUTION_COUNT);
            p.put("MUTATION_PROBABILITY", "" + Problem.MUTATION_PROBABILITY);
            p.put("LOAD_FIELD", "");
            p.put("SELECTION_METHOD", "" + Problem.SELECTION_METHOD);
            p.put("GENERATIONS", "" + Problem.GENERATIONS);
            p.put("TOURNAMENT_SIZE", "" + Problem.TOURNAMENT_SIZE);
            p.put("RUN_COUNT", "" + Problem.RUN_COUNT);
            p.put("STARTING_SOLUTIONS", "");

            FileOutputStream out = new FileOutputStream(f);
            p.store(out, "Settings for Genetic Algorithm Circuit Optimizer");
        } catch (Exception e) {
            System.err.println(e);
        }
    }

    public static ArrayList<String> loadRunFile() {
        ArrayList<String> runs = new ArrayList<String>();

        try {
            BufferedReader in = new BufferedReader(new FileReader("MultipleRuns/run.txt"));
            String         line;
            while ((line = in.readLine()) != null)
                runs.add(line);

            in.close();
        } catch (Exception e) {
            System.err.println(e);
        }

        return runs;
    }

    public static Problem makeProblem(String st) {
        String[] s = st.split(";");
        TruthTableUtils.FILE_NAME = s[0];

        Problem.SELECTION_METHOD = Integer.parseInt(s[1]);
        Problem.TOURNAMENT_SIZE = Integer.parseInt(s[2]);
        Problem.RUN_COUNT = Integer.parseInt(s[3]);
        Problem.GENERATIONS = Integer.parseInt(s[4]);
        Problem.SOLUTION_COUNT = Integer.parseInt(s[5]);
        Problem.ELITE_COUNT = Integer.parseInt(s[6]);
        Problem.MUTATION_PROBABILITY = Double.parseDouble(s[7]);
        TruthTableUtils.STARTING_SOLUTIONS = s[8];

        return TruthTableUtils.loadTruthTable();
    }

    public static String makeParametersString() {
        String[] params = new String[9];
        params[0] = TruthTableUtils.FILE_NAME;
        params[1] = "" + Problem.SELECTION_METHOD;
        params[2] = "" + Problem.TOURNAMENT_SIZE;
        params[3] = "" + Problem.RUN_COUNT;
        params[4] = "" + Problem.GENERATIONS;
        params[5] = "" + Problem.SOLUTION_COUNT;
        params[6] = "" + Problem.ELITE_COUNT;
        params[7] = "" + Problem.MUTATION_PROBABILITY;
        params[8] = TruthTableUtils.STARTING_SOLUTIONS;

        String full_params = "";
        for (String s : params) {
            full_params += s + ";";
        }

        return full_params;
    }
}
