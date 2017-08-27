package niedermeyer.nonogram.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import niedermeyer.nonogram.R;
import niedermeyer.nonogram.logics.NonogramConstants;
import niedermeyer.nonogram.persistence.StatisticsPersistence;

public class StatisticsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);
        addRows();
    }

    private void addRows() {
        StatisticsPersistence persistence = new StatisticsPersistence(this);

        TableLayout table = (TableLayout) findViewById(R.id.activity_statistics_table);

        for (int rowCount = NonogramConstants.NONOGRAM_SIZE_MINIMUM; rowCount <= NonogramConstants.NONOGRAM_SIZE_MAXIMUM; rowCount++) {
            for (int columnCount = NonogramConstants.NONOGRAM_SIZE_MINIMUM; columnCount <= NonogramConstants.NONOGRAM_SIZE_MAXIMUM; columnCount++) {
                int score = persistence.getScore(rowCount, columnCount);
                if (score > 0) {

                    TableRow row = new TableRow(this);

                    TextView size = new TextView(this, null, R.style.Text);
                    size.setText(String.format(getString(R.string.game_size_row_column), rowCount, columnCount));
                    size.setGravity(Gravity.CENTER);
                    // TODO: 27.08.2017 Why is the text size not in the style above
                    size.setTextSize(18);

                    TextView solvedPuzzles = new TextView(this, null, R.style.Text);
                    solvedPuzzles.setText(Integer.toString(score));
                    solvedPuzzles.setGravity(Gravity.CENTER);
                    // TODO: 27.08.2017 Why is the text size not in the style above
                    solvedPuzzles.setTextSize(18);

                    row.addView(size);
                    row.addView(solvedPuzzles);

                    table.addView(row);
                }
            }
        }
    }
}
