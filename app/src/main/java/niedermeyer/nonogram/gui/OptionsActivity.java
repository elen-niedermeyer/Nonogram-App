package niedermeyer.nonogram.gui;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import java.util.Locale;

import niedermeyer.nonogram.R;
import niedermeyer.nonogram.persistence.GameSizePersistence;

/**
 * @author Elen Niedermeyer, last modified 2017-09-21
 */
public class OptionsActivity extends AppCompatActivity {

    /**
     * Overrides the method {@link AppCompatActivity#onCreate(Bundle, PersistableBundle)}.
     * Sets the layout.
     * Initializes the buttons for the puzzle's size calling {@link #setButtonsForPuzzleSize()}.
     *
     * @param savedInstanceState saved information about the activity given by the system
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);

        setButtonsForPuzzleSize();
    }

    /**
     * Sets the buttons for the game field.
     */
    private void setButtonsForPuzzleSize() {
        setRowNumberButton();
        setColumnNumberButton();
    }

    /**
     * Initializes the button for changing the number of rows in the puzzles.
     * Sets an on-click-listener on it. Opens a dialog and sets the new size globally in the {@link GameSizePersistence}.
     */
    private void setRowNumberButton() {
        final GameSizePersistence gameSizeHandler = new GameSizePersistence(this);
        final NumberPickerDialog numberPickerDialog = new NumberPickerDialog(this);

        // initialize the button for the number of rows
        final Button setRowsButton = (Button) findViewById(R.id.activity_options_btn_rows);
        setRowsButton.setText(String.format(Locale.getDefault(), "%d", GameSizePersistence.numberOfRows));
        // set the on-click-listener
        setRowsButton.setOnClickListener(new View.OnClickListener() {
            /**
             * Makes a number picker dialog.
             *
             * @param v the button for changing the number of rows of the puzzles
             */
            @Override
            public void onClick(View v) {
                final int number = GameSizePersistence.numberOfRows;
                // get number picker dialog
                AlertDialog dialog = numberPickerDialog.makeDialog(true);
                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    /**
                     * Saves the number of rows globally in {@link GameSizePersistence}
                     * @param dialog the closed number picker dialog
                     */
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        if (number != GameSizePersistence.numberOfRows) {
                            // when clicked the positive button and the number was changed
                            setRowsButton.setText(String.format(Locale.getDefault(), "%d", GameSizePersistence.numberOfRows));
                            gameSizeHandler.saveGameSize();
                        }
                    }
                });
                // show the dialog
                dialog.show();
            }
        });
    }

    /**
     * Initializes the button for changing the number of columns in the puzzles.
     * Sets an on-click-listener on it. Opens a dialog and sets the new size globally in the {@link GameSizePersistence}.
     */
    private void setColumnNumberButton() {
        final GameSizePersistence gameSizeHandler = new GameSizePersistence(this);
        final NumberPickerDialog menuActions = new NumberPickerDialog(this);

        // initialize the button for the number of columns
        final Button setColumnsButton = (Button) findViewById(R.id.activity_options_btn_columns);
        setColumnsButton.setText(String.format(Locale.getDefault(), "%d", GameSizePersistence.numberOfColumns));
        // set the on-click-listener
        setColumnsButton.setOnClickListener(new View.OnClickListener() {
            /**
             * Makes a number picker dialog.
             * @param v the button for changing the number of columns of the puzzles
             */
            @Override
            public void onClick(View v) {
                final int number = GameSizePersistence.numberOfColumns;
                // get the number picker dialog
                AlertDialog dialog = menuActions.makeDialog(false);
                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    /**
                     * Saves the number of rows globally in {@link GameSizePersistence}
                     * @param dialog the closed number picker dialog
                     */
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        if (number != GameSizePersistence.numberOfColumns) {
                            // when clicked the positive button and the number was changed
                            setColumnsButton.setText(String.format(Locale.getDefault(), "%d", GameSizePersistence.numberOfColumns));
                            gameSizeHandler.saveGameSize();
                        }
                    }
                });
                // show the dialog
                dialog.show();
            }
        });
    }
}
