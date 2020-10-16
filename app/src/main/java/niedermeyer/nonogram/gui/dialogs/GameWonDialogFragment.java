package niedermeyer.nonogram.gui.dialogs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.DialogFragment;

import niedermeyer.nonogram.R;

/**
 * @author Elen Niedermeyer, last updated 2020-10-16
 */
public class GameWonDialogFragment extends DialogFragment {

    /**
     * Listener for clicks. Shows the next step.
     */
    private View.OnClickListener clickOnDisplay = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            dismiss();
        }
    };

    /**
     * The system calls this to get the DialogFragment's layout.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.game_won, container, false);
        layout.findViewById(R.id.game_won_root);

        return layout;
    }

}
