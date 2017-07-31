package niedermeyer.nonogram.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.io.File;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import niedermeyer.nonogram.R;

/**
 * @author Elen Niedermeyer, last updated 2017-07-16
 */
public class NonogramActivity extends AppCompatActivity {

    public static int numberOfRows;
    public static int numberOfColumns;

    private GameHandler game = new GameHandler(this);
    private Menu menu = new Menu(this);

    private String nonogramFileName = "nonogram";
    private File nonogramFile;
    private String actualFieldFileName = "actual_field";
    private File actualFieldFile;

    private ImageButton menuButton;
    private TextView fieldSizeView;
    private Button newGameButton;
    private Button resetGameButton;

    /**
     * Getter for the {@link GameHandler}.
     *
     * @return {@link #game}
     */
    public GameHandler getGameHandler() {
        return this.game;
    }

    public void updateGameSizeView() {
        fieldSizeView.setText(numberOfColumns + " x " + numberOfRows);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_nonogram);

        // initialize new game button
        newGameButton = (Button) findViewById(R.id.activity_nonogram_buttons_menu_new);
        newGameButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                game.newGame();
            }
        });

        // initialize reset game button
        resetGameButton = (Button) findViewById(R.id.activity_nonogram_buttons_menu_reset);
        resetGameButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                game.resetGame();
            }
        });

        // initialize menu button
        menuButton = (ImageButton) findViewById(R.id.activity_nonogram_buttons_menu_menu);
        menuButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v == menuButton) {
                    menu.showMenu(v);
                }
            }
        });

        initializeFieldSizes();
        // initialize size view
        fieldSizeView = (TextView) findViewById(R.id.game_field_size_view);
        updateGameSizeView();

        loadLastNonogramAndField();
    }

    @Override
    protected void onPause() {
        super.onPause();

        saveGameSize();
        saveNonogramAndField();
    }

    private void initializeFieldSizes() {
        SharedPreferences prefs = getPreferences(Context.MODE_PRIVATE);
        numberOfRows = prefs.getInt(getString(R.string.prefs_rows), 5);
        numberOfColumns = prefs.getInt(getString(R.string.prefs_columns), 5);
    }

    private void saveGameSize() {
        SharedPreferences prefs = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEdit = prefs.edit();
        prefsEdit.putInt(getString(R.string.prefs_rows), numberOfRows);
        prefsEdit.putInt(getString(R.string.prefs_columns), numberOfColumns);
        prefsEdit.apply();
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

        // start game with loaded arrays
        game.newGame(nonogram, actualField);
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