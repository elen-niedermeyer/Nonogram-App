package niedermeyer.nonogram.persistence;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import java.io.File;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import niedermeyer.nonogram.R;
import niedermeyer.nonogram.gui.GameActivity;

/**
 * @author Elen Niedermeyer, last modified 2020-12-11
 */
public class PuzzlePersistence {

    /**
     * File names
     */
    private static final String NONOGRAM_FILE_NAME = "nonogram";
    private static final String CURRENT_FIELD_FILE_NAME = "current_field";

    /**
     * Context activity
     */
    private final Activity activity;

    /**
     * Constructor. Initializes {@link #activity}.
     *
     * @param pActivity the context activity
     */
    public PuzzlePersistence(Activity pActivity) {
        activity = pActivity;
    }

    /**
     * Saves the given array as nonogram in the file named {@link #NONOGRAM_FILE_NAME}.
     *
     * @param pNonogram the array representing the current nonogram
     */
    public void saveNonogram(int[][] pNonogram) {
        // make file object
        File nonogramFile = new File(activity.getFilesDir(), NONOGRAM_FILE_NAME);
        // look if the file exists
        if (!nonogramFile.exists()) {
            // create a new file if it doesn't exist
            try {
                nonogramFile.createNewFile();
            } catch (Exception e) {
                Logger.getLogger(GameActivity.class.getName()).log(Level.WARNING, null, e);
            }
        }

        writeArrayObject(pNonogram, NONOGRAM_FILE_NAME);
    }

    /**
     * Saves the given array as current user field in the file named {@link #CURRENT_FIELD_FILE_NAME}.
     *
     * @param pCurrentField the array representing the user's current field
     */
    public void saveCurrentField(int[][] pCurrentField) {
        // look if the file exists
        File currentFieldFile = new File(activity.getFilesDir(), CURRENT_FIELD_FILE_NAME);
        if (!currentFieldFile.exists()) {
            // create a new file if it doesn't exist
            try {
                currentFieldFile.createNewFile();
            } catch (Exception e) {
                Logger.getLogger(GameActivity.class.getName()).log(Level.WARNING, null, e);
            }
        }

        writeArrayObject(pCurrentField, CURRENT_FIELD_FILE_NAME);
    }

    /**
     * Load the nonogram from file if it exists.
     *
     * @return an array representing a nonogram or null if reading the array failed.
     */
    public int[][] loadLastNonogram() {
        File nonogramFile = new File(activity.getFilesDir(), NONOGRAM_FILE_NAME);
        if (nonogramFile.exists()) {
            // loads the nonogram if the file exists
            return (readArrayObject(NONOGRAM_FILE_NAME));
        } else {
            return null;
        }
    }

    /**
     * Load the user's field from file if it exists.
     *
     * @return an array representing the user's field or null if reading the array failed
     */
    public int[][] loadLastUserField() {
        File currentFieldFile = new File(activity.getFilesDir(), CURRENT_FIELD_FILE_NAME);
        if (currentFieldFile.exists()) {
            // loads the field if the file exists
            return (readArrayObject(CURRENT_FIELD_FILE_NAME));
        } else {
            return null;
        }
    }

    /**
     * Checks if there was played before. Updates the preference.
     *
     * @return true, if it's the first puzzle, false otherwise
     */
    public boolean isFirstPuzzle() {
        SharedPreferences prefs = activity.getPreferences(Context.MODE_PRIVATE);
        // read the preference, true is the default value
        boolean isFirstGame = prefs.getBoolean(activity.getString(R.string.prefs_first_game), true);
        if (isFirstGame) {
            // set the preference to false
            SharedPreferences.Editor edit = prefs.edit();
            edit.putBoolean(activity.getString(R.string.prefs_first_game), false);
            edit.apply();
        }

        return isFirstGame;
    }

    /**
     * Writes the given object to the given file.
     *
     * @param pObject   the array that should be saved
     * @param pFileName the name of the file in which the array should be wrote
     */
    private void writeArrayObject(int[][] pObject, String pFileName) {
        ObjectOutputStream out = null;
        try {
            // write the object
            out = new ObjectOutputStream(activity.openFileOutput(pFileName, Context.MODE_PRIVATE));
            out.writeObject(pObject);
        } catch (Exception e) {
            Logger.getLogger(GameActivity.class.getName()).log(Level.WARNING, null, e);
        } finally {
            // close the stream
            try {
                out.close();
            } catch (Exception e) {
                Logger.getLogger(GameActivity.class.getName()).log(Level.WARNING, null, e);
            }
        }
    }

    /**
     * Reads an array from the given file.
     *
     * @param pFileName the name of the file which contains the array
     * @return the read array
     */
    private int[][] readArrayObject(String pFileName) {
        int[][] readArray = null;
        ObjectInputStream in = null;
        try {
            // read the object
            in = new ObjectInputStream(activity.openFileInput(pFileName));
            readArray = (int[][]) in.readObject();
        } catch (Exception e) {
            Logger.getLogger(GameActivity.class.getName()).log(Level.WARNING, null, e);
        } finally {
            // close the stream
            try {
                in.close();
            } catch (Exception e) {
                Logger.getLogger(GameActivity.class.getName()).log(Level.WARNING, null, e);
            }
        }

        return readArray;
    }
}
