package niedermeyer.nonogram.gui.dialogs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;

import niedermeyer.nonogram.R;

/**
 * @author Elen Niedermeyer, last updated 2020-10-15
 */
public class TutorialDialogFragment extends DialogFragment {

    private LayoutInflater inflater;

    /**
     * layout elements
     */
    private RelativeLayout root;
    private TextView instruction;
    private TableLayout table;
    private Button skipButton;

    /**
     * Index for the arrays {@link #instructions} and {@link #tablesLayoutIds}.
     */
    private int index = 0;

    /**
     * String array of instruction texts
     */
    private String[] instructions;

    /**
     * Array of table layouts supporting the instructions
     */
    private int[] tablesLayoutIds;

    /**
     * Listener for clicks. Shows the next step.
     */
    private View.OnClickListener clickOnDisplay = new View.OnClickListener() {
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
        initTableArray();
        if (instructions.length != tablesLayoutIds.length) {
            // validation failed
            // this would cause an error, so close the dialog
            this.dismiss();
        }

        skipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        // add the on click listener
        root.setOnClickListener(clickOnDisplay);


        // init index
        index = 0;
        updateViews();

        return layout;
    }

    /**
     * Updates content of the views {@link #instruction} and {@link #table}.
     */
    public void updateViews() {
        instruction.setText(instructions[index]);

        table.removeAllViews();
        int layoutId = tablesLayoutIds[index];
        if (layoutId > 0) {
            inflater.inflate(tablesLayoutIds[index], table);
        }
    }

    /**
     * Initializes the view elements {@link #root}, {@link #instruction}, {@link #table} and {@link #skipButton}.
     *
     * @param layout the dialog layout
     */
    private void initLayoutElements(View layout) {
        root = (RelativeLayout) layout.findViewById(R.id.tutorial_root);
        instruction = (TextView) layout.findViewById(R.id.tutorial_instruction);
        table = (TableLayout) layout.findViewById(R.id.tutorial_table);
        skipButton = (Button) layout.findViewById(R.id.tutorial_btn_skip);
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

    /**
     * Initializes the array {@link #tablesLayoutIds}.
     */
    private void initTableArray() {
        // load table layouts
        tablesLayoutIds = new int[]{
                R.layout.tutorial_table_1,
                R.layout.tutorial_table_2,
                R.layout.tutorial_table_3,
                R.layout.tutorial_table_4,
                R.layout.tutorial_table_5,
                -1 // no table at the last screen
        };
    }

}
