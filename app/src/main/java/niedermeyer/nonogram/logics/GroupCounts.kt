package niedermeyer.nonogram.logics

import java.io.Serializable

abstract class GroupCounts : Serializable {

    val counts: ArrayList<ArrayList<GroupCountCell>> = ArrayList()

    /**
     * Looks if the count is empty. In this case it means, there are only zeros.
     *
     * @return true, if the count is empty, false otherwise
     */
    val isEmpty: Boolean
        get() {
            // check if there is at least one field filled
            for (currentList in counts) {
                if (currentList.any { cell -> cell.value != 0 }) {
                    return false
                }
            }

            // returns true here
            // there are only zeros in the count
            return true
        }

    val maxLength: Int
        get() {
            var length = 0
            for (c in counts) {
                if (c.size > length) {
                    length = c.size
                }
            }
            return length
        }

    override fun equals(other: Any?): Boolean {
        if (other is GroupCounts) {
            return counts == other.counts
        }

        // return false
        // the given object is of another type
        return false
    }

    override fun hashCode(): Int {
        var result = counts.hashCode()
        result = 31 * result + isEmpty.hashCode()
        return result
    }

    fun get(index: Int): ArrayList<GroupCountCell> {
        return counts[index]
    }

    fun get(outerIndex: Int, innerIndex: Int): GroupCountCell {
        return counts[outerIndex][innerIndex]
    }

    /**
     * Adds a count to the end of the list at the given index in [.counts].
     *
     * @param outerIndex index of the count
     * @param value      the count's value
     */
    fun addValueToList(outerIndex: Int, value: Int) {
        try {
            // add the given count to the list
            val currentList = counts[outerIndex]
            currentList.add(GroupCountCell(value))
        } catch (e: IndexOutOfBoundsException) {
            // make a new list for this index
            // add the given count to the new list
            val newList = ArrayList<GroupCountCell>()
            newList.add(GroupCountCell(value))
            counts.add(newList)
        }
    }

    /**
     * Looks if the list with the given index exists.
     *
     * @param outerIndex the index of the list
     * @return true if the list exists, false otherwise
     */
    fun existsList(outerIndex: Int): Boolean {
        return try {
            counts[outerIndex]
            true
        } catch (e: IndexOutOfBoundsException) {
            false
        }
    }

}