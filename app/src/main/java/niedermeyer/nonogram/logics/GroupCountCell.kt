package niedermeyer.nonogram.logics

import java.io.Serializable

class GroupCountCell(val value: Int, var isCrossedOut: Boolean = false) : Serializable {

    override fun equals(other: Any?): Boolean {
        if (other is GroupCountCell) {
            return value == other.value && isCrossedOut == other.isCrossedOut
        }
        return false
    }

    override fun hashCode(): Int {
        var result = value
        result = 31 * result + isCrossedOut.hashCode()
        return result
    }

    fun toggleCrossedOut() {
        isCrossedOut = !isCrossedOut
    }

}