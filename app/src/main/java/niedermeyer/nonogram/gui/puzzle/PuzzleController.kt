package niedermeyer.nonogram.gui.puzzle

import android.content.Context
import android.view.View
import niedermeyer.nonogram.gui.observer.PuzzleSolvedObserver
import niedermeyer.nonogram.logics.Nonogram
import niedermeyer.nonogram.persistence.puzzle.NonogramPersistence
import niedermeyer.nonogram.persistence.puzzle.PuzzlePersistence

class PuzzleController(context: Context) {
    val isFirstPuzzle: Boolean
        get() = puzzlePersistence.isFirstPuzzle

    val onFieldClick = View.OnClickListener { v ->
        val cellView = v as GameFieldCellView
        val id = cellView.viewId

        val cell = nonogram!!.cells[id]
        cell.nextUserValue()
        cellView.updateBackground(cell.userValue)

        // check if the nonogram is solved now
        if (nonogram!!.isSolved()) {
            for (observer in puzzleSolvedObservers) {
                observer.callback()
            }
        }
    }

    private val nonogramPersistence: NonogramPersistence = NonogramPersistence(context)
    var nonogram: Nonogram? = nonogramPersistence.loadPuzzle()

    private val puzzlePersistence: PuzzlePersistence = PuzzlePersistence(context)
    private val puzzleSolvedObservers = ArrayList<PuzzleSolvedObserver>()

    fun addPuzzleSolvedObserver(observer: PuzzleSolvedObserver) {
        puzzleSolvedObservers.add(observer)
    }

    fun startGame(rows: Int, columns: Int) {
        if (nonogram == null || nonogram!!.rowNumber != rows || nonogram!!.columnNumber != columns) {
            // start new game if the size was changed or is not identical with the one of the current field
            nonogram = Nonogram(rows, columns)
        }
    }

    fun newGame(rows: Int, columns: Int) {
        nonogram = Nonogram(rows, columns)
    }

    fun resetGame() {
        nonogram!!.reset()
    }

    fun saveGame() {
        nonogramPersistence.savePuzzle(nonogram!!)
    }

}