package si.feri.ga.selectioners;

import si.feri.ga.entity.Chromosome;
import si.feri.ga.entity.Problem;
import si.feri.ga.utils.Utils;

import java.util.LinkedList;

/**
 * @author karakatic
 */
public class RankSelectioner implements Selectioner {

    @Override
    public LinkedList<Chromosome> getSelection(Problem p) {
        int[] roulette  = new int[Problem.SOLUTION_COUNT];
        int   sum_sofar = 0;

        // Make roulette
        for (int i = 0; i < roulette.length; i++) {
            int d = 2 * (p.population.size() - i);
            sum_sofar += d;
            roulette[i] = sum_sofar;
        }

        LinkedList<Chromosome> selection = new LinkedList<Chromosome>();

        // Number of pairs
        int selection_size = Problem.SOLUTION_COUNT - Problem.ELITE_COUNT;
        if (selection_size % 2 == 1)
            selection_size++;

        // Choose pairs
        for (int i = 0; i < selection_size; i++) {
            // Ball on roulette
            int r = Problem.rnd.nextInt(sum_sofar + 1);

            Chromosome s = null;
            int        j = 0;
            while (s == null) {
                if (r <= roulette[j]) {
                    s = p.population.get(j);
                }
                j++;
            }

            // Check if spouse1 equals spouse2
            if (i % 2 == 1 && Utils.problem(s, selection.getLast())) {
                i--;
                continue;
            }
            selection.add(s);
        }
        return selection;
    }
}
