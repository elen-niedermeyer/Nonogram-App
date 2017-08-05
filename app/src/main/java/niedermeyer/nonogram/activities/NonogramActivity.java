package niedermeyer.nonogram.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.io.File;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import niedermeyer.nonogram.R;
import niedermeyer.nonogram.persistence.GameSizeHandler;

/**
 * @author Elen Niedermeyer, last updated 2017-08-04
 */
public class NonogramActivity extends AppCompatActivity {

    private GameHandler game = new GameHandler(this);
    private MenuActions menuActions = new MenuActions(this);
    private GameSizeHandler gameSize;

    private String nonogramFileName = "nonogram";
    private File nonogramFile;
    private String actualFieldFileName = "actual_field";
    private File actualFieldFile;

    private Toolbar toolbar;

    /**
     * Getter for the {@link GameHandler}.
     *
     * @return {@link #game}
     */
    public GameHandler getGameHandler() {
        return this.game;
    }

    public void updateToolbarTitle() {
        String title = String.format(getString(R.string.toolbar_title), GameSizeHandler.numberOfColumns, GameSizeHandler.numberOfRows);
        getSupportActionBar().setTitle(title);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        // Inflate the menuActions; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_activity_nonogram, menu);

        MenuItem rowsNumber = menu.findItem(R.id.toolbar_game_rows);
        rowsNumber.setTitle(getString(R.string.number_of_rows) + ": " + Integer.toString(GameSizeHandler.numberOfRows));

        MenuItem columnsNumber = menu.findItem(R.id.toolbar_game_columns);
        columnsNumber.setTitle(getString(R.string.number_of_columns) + ": " + Integer.toString(GameSizeHandler.numberOfColumns));

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        final MenuItem clickedItem = item;
        switch (item.getItemId()) {
            case R.id.toolbar_game_new:
                game.newGame();
                return true;

            case R.id.toolbar_game_reset:
                game.resetGame();
                return true;

            case R.id.toolbar_game_rows:
                AlertDialog dialogRows = menuActions.makeNumberPickerForGameSize(true);
                final int saveNumberRows = GameSizeHandler.numberOfRows;
                dialogRows.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        if (saveNumberRows != GameSizeHandler.numberOfRows) {
                            clickedItem.setTitle(getString(R.string.number_of_rows) + ": " + Integer.toString(GameSizeHandler.numberOfRows));
                            game.newGame();
                        }
                    }
                });
                dialogRows.show();
                return true;

            case R.id.toolbar_game_columns:
                AlertDialog dialogColumns = menuActions.makeNumberPickerForGameSize(false);
                final int saveNumberColumns = GameSizeHandler.numberOfColumns;
                dialogColumns.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        if (saveNumberColumns != GameSizeHandler.numberOfColumns) {
                            clickedItem.setTitle(getString(R.string.number_of_columns) + ": " + Integer.toString(GameSizeHandler.numberOfColumns));
                            game.newGame();
                        }
                    }
                });
                dialogColumns.show();
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_nonogram);

        gameSize = new GameSizeHandler(this);

        toolbar = (Toolbar) findViewById(R.id.activity_nonogram_toolbar);
        setSupportActionBar(toolbar);
        updateToolbarTitle();

        loadLastNonogramAndField();
    }

    @Override
    protected void onPause() {
        super.onPause();

        gameSize.saveGameSize();
        saveNonogramAndField();
    }

    private void loadLastNonogramAndField() {
        ObjectInputStream in = null;

        // read the last nonogram
        nonogramFile = new File(this.getFilesDir(), nonogramFileName);
        int[][] nonogram = null;
        if (nonogramFile.exists()) {
            try {
                // read the object
                in = new ObjectInputStream(openFileInput(nonogramFileName));
                nonogram = (int[][]) in.readObject();
            } catch (Exception e) {
                Logger.getLogger(NonogramActivity.class.getName()).log(Level.WARNING, null, e);
            } finally {
                try {
                    in.close();
                } catch (Exception e) {
                    Logger.getLogger(NonogramActivity.class.getName()).log(Level.WARNING, null, e);
                }
            }
        }


        // read the last actual solution of the field
        actualFieldFile = new File(this.getFilesDir(), actualFieldFileName);
        int[][] actualField = null;
        if (actualFieldFile.exists()) {
            try {
                // read the object
                in = new ObjectInputStream(openFileInput(actualFieldFileName));
                actualField = (int[][]) in.readObject();
            } catch (Exception e) {
                Logger.getLogger(NonogramActivity.class.getName()).log(Level.WARNING, null, e);
            } finally {
                try {
                    in.close();
                } catch (Exception e) {
                    Logger.getLogger(NonogramActivity.class.getName()).log(Level.WARNING, null, e);
                }
            }
        }

        if (nonogram != null && nonogram.length == GameSizeHandler.numberOfRows && nonogram[0].length == GameSizeHandler.numberOfColumns) {
            // start game with loaded arrays if the size haven't changed
            game.newGame(nonogram, actualField);
        } else {
            // start new game if the size was changed
            game.newGame();
        }
    }

    private void saveNonogramAndField() {
        ObjectOutputStream out = null;

        // save the nonogram
        int[][] nonogram = game.getNonogram();

        // look if the file exists
        if (!nonogramFile.exists()) {
            try {
                nonogramFile.createNewFile();
            } catch (Exception e) {
                Logger.getLogger(NonogramActivity.class.getName()).log(Level.WARNING, null, e);
            }
        }

        try {
            // write the game field
            out = new ObjectOutputStream(openFileOutput(nonogramFileName, Context.MODE_PRIVATE));
            out.writeObject(nonogram);
        } catch (Exception e) {
            Logger.getLogger(NonogramActivity.class.getName()).log(Level.WARNING, null, e);
        } finally {
            try {
                out.close();
            } catch (Exception e) {
                Logger.getLogger(NonogramActivity.class.getName()).log(Level.WARNING, null, e);
            }
        }

        // save the field
        int[][] actualField = game.getActualField();

        // look if the file exists
        if (!actualFieldFile.exists()) {
            try {
                actualFieldFile.createNewFile();
            } catch (Exception e) {
                Logger.getLogger(NonogramActivity.class.getName()).log(Level.WARNING, null, e);
            }
        }

        try {
            // write the game field
            out = new ObjectOutputStream(openFileOutput(actualFieldFileName, Context.MODE_PRIVATE));
            out.writeObject(actualField);
        } catch (Exception e) {
            Logger.getLogger(NonogramActivity.class.getName()).log(Level.WARNING, null, e);
        } finally {
            try {
                out.close();
            } catch (Exception e) {
                Logger.getLogger(NonogramActivity.class.getName()).log(Level.WARNING, null, e);
            }
        }
    }
}