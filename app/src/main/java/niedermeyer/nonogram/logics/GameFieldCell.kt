package niedermeyer.nonogram.logics

import java.io.Serializable

class GameFieldCell(val value: Int, var userValue: Int = NonogramConstants.FIELD_NO_DECISION) :
    Serializable {

    fun nextUserValue() {
        when (userValue) {
            NonogramConstants.FIELD_NO_DECISION -> userValue =
                NonogramConstants.FIELD_FILLED
            NonogramConstants.FIELD_FILLED -> userValue =
                NonogramConstants.FIELD_EMPTY
            NonogramConstants.FIELD_EMPTY -> userValue =
                NonogramConstants.FIELD_NO_DECISION
        }
    }

    fun isCorrectSolution(): Boolean {
        return value == userValue || (value == NonogramConstants.FIELD_EMPTY && userValue == NonogramConstants.FIELD_NO_DECISION)
    }

}