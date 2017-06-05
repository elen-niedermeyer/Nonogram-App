package niedermeyer.nonogram.logics;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * @author Elen Niedermeyer, last modified 2017-04-28
 */

public class NonogramGenerator {

    private int[][] nonogram;
    private Map<Integer, ArrayList<Integer>> countsRow;
    private Map<Integer, ArrayList<Integer>> countsColumn;

    private Random random = new Random();

    public int[][] getNonogram() {
        return nonogram;
    }

    public Map<Integer, ArrayList<Integer>> getCountsRow() {
        return countsRow;
    }

    public Map<Integer, ArrayList<Integer>> getCountsColumn() {
        return countsColumn;
    }

    public void makeNewGame(int pNumberOfRows, int pNumberOfColumns) {
        boolean isOnlyZero;
        do {
            nonogram = generateNonogram(pNumberOfRows, pNumberOfColumns);
            countsRow = countProvedFieldsPerRow();
            countsColumn = countProvedFieldsPerColumn();

            // prove if there is at least one field proved
            isOnlyZero = true;
            for (ArrayList<Integer> list : countsRow.values()) {
                for (Integer count : list) {
                    if (count != 0) {
                        isOnlyZero = false;
                        break;
                    }
                }
                if (!isOnlyZero) {
                    break;
                }
            }
        } while (isOnlyZero);
    }

    private int[][] generateNonogram(int pNumberOfRows, int pNumberOfColumns) {
        int[][] nonogram = new int[pNumberOfRows][pNumberOfColumns];

        for (int i = 0; i < nonogram.length; i++) {
            for (int j = 0; j < nonogram[i].length; j++) {
                nonogram[i][j] = random.nextInt(2);
            }
        }

        return nonogram;
    }

    private Map<Integer, ArrayList<Integer>> countProvedFieldsPerRow() {
        HashMap<Integer, ArrayList<Integer>> provedFields = new HashMap<>();
        for (int i = 0; i < nonogram.length; i++) {
            int res = 0;
            ArrayList<Integer> results = new ArrayList<>();
            for (int j = 0; j < nonogram[i].length; j++) {
                if (nonogram[i][j] == 1) {
                    res += 1;
                }
                if (nonogram[i][j] == 0 && res != 0) {
                    results.add(res);
                    res = 0;
                }
            }

            if (res != 0) {
                results.add(res);
            }

            if (results.isEmpty()) {
                results.add(0);
            }

            provedFields.put(i, results);
        }

        return provedFields;
    }

    private Map<Integer, ArrayList<Integer>> countProvedFieldsPerColumn() {
        HashMap<Integer, ArrayList<Integer>> provedFields = new HashMap<>();
        for (int i = 0; i < nonogram[0].length; i++) {
            int res = 0;
            ArrayList<Integer> results = new ArrayList<>();
            for (int j = 0; j < nonogram.length; j++) {
                if (nonogram[j][i] == 1) {
                    res += 1;
                }
                if (nonogram[j][i] == 0 && res != 0) {
                    results.add(res);
                    res = 0;
                }
            }

            if (res != 0) {
                results.add(res);
            }

            if (results.isEmpty()) {
                results.add(0);
            }

            provedFields.put(i, results);
        }

        return provedFields;
    }
}
