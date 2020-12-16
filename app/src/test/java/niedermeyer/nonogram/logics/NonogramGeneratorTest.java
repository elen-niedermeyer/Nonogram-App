package niedermeyer.nonogram.logics;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class NonogramGeneratorTest {

    private int[][] nonogram;
    private NonogramGenerator gen;

    @Before
    public void setUp() throws Exception {
        nonogram = new int[][]{
                {NonogramConstants.FIELD_EMPTY, NonogramConstants.FIELD_EMPTY, NonogramConstants.FIELD_EMPTY},
                {NonogramConstants.FIELD_FILLED, NonogramConstants.FIELD_EMPTY, NonogramConstants.FIELD_FILLED},
                {NonogramConstants.FIELD_FILLED, NonogramConstants.FIELD_EMPTY, NonogramConstants.FIELD_EMPTY},
                {NonogramConstants.FIELD_FILLED, NonogramConstants.FIELD_EMPTY, NonogramConstants.FIELD_FILLED}
        };

        gen = new NonogramGenerator();
        gen.setNonogram(nonogram);
    }

    @Test
    public void testGetNonogram() {
        NonogramGenerator gen = new NonogramGenerator();
        gen.setNonogram(nonogram);
        Assert.assertArrayEquals(nonogram, gen.getNonogram());
    }

    @Test
    public void testSetNonogram() {
        int[][] nonogram2 = new int[][]{
                {NonogramConstants.FIELD_FILLED, NonogramConstants.FIELD_FILLED},
                {NonogramConstants.FIELD_FILLED, NonogramConstants.FIELD_EMPTY}
        };
        gen.setNonogram(nonogram2);
        Assert.assertArrayEquals(nonogram2, gen.getNonogram());
    }

    @Test
    public void testGetRowCount() {
        GroupCount rowCount = new GroupCount();
        rowCount.addValueToList(0, 0);
        rowCount.addValueToList(1, 1);
        rowCount.addValueToList(1, 1);
        rowCount.addValueToList(2, 1);
        rowCount.addValueToList(3, 1);
        rowCount.addValueToList(3, 1);

        Assert.assertEquals(rowCount, gen.getRowCount());
    }

    @Test
    public void testGetColumnCount() {
        GroupCount columnCount = new GroupCount();
        columnCount.addValueToList(0, 3);
        columnCount.addValueToList(1, 0);
        columnCount.addValueToList(2, 1);
        columnCount.addValueToList(2, 1);

        Assert.assertEquals(columnCount, gen.getColumnCount());
    }

    @Test
    public void makeNewGame() {
        gen.makeNewGame(5, 6);

        Assert.assertNotEquals(nonogram, gen.getNonogram());
        Assert.assertEquals(5, gen.getNonogram().length);
        Assert.assertEquals(6, gen.getNonogram()[0].length);
    }
}