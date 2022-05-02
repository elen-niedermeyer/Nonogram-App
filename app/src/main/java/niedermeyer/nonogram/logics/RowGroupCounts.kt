package niedermeyer.nonogram.logics

class RowGroupCounts(nonogram: Nonogram) : GroupCounts() {

    init {
        var rowCount = 0
        var newGroupCount = 0
        for (i in nonogram.cells.indices) {
            val cellValue = nonogram.cells[i].value
            if (cellValue == NonogramConstants.FIELD_FILLED) {
                // here's a group of filled fields
                // add 1 for each field to the counter
                newGroupCount++
            }
            if (cellValue == NonogramConstants.FIELD_EMPTY && newGroupCount != 0) {
                // here's the end of one group
                // add the number and resets the counter
                addValueToList(rowCount, newGroupCount)
                newGroupCount = 0
            }

            if ((i + 1) % nonogram.columnNumber == 0) {
                // row completed
                if (newGroupCount != 0) {
                    // add the last counter if there is one
                    addValueToList(rowCount, newGroupCount)
                }
                if (!existsList(rowCount)) {
                    // if the list is empty, there are no filled fields
                    // add 0
                    addValueToList(rowCount, 0)
                }
                rowCount++
                newGroupCount = 0
            }
        }
    }

}