package niedermeyer.nonogram.logics;

/**
 * @author Elen Niedermeyer, last modified 2017-11-04
 */
public class CountValue {

    private int value;
    private boolean isCrossedOut;

    /**
     * Constructor.
     * Sets {@link #isCrossedOut} to false.
     *
     * @param pValue the count's value
     */
    public CountValue(int pValue) {
        value = pValue;
        isCrossedOut = false;
    }

    /**
     * Constructor.
     * Initializes {@link #value} and {@link #isCrossedOut}.
     *
     * @param pValue        the count's value
     * @param pIsCrossedOut boolean saying if the value is crossed out
     */
    public CountValue(int pValue, boolean pIsCrossedOut) {
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

    /**
     * Overrides {@link Object#equals(Object)}.
     *
     * @param pOther an object to compare to this one
     * @return a boolean, true if the {@link CountValue}s are equal.
     */
    @Override
    public boolean equals(Object pOther) {
        if (pOther instanceof CountValue) {
            CountValue other = (CountValue) pOther;
            return this.value == other.value && this.isCrossedOut == other.isCrossedOut;
        }
        return false;
    }

}
