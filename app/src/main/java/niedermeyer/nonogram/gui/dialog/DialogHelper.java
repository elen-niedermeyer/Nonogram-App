package niedermeyer.nonogram.gui.dialog;

import android.content.DialogInterface;
import android.view.LayoutInflater;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

/**
 * @author Elen Niedermeyer, last modified 2020-10-15
 */
public class DialogHelper {

    public void openFieldSizeDialog(LayoutInflater layoutInflater, DialogInterface.OnDismissListener onDismissListener) {
        AlertDialog dialogRows = new FieldSizeDialog().makeDialog(layoutInflater);
        if (onDismissListener != null) {
            dialogRows.setOnDismissListener(onDismissListener);
        }
        dialogRows.show();
    }

    public void openTutorialDialogFullscreen(FragmentManager fragmentManager) {
        TutorialDialogFragment fragment = new TutorialDialogFragment();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.add(android.R.id.content, fragment)
                .addToBackStack(null).commit();
    }

    public void openGameWonDialogFullscreen(FragmentManager fragmentManager) {
        GameWonDialogFragment fragment = new GameWonDialogFragment();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.add(android.R.id.content, fragment)
                .addToBackStack(null).commit();
    }

}
