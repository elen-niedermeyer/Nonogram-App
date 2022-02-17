package niedermeyer.nonogram.gui.activity;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.Gravity;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.Locale;

import niedermeyer.nonogram.R;
import niedermeyer.nonogram.logics.NonogramConstants;
import niedermeyer.nonogram.persistence.StatisticsPersistence;

/**
 * @author Elen Niedermeyer, last modified 2022-02-14
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
        boolean isEmpty = addRows();
        if (isEmpty) {
            setContentView(R.layout.activity_statistics_empty);
        }

        // sets the toolbar
        Toolbar toolbar = findViewById(R.id.activity_statistics_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
    }

    /**
     * Gets for each possible size the number of solved puzzles.
     * Loads the data from an instance if {@link StatisticsPersistence}.
     * Makes a row in the table layout if a puzzle of the size was solved.
     *
     * @return
     */
    private boolean addRows() {
        StatisticsPersistence persistence = new StatisticsPersistence(this);

        // get the table layout
        TableLayout table = findViewById(R.id.activity_statistics_table);

        boolean isEmpty = true;
        // iterate over the number of possible rows
        for (int rowCount = NonogramConstants.NONOGRAM_SIZE_MINIMUM; rowCount <= NonogramConstants.NONOGRAM_SIZE_MAXIMUM; rowCount++) {
            // iterate over the columns, only column numbers that are equals or greater then the row's number
            for (int columnCount = rowCount; columnCount <= NonogramConstants.NONOGRAM_SIZE_MAXIMUM; columnCount++) {

                int numberOfSavedPuzzles = persistence.getCountOfSSolvedPuzzles(rowCount, columnCount);
                // add a column if the user solved a puzzle of the given size
                if (numberOfSavedPuzzles > 0) {
                    isEmpty = false;
                    // make a new row
                    TableRow row = new TableRow(this);

                    // make the view with the puzzle's size
                    TextView puzzleSizeView = new TextView(this, null, R.style.TextAppearance_MaterialComponents_Body1);
                    puzzleSizeView.setTextAppearance(this, R.style.TextAppearance_MaterialComponents_Body1);
                    puzzleSizeView.setText(String.format(Locale.getDefault(), "%1$d %2$s %3$d", rowCount, getString(R.string.size_separator), columnCount));
                    puzzleSizeView.setGravity(Gravity.CENTER);

                    // make the view with the number of solved puzzles
                    TextView numberOfSolvedPuzzlesView = new TextView(this, null, R.style.TextAppearance_MaterialComponents_Body1);
                    numberOfSolvedPuzzlesView.setTextAppearance(this, R.style.TextAppearance_MaterialComponents_Body1);
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

        return isEmpty;
    }
}