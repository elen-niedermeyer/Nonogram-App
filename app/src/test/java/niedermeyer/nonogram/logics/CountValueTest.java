package niedermeyer.nonogram.logics;

import org.junit.Assert;
import org.junit.Test;

public class CountValueTest {

    @Test
    public void testGetValue() {
        CountValue currentValue = new CountValue(2);
        Assert.assertEquals(2, currentValue.getValue());
    }

    @Test
    public void testIsCrossedOut() {
        CountValue currentValue = new CountValue(2);
        Assert.assertFalse(currentValue.isCrossedOut());

        currentValue = new CountValue(2, false);
        Assert.assertFalse(currentValue.isCrossedOut());

        currentValue = new CountValue(2, true);
        Assert.assertTrue(currentValue.isCrossedOut());
    }

    @Test
    public void testSetCrossedOut() {
        CountValue currentValue = new CountValue(2);

        currentValue.setCrossedOut(false);
        Assert.assertFalse(currentValue.isCrossedOut());

        currentValue.setCrossedOut(false);
        Assert.assertFalse(currentValue.isCrossedOut());
    }

    @Test
    public void testEquals() {
        CountValue currentValue = new CountValue(2);
        Assert.assertNotEquals(currentValue, 3);
        CountValue otherValue = new CountValue(2);
        Assert.assertEquals(currentValue, otherValue);

        otherValue.setCrossedOut(true);
        Assert.assertNotEquals(currentValue, otherValue);

        currentValue.setCrossedOut(true);
        Assert.assertEquals(currentValue, otherValue);

        otherValue = new CountValue(3);
        Assert.assertNotEquals(currentValue, otherValue);
    }
}