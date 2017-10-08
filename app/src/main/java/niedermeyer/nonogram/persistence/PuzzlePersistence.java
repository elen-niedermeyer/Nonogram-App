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
import niedermeyer.nonogram.gui.NonogramActivity;

/**
 * @author Elen Niedermeyer, last updated 2017-10-08
 */
public class PuzzlePersistence {

    private static final String NONOGRAM_FILE_NAME = "nonogram";
    private static final String CURRENT_FIELD_FILE_NAME = "current_field";

    /**
     * Context activity
     */
    private Activity activity;

    public PuzzlePersistence(Activity pActivity) {
        activity = pActivity;
    }

    public void saveNonogram(int[][] pNonogram) {
        // look if the file exists
        File nonogramFile = new File(activity.getFilesDir(), NONOGRAM_FILE_NAME);
        if (!nonogramFile.exists()) {
            // create a new file if it doesn't exist
            try {
                nonogramFile.createNewFile();
            } catch (Exception e) {
                Logger.getLogger(NonogramActivity.class.getName()).log(Level.WARNING, null, e);
            }
        }

        writeArrayObject(pNonogram, NONOGRAM_FILE_NAME);
    }

    public void saveCurrentField(int[][] pCurrentField) {
        // look if the file exists
        File currentFieldFile = new File(activity.getFilesDir(), CURRENT_FIELD_FILE_NAME);
        if (!currentFieldFile.exists()) {
            // create a new file if it doesn't exist
            try {
                currentFieldFile.createNewFile();
            } catch (Exception e) {
                Logger.getLogger(NonogramActivity.class.getName()).log(Level.WARNING, null, e);
            }
        }

        writeArrayObject(pCurrentField, CURRENT_FIELD_FILE_NAME);
    }

    public int[][] loadLastNonogram() {
        File nonogramFile = new File(activity.getFilesDir(), NONOGRAM_FILE_NAME);
        if (nonogramFile.exists()) {
            // loads the nonogram if the file exists
            return (readArrayObject(NONOGRAM_FILE_NAME));
        } else {
            return null;
        }
    }

    public int[][] loadLastUserField() {
        File currentFieldFile = new File(activity.getFilesDir(), CURRENT_FIELD_FILE_NAME);
        if (currentFieldFile.exists()) {
            // loads the field if the file exists
            return (readArrayObject(CURRENT_FIELD_FILE_NAME));
        } else {
            return null;
        }
    }

    public boolean isFirstPuzzle() {
        SharedPreferences prefs = activity.getPreferences(Context.MODE_PRIVATE);
        boolean isFirstGame = prefs.getBoolean(activity.getString(R.string.prefs_first_game), true);
        if (isFirstGame) {
            SharedPreferences.Editor edit = prefs.edit();
            edit.putBoolean(activity.getString(R.string.prefs_first_game), false);
            edit.apply();
        }

        return isFirstGame;
    }

    private void writeArrayObject(int[][] pObject, String pFileName) {
        ObjectOutputStream out = null;
        try {
            // write the object
            out = new ObjectOutputStream(activity.openFileOutput(pFileName, Context.MODE_PRIVATE));
            out.writeObject(pObject);
        } catch (Exception e) {
            Logger.getLogger(NonogramActivity.class.getName()).log(Level.WARNING, null, e);
        } finally {
            // close the stream
            try {
                out.close();
            } catch (Exception e) {
                Logger.getLogger(NonogramActivity.class.getName()).log(Level.WARNING, null, e);
            }
        }
    }

    private int[][] readArrayObject(String pFileName) {
        int[][] readArray = null;
        ObjectInputStream in = null;
        try {
            // read the object
            in = new ObjectInputStream(activity.openFileInput(pFileName));
            readArray = (int[][]) in.readObject();
        } catch (Exception e) {
            Logger.getLogger(NonogramActivity.class.getName()).log(Level.WARNING, null, e);
        } finally {
            try {
                in.close();
            } catch (Exception e) {
                Logger.getLogger(NonogramActivity.class.getName()).log(Level.WARNING, null, e);
            }
        }

        return readArray;
    }
}
