package niedermeyer.nonogram.activities;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.Locale;

import niedermeyer.nonogram.R;
import niedermeyer.nonogram.persistence.GameSizeHandler;

public class OptionsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);

        setPartForGameSize();
    }

    private void setPartForGameSize() {
        final GameSizeHandler gameSizeHandler = new GameSizeHandler(this);
        gameSizeHandler.initializeFieldSizes();
        final Menu menuHelper = new Menu(this);

        final Button setRows = (Button) findViewById(R.id.activity_options_btn_rows);
        setRows.setText(String.format(Locale.getDefault(), "%d", GameSizeHandler.numberOfRows));
        setRows.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int number = GameSizeHandler.numberOfRows;
                AlertDialog dialog = menuHelper.makeNumberPickerForGameSize(true);
                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        if (number != GameSizeHandler.numberOfRows) {
                            setRows.setText(String.format(Locale.getDefault(), "%d", GameSizeHandler.numberOfRows));
                            gameSizeHandler.saveGameSize();
                        }
                    }
                });
                dialog.show();
            }
        });

        final Button setColumns = (Button) findViewById(R.id.activity_options_btn_columns);
        setColumns.setText(String.format(Locale.getDefault(), "%d", GameSizeHandler.numberOfColumns));
        setColumns.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int number = GameSizeHandler.numberOfColumns;
                AlertDialog dialog = menuHelper.makeNumberPickerForGameSize(false);
                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        if (number != GameSizeHandler.numberOfColumns) {
                            setColumns.setText(String.format(Locale.getDefault(), "%d", GameSizeHandler.numberOfColumns));
                            gameSizeHandler.saveGameSize();
                        }
                    }
                });
                dialog.show();
            }
        });
    }
}
