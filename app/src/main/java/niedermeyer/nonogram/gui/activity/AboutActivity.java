package niedermeyer.nonogram.gui.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import niedermeyer.nonogram.R;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        // set the toolbar
        Toolbar toolbar = findViewById(R.id.activity_about_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        // set clickable github url
        final Button githubButton = findViewById(R.id.activity_about_btn_github);
        githubButton.setOnClickListener(view -> {
            Uri uri = Uri.parse(getString(R.string.github_link));
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        });
    }
}