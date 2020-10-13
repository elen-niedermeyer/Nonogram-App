package niedermeyer.nonogram.gui;

import android.app.Activity;
import android.content.DialogInterface;
import android.view.View;
import android.widget.NumberPicker;

import androidx.appcompat.app.AlertDialog;

import niedermeyer.nonogram.R;
import niedermeyer.nonogram.logics.NonogramConstants;
import niedermeyer.nonogram.persistence.PuzzleSizePersistence;

/**
 * @author Elen Niedermeyer, last updated 2017-10-08
 */
public class NumberPickerDialog {

    /**
     * Context activity
     */
    private Activity activity;

    /**
     * Constructor
     * Initializes {@link #activity}.
     *
     * @param pActivity the context activity
     */
    public NumberPickerDialog(Activity pActivity) {
        activity = pActivity;
    }

    /**
     * Makes a number picker dialog for choosing the puzzle size.
     *
     * @param isRow true, if you wan't to change the number of rows, false if it should be the number of columns
     * @return the number picker dialog
     */
    public AlertDialog makeDialog(final boolean isRow) {
        // inflate the layout
        View layout = activity.getLayoutInflater().inflate(R.layout.dialog_number_picker, null);

        // make the number picker
        final NumberPicker numberPicker = (NumberPicker) layout.findViewById(R.id.menu_number_picker);
        // set minimum and maximum
        numberPicker.setMinValue(NonogramConstants.NONOGRAM_SIZE_MINIMUM);
        numberPicker.setMaxValue(NonogramConstants.NONOGRAM_SIZE_MAXIMUM);
        // set current value
        if (isRow) {
            numberPicker.setValue(PuzzleSizePersistence.numberOfRows);
        } else {
            numberPicker.setValue(PuzzleSizePersistence.numberOfColumns);
        }

        // make the dialog
        AlertDialog pickerDialog = new AlertDialog.Builder(activity)
                .setView(layout)
                // save the new value if the positive button was clicked
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface pDialog, int which) {
                        int newValue = numberPicker.getValue();
                        if (isRow) {
                            PuzzleSizePersistence.numberOfRows = newValue;
                        } else {
                            PuzzleSizePersistence.numberOfColumns = newValue;
                        }
                        pDialog.dismiss();
                    }
                })
                .setNegativeButton(R.string.cancel, null)
                .create();

        return pickerDialog;
    }
}
