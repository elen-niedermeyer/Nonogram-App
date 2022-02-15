package niedermeyer.nonogram.gui.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.HorizontalScrollView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import niedermeyer.nonogram.R;
import niedermeyer.nonogram.gui.puzzle.GameManager;
import niedermeyer.nonogram.gui.observer.PuzzleSolvedObserver;
import niedermeyer.nonogram.gui.dialog.DialogHelper;
import niedermeyer.nonogram.gui.puzzle.PuzzleDisplayer;
import niedermeyer.nonogram.persistence.GameOptionsPersistence;
import niedermeyer.nonogram.persistence.StatisticsPersistence;


/**
 * @author Elen Niedermeyer, last modified 2020-12-11
 */
public class GameActivity extends AppCompatActivity {

    private static final int ZOOM_STEP = 10;

    private final PuzzleDisplayer puzzleDisplayer = new PuzzleDisplayer(this);

    private final Toolbar.OnMenuItemClickListener toolbarMenuClickListener = new Toolbar.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem item) {
            DialogHelper dialogHelper = new DialogHelper();

            // switch on the item id
            switch (item.getItemId()) {
                case R.id.toolbar_game_new_puzzle:
                    // start a new puzzle
                    gameManager.newGame(options.getNumberOfRows(), options.getNumberOfColumns());
                    GameActivity.this.displayGame();
                    return true;

                case R.id.toolbar_game_reset_puzzle:
                    // reset the current puzzle
                    gameManager.resetGame(options.getNumberOfRows(), options.getNumberOfColumns());
                    GameActivity.this.displayGame();
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
                            gameManager.newGame(options.getNumberOfRows(), options.getNumberOfColumns());
                            GameActivity.this.displayGame();
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

    private final PuzzleSolvedObserver puzzleSolvedObserver = new PuzzleSolvedObserver() {
        @Override
        public void callback() {
            // save won puzzle in statistics
            statistics.saveNewScore();

            // show the won dialog
            new DialogHelper().openGameWonDialogFullscreen(getSupportFragmentManager());
            gameManager.newGame(options.getNumberOfRows(), options.getNumberOfColumns());
            displayGame();
        }
    };

    private GameManager gameManager;

    private GameOptionsPersistence options;
    private StatisticsPersistence statistics;


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
     * Initializes {@link #options} adn {@link #statistics}.
     *
     * @param savedInstanceState saved information about the activity given by the system
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_game);

        // sets the toolbar
        Toolbar toolbar = findViewById(R.id.activity_game_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        toolbar.setOnMenuItemClickListener(toolbarMenuClickListener);

        options = new GameOptionsPersistence(this);
        statistics = new StatisticsPersistence(this);

        gameManager = new GameManager(this);
        gameManager.addPuzzleSolvedObserver(puzzleSolvedObserver);

        // start the tutorial if it's the first puzzle
        if (gameManager.isFirstPuzzle()) {
            new DialogHelper().openTutorialDialogFullscreen(getSupportFragmentManager());
        }

        gameManager.startGame(options.getNumberOfRows(), options.getNumberOfColumns());
        this.displayGame();
    }

    /**
     * Overrides {@link AppCompatActivity#onPause()}.
     * Saves the puzzle size and the arrays.
     * Saves the counts.
     */
    @Override
    protected void onPause() {
        super.onPause();

        gameManager.saveGame();
    }

    /**
     * Starts a new game or saved game if available.
     */
    private void displayGame() {
        HorizontalScrollView view = findViewById(R.id.activity_game_scroll_horizontal);
        view.removeAllViews();
        view.addView(puzzleDisplayer.getGameView(gameManager.getCurrentUserField(), gameManager.getRowCount(), gameManager.getColumnCount(), options.getCellSize(), gameManager.getOnFieldClick()));
    }

    /**
     * Reloads the game with new cell size.
     *
     * @param sizeDelta the delta for cell size
     */
    private void zoomGameField(int sizeDelta) {
        gameManager.saveGame();
        options.setCellSize(options.getCellSize() + sizeDelta);
        this.displayGame();
    }

}