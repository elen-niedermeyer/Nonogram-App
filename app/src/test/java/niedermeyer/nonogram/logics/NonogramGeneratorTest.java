package niedermeyer.nonogram.logics;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import static junit.framework.Assert.assertEquals;

/**
 * @author Elen Niedermeyer, last modified 2018-02-08
 */
public class NonogramGeneratorTest {

    private int[][] nonogram;

    @Before
    public void initializeNonogram() {
        nonogram = new int[][]{
                {NonogramConstants.FIELD_EMPTY, NonogramConstants.FIELD_EMPTY, NonogramConstants.FIELD_PROVED},
                {NonogramConstants.FIELD_EMPTY, NonogramConstants.FIELD_PROVED, NonogramConstants.FIELD_PROVED},
                {NonogramConstants.FIELD_EMPTY, NonogramConstants.FIELD_EMPTY, NonogramConstants.FIELD_PROVED},
                {NonogramConstants.FIELD_PROVED, NonogramConstants.FIELD_PROVED, NonogramConstants.FIELD_PROVED},
                {NonogramConstants.FIELD_EMPTY, NonogramConstants.FIELD_EMPTY, NonogramConstants.FIELD_PROVED}
        };
    }

    @Test
    public void getCountsColumns() throws Exception {
        NonogramGenerator generator = new NonogramGenerator();
        generator.setNonogram(nonogram);
        FilledFieldsCount columnCounts = generator.getCountsColumns();
        ArrayList<ArrayList<CountValue>> actualCounts = columnCounts.getCountsList();

        ArrayList<ArrayList<CountValue>> expectedCounts = new ArrayList<>();
        // add first column
        expectedCounts.add(new ArrayList<>(Collections.singletonList(new CountValue(1, false))));
        // add second column
        expectedCounts.add(new ArrayList<>(Arrays.asList(new CountValue(1, false), new CountValue(1, false))));
        // add third column
        expectedCounts.add(new ArrayList<>(Collections.singletonList(new CountValue(5, false))));

        // compare sizes
        assertEquals("Column counts do not have the same size", expectedCounts.size(), actualCounts.size());
        for (int i = 0; i < actualCounts.size(); i++) {
            assertEquals("Inner list of column counts do not have the same size", expectedCounts.get(i).size(), actualCounts.get(i).size());
        }

        // compare values
        for (int i = 0; i < actualCounts.size(); i++) {
            ArrayList<CountValue> actualInnerList = actualCounts.get(i);
            ArrayList<CountValue> expectedInnerList = expectedCounts.get(i);
            for (int j = 0; j < actualInnerList.size(); j++) {
                assertEquals("Column counts values are not equals.", expectedInnerList.get(j), actualInnerList.get(j));
            }
        }
    }

    @Test
    public void getCountsRows() throws Exception {
    }

    @Test
    public void makeNewGame() throws Exception {
    }

}