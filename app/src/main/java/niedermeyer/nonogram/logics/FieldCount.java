package niedermeyer.nonogram.logics;

/**
 * @author Elen Niedermeyer, last modified 2017-11-04
 */
public class FieldCount {

    private int value;
    private boolean isCrossedOut;

    /**
     * Constructor.
     * Initializes {@link #value} and {@link #isCrossedOut}.
     *
     * @param pValue        the count's value
     * @param pIsCrossedOut boolean saying if the value is crossed out
     */
    public FieldCount(int pValue, boolean pIsCrossedOut) {
        value = pValue;
        isCrossedOut = pIsCrossedOut;
    }

    /**
     * Getter for {@link #value}.
     *
     * @return {@link #value}
     */
    public int getValue() {
        return value;
    }

    /**
     * Getter for {@link #isCrossedOut}.
     *
     * @return {@link #isCrossedOut}
     */
    public boolean getIsCrossedOut() {
        return isCrossedOut;
    }

    /**
     * Setter for {@link #isCrossedOut}.
     *
     * @param pIsCrossedOut boolean saying if the value is crossed out
     */
    public void setIsCrossedOut(boolean pIsCrossedOut) {
        isCrossedOut = pIsCrossedOut;
    }
}
