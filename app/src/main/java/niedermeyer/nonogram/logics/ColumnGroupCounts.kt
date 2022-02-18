package niedermeyer.nonogram.logics

class ColumnGroupCounts(nonogram: Nonogram) : GroupCounts() {

    init {
        for (columnCount in 0 until nonogram.columnNumber) {
            var newGroupCount = 0

            for (i in columnCount until nonogram.cells.size step nonogram.rowNumber) {
                val cellValue = nonogram.cells[i].value
                if (cellValue == NonogramConstants.FIELD_FILLED) {
                    // here's a group of filled fields
                    // add 1 for each field to the counter
                    newGroupCount++
                }
                if (cellValue == NonogramConstants.FIELD_EMPTY && newGroupCount != 0) {
                    // here's the end of one group
                    // add the number and resets the counter
                    addValueToList(columnCount, newGroupCount)
                    newGroupCount = 0
                }
            }

            // row completed
            if (newGroupCount != 0) {
                // add the last counter if there is one
                addValueToList(columnCount, newGroupCount)
            }
            if (!existsList(columnCount)) {
                // if the list is empty, there are no filled fields
                // add 0
                addValueToList(columnCount, 0)
            }
        }
    }

}