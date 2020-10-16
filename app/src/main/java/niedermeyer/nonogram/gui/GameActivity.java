package niedermeyer.nonogram.gui;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import niedermeyer.nonogram.R;
import niedermeyer.nonogram.persistence.CountFilledFieldsPersistence;
import niedermeyer.nonogram.persistence.PuzzlePersistence;
import niedermeyer.nonogram.persistence.PuzzleSizePersistence;

/**
 * @author Elen Niedermeyer, last updated 2020-10-15
 */
public class GameActivity extends AppCompatActivity {

    private PuzzleDisplayer puzzleDisplayer = new PuzzleDisplayer(this);

    /**
     * Persistences
     */
    private PuzzlePersistence persistence;
    private PuzzleSizePersistence puzzleSize;
    private CountFilledFieldsPersistence countsPersistence;

    private Toolbar.OnMenuItemClickListener toolbarMenuClickListener = new Toolbar.OnMenuItemClickListener() {

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            DialogHelper dialogHelper = new DialogHelper();

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

                case R.id.toolbar_game_puzzle_size:
                    // make the field size dialog
                    dialogHelper.openFieldSizeDialog(getLayoutInflater(), new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialogInterface) {
                            puzzleDisplayer.displayNewGame();
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
     * Getter for the {@link PuzzleDisplayer}.
     *
     * @return {@link #puzzleDisplayer}
     */
    public PuzzleDisplayer getGameHandler() {
        return this.puzzleDisplayer;
    }

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
     * Initializes {@link #persistence}, {@link #puzzleSize} and {@link #countsPersistence}.
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

        // load the last game
        persistence = new PuzzlePersistence(this);
        puzzleSize = new PuzzleSizePersistence(this);
        countsPersistence = new CountFilledFieldsPersistence(this);
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

        puzzleSize.savePuzzleSize();
        persistence.saveNonogram(puzzleDisplayer.getNonogram());
        persistence.saveCurrentField(puzzleDisplayer.getUsersCurrentField());
        countsPersistence.saveCountFilledFields(puzzleDisplayer.getColumnCounts(), true);
        countsPersistence.saveCountFilledFields(puzzleDisplayer.getRowCounts(), false);
    }

}