package niedermeyer.nonogram.gui;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class DialogHelper {

    public void openTutorialDialogFullscreen(FragmentManager fragmentManager) {
        TutorialDialogFragment fragment = new TutorialDialogFragment();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.add(android.R.id.content, fragment)
                .addToBackStack(null).commit();
    }

}
