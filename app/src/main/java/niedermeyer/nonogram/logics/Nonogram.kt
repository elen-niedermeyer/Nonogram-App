package niedermeyer.nonogram.logics

import android.util.Log
import java.io.Serializable
import java.util.*

class Nonogram(var rowNumber: Int, var columnNumber: Int) : Serializable {
    var cells: MutableList<GameFieldCell> = mutableListOf()
    var rowCounts: GroupCounts? = null
    var columnCounts: GroupCounts? = null

    private val random = Random()

    init {
        do {
            cells = mutableListOf()
            // fill the array with random values
            for (i in 0 until rowNumber * columnNumber) {
                cells.add(GameFieldCell(random.nextInt(2)))
            }
        } while (!cells.any { cell -> cell.value == NonogramConstants.FIELD_FILLED })

        rowCounts = RowGroupCounts(this)
        columnCounts = ColumnGroupCounts(this)
    }

    fun reset() {
        cells.forEach { cell -> cell.userValue = NonogramConstants.FIELD_NO_DECISION }
    }

    fun isSolved(): Boolean {
        return !cells.any { cell -> !cell.isCorrectSolution() }
    }

}