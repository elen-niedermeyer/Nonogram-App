package niedermeyer.nonogram.gui;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.util.logging.Level;
import java.util.logging.Logger;

import niedermeyer.nonogram.R;
import niedermeyer.nonogram.logics.NonogramConstants;
import niedermeyer.nonogram.persistence.GameSizePersistence;

/**
 * @author Elen Niedermeyer, last updated 2017-07-16
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
            picker.setValue(GameSizePersistence.numberOfRows);
        } else {
            picker.setValue(GameSizePersistence.numberOfColumns);
        }

        AlertDialog dialog = new AlertDialog.Builder(activity)
                .setView(layout)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int newValue = picker.getValue();
                        if (isRow) {
                            GameSizePersistence.numberOfRows = newValue;
                        } else {
                            GameSizePersistence.numberOfColumns = newValue;
                        }
                        dialog.dismiss();
                    }
                })
                .setNegativeButton(R.string.cancel, null)
                .create();

        return dialog;
    }
}
