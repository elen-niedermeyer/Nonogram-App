package niedermeyer.nonogram.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.Locale;

import niedermeyer.nonogram.R;

public class OptionsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);

        final Menu menuHelper = new Menu(this);

        final Button setRows = (Button) findViewById(R.id.activity_options_btn_rows);
        setRows.setText(String.format(Locale.getDefault(), "%d", NonogramActivity.numberOfRows));
        setRows.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int number = NonogramActivity.numberOfRows;
                AlertDialog dialog = menuHelper.makeNumberPickerForGameSize(true, false, NonogramActivity.numberOfRows);
                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        if (number != NonogramActivity.numberOfRows) {
                            setRows.setText(String.format(Locale.getDefault(), "%d", NonogramActivity.numberOfRows));
                        }
                    }
                });
                dialog.show();
            }
        });

        final Button setColumns = (Button) findViewById(R.id.activity_options_btn_columns);
        setColumns.setText(String.format(Locale.getDefault(), "%d", NonogramActivity.numberOfColumns));
        setColumns.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int number = NonogramActivity.numberOfColumns;
                AlertDialog dialog = menuHelper.makeNumberPickerForGameSize(true, false, NonogramActivity.numberOfColumns);
                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        if (number != NonogramActivity.numberOfColumns) {
                            setColumns.setText(String.format(Locale.getDefault(), "%d", NonogramActivity.numberOfColumns));
                        }
                    }
                });
                dialog.show();
            }
        });
    }
}
