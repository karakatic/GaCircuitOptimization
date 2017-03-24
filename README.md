# GaCircuitOptimization
Genetic algorithm that creates electrical circuits.

Instructions
1. Settings of GA are in the OneRun/settings.ini
2. To change the operator set, edit the Chromosome.java -> ArrayList<String> OPERATORS. If you add some new operators, you should add the operation in the OperatorCalculator.
3. Run the Genetic algorithm with running the OneRun.java
4. Results are saved in the OneRun directory in the following way:
    - individual results (generation by generation) are saved in the OneRun/results as log files
    - randomly generated truth tabels are saved in OneRun/TruthTables
    - final results (only final results) of all runs are saved as log files in OneRun directory
