package niedermeyer.nonogram.logics;

/**
 * Enumeration of states that a field of the nonogram can have
 *
 * @author Elen Niedermeyer, last update 2017-04-28
 */
public enum NonogramFields {
    /**
     * values of the enum
     */
    PROVED(1), EMPTY(0), NOTHING(-1);

    private final int value;

    private NonogramFields(int value) {
        this.value = value;
    }

    /**
     * Method to get the values.
     *
     * @return the selected value
     */
    public int getValue() {
        return value;
    }
}
