package niedermeyer.nonogram.gui.activity

import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.marginTop
import niedermeyer.nonogram.R
import niedermeyer.nonogram.gui.dialog.DialogHelper
import niedermeyer.nonogram.gui.observer.PuzzleSolvedObserver
import niedermeyer.nonogram.gui.puzzle.PuzzleController
import niedermeyer.nonogram.gui.puzzle.PuzzleView
import niedermeyer.nonogram.persistence.GameOptionsPersistence
import niedermeyer.nonogram.persistence.StatisticsPersistence

class GameActivity : AppCompatActivity() {

    private val toolbarMenuClickListener = Toolbar.OnMenuItemClickListener { item ->
        val dialogHelper = DialogHelper()
        // choose correct action
        when (item.itemId) {
            R.id.toolbar_game_new_puzzle -> {
                // start a new puzzle
                gameManager!!.newGame(options!!.numberOfRows, options!!.numberOfColumns)
                displayGame()
                true
            }
            R.id.toolbar_game_reset_puzzle -> {
                // reset the current puzzle
                gameManager!!.resetGame()
                displayGame()
                true
            }
            R.id.toolbar_game_puzzle_size -> {
                // create the field size dialog
                dialogHelper.openFieldSizeDialog(layoutInflater) {
                    gameManager!!.newGame(options!!.numberOfRows, options!!.numberOfColumns)
                    displayGame()
                }
                true
            }
            R.id.toolbar_game_tutorial -> {
                // open the tutorial
                dialogHelper.openTutorialDialogFullscreen(supportFragmentManager)
                true
            }
            else -> {
                false
            }
        }
    }

    private val puzzleSolvedObserver: PuzzleSolvedObserver = object : PuzzleSolvedObserver {
        override fun callback() {
            // save won puzzle in statistics
            statistics!!.saveNewScore()

            // show the won dialog
            DialogHelper().openGameWonDialogFullscreen(supportFragmentManager)
            gameManager!!.newGame(options!!.numberOfRows, options!!.numberOfColumns)
            displayGame()
        }
    }

    private var gameManager: PuzzleController? = null
    private var options: GameOptionsPersistence? = null
    private var statistics: StatisticsPersistence? = null

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.top_app_bar_game, menu)
        return true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        // set the toolbar
        val toolbar = findViewById<Toolbar>(R.id.activity_game_toolbar)
        setSupportActionBar(toolbar)
        toolbar.setNavigationOnClickListener { onBackPressed() }
        toolbar.setOnMenuItemClickListener(toolbarMenuClickListener)

        // initialize class members
        options = GameOptionsPersistence(this)
        statistics = StatisticsPersistence(this)
        gameManager = PuzzleController(this)
        gameManager!!.addPuzzleSolvedObserver(puzzleSolvedObserver)

        // start the tutorial if it is the first puzzle
        if (gameManager!!.isFirstPuzzle) {
            DialogHelper().openTutorialDialogFullscreen(supportFragmentManager)
        }

        // start the game
        gameManager!!.startGame(options!!.numberOfRows, options!!.numberOfColumns)
        displayGame()
    }

    override fun onPause() {
        super.onPause()
        gameManager!!.saveGame()
    }

    /**
     * Starts a new game or saved game if available.
     */
    private fun displayGame() {
        val view = findViewById<GameActivityView>(R.id.activity_game_scroll_vertical)
        view.removeAllViews()
        view.addView(
            PuzzleView(
                this,
                view.marginTop,
                gameManager!!.nonogram!!,
                gameManager!!.onFieldClick
            )
        )
    }

    companion object
}