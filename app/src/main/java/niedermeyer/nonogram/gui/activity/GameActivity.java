package niedermeyer.nonogram.gui.activity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.HorizontalScrollView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import niedermeyer.nonogram.R;
import niedermeyer.nonogram.gui.puzzle.PuzzleController;
import niedermeyer.nonogram.gui.observer.PuzzleSolvedObserver;
import niedermeyer.nonogram.gui.dialog.DialogHelper;
import niedermeyer.nonogram.gui.puzzle.PuzzleDisplayer;
import niedermeyer.nonogram.persistence.GameOptionsPersistence;
import niedermeyer.nonogram.persistence.StatisticsPersistence;

public class GameActivity extends AppCompatActivity {

    private static final int ZOOM_STEP = 10;

    private final PuzzleDisplayer puzzleDisplayer = new PuzzleDisplayer(this);

    private final Toolbar.OnMenuItemClickListener toolbarMenuClickListener = new Toolbar.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem item) {
            DialogHelper dialogHelper = new DialogHelper();

            int id = item.getItemId();
            // choose correct action
            if (id == R.id.toolbar_game_new_puzzle) {
                // start a new puzzle
                gameManager.newGame(options.getNumberOfRows(), options.getNumberOfColumns());
                GameActivity.this.displayGame();
                return true;

            } else if (id == R.id.toolbar_game_reset_puzzle) {
                // reset the current puzzle
                gameManager.resetGame();
                GameActivity.this.displayGame();
                return true;

            } else if (id == R.id.toolbar_game_zoom_in) {
                zoomGameField(ZOOM_STEP);
                return true;

            } else if (id == R.id.toolbar_game_zoom_out) {
                zoomGameField(-ZOOM_STEP);
                return true;

            } else if (id == R.id.toolbar_game_puzzle_size) {
                // create the field size dialog
                dialogHelper.openFieldSizeDialog(getLayoutInflater(), dialogInterface -> {
                    gameManager.newGame(options.getNumberOfRows(), options.getNumberOfColumns());
                    GameActivity.this.displayGame();
                });
                return true;

            } else if (id == R.id.toolbar_game_tutorial) {
                // open the tutorial
                dialogHelper.openTutorialDialogFullscreen(getSupportFragmentManager());
                return true;

            } else {
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

    private PuzzleController gameManager;

    private GameOptionsPersistence options;
    private StatisticsPersistence statistics;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.top_app_bar_game, menu);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        // set the toolbar
        Toolbar toolbar = findViewById(R.id.activity_game_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
        toolbar.setOnMenuItemClickListener(toolbarMenuClickListener);

        // initialize class members
        options = new GameOptionsPersistence(this);
        statistics = new StatisticsPersistence(this);
        gameManager = new PuzzleController(this);
        gameManager.addPuzzleSolvedObserver(puzzleSolvedObserver);

        // start the tutorial if it is the first puzzle
        if (gameManager.isFirstPuzzle()) {
            new DialogHelper().openTutorialDialogFullscreen(getSupportFragmentManager());
        }

        // start the game
        gameManager.startGame(options.getNumberOfRows(), options.getNumberOfColumns());
        this.displayGame();
    }

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
        view.addView(puzzleDisplayer.getGameView(gameManager.getNonogram(), options.getCellSize(), gameManager.getOnFieldClick()));
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