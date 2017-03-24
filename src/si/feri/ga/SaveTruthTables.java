package si.feri.ga;

import si.feri.ga.entity.Problem;
import si.feri.ga.utils.TruthTableUtils;
import si.feri.ga.utils.Utils;

/**
 * @author karakatic
 */
public class SaveTruthTables {
    public static void main(String[] args) {
        Utils.readSettings();

        try {
            Problem p = new Problem();
            String  name = TruthTableUtils.saveTruthTable();
            if (name == null)
                System.out.println("Error saving.");
            else
                System.out.println("Truth table saved to \"" + name + "\"");
        } catch (Exception e) {
            System.err.println(e);
        }
    }
}
