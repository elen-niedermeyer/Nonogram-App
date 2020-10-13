package niedermeyer.nonogram.logics;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;

/**
 * @author Elen Niedermeyer
 *         Last modified 2017-02-08
 */
public class CountValueTest {

    /**
     * Test for {@link CountValue#equals(Object)}.
     *
     * @throws Exception if the test failed
     */
    @Test
    public void equals() throws Exception {
        CountValue countValueOne = new CountValue(2, true);
        CountValue countValueTwo = new CountValue(2, false);
        CountValue countValueThree = new CountValue(1, true);
        CountValue countValueFour = new CountValue(1, false);

        // compare all to the first field count
        assertEquals(true, countValueOne.equals(countValueOne));
        assertEquals(false, countValueOne.equals(countValueTwo));
        assertEquals(false, countValueOne.equals(countValueThree));
        assertEquals(false, countValueOne.equals(countValueFour));

        // compare all to the second field count
        assertEquals(false, countValueTwo.equals(countValueOne));
        assertEquals(true, countValueTwo.equals(countValueTwo));
        assertEquals(false, countValueTwo.equals(countValueThree));
        assertEquals(false, countValueTwo.equals(countValueFour));

        // compare all to the third field count
        assertEquals(false, countValueThree.equals(countValueOne));
        assertEquals(false, countValueThree.equals(countValueTwo));
        assertEquals(true, countValueThree.equals(countValueThree));
        assertEquals(false, countValueThree.equals(countValueFour));

        // compare all to the fourth field count
        assertEquals(false, countValueFour.equals(countValueOne));
        assertEquals(false, countValueFour.equals(countValueTwo));
        assertEquals(false, countValueFour.equals(countValueThree));
        assertEquals(true, countValueFour.equals(countValueFour));

        // compare to null
        assertEquals(false, countValueOne.equals(null));
        // compare to any object
        assertEquals(false, countValueOne.equals(new Object()));
    }

}