package niedermeyer.nonogram.gui;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import niedermeyer.nonogram.R;
import niedermeyer.nonogram.persistence.CountFilledFieldsPersistence;
import niedermeyer.nonogram.persistence.PuzzlePersistence;
import niedermeyer.nonogram.persistence.PuzzleSizePersistence;

/**
 * @author Elen Niedermeyer, last updated 2020-10-13
 */
public class GameActivity extends AppCompatActivity {

    private PuzzleDisplayer puzzleDisplayer = new PuzzleDisplayer(this);

    private NumberPickerDialog numberPickerDialog = new NumberPickerDialog(this);

    /**
     * Persistences
     */
    private PuzzlePersistence persistence;
    private PuzzleSizePersistence puzzleSize;
    private CountFilledFieldsPersistence countsPersistence;

    private Toolbar.OnMenuItemClickListener toolbarMenuClickListener = new Toolbar.OnMenuItemClickListener() {

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            final MenuItem clickedItem = item;

            // switch on the item id
            switch (item.getItemId()) {
                case R.id.toolbar_game_new_puzzle:
                    // start a new puzzle
                    puzzleDisplayer.displayNewGame();
                    return true;

                case R.id.toolbar_game_reset_puzzle:
                    // reset the current puzzle
                    puzzleDisplayer.resetGame();
                    return true;

                case R.id.toolbar_game_puzzle_rows:
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

                case R.id.toolbar_game_puzzle_columns:
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

                case R.id.toolbar_game_tutorial:
                    // open the tutorial activity
                    //Intent i = new Intent(GameActivity.this, InstructionActivity.class);
                    //startActivity(i);

                default:
                    return false;
            }
        }
    };

    /**
     * Getter for the {@link PuzzleDisplayer}.
     *
     * @return {@link #puzzleDisplayer}
     */
    public PuzzleDisplayer getGameHandler() {
        return this.puzzleDisplayer;
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

        getMenuInflater().inflate(R.menu.top_app_bar_game, menu);

        // sets the text of the item that shows how much rows the puzzle has
        MenuItem rowsNumber = menu.findItem(R.id.toolbar_game_puzzle_rows);
        rowsNumber.setTitle(String.format(getString(R.string.number_of_rows_text), PuzzleSizePersistence.numberOfRows));

        // sets the text of the item that shows how much columns the puzzle has
        MenuItem columnsNumber = menu.findItem(R.id.toolbar_game_puzzle_columns);
        columnsNumber.setTitle(String.format(getString(R.string.number_of_columns_text), PuzzleSizePersistence.numberOfColumns));

        return true;
    }

    /**
     * Overrides {@link AppCompatActivity#onCreate(Bundle)}.
     * Sets the layout.
     * Initializes {@link #persistence}, {@link #puzzleSize} and {@link #countsPersistence}.
     *
     * @param savedInstanceState saved information about the activity given by the system
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_game);

        persistence = new PuzzlePersistence(this);
        puzzleSize = new PuzzleSizePersistence(this);
        countsPersistence = new CountFilledFieldsPersistence(this);

        // start the instruction activity if it's the first puzzle
        //if (persistence.isFirstPuzzle()) {
         //   startActivity(new Intent(this, InstructionActivity.class));

       // }

        // sets the toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.activity_game_toolbar);
        setSupportActionBar(toolbar);
        updateToolbarTitle();
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        toolbar.setOnMenuItemClickListener(toolbarMenuClickListener);

        // load the last game
        // start a new game
        int[][] nonogram = persistence.loadLastNonogram();
        int[][] currentField = persistence.loadLastUserField();
        if (nonogram != null && nonogram.length == PuzzleSizePersistence.numberOfRows && nonogram[0].length == PuzzleSizePersistence.numberOfColumns) {
            // start puzzle with loaded arrays if the size haven't changed
            puzzleDisplayer.displayNewGame(nonogram, currentField, countsPersistence.loadCountsColumns(), countsPersistence.loadCountsRows());
        } else {
            // start new game if the size was changed
            puzzleDisplayer.displayNewGame();
        }

        FragmentManager man = getSupportFragmentManager();
        TutorialDialogFragment frag = new TutorialDialogFragment();
        FragmentTransaction transaction = man.beginTransaction();
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.add(android.R.id.content, frag)
                .addToBackStack(null).commit();
    }

    /**
     * Overrides {@link AppCompatActivity#onPause()}.
     * Saves the puzzle size and the arrays.
     * Saves the counts.
     */
    @Override
    protected void onPause() {
        super.onPause();

        puzzleSize.savePuzzleSize();
        persistence.saveNonogram(puzzleDisplayer.getNonogram());
        persistence.saveCurrentField(puzzleDisplayer.getUsersCurrentField());
        countsPersistence.saveCountFilledFields(puzzleDisplayer.getColumnCounts(), true);
        countsPersistence.saveCountFilledFields(puzzleDisplayer.getRowCounts(), false);
    }

    /**
     * Updates the title of the toolbar.
     * It's necessary if the puzzle size was updated.
     */
    private void updateToolbarTitle() {
        String title = String.format(getString(R.string.game_activity), PuzzleSizePersistence.numberOfColumns, PuzzleSizePersistence.numberOfRows);
        getSupportActionBar().setTitle(title);
    }

}