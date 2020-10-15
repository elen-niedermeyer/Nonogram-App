package niedermeyer.nonogram.gui;

import android.content.DialogInterface;
import android.view.LayoutInflater;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

/**
 * @author Elen Niedermeyer, last updated 2020-10-15
 */
public class DialogHelper {

    public void openTutorialDialogFullscreen(FragmentManager fragmentManager) {
        TutorialDialogFragment fragment = new TutorialDialogFragment();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.add(android.R.id.content, fragment)
                .addToBackStack(null).commit();
    }

    public void openGameSizeDialog(LayoutInflater layoutInflater, DialogInterface.OnDismissListener onDismissListener) {
        AlertDialog dialogRows = new GameSizeDialog().makeDialog(layoutInflater);
        if (onDismissListener != null) {
            dialogRows.setOnDismissListener(onDismissListener);
        }
        dialogRows.show();
    }

    public void openGameSizeDialog(LayoutInflater layoutInflater) {
        openGameSizeDialog(layoutInflater, null);
    }


}
