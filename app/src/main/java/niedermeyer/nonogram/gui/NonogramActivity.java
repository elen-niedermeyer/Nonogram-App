package niedermeyer.nonogram.gui;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import niedermeyer.nonogram.R;
import niedermeyer.nonogram.persistence.PuzzlePersistence;
import niedermeyer.nonogram.persistence.PuzzleSizePersistence;

/**
 * @author Elen Niedermeyer, last updated 2017-10-08
 */
public class NonogramActivity extends AppCompatActivity {

    private PuzzleDisplayer puzzleDisplayer = new PuzzleDisplayer(this);

    private NumberPickerDialog numberPickerDialog = new NumberPickerDialog(this);

    /**
     * Persistences
     */
    private PuzzlePersistence persistence;
    private PuzzleSizePersistence puzzleSize;

    /**
     * Getter for the {@link PuzzleDisplayer}.
     *
     * @return {@link #puzzleDisplayer}
     */
    public PuzzleDisplayer getGameHandler() {
        return this.puzzleDisplayer;
    }

    /**
     * Overrides {@link AppCompatActivity#onBackPressed()}.
     * Goes always back to {@link StartActivity}, if the devices back button is pressed.
     */
    @Override
    public void onBackPressed() {
        NavUtils.navigateUpFromSameTask(this);
    }

    /**
     * Overrides {@link AppCompatActivity#onCreateOptionsMenu(Menu)}.
     * Inflates the menu. Sets the text of some items.
     *
     * @param menu the menu to initialize
     * @return true, if the menu was created
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        getMenuInflater().inflate(R.menu.menu_activity_nonogram, menu);

        // sets the text of the item that shows how much rows the puzzle has
        MenuItem rowsNumber = menu.findItem(R.id.menu_activity_nonogram_puzzle_rows);
        rowsNumber.setTitle(String.format(getString(R.string.number_of_rows_text), PuzzleSizePersistence.numberOfRows));

        // sets the text of the item that shows how much columns the puzzle has
        MenuItem columnsNumber = menu.findItem(R.id.menu_activity_nonogram_puzzle_columns);
        columnsNumber.setTitle(String.format(getString(R.string.number_of_columns_text), PuzzleSizePersistence.numberOfColumns));

        return true;
    }

    /**
     * Overrides {@link AppCompatActivity#onOptionsItemSelected(MenuItem)}.
     * Makes an actions according to which menu item was clicked.
     * Starts a new game, resets the current game, updates the number of rows or columns or shows the instruction.
     *
     * @param item the clicked menu item
     * @return true, if the action was successful
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        final MenuItem clickedItem = item;

        // switch on the item id
        switch (item.getItemId()) {
            case R.id.menu_activity_nonogram_new_puzzle:
                // start a new puzzle
                puzzleDisplayer.displayNewGame();
                return true;

            case R.id.menu_activity_nonogram_reset_puzzle:
                // reset the current puzzle
                puzzleDisplayer.resetGame();
                return true;

            case R.id.menu_activity_nonogram_puzzle_rows:
                // make the number picker dialog
                AlertDialog dialogRows = numberPickerDialog.makeDialog(true);
                final int saveNumberRows = PuzzleSizePersistence.numberOfRows;
                dialogRows.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        if (saveNumberRows != PuzzleSizePersistence.numberOfRows) {
                            // if the number of rows was changed
                            // update title of item and of the title bar
                            clickedItem.setTitle(String.format(getString(R.string.number_of_rows_text), PuzzleSizePersistence.numberOfRows));
                            updateToolbarTitle();
                            // start a new game with the new size
                            puzzleDisplayer.displayNewGame();
                        }
                    }
                });
                // show the dialog
                dialogRows.show();
                return true;

            case R.id.menu_activity_nonogram_puzzle_columns:
                // make the number picker dialog
                AlertDialog dialogColumns = numberPickerDialog.makeDialog(false);
                final int saveNumberColumns = PuzzleSizePersistence.numberOfColumns;
                dialogColumns.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        if (saveNumberColumns != PuzzleSizePersistence.numberOfColumns) {
                            // if the number of column was changed
                            // update title of item and of the tool bar
                            clickedItem.setTitle(String.format(getString(R.string.number_of_columns_text), PuzzleSizePersistence.numberOfColumns));
                            updateToolbarTitle();
                            // start a new game with the new size
                            puzzleDisplayer.displayNewGame();
                        }
                    }
                });
                // show the dialog
                dialogColumns.show();
                return true;

            case R.id.menu_activity_nonogram_instruction:
                // open the instruction activity
                Intent i = new Intent(this, InstructionActivity.class);
                startActivity(i);

            default:
                // if we got here, the user's action was not recognized
                // invoke the superclass to handle it
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Overrides {@link AppCompatActivity#onCreate(Bundle)}.
     * Sets the layout.
     * Initializes {@link #persistence} and {@link #puzzleSize}.
     * Starts {@link InstructionActivity} if it's the first puzzle.
     *
     * @param savedInstanceState saved information about the activity given by the system
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_nonogram);

        persistence = new PuzzlePersistence(this);
        puzzleSize = new PuzzleSizePersistence(this);

        // start the instruction activity if it's the first puzzle
        if (persistence.isFirstPuzzle()) {
            startActivity(new Intent(this, InstructionActivity.class));
        }

        // sets the toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.activity_nonogram_toolbar);
        setSupportActionBar(toolbar);
        updateToolbarTitle();

        // load the last game
        // start a new game
        int[][] nonogram = persistence.loadLastNonogram();
        int[][] currentField = persistence.loadLastUserField();
        if (nonogram != null && nonogram.length == PuzzleSizePersistence.numberOfRows && nonogram[0].length == PuzzleSizePersistence.numberOfColumns) {
            // start puzzle with loaded arrays if the size haven't changed
            puzzleDisplayer.displayNewGame(nonogram, currentField);
        } else {
            // start new game if the size was changed
            puzzleDisplayer.displayNewGame();
        }
    }

    /**
     * Overrides {@link AppCompatActivity#onPause()}.
     * Saves the puzzle size and the arrays.
     */
    @Override
    protected void onPause() {
        super.onPause();

        puzzleSize.savePuzzleSize();
        persistence.saveNonogram(puzzleDisplayer.getNonogram());
        persistence.saveCurrentField(puzzleDisplayer.getUsersCurrentField());
    }

    /**
     * Updates the title of the toolbar.
     * It's necessary if the puzzle size was updated.
     */
    private void updateToolbarTitle() {
        String title = String.format(getString(R.string.toolbar_title), PuzzleSizePersistence.numberOfColumns, PuzzleSizePersistence.numberOfRows);
        getSupportActionBar().setTitle(title);
    }

}