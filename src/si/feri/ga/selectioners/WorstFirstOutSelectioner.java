package si.feri.ga.selectioners;

import si.feri.ga.entity.Chromosome;
import si.feri.ga.entity.Problem;
import si.feri.ga.utils.Utils;

import java.util.LinkedList;

/**
 * @author karakatic
 */
public class WorstFirstOutSelectioner implements Selectioner {

    @Override
    public LinkedList<Chromosome> getSelection(Problem p) {
        LinkedList<Chromosome> selection = new LinkedList<Chromosome>();

        // Number of pairs
        int selection_size = Problem.SOLUTION_COUNT - Problem.ELITE_COUNT;
        if (selection_size % 2 == 1)
            selection_size++;

        // Choose pairs
        for (int i = p.population.size(); i > 0; i--) {
            int        r = Problem.rnd.nextInt(i);
            Chromosome s = p.population.get(r);

            // Check if spouse1 equals spouse2
            if (i % 2 == 1 && i != 1 && Utils.problem(s, selection.getLast())) {
                i++;
                continue;
            }
            selection.add(s);

            if (selection.size() == selection_size)
                break;
        }
        return selection;
    }
}
