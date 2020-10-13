package niedermeyer.nonogram.gui;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.Gravity;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

import niedermeyer.nonogram.R;
import niedermeyer.nonogram.logics.NonogramConstants;
import niedermeyer.nonogram.persistence.StatisticsPersistence;

/**
 * @author Elen Niedermeyer, last modified 2017-09-21
 */
public class StatisticsActivity extends AppCompatActivity {

    /**
     * Overrides the method {@link AppCompatActivity#onCreate(Bundle, PersistableBundle)}.
     * Sets the layout.
     * Makes a table with the statistics calling {@link #addRows()}.
     *
     * @param savedInstanceState saved information about the activity given by the system
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        // add rows to the table given in the layout
        addRows();
    }

    /**
     * Gets for each possible size the number of solved puzzles.
     * Loads the data from an instance if {@link StatisticsPersistence}.
     * Makes a row in the table layout if a puzzle of the size was solved.
     */
    private void addRows() {
        StatisticsPersistence persistence = new StatisticsPersistence(this);

        // get the table layout
        TableLayout table = (TableLayout) findViewById(R.id.activity_statistics_table);

        // iterate over the number of possible rows
        for (int rowCount = NonogramConstants.NONOGRAM_SIZE_MINIMUM; rowCount <= NonogramConstants.NONOGRAM_SIZE_MAXIMUM; rowCount++) {
            // iterate over the columns, only column numbers that are equals or greater then the row's number
            for (int columnCount = rowCount; columnCount <= NonogramConstants.NONOGRAM_SIZE_MAXIMUM; columnCount++) {

                int numberOfSavedPuzzles = persistence.getCountOfSSolvedPuzzles(rowCount, columnCount);
                // add a column if the user solved a puzzle of the given size
                if (numberOfSavedPuzzles > 0) {

                    // make a new row
                    TableRow row = new TableRow(this);

                    // make the view with the puzzle's size
                    TextView puzzleSizeView = new TextView(this, null, R.style.Text);
                    puzzleSizeView.setTextAppearance(this, R.style.Text);
                    puzzleSizeView.setText(String.format(getString(R.string.puzzle_size_row_column), rowCount, columnCount));
                    puzzleSizeView.setGravity(Gravity.CENTER);

                    // make the view with the number of solved puzzles
                    TextView numberOfSolvedPuzzlesView = new TextView(this, null, R.style.Text);
                    numberOfSolvedPuzzlesView.setTextAppearance(this, R.style.Text);
                    numberOfSolvedPuzzlesView.setText(String.format(Locale.getDefault(), "%1$d", numberOfSavedPuzzles));
                    numberOfSolvedPuzzlesView.setGravity(Gravity.CENTER);

                    // add the views to the row
                    row.addView(puzzleSizeView);
                    row.addView(numberOfSolvedPuzzlesView);
                    // add the row to the table
                    table.addView(row);
                }
            }
        }
    }
}