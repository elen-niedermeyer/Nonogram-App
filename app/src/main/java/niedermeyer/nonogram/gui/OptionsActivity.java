package niedermeyer.nonogram.gui;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.Locale;

import niedermeyer.nonogram.R;
import niedermeyer.nonogram.gui.dialogs.DialogHelper;
import niedermeyer.nonogram.persistence.PuzzleSizePersistence;

/**
 * @author Elen Niedermeyer, last modified 2020-10-16
 */
public class OptionsActivity extends AppCompatActivity {

    /**
     * Overrides the method {@link AppCompatActivity#onCreate(Bundle, PersistableBundle)}.
     * Sets the layout.
     * Initializes the buttons.
     *
     * @param savedInstanceState saved information about the activity given by the system
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);

        // sets the toolbar
        Toolbar toolbar = findViewById(R.id.activity_options_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        final Button fieldSizeButton = findViewById(R.id.activity_options_btn_field_size);
        fieldSizeButton.setText(String.format(Locale.getDefault(), "%1$d %2$s %3$d", PuzzleSizePersistence.numberOfRows, getString(R.string.size_separator), PuzzleSizePersistence.numberOfColumns));
        fieldSizeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DialogHelper().openFieldSizeDialog(getLayoutInflater(), new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        fieldSizeButton.setText(String.format(Locale.getDefault(), "%1$d %2$s %3$d", PuzzleSizePersistence.numberOfRows, getString(R.string.size_separator), PuzzleSizePersistence.numberOfColumns));
                    }
                });
            }
        });
    }

}
