package niedermeyer.nonogram.activities;

import android.content.DialogInterface;
import android.os.PersistableBundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.Locale;

import niedermeyer.nonogram.R;
import niedermeyer.nonogram.persistence.GameSizeHandler;

public class OptionsActivity extends AppCompatActivity {

    /**
     * Overrides the method {@link AppCompatActivity#onCreate(Bundle, PersistableBundle)}.
     * Sets the layout.
     * Initializes the button for the game size, calling {@link #setPartForGameSize()}.
     *
     * @param savedInstanceState saved information about the activity given by the system
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);

        setPartForGameSize();
    }

    /**
     * Sets the buttons for the game field. Sets on click listener on it, that sets the new size.
     */
    private void setPartForGameSize() {
        final GameSizeHandler gameSizeHandler = new GameSizeHandler(this);
        final MenuActions menuActions = new MenuActions(this);

        // initialize the button for the number of rows
        final Button setRows = (Button) findViewById(R.id.activity_options_btn_rows);
        setRows.setText(String.format(Locale.getDefault(), "%d", GameSizeHandler.numberOfRows));
        setRows.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int number = GameSizeHandler.numberOfRows;
                // get the number picker dialog
                AlertDialog dialog = menuActions.makeNumberPickerForGameSize(true);
                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        if (number != GameSizeHandler.numberOfRows) {
                            // when clicked the positive button and the number was changed
                            setRows.setText(String.format(Locale.getDefault(), "%d", GameSizeHandler.numberOfRows));
                            gameSizeHandler.saveGameSize();
                        }
                    }
                });
                // show the dialog
                dialog.show();
            }
        });

        // initialize the button for the number of columns
        final Button setColumns = (Button) findViewById(R.id.activity_options_btn_columns);
        setColumns.setText(String.format(Locale.getDefault(), "%d", GameSizeHandler.numberOfColumns));
        setColumns.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int number = GameSizeHandler.numberOfColumns;
                // get the number picker dialog
                AlertDialog dialog = menuActions.makeNumberPickerForGameSize(false);
                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        if (number != GameSizeHandler.numberOfColumns) {
                            // when clicked the positive button and the number was changed
                            setColumns.setText(String.format(Locale.getDefault(), "%d", GameSizeHandler.numberOfColumns));
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
