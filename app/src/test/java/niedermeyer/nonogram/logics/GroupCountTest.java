package niedermeyer.nonogram.logics;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;


public class GroupCountTest {

    @Test
    public void testGetCounts() {
        GroupCount currentCount = new GroupCount();
        ArrayList<ArrayList<GroupCountCell>> expectedCount = new ArrayList<>();
        Assert.assertEquals(expectedCount, currentCount.getCounts());

        currentCount.addValueToList(0, 2);
        ArrayList<GroupCountCell> list = new ArrayList<GroupCountCell>();
        list.add(new GroupCountCell(2, false));
        expectedCount.add(list);
        Assert.assertEquals(expectedCount, currentCount.getCounts());
    }

    @Test()
    public void testGetList() {
        GroupCount currentCount = new GroupCount();
        currentCount.addValueToList(0, 2);

        ArrayList<GroupCountCell> expectedList = new ArrayList<GroupCountCell>();
        expectedList.add(new GroupCountCell(2, false));
        Assert.assertEquals(expectedList, currentCount.getList(0));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetListWithException() {
        GroupCount currentCount = new GroupCount();
        currentCount.addValueToList(0, 2);
        currentCount.getList(1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetListWithEmptyCount() {
        GroupCount currentCount = new GroupCount();
        currentCount.getList(0);
    }

    @Test
    public void testGetValue() {
        GroupCount currentCount = new GroupCount();
        currentCount.addValueToList(0, 2);
        currentCount.addValueToList(0, 1);

        Assert.assertEquals(1, currentCount.getValue(0, 1));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetValueWithExceptionOuterIndex() {
        GroupCount currentCount = new GroupCount();
        currentCount.addValueToList(0, 2);
        currentCount.getValue(1, 0);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetValueWithExceptionInnerIndex() {
        GroupCount currentCount = new GroupCount();
        currentCount.addValueToList(0, 2);
        currentCount.getValue(0, 1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetValueWithEmptyCount() {
        GroupCount currentCount = new GroupCount();
        currentCount.getValue(0, 0);
    }

    @Test
    public void testIsValueCrossedOut() {
        GroupCount currentCount = new GroupCount();
        currentCount.addValueToList(0, 2);
        Assert.assertFalse(currentCount.isValueCrossedOut(0, 0));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testIsValueCrossedOutWithExceptionInnerIndex() {
        GroupCount currentCount = new GroupCount();
        currentCount.addValueToList(0, 2);
        currentCount.isValueCrossedOut(0, 1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testIsValueCrossedOutWithExceptionOuterIndex() {
        GroupCount currentCount = new GroupCount();
        currentCount.addValueToList(0, 2);
        currentCount.isValueCrossedOut(1, 0);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testIsValueCrossedOutWithEmptyCount() {
        GroupCount currentCount = new GroupCount();
        currentCount.isValueCrossedOut(0, 0);
    }

    @Test
    public void testAddValueToList() {
        GroupCount currentCount = new GroupCount();

        currentCount.addValueToList(0, 1);
        ArrayList<GroupCountCell> currentList = currentCount.getList(0);
        Assert.assertEquals(1, currentList.size());
        Assert.assertEquals(1, currentList.get(currentList.size() - 1).getValue());

        currentCount.addValueToList(0, 3);
        currentList = currentCount.getList(0);
        Assert.assertEquals(2, currentList.size());
        Assert.assertEquals(3, currentList.get(currentList.size() - 1).getValue());
    }

    @Test
    public void testIsEmpty() {
        GroupCount currentCount = new GroupCount();
        Assert.assertTrue(currentCount.isEmpty());

        currentCount.addValueToList(0, 0);
        Assert.assertTrue(currentCount.isEmpty());

        currentCount.addValueToList(1, 1);
        Assert.assertFalse(currentCount.isEmpty());
    }

    @Test
    public void testExistsList() {
        GroupCount currentCount = new GroupCount();
        Assert.assertFalse(currentCount.existsList(0));
        Assert.assertFalse(currentCount.existsList(1));

        currentCount.addValueToList(0, 1);
        Assert.assertTrue(currentCount.existsList(0));
        Assert.assertFalse(currentCount.existsList(1));
    }

    @Test
    public void testToggleCrossedOut() {
        GroupCount currentCount = new GroupCount();
        currentCount.addValueToList(0, 2);
        Assert.assertFalse(currentCount.isValueCrossedOut(0, 0));

        currentCount.toggleCrossedOut(0, 0);
        Assert.assertTrue(currentCount.isValueCrossedOut(0, 0));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testToggleCrossedOutWithExceptionInnerIndex() {
        GroupCount currentCount = new GroupCount();
        currentCount.addValueToList(0, 2);

        currentCount.toggleCrossedOut(0, 1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testToggleCrossedOutWithExceptionOuterIndex() {
        GroupCount currentCount = new GroupCount();
        currentCount.addValueToList(0, 2);

        currentCount.toggleCrossedOut(0, 1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testToggleCrossedOutWithEmptyCount() {
        GroupCount currentCount = new GroupCount();

        currentCount.toggleCrossedOut(0, 1);
    }

    @Test
    public void testEquals() {
        GroupCount currentCount = new GroupCount();
        Assert.assertNotEquals(currentCount, new ArrayList<GroupCountCell>());
        GroupCount otherCount = new GroupCount();
        Assert.assertEquals(currentCount, otherCount);

        currentCount.addValueToList(0, 1);
        Assert.assertNotEquals(currentCount, otherCount);

        otherCount.addValueToList(0, 2);
        Assert.assertNotEquals(currentCount, otherCount);

        otherCount = new GroupCount();
        otherCount.addValueToList(0, 1);
        Assert.assertEquals(currentCount, otherCount);

        otherCount.addValueToList(0, 1);
        Assert.assertNotEquals(currentCount, otherCount);

        otherCount = new GroupCount();
        otherCount.addValueToList(0, 1);
        otherCount.addValueToList(1, 1);
        Assert.assertNotEquals(currentCount, otherCount);

        otherCount = new GroupCount();
        otherCount.addValueToList(0, 1);
        otherCount.toggleCrossedOut(0, 0);
        Assert.assertNotEquals(currentCount, otherCount);
    }
}