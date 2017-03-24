package si.feri.ga.selectioners;

import si.feri.ga.entity.Chromosome;
import si.feri.ga.entity.Problem;
import si.feri.ga.utils.Utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;

/**
 * @author karakatic
 */
public class TournamentSelectioner implements Selectioner {

    @Override
    public LinkedList<Chromosome> getSelection(Problem p) {
        LinkedList<Chromosome> selection = new LinkedList<Chromosome>();

        // Number of pairs
        int selection_size = Problem.SOLUTION_COUNT - Problem.ELITE_COUNT;
        if (selection_size % 2 == 1)
            selection_size++;

        for (int i = 0; i < selection_size; i++) {
            ArrayList<Integer> ints = new ArrayList<Integer>(Problem.TOURNAMENT_SIZE);
            for (int j = 0; j < Problem.TOURNAMENT_SIZE; j++) {
                int sol_index = Problem.rnd.nextInt(Problem.SOLUTION_COUNT);
                while (ints.contains(sol_index))
                    sol_index = Problem.rnd.nextInt(Problem.SOLUTION_COUNT);

                ints.add(sol_index);
            }

            LinkedList<Chromosome> candidates = new LinkedList<Chromosome>();
            for (Integer k : ints)
                candidates.add(p.population.get(k));

            Collections.sort(candidates);

            // Check if spouse1 equals spouse2
            if (i % 2 == 1 && Utils.problem(candidates.getFirst(), selection.getLast())) {
                i--;
                continue;
            }

            selection.add(candidates.getFirst());
        }

        return selection;
    }
}
