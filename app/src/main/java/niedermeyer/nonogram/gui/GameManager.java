package niedermeyer.nonogram.gui;

import android.content.Context;
import android.view.View;

import java.util.ArrayList;
import java.util.Arrays;

import niedermeyer.nonogram.gui.puzzle.GameFieldDisplayer;
import niedermeyer.nonogram.logics.GroupCount;
import niedermeyer.nonogram.logics.NonogramConstants;
import niedermeyer.nonogram.logics.NonogramGenerator;
import niedermeyer.nonogram.persistence.PuzzlePersistence;

public class GameManager {

    private final NonogramGenerator nonogramGenerator = new NonogramGenerator();

    /**
     * Listener for the buttons on the puzzle.
     */
    private final View.OnClickListener onFieldClick = new View.OnClickListener() {
        /**
         * Overrides {@link View.OnClickListener#onClick(View)}.
         * Changes the background of the clicked field in the puzzle:
         * If the field was {@link NonogramConstants#FIELD_NO_DECISION} it becomes {@link NonogramConstants#FIELD_FILLED}.
         * If it was {@link NonogramConstants#FIELD_FILLED} it becomes {@link NonogramConstants#FIELD_EMPTY}.
         * If it was {@link NonogramConstants#FIELD_EMPTY} it becomes {@link NonogramConstants#FIELD_NO_DECISION}.
         * <p>
         * Looks if the nonogram is solved.
         *
         * @param v the clicked view, a field of the nonogram
         */
        @Override
        public void onClick(View v) {
            // get index of view
            final int id = v.getId();
            final int rowIndex = id / 100;
            final int columnIndex = id % 100;

            switch (currentUserField[rowIndex][columnIndex]) {
                case NonogramConstants.FIELD_NO_DECISION:
                    currentUserField[rowIndex][columnIndex] = NonogramConstants.FIELD_FILLED;
                    break;
                case NonogramConstants.FIELD_FILLED:
                    currentUserField[rowIndex][columnIndex] = NonogramConstants.FIELD_EMPTY;
                    break;
                case NonogramConstants.FIELD_EMPTY:
                    currentUserField[rowIndex][columnIndex] = NonogramConstants.FIELD_NO_DECISION;
                    break;
            }

            v.setBackgroundResource(GameFieldDisplayer.getFieldBackgroundResource(currentUserField[rowIndex][columnIndex]));

            // prove if the nonogram is solved now
            if (isPuzzleSolved()) {
                for (PuzzleSolvedObserver observer : puzzleSolvedObservers) {
                    observer.callback();
                }
            }
        }
    };

    private final PuzzlePersistence persistence;

    private final ArrayList<PuzzleSolvedObserver> puzzleSolvedObservers = new ArrayList<>();

    private int[][] currentUserField;


    public GameManager(Context pContext) {
        persistence = new PuzzlePersistence(pContext);

        final int[][] nonogram = persistence.loadLastNonogram();
        nonogramGenerator.setNonogram(nonogram);
        currentUserField = persistence.loadLastUserField();
    }


    public int[][] getCurrentUserField() {
        return currentUserField;
    }

    public GroupCount getRowCount() {
        return nonogramGenerator.getRowCount();
    }

    public GroupCount getColumnCount() {
        return nonogramGenerator.getColumnCount();
    }

    public View.OnClickListener getOnFieldClick() {
        return onFieldClick;
    }

    public boolean isFirstPuzzle() {
        return persistence.isFirstPuzzle();
    }

    public void addPuzzleSolvedObserver(PuzzleSolvedObserver pObserver) {
        puzzleSolvedObservers.add(pObserver);
    }

    public void startGame(int pRows, int pColumns) {
        final int[][] nonogram = nonogramGenerator.getNonogram();

        if (nonogram == null ||
                nonogram.length != pRows || nonogram[0].length != pColumns ||
                nonogram.length != currentUserField.length || nonogram[0].length != currentUserField[0].length) {
            // start new game if the size was changed or is not identical with the one of the current field
            newGame(pRows, pColumns);
        }
    }

    public void newGame(int pRows, int pColumns) {
        nonogramGenerator.makeNewGame(pRows, pColumns);
        setNewUserField(pRows, pColumns);
    }

    public void resetGame(int pRows, int pColumns) {
        setNewUserField(pRows, pColumns);
    }

    /**
     * Saves the current game.
     */
    public void saveGame() {
        persistence.saveNonogram(nonogramGenerator.getNonogram());
        persistence.saveCurrentField(currentUserField);
    }

    private void setNewUserField(int pRows, int pColumns) {
        currentUserField = new int[pRows][pColumns];
        for (int[] array : currentUserField) {
            Arrays.fill(array, NonogramConstants.FIELD_NO_DECISION);
        }
    }

    /**
     * Proofs if the puzzle is solved by comparing the {@link #nonogramGenerator#getNonogram()} with the {@link #currentUserField}.
     *
     * @return a boolean, true if the puzzle is solved.
     */
    private boolean isPuzzleSolved() {
        // make a copy of the field generated by the user
        // therefore fields without a decision must be seen as empty
        int[][] usersFieldCopy = new int[currentUserField.length][currentUserField[0].length];
        for (int i = 0; i < currentUserField.length; i++) {
            for (int j = 0; j < currentUserField[i].length; j++) {
                if (currentUserField[i][j] == NonogramConstants.FIELD_NO_DECISION) {
                    usersFieldCopy[i][j] = NonogramConstants.FIELD_EMPTY;
                } else {
                    usersFieldCopy[i][j] = currentUserField[i][j];
                }
            }
        }

        return (Arrays.deepEquals(usersFieldCopy, nonogramGenerator.getNonogram()));
    }

}
