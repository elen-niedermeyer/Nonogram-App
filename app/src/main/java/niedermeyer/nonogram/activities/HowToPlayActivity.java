package niedermeyer.nonogram.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import niedermeyer.nonogram.R;

/**
 * @author Elen Niedermeyer, last modified 2017-08-10
 */
public class HowToPlayActivity extends AppCompatActivity implements OnClickListener {

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
    private Button skip;

    /**
     * Index for the arrays {@link #instructions} and {@link #tables}.
     * Instruction and table with the same index are a pair.
     */
    private int index = 0;

    /**
     * String array of instructions
     */
    private String[] instructions;

    /**
     * Array of table layouts supporting the instructions
     */
    private TableLayout[] tables;

    /**
     * Overrides the method {@link OnClickListener#onClick(View)}.
     * Update the instruction with the next step.
     * Increases the {@link #index} and sets the next string and table.
     * If the end of the array is reached, start the {@link NonogramActivity} by an intent to start a game.
     *
     * @param v the clicked button view, it can only be {@link #root}
     */
    @Override
    public void onClick(View v) {
        // increase index
        index++;
        if (index < instructions.length) {
            // if it's not the end of the array, update views
            instruction.setText(instructions[index]);
            root.removeViewAt(INDEX_POSITION_TABLE);
            root.addView(tables[index], INDEX_POSITION_TABLE);
            if (index == instructions.length - 1) {
                // if it's the last step, change the string below
                clickText.setText(getString(R.string.click_here_new_game));
            }
        } else {
            changeToNonogramActivity();
        }
    }

    /**
     * Overrides the method {@link AppCompatActivity#onCreate(Bundle, PersistableBundle)}.
     * Sets the layout. Initializes the views {@link #root}, {@link #instruction}, {@link #clickText} and {@link #skip}. Sets an on click listener for {@link #skip}.
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

        skip = (Button) findViewById(R.id.activity_how_to_play_btn_skip);
        // start the nonogram activity, when the skip button was clicked
        skip.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                changeToNonogramActivity();
            }
        });

        index = 0;
        // load strings
        instructions = new String[]{getString(R.string.instruction_1),
                getString(R.string.instruction_2),
                getString(R.string.instruction_3),
                getString(R.string.instruction_4),
                getString(R.string.instruction_5)};
        // load table layouts
        tables = new TableLayout[]{(TableLayout) LayoutInflater.from(this).inflate(R.layout.activity_how_to_play_table_1, null),
                (TableLayout) LayoutInflater.from(this).inflate(R.layout.activity_how_to_play_table_2, null),
                (TableLayout) LayoutInflater.from(this).inflate(R.layout.activity_how_to_play_table_3, null),
                (TableLayout) LayoutInflater.from(this).inflate(R.layout.activity_how_to_play_table_4, null),
                (TableLayout) LayoutInflater.from(this).inflate(R.layout.activity_how_to_play_table_5, null)};

        // sets the instruction text
        instruction.setText(instructions[index]);
        // adds the table
        root.addView(tables[index], INDEX_POSITION_TABLE);

        // adds the on click listener
        root.setOnClickListener(this);
    }

    /**
     * Sets the index to zero.
     * Start the nonogram activity.
     */
    private void changeToNonogramActivity() {
        // it's the end of the array, start game
        index = 0;
        startActivity(new Intent(this, NonogramActivity.class));
    }
}
