package niedermeyer.nonogram.persistence;

import android.app.Activity;
import android.content.Context;

import com.google.gson.Gson;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import niedermeyer.nonogram.gui.NonogramActivity;
import niedermeyer.nonogram.logics.CountFilledFields;

//TODO: refactoring

/**
 * @author Elen Niedermeyer
 *         Last update 2017-11-04
 */
public class CountFilledFieldsPersistence {

    /**
     * File names
     */
    private static final String COUNTS_ROW_FILE_NAME = "counts_row";
    private static final String COUNTS_COLUMN_FILE_NAME = "counts_column";

    /**
     * Context activity
     */
    private Activity activity;

    public CountFilledFieldsPersistence(Activity pActivity) {
        activity = pActivity;
    }

    public void saveCountFilledFields(CountFilledFields pCounts, boolean isColumnCounts) {
        // make file object
        File countFile = null;
        if (isColumnCounts) {
            countFile = new File(activity.getFilesDir(), COUNTS_COLUMN_FILE_NAME);
        } else {
            countFile = new File(activity.getFilesDir(), COUNTS_ROW_FILE_NAME);
        }

        // look if the file exists
        if (!countFile.exists()) {
            // create a new file if it doesn't exist
            try {
                countFile.createNewFile();
            } catch (Exception e) {
                Logger.getLogger(NonogramActivity.class.getName()).log(Level.WARNING, null, e);
            }
        }

        writeCountObject(pCounts, countFile.getName());
    }

    public CountFilledFields loadCountsColumns() {
        // make file object
        File countFile = new File(activity.getFilesDir(), COUNTS_COLUMN_FILE_NAME);

        // look if the file exists
        if (!countFile.exists()) {
            return readCountObject(COUNTS_COLUMN_FILE_NAME);
        } else {
            return null;
        }
    }

    public CountFilledFields loadCountsRows() {
        // make file object
        File countFile = new File(activity.getFilesDir(), COUNTS_ROW_FILE_NAME);

        // look if the file exists
        if (!countFile.exists()) {
            return readCountObject(COUNTS_ROW_FILE_NAME);
        } else {
            return null;
        }
    }

    private CountFilledFields readCountObject(String pFileName) {
        CountFilledFields counts = null;

        InputStream in = null;
        try {
            // read the object
            in = activity.openFileInput(pFileName);
            byte[] input = new byte[in.available()];
            in.read(input);

            // parse the input
            String countsJson = new String(input);
            Gson gson = new Gson();
            counts = gson.fromJson(countsJson, CountFilledFields.class);

        } catch (Exception e) {
            Logger.getLogger(NonogramActivity.class.getName()).log(Level.WARNING, null, e);
        } finally {
            // close the stream
            try {
                in.close();
            } catch (Exception e) {
                Logger.getLogger(NonogramActivity.class.getName()).log(Level.WARNING, null, e);
            }
        }

        return counts;
    }

    private void writeCountObject(CountFilledFields pCounts, String pFileName) {
        // serialize counts
        Gson gson = new Gson();
        String countsJson = gson.toJson(pCounts);

        OutputStream out = null;
        try {
            // write the object
            out = activity.openFileOutput(pFileName, Context.MODE_PRIVATE);
            out.write(countsJson.getBytes());
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
}
