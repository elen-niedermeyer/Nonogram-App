package niedermeyer.nonogram.gui.dialogs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;

import niedermeyer.nonogram.R;
import niedermeyer.nonogram.gui.puzzle.PuzzleDisplayer;
import niedermeyer.nonogram.logics.NonogramConstants;
import niedermeyer.nonogram.logics.NonogramGenerator;

/**
 * @author Elen Niedermeyer, last modified 2020-12-11
 */
public class TutorialDialogFragment extends DialogFragment {

    private final int[][] nonogram = new int[][]{
            {NonogramConstants.FIELD_FILLED, NonogramConstants.FIELD_FILLED, NonogramConstants.FIELD_FILLED, NonogramConstants.FIELD_EMPTY},
            {NonogramConstants.FIELD_FILLED, NonogramConstants.FIELD_EMPTY, NonogramConstants.FIELD_FILLED, NonogramConstants.FIELD_FILLED},
            {NonogramConstants.FIELD_FILLED, NonogramConstants.FIELD_FILLED, NonogramConstants.FIELD_FILLED, NonogramConstants.FIELD_FILLED},
            {NonogramConstants.FIELD_FILLED, NonogramConstants.FIELD_EMPTY, NonogramConstants.FIELD_EMPTY, NonogramConstants.FIELD_FILLED}
    };

    private LayoutInflater inflater;

    /**
     * layout elements
     */
    private RelativeLayout root;
    private TextView instruction;
    private RelativeLayout tableContainer;
    private Button skipButton;

    /**
     * Index for the arrays {@link #instructions}.
     */
    private int index = 0;

    /**
     * String array of instruction texts
     */
    private String[] instructions;

    /**
     * Listener for clicks. Shows the next step.
     */
    private final View.OnClickListener clickOnDisplay = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // increase index
            index++;
            if (index < instructions.length) {
                // if it's not the end of the array, update views
                updateViews();
            } else {
                dismiss();
            }
        }
    };

    /**
     * The system calls this to get the DialogFragment's layout.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.inflater = inflater;

        View layout = inflater.inflate(R.layout.tutorial, container, false);
        initLayoutElements(layout);

        initInstructionArray();

        skipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        // add the on click listener
        root.setOnClickListener(clickOnDisplay);


        tableContainer.removeAllViews();
        NonogramGenerator gen = new NonogramGenerator();
        gen.setNonogram(nonogram);
        tableContainer.addView(new PuzzleDisplayer(container.getContext()).getGameView(nonogram, gen.getRowCount(), gen.getColumnCount(), 100, null));

        // init index
        index = 0;
        updateViews();

        return layout;
    }

    /**
     * Updates content of the views {@link #instruction} and {@link #tableContainer}.
     */
    public void updateViews() {
        instruction.setText(instructions[index]);

    }

    /**
     * Initializes the view elements {@link #root}, {@link #instruction}, {@link #tableContainer} and {@link #skipButton}.
     *
     * @param layout the dialog layout
     */
    private void initLayoutElements(View layout) {
        root = layout.findViewById(R.id.tutorial_root);
        instruction = layout.findViewById(R.id.tutorial_instruction);
        tableContainer = layout.findViewById(R.id.tutorial_table_container);
        skipButton = layout.findViewById(R.id.tutorial_btn_skip);
    }

    /**
     * Initializes the array {@link #instructions}.
     */
    private void initInstructionArray() {
        // load strings
        instructions = new String[]{getString(R.string.instruction_1),
                getString(R.string.instruction_2),
                getString(R.string.instruction_3),
                getString(R.string.instruction_4),
                getString(R.string.instruction_5),
                getString(R.string.instruction_6)};
    }

}
