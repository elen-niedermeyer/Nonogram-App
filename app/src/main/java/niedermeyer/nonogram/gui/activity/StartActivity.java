package niedermeyer.nonogram.gui.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import niedermeyer.nonogram.R;
import niedermeyer.nonogram.gui.dialog.DialogHelper;

public class StartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        // set buttons
        findViewById(R.id.activity_start_btn_play).setOnClickListener(view -> startActivity(new Intent(view.getContext(), GameActivity.class)));
        findViewById(R.id.activity_start_btn_statistics).setOnClickListener(view -> startActivity(new Intent(view.getContext(), StatisticsActivity.class)));
        findViewById(R.id.activity_start_btn_options).setOnClickListener(view -> startActivity(new Intent(view.getContext(), OptionsActivity.class)));
        findViewById(R.id.activity_start_btn_tutorial).setOnClickListener(view -> new DialogHelper().openTutorialDialogFullscreen(getSupportFragmentManager()));

        // sets the toolbar
        Toolbar toolbar = findViewById(R.id.activity_start_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
    }

}