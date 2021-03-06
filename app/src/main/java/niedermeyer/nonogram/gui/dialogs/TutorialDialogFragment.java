package niedermeyer.nonogram.gui.dialogs;

import android.content.Context;
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

    private final int[][] emptyUserField = new int[][]{
            {NonogramConstants.FIELD_NO_DECISION, NonogramConstants.FIELD_NO_DECISION, NonogramConstants.FIELD_NO_DECISION, NonogramConstants.FIELD_NO_DECISION},
            {NonogramConstants.FIELD_NO_DECISION, NonogramConstants.FIELD_NO_DECISION, NonogramConstants.FIELD_NO_DECISION, NonogramConstants.FIELD_NO_DECISION},
            {NonogramConstants.FIELD_NO_DECISION, NonogramConstants.FIELD_NO_DECISION, NonogramConstants.FIELD_NO_DECISION, NonogramConstants.FIELD_NO_DECISION},
            {NonogramConstants.FIELD_NO_DECISION, NonogramConstants.FIELD_NO_DECISION, NonogramConstants.FIELD_NO_DECISION, NonogramConstants.FIELD_NO_DECISION}
    };

    private final int[][] solvedUserField = new int[][]{
            {NonogramConstants.FIELD_FILLED, NonogramConstants.FIELD_FILLED, NonogramConstants.FIELD_FILLED, NonogramConstants.FIELD_EMPTY},
            {NonogramConstants.FIELD_FILLED, NonogramConstants.FIELD_NO_DECISION, NonogramConstants.FIELD_FILLED, NonogramConstants.FIELD_FILLED},
            {NonogramConstants.FIELD_FILLED, NonogramConstants.FIELD_FILLED, NonogramConstants.FIELD_FILLED, NonogramConstants.FIELD_FILLED},
            {NonogramConstants.FIELD_FILLED, NonogramConstants.FIELD_EMPTY, NonogramConstants.FIELD_EMPTY, NonogramConstants.FIELD_FILLED}
    };

    private final NonogramGenerator nonogramGenerator = new NonogramGenerator();

    /**
     * layout elements
     */
    private TextView title;
    private TextView instruction;
    private RelativeLayout tableContainer;
    private Button nextButton;
    private Button previousButton;

    /**
     * Index for the arrays {@link #instructions}.
     */
    private int index = 0;

    /**
     * String array of instruction title texts
     */
    private String[] titles;

    /**
     * String array of instruction texts
     */
    private String[] instructions;

    /**
     * Listener for clicks. Shows the next step.
     */
    private final View.OnClickListener clickNext = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // increase index
            index++;
            if (index < instructions.length) {
                // if it's not the end of the array, update views
                updateViews(v.getContext());
            } else {
                dismiss();
            }
        }
    };

    /**
     * Listener for clicks. Shows the previous step.
     */
    private final View.OnClickListener clickPrevious = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // increase index
            index--;
            if (index >= 0) {
                // if it's not the end of the array, update views
                updateViews(v.getContext());
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
        View layout = inflater.inflate(R.layout.tutorial, container, false);
        initLayoutElements(layout);

        initTitleArray();
        initInstructionArray();

        nonogramGenerator.setNonogram(nonogram);

        // init index
        index = 0;
        updateViews(container.getContext());

        return layout;
    }

    /**
     * Updates content of the views {@link #title}, {@link #instruction} and {@link #tableContainer}.
     */
    public void updateViews(Context pContext) {
        title.setText(titles[index]);
        instruction.setText(instructions[index]);

        if (index == 0) {
            tableContainer.removeAllViews();
            TableLayout table = new PuzzleDisplayer(pContext).getGameView(emptyUserField, nonogramGenerator.getRowCount(), nonogramGenerator.getColumnCount(), 100, null);
            tableContainer.addView(table);

            previousButton.setVisibility(View.INVISIBLE);

        } else if (index == 1) {
            // set table when going forward
            tableContainer.removeAllViews();
            tableContainer.addView(new PuzzleDisplayer(pContext).getGameView(solvedUserField, nonogramGenerator.getRowCount(), nonogramGenerator.getColumnCount(), 100, null));

            // set previous button for following views
            previousButton.setVisibility(View.VISIBLE);

        } else if (index == instructions.length - 2) {
            // set table when going backwards
            tableContainer.removeAllViews();
            tableContainer.addView(new PuzzleDisplayer(pContext).getGameView(solvedUserField, nonogramGenerator.getRowCount(), nonogramGenerator.getColumnCount(), 100, null));

            // set button text when going backwards
            nextButton.setText(R.string.next);

        } else if (index == instructions.length - 1) {
            tableContainer.removeAllViews();

            nextButton.setText(R.string.close);
        }
    }

    /**
     * Initializes the view elements {@link #title}, {@link #instruction}, {@link #tableContainer}, {@link #nextButton} and {@link #previousButton}.
     * Sets {@link android.view.View.OnClickListener} to the buttons.
     *
     * @param layout the dialog layout
     */
    private void initLayoutElements(View layout) {
        title = layout.findViewById(R.id.tutorial_title);
        instruction = layout.findViewById(R.id.tutorial_instruction);
        tableContainer = layout.findViewById(R.id.tutorial_table_container);

        nextButton = layout.findViewById(R.id.tutorial_btn_next);
        nextButton.setOnClickListener(clickNext);
        previousButton = layout.findViewById(R.id.tutorial_btn_previous);
        previousButton.setOnClickListener(clickPrevious);

        layout.findViewById(R.id.tutorial_btn_skip).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    /**
     * Initializes the array {@link #titles}.
     */
    private void initTitleArray() {
        // load strings
        titles = new String[]{getString(R.string.instruction_title_1),
                getString(R.string.instruction_title_2),
                getString(R.string.instruction_title_3),
                getString(R.string.instruction_title_4),
                getString(R.string.instruction_title_5)};
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
                getString(R.string.instruction_5)};
    }

}
