package niedermeyer.nonogram.logics;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static junit.framework.Assert.assertEquals;

/**
 * @author Elen Niedermeyer
 *         Last modified 2018-02-08
 */
public class FilledFieldsCountTest {

    private FilledFieldsCount exampleCount;

    @Before
    public void initializeExampleCount() {
        exampleCount = new FilledFieldsCount();
        // outer list with index 0
        exampleCount.addCount(0, 1);
        exampleCount.addCount(0, 2);
        // outer list with index 1
        exampleCount.addCount(1, 3);
    }

    @Test
    public void get() throws Exception {
        List<CountValue> expectedIndexZero = new ArrayList<>(Arrays.asList(new CountValue(1), new CountValue(2)));
        assertEquals(expectedIndexZero, exampleCount.get(0));
        List<CountValue> expectedIndexOne = new ArrayList<>(Arrays.asList(new CountValue(3)));
        assertEquals(expectedIndexOne, exampleCount.get(1));
    }

    @Test
    public void getValue() throws Exception {
        // get value 1
        assertEquals(1, exampleCount.getValue(0,0));
        // get value 2
        assertEquals(2, exampleCount.getValue(0,1));
        // get value 3
        assertEquals(3, exampleCount.getValue(1,0));
    }

    @Test
    public void isValueCrossedOut() throws Exception {
    }

    @Test
    public void addCount() throws Exception {
    }

    @Test
    public void isEmpty() throws Exception {
    }

    @Test
    public void isEmpty1() throws Exception {
    }

    @Test
    public void toggleCrossedOut() throws Exception {
    }

    @Test
    public void equals() throws Exception {
    }

}