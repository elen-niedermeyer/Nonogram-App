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

    /**
     * Initializes the example before executing a test.
     */
    @Before
    public void initializeExampleCount() {
        exampleCount = new FilledFieldsCount();
        // outer list with index 0
        exampleCount.addCount(0, 1);
        exampleCount.addCount(0, 2);
        // outer list with index 1
        exampleCount.addCount(1, 3);
    }

    /**
     * Test for {@link FilledFieldsCount#get(int)}.
     *
     * @throws Exception if the test failed
     */
    @Test
    public void get() throws Exception {
        // expected list on index 0
        List<CountValue> expectedIndexZero = new ArrayList<>(Arrays.asList(new CountValue(1), new CountValue(2)));
        assertEquals(expectedIndexZero, exampleCount.get(0));
        // expected list on index 1
        List<CountValue> expectedIndexOne = new ArrayList<>(Arrays.asList(new CountValue(3)));
        assertEquals(expectedIndexOne, exampleCount.get(1));
    }

    /**
     * Test for {@link FilledFieldsCount#getValue(int, int)}.
     *
     * @throws Exception if the test failed
     */
    @Test
    public void getValue() throws Exception {
        // get value 1
        assertEquals(1, exampleCount.getValue(0, 0));
        // get value 2
        assertEquals(2, exampleCount.getValue(0, 1));
        // get value 3
        assertEquals(3, exampleCount.getValue(1, 0));
    }

    /**
     * Test for {@link FilledFieldsCount#isValueCrossedOut(int, int)}.
     *
     * @throws Exception if the test failed
     */
    @Test
    public void isValueCrossedOut() throws Exception {
        // should not be crossed out
        assertEquals(false, exampleCount.isValueCrossedOut(0, 0));
        // toggle
        exampleCount.toggleCrossedOut(0, 0);
        // not it should be crossed out
        assertEquals(true, exampleCount.isValueCrossedOut(0, 0));
    }

    /**
     * Test for {@link FilledFieldsCount#addCount(int, int)}.
     *
     * @throws Exception if the test failed
     */
    @Test
    public void addCount() throws Exception {
        // add value 4 on index 1
        // it should be on index 1,1
        exampleCount.addCount(1, 4);
        assertEquals(4, exampleCount.getValue(1, 1));

        // add value 5 on index 2
        // it should be on index 2,0
        exampleCount.addCount(2, 5);
        assertEquals(5, exampleCount.getValue(2, 0));
    }

    /**
     * Test for {@link FilledFieldsCount#isEmpty()}.
     *
     * @throws Exception if the test failed
     */
    @Test
    public void isEmpty() throws Exception {
        // the example shouldn't be empty
        assertEquals(false, exampleCount.isEmpty());

        // a new count should be empty
        assertEquals(true, new FilledFieldsCount().isEmpty());
    }

    /**
     * Test for {@link FilledFieldsCount#isEmpty(int)}.
     *
     * @throws Exception if the test failed
     */
    @Test
    public void isEmpty1() throws Exception {
        // index 0 of the example should not be empty
        assertEquals(false, exampleCount.isEmpty(0));

        // index 1 of the example should be empty
        assertEquals(true, exampleCount.isEmpty(2));
    }

    /**
     * Test for {@link FilledFieldsCount#toggleCrossedOut(int, int)}.
     *
     * @throws Exception if the test failed
     */
    @Test
    public void toggleCrossedOut() throws Exception {
        boolean before = exampleCount.isValueCrossedOut(0, 0);
        // toggle
        exampleCount.toggleCrossedOut(0, 0);
        assertEquals(!before, exampleCount.isValueCrossedOut(0, 0));
        // toggle back
        exampleCount.toggleCrossedOut(0, 0);
        assertEquals(before, exampleCount.isValueCrossedOut(0, 0));
    }

    /**
     * Test for {@link FilledFieldsCount#equals(Object)}.
     *
     * @throws Exception if the test failed
     */
    @Test
    public void equals() throws Exception {
        // compare to null
        assertEquals(false, exampleCount.equals(null));

        // compare to any object
        assertEquals(false, exampleCount.equals(new Object()));

        // compare to empty count
        assertEquals(false, exampleCount.equals(new FilledFieldsCount()));

        // copy the example
        FilledFieldsCount exampleCountCopy = new FilledFieldsCount();
        exampleCountCopy.addCount(0, exampleCount.getValue(0, 0));
        exampleCountCopy.addCount(0, exampleCount.getValue(0, 1));
        exampleCountCopy.addCount(1, exampleCount.getValue(1, 0));
        // compare the example and its copy
        assertEquals(true, exampleCount.equals(exampleCountCopy));

        // change one value of the count
        exampleCountCopy.toggleCrossedOut(0, 0);
        assertEquals(false, exampleCount.equals(exampleCountCopy));

        // toggle back
        // add one more value to the count
        exampleCountCopy.toggleCrossedOut(0, 0);
        exampleCountCopy.addCount(1, 4);
        assertEquals(false, exampleCount.equals(exampleCountCopy));


    }

}