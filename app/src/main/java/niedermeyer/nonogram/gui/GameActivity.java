package niedermeyer.nonogram.gui;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.HorizontalScrollView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.Arrays;

import niedermeyer.nonogram.R;
import niedermeyer.nonogram.gui.dialogs.DialogHelper;
import niedermeyer.nonogram.gui.puzzle.GameFieldDisplayer;
import niedermeyer.nonogram.gui.puzzle.PuzzleDisplayer;
import niedermeyer.nonogram.logics.NonogramConstants;
import niedermeyer.nonogram.logics.NonogramGenerator;
import niedermeyer.nonogram.persistence.GameOptionsPersistence;
import niedermeyer.nonogram.persistence.PuzzlePersistence;
import niedermeyer.nonogram.persistence.StatisticsPersistence;


/**
 * @author Elen Niedermeyer, last modified 2020-12-11
 */
public class GameActivity extends AppCompatActivity {

    private static final int ZOOM_STEP = 10;

    private final PuzzleDisplayer puzzleDisplayer = new PuzzleDisplayer(this);

    /**
     * Persistences
     */
    private PuzzlePersistence persistence;
    private GameOptionsPersistence gameOptions;
    private StatisticsPersistence statistics;

    private NonogramGenerator nonogramGenerator;
    private int[][] currentUserField;

    private final Toolbar.OnMenuItemClickListener toolbarMenuClickListener = new Toolbar.OnMenuItemClickListener() {

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            DialogHelper dialogHelper = new DialogHelper();

            // switch on the item id
            switch (item.getItemId()) {
                case R.id.toolbar_game_new_puzzle:
                    // start a new puzzle
                    nonogramGenerator.makeNewGame(gameOptions.getNumberOfRows(), gameOptions.getNumberOfColumns());
                    setNewUserField();
                    startGame();
                    return true;

                case R.id.toolbar_game_reset_puzzle:
                    // reset the current puzzle
                    setNewUserField();
                    startGame();
                    return true;

                case R.id.toolbar_game_zoom_in:
                    zoomGameField(ZOOM_STEP);
                    return true;

                case R.id.toolbar_game_zoom_out:
                    zoomGameField(-ZOOM_STEP);
                    return true;

                case R.id.toolbar_game_puzzle_size:
                    // make the field size dialog
                    dialogHelper.openFieldSizeDialog(getLayoutInflater(), new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialogInterface) {
                            nonogramGenerator.makeNewGame(gameOptions.getNumberOfRows(), gameOptions.getNumberOfColumns());
                            currentUserField = new int[gameOptions.getNumberOfRows()][gameOptions.getNumberOfColumns()];
                            startGame();
                        }
                    });
                    return true;

                case R.id.toolbar_game_tutorial:
                    // open the tutorial
                    dialogHelper.openTutorialDialogFullscreen(getSupportFragmentManager());
                    return true;

                default:
                    return false;
            }
        }
    };

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
                // save won puzzle in statistics
                statistics.saveNewScore();

                // show the won dialog
                new DialogHelper().openGameWonDialogFullscreen(((AppCompatActivity) v.getContext()).getSupportFragmentManager());

                // start new game
                nonogramGenerator.makeNewGame(gameOptions.getNumberOfRows(), gameOptions.getNumberOfColumns());
                setNewUserField();
                startGame();
            }
        }
    };

    /**
     * Overrides {@link AppCompatActivity#onCreateOptionsMenu(Menu)}.
     * Inflates the overflow menu.
     *
     * @param menu the menu to initialize
     * @return true, if the menu was created
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        getMenuInflater().inflate(R.menu.top_app_bar_game, menu);
        return true;
    }

    /**
     * Overrides {@link AppCompatActivity#onCreate(Bundle)}.
     * Sets the layout.
     * Initializes {@link #nonogramGenerator}, {@link #persistence} and {@link #gameOptions}
     *
     * @param savedInstanceState saved information about the activity given by the system
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_game);

        // sets the toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.activity_game_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        toolbar.setOnMenuItemClickListener(toolbarMenuClickListener);

        nonogramGenerator = new NonogramGenerator();

        // load the last game
        persistence = new PuzzlePersistence(this);
        gameOptions = new GameOptionsPersistence(this);
        statistics = new StatisticsPersistence(this);

        final int[][] nonogram = persistence.loadLastNonogram();
        nonogramGenerator.setNonogram(nonogram);
        currentUserField = persistence.loadLastUserField();
        startGame();

        // start the tutorial if it's the first puzzle
        if (persistence.isFirstPuzzle()) {
            new DialogHelper().openTutorialDialogFullscreen(getSupportFragmentManager());
        }
    }

    /**
     * Overrides {@link AppCompatActivity#onPause()}.
     * Saves the puzzle size and the arrays.
     * Saves the counts.
     */
    @Override
    protected void onPause() {
        super.onPause();

        saveGame();
    }

    /**
     * Starts a new game or saved game if available.
     */
    private void startGame() {
        final int[][] nonogram = nonogramGenerator.getNonogram();
        final int rowNumber = gameOptions.getNumberOfRows();
        final int columnNumber = gameOptions.getNumberOfColumns();

        if (nonogram == null ||
                nonogram.length != rowNumber || nonogram[0].length != columnNumber ||
                nonogram.length != currentUserField.length || nonogram[0].length != currentUserField[0].length) {
            // start new game if the size was changed or is not identical with the one of the current field
            nonogramGenerator.makeNewGame(rowNumber, columnNumber);
            setNewUserField();
        }

        HorizontalScrollView view = findViewById(R.id.activity_game_scroll_horizontal);
        view.removeAllViews();
        view.addView(puzzleDisplayer.getGameView(currentUserField, nonogramGenerator.getRowCount(), nonogramGenerator.getColumnCount(), gameOptions.getCellSize(), onFieldClick));
    }

    private void setNewUserField() {
        currentUserField = new int[gameOptions.getNumberOfRows()][gameOptions.getNumberOfColumns()];
        for (int[] array : currentUserField) {
            Arrays.fill(array, NonogramConstants.FIELD_NO_DECISION);
        }
    }

    /**
     * Saves the current game.
     */
    private void saveGame() {
        persistence.saveNonogram(nonogramGenerator.getNonogram());
        persistence.saveCurrentField(currentUserField);
    }

    /**
     * Reloads the game with new cell size.
     *
     * @param sizeDelta the delta for cell size
     */
    private void zoomGameField(int sizeDelta) {
        saveGame();
        gameOptions.setCellSize(gameOptions.getCellSize() + sizeDelta);
        startGame();
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