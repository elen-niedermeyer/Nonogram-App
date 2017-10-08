package niedermeyer.nonogram.gui;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.NumberPicker;

import niedermeyer.nonogram.R;
import niedermeyer.nonogram.logics.NonogramConstants;
import niedermeyer.nonogram.persistence.PuzzleSizePersistence;

/**
 * @author Elen Niedermeyer, last updated 2017-09-21
 */
public class NumberPickerDialog {

    private Activity activity;

    public NumberPickerDialog(Activity pActivity) {
        activity = pActivity;
    }

    public AlertDialog makeDialog(final boolean isRow) {
        View layout = activity.getLayoutInflater().inflate(R.layout.dialog_number_picker, null);
        final NumberPicker picker = (NumberPicker) layout.findViewById(R.id.menu_number_picker);
        picker.setMinValue(NonogramConstants.NONOGRAM_SIZE_MINIMUM);
        picker.setMaxValue(NonogramConstants.NONOGRAM_SIZE_MAXIMUM);
        if (isRow) {
            picker.setValue(PuzzleSizePersistence.numberOfRows);
        } else {
            picker.setValue(PuzzleSizePersistence.numberOfColumns);
        }

        AlertDialog dialog = new AlertDialog.Builder(activity)
                .setView(layout)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int newValue = picker.getValue();
                        if (isRow) {
                            PuzzleSizePersistence.numberOfRows = newValue;
                        } else {
                            PuzzleSizePersistence.numberOfColumns = newValue;
                        }
                        dialog.dismiss();
                    }
                })
                .setNegativeButton(R.string.cancel, null)
                .create();

        return dialog;
    }
}
