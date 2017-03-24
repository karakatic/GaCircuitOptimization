package si.feri.ga.selectioners;

import si.feri.ga.entity.Chromosome;
import si.feri.ga.entity.Problem;

import java.util.LinkedList;

/**
 *
 * @author karakatic
 */
public interface Selectioner {
    public LinkedList<Chromosome> getSelection(Problem p);
}
