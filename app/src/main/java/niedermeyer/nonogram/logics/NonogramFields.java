package niedermeyer.nonogram.logics;

/**
 * @author Elen Niedermeyer, last update 2017-04-28
 */

public enum NonogramFields {
    PROVED(1), EMPTY(0), NOTHING(-1);
    private final int value;

    private NonogramFields(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
