package niedermeyer.nonogram.gui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.Locale;

import niedermeyer.nonogram.R;
import niedermeyer.nonogram.gui.dialog.DialogHelper;
import niedermeyer.nonogram.persistence.GameOptionsPersistence;

public class OptionsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);

        // set the toolbar
        Toolbar toolbar = findViewById(R.id.activity_options_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        // load and set game field options
        final GameOptionsPersistence gameOptions = new GameOptionsPersistence(this);
        final Button fieldSizeButton = findViewById(R.id.activity_options_btn_field_size);
        fieldSizeButton.setText(String.format(Locale.getDefault(), "%1$d %2$s %3$d", gameOptions.getNumberOfRows(), getString(R.string.size_separator), gameOptions.getNumberOfColumns()));
        fieldSizeButton.setOnClickListener(view -> new DialogHelper().openFieldSizeDialog(getLayoutInflater(), dialogInterface -> fieldSizeButton.setText(String.format(Locale.getDefault(), "%1$d %2$s %3$d", gameOptions.getNumberOfRows(), getString(R.string.size_separator), gameOptions.getNumberOfColumns()))));

        // set about button
        final Button aboutButton = findViewById(R.id.activity_options_btn_about);
        aboutButton.setOnClickListener(view -> startActivity(new Intent(view.getContext(), AboutActivity.class)));
    }

}
