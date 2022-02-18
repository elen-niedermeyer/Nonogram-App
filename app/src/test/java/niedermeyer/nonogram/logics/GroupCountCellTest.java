package niedermeyer.nonogram.logics;

import org.junit.Assert;
import org.junit.Test;

public class GroupCountCellTest {

    @Test
    public void testGetValue() {
        GroupCountCell currentValue = new GroupCountCell(2, false);
        Assert.assertEquals(2, currentValue.getValue());
    }

    @Test
    public void testIsCrossedOut() {
        GroupCountCell currentValue = new GroupCountCell(2, false);
        Assert.assertFalse(currentValue.isCrossedOut());

        currentValue = new GroupCountCell(2, false);
        Assert.assertFalse(currentValue.isCrossedOut());

        currentValue = new GroupCountCell(2, true);
        Assert.assertTrue(currentValue.isCrossedOut());
    }

    @Test
    public void testSetCrossedOut() {
        GroupCountCell currentValue = new GroupCountCell(2, false);

        currentValue.setCrossedOut(false);
        Assert.assertFalse(currentValue.isCrossedOut());

        currentValue.setCrossedOut(false);
        Assert.assertFalse(currentValue.isCrossedOut());
    }

    @Test
    public void testEquals() {
        GroupCountCell currentValue = new GroupCountCell(2, false);
        Assert.assertNotEquals(currentValue, 3);
        GroupCountCell otherValue = new GroupCountCell(2, false);
        Assert.assertEquals(currentValue, otherValue);

        otherValue.setCrossedOut(true);
        Assert.assertNotEquals(currentValue, otherValue);

        currentValue.setCrossedOut(true);
        Assert.assertEquals(currentValue, otherValue);

        otherValue = new GroupCountCell(3, false);
        Assert.assertNotEquals(currentValue, otherValue);
    }
}