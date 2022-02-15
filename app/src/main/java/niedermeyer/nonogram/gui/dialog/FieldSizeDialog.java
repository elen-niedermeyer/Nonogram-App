package niedermeyer.nonogram.gui.dialog;

import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.NumberPicker;

import androidx.appcompat.app.AlertDialog;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import niedermeyer.nonogram.R;
import niedermeyer.nonogram.logics.NonogramConstants;
import niedermeyer.nonogram.persistence.GameOptionsPersistence;

/**
 * @author Elen Niedermeyer, last modified 2020-10-15
 */
public class FieldSizeDialog {

    /**
     * Makes a number picker dialog for choosing the puzzle size.
     *
     * @param layoutInflater the layout inflater of the context
     * @return the number picker dialog
     */
    public AlertDialog makeDialog(final LayoutInflater layoutInflater) {
        final GameOptionsPersistence persistence = new GameOptionsPersistence(layoutInflater.getContext());

        // inflate the layout
        final View layout = layoutInflater.inflate(R.layout.dialog_number_picker, null);

        final NumberPicker pickerRows = layout.findViewById(R.id.number_picker_rows);
        setMinMax(pickerRows);
        pickerRows.setValue(persistence.getNumberOfRows());

        final NumberPicker pickerColumns = layout.findViewById(R.id.number_picker_columns);
        setMinMax(pickerColumns);
        pickerColumns.setValue(persistence.getNumberOfColumns());

        // make and return the dialog
        return new MaterialAlertDialogBuilder(layout.getContext())
                .setView(layout)
                .setTitle(R.string.field_size)
                // save the new value if the positive button was clicked
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface pDialog, int which) {
                        persistence.setNumberOfRows(pickerRows.getValue());
                        persistence.setNumberOfColumns(pickerColumns.getValue());
                        pDialog.dismiss();
                    }
                })
                .setNegativeButton(R.string.cancel, null)
                .create();
    }

    /**
     * Sets minimum and maximum
     *
     * @param numberPicker the number picker where to set the minimum and maximum
     */
    private void setMinMax(NumberPicker numberPicker) {
        numberPicker.setMinValue(NonogramConstants.NONOGRAM_SIZE_MINIMUM);
        numberPicker.setMaxValue(NonogramConstants.NONOGRAM_SIZE_MAXIMUM);
    }
}
