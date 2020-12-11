package niedermeyer.nonogram.gui;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import niedermeyer.nonogram.R;
import niedermeyer.nonogram.gui.dialogs.DialogHelper;
import niedermeyer.nonogram.persistence.CountFilledFieldsPersistence;
import niedermeyer.nonogram.persistence.GameOptionsPersistence;
import niedermeyer.nonogram.persistence.PuzzlePersistence;

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
    private CountFilledFieldsPersistence countsPersistence;

    private final Toolbar.OnMenuItemClickListener toolbarMenuClickListener = new Toolbar.OnMenuItemClickListener() {

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
     * Initializes {@link #persistence}, {@link #gameOptions} and {@link #countsPersistence}.
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
        gameOptions = new GameOptionsPersistence(this);
        countsPersistence = new CountFilledFieldsPersistence(this);
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
        int[][] nonogram = persistence.loadLastNonogram();
        int[][] currentField = persistence.loadLastUserField();
        if (nonogram != null && nonogram.length == gameOptions.getNumberOfRows() && nonogram[0].length == gameOptions.getNumberOfColumns()) {
            // start puzzle with loaded arrays if the size haven't changed
            puzzleDisplayer.displayNewGame(nonogram, currentField, countsPersistence.loadCountsColumns(), countsPersistence.loadCountsRows());
        } else {
            // start new game if the size was changed
            puzzleDisplayer.displayNewGame();
        }
    }

    /**
     * Saves the current game.
     */
    private void saveGame() {
        persistence.saveNonogram(puzzleDisplayer.getNonogram());
        persistence.saveCurrentField(puzzleDisplayer.getUsersCurrentField());
        countsPersistence.saveCountFilledFields(puzzleDisplayer.getColumnCounts(), true);
        countsPersistence.saveCountFilledFields(puzzleDisplayer.getRowCounts(), false);
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

}