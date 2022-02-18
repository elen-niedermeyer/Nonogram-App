package niedermeyer.nonogram.gui.dialog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.DialogFragment;

import niedermeyer.nonogram.R;

public class GameWonDialogFragment extends DialogFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.game_won, container, false);
        layout.findViewById(R.id.game_won_root).setOnClickListener(v -> dismiss());

        return layout;
    }

}
