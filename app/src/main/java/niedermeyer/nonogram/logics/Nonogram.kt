package niedermeyer.nonogram.logics

import java.io.Serializable
import java.util.*

class Nonogram(var rowNumber: Int, var columnNumber: Int) : Serializable {
    var cells: MutableList<GameFieldCell> = mutableListOf()
    var rowCounts: GroupCount? = null
    var columnCounts: GroupCount? = null

    private val random = Random()

    init {
        do {
            cells = mutableListOf()
            // fill the array with random values
            for (i in 0 until rowNumber * columnNumber) {
                cells.add(GameFieldCell(random.nextInt(2)))
            }
        } while (!cells.any { cell -> cell.value == NonogramConstants.FIELD_FILLED })

        rowCounts = createRowCount()
        columnCounts = createColumnCount()
    }

    fun reset() {
        cells.forEach { cell -> cell.userValue = NonogramConstants.FIELD_NO_DECISION }
    }

    fun isSolved(): Boolean {
        return !cells.any { cell -> !cell.isCorrectSolution() }
    }

    private fun createRowCount(): GroupCount {
        val newRowCount = GroupCount()

        var rowCount = 0
        var newGroupCount = 0
        for (i in cells.indices) {
            val cellValue = cells[i].value
            if (cellValue == NonogramConstants.FIELD_FILLED) {
                // here's a group of filled fields
                // add 1 for each field to the counter
                newGroupCount++
            }
            if (cellValue == NonogramConstants.FIELD_EMPTY && newGroupCount != 0) {
                // here's the end of one group
                // add the number and resets the counter
                newRowCount.addValueToList(rowCount, newGroupCount)
                newGroupCount = 0
            }

            if ((i + 1) % rowNumber == 0) {
                // row completed
                if (newGroupCount != 0) {
                    // add the last counter if there is one
                    newRowCount.addValueToList(rowCount, newGroupCount)
                }
                if (!newRowCount.existsList(rowCount)) {
                    // if the list is empty, there are no filled fields
                    // add 0
                    newRowCount.addValueToList(rowCount, 0)
                }
                rowCount++
                newGroupCount = 0
            }
        }

        return newRowCount
    }

    private fun createColumnCount(): GroupCount {
        val newColumnCount = GroupCount()

        for (columnCount in 0 until columnNumber) {
            var newGroupCount = 0

            for (i in columnCount until cells.size step rowNumber) {
                val cellValue = cells[i].value
                if (cellValue == NonogramConstants.FIELD_FILLED) {
                    // here's a group of filled fields
                    // add 1 for each field to the counter
                    newGroupCount++
                }
                if (cellValue == NonogramConstants.FIELD_EMPTY && newGroupCount != 0) {
                    // here's the end of one group
                    // add the number and resets the counter
                    newColumnCount.addValueToList(columnCount, newGroupCount)
                    newGroupCount = 0
                }
            }

            // row completed
            if (newGroupCount != 0) {
                // add the last counter if there is one
                newColumnCount.addValueToList(columnCount, newGroupCount)
            }
            if (!newColumnCount.existsList(columnCount)) {
                // if the list is empty, there are no filled fields
                // add 0
                newColumnCount.addValueToList(columnCount, 0)
            }
        }

        return newColumnCount
    }

}