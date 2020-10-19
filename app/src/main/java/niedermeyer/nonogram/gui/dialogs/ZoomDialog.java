package niedermeyer.nonogram.gui.dialogs;

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
 * @author Elen Niedermeyer, last updated 2020-10-19
 */
public class ZoomDialog {

    /**
     * Makes a zoom dialog.
     *
     * @param layoutInflater
     * @return the number picker dialog
     */
    public AlertDialog makeDialog(final LayoutInflater layoutInflater) {
        final GameOptionsPersistence persistence = new GameOptionsPersistence(layoutInflater.getContext());

        // inflate the layout
        final View layout = layoutInflater.inflate(R.layout.dialog_zoom, null);

        return null;
    }

}
