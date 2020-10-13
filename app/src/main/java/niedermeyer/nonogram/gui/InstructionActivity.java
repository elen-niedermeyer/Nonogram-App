package niedermeyer.nonogram.gui;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import niedermeyer.nonogram.R;

/**
 * @author Elen Niedermeyer, last modified 2017-09-30
 */
public class InstructionActivity extends AppCompatActivity {

    /**
     * Constant for the position of the table, third element in root layout
     */
    private static final int INDEX_POSITION_TABLE = 2;

    /**
     * Views
     */
    private LinearLayout root;
    private TextView instruction;
    private TextView clickText;
    private Button skipButton;

    /**
     * Index for the arrays {@link #instructions} and {@link #tables}.
     * Instruction and table with the same index are a pair.
     */
    private int index = 0;

    /**
     * String array of instruction texts
     */
    private String[] instructions;

    /**
     * Array of table layouts supporting the instructions
     */
    private TableLayout[] tables;

    /**
     * Listener for clicks. Shows the next step.
     */
    private OnClickListener clickOnDisplay = new OnClickListener() {
        /**
         * Overrides the method {@link OnClickListener#onClick(View)}.
         * Updates the instruction with the next step.
         * Increases the {@link InstructionActivity#index} and sets the next string and table.
         * If the end of the array is reached, start the {@link NonogramActivity} by an intent to start a game.
         *
         * @param v the clicked view, it can only be {@link InstructionActivity#root}
         */
        @Override
        public void onClick(View v) {
            // increase index
            index++;
            if (index < instructions.length) {
                // if it's not the end of the array, update views
                instruction.setText(instructions[index]);
                root.removeViewAt(INDEX_POSITION_TABLE);
                // adds the table
                root.addView(tables[index], INDEX_POSITION_TABLE);

                if (index == instructions.length - 1) {
                    // if it's the last step, change the string below
                    clickText.setText(getString(R.string.click_here_new_game));
                }
            } else {
                changeToNonogramActivity();
            }
        }
    };

    /**
     * Overrides the method {@link AppCompatActivity#onCreate(Bundle, PersistableBundle)}.
     * Sets the layout.
     * Initializes the views {@link #root}, {@link #instruction}, {@link #clickText} and {@link #skipButton}. Sets an on click listener for {@link #skipButton}.
     * Initializes the arrays {@link #instructions} and {@link #tables}. Sets the first instruction pair of string and table.
     * Sets the on click listener at the root view.
     *
     * @param savedInstanceState saved information about the activity given by the system
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_how_to_play);

        root = (LinearLayout) findViewById(R.id.activity_how_to_play_root);
        instruction = (TextView) findViewById(R.id.activity_how_to_play_description);
        clickText = (TextView) findViewById(R.id.activity_how_to_play_text_click);

        skipButton = (Button) findViewById(R.id.activity_how_to_play_btn_skip);
        // start the nonogram activity, when the skip button was clicked
        skipButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                changeToNonogramActivity();
            }
        });

        // start the instruction
        index = 0;
        // load strings
        instructions = new String[]{getString(R.string.instruction_1),
                getString(R.string.instruction_2),
                getString(R.string.instruction_3),
                getString(R.string.instruction_4),
                getString(R.string.instruction_5),
                getString(R.string.instruction_6)};
        // load table layouts
        tables = initializeTablesArray();

        // set the first instruction text
        instruction.setText(instructions[index]);
        // add the first table
        root.addView(tables[index], INDEX_POSITION_TABLE);

        // add the on click listener
        root.setOnClickListener(clickOnDisplay);
    }

    /**
     * Initializes the {@link #tables} array.
     * Loads the layouts and makes the views clickable by using the {@link #clickOnDisplay} listener.
     *
     * @return an array containing table layouts
     */
    private TableLayout[] initializeTablesArray() {
        // load table layouts
        TableLayout[] tables = new TableLayout[]{
                (TableLayout) LayoutInflater.from(this).inflate(R.layout.activity_how_to_play_table_1, null),
                (TableLayout) LayoutInflater.from(this).inflate(R.layout.activity_how_to_play_table_2, null),
                (TableLayout) LayoutInflater.from(this).inflate(R.layout.activity_how_to_play_table_3, null),
                (TableLayout) LayoutInflater.from(this).inflate(R.layout.activity_how_to_play_table_4, null),
                (TableLayout) LayoutInflater.from(this).inflate(R.layout.activity_how_to_play_table_5, null),
                new TableLayout(this)};

        // make parameters and layout weight
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1f);
        for (TableLayout table : tables) {
            // set table parameter
            table.setLayoutParams(params);
            // make the field clickable and set a listener to go to the next step
            table.setClickable(true);
            table.setOnClickListener(clickOnDisplay);

            // iterate over the buttons in the table and set the same on click listener
            for (int i = 0; i < table.getChildCount(); i++) {
                View child = table.getChildAt(i);
                if (child instanceof TableRow) {
                    TableRow row = (TableRow) child;
                    for (int r = 0; r < row.getChildCount(); r++) {
                        View childOfRow = row.getChildAt(r);
                        childOfRow.setClickable(true);
                        childOfRow.setOnClickListener(clickOnDisplay);
                    }
                }
            }
        }
        return tables;
    }

    /**
     * Starts the nonogram activity.
     * Used after the end of the instruction or when the user skips it.
     */
    private void changeToNonogramActivity() {
        startActivity(new Intent(this, NonogramActivity.class));
    }
}
