package niedermeyer.nonogram.gui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;

import niedermeyer.nonogram.R;

/**
 * @author Elen Niedermeyer, last modified 2020-10-13
 */
public class StartActivity extends AppCompatActivity {

    /**
     * Buttons in the start activity
     */
    private Button playButton;
    private Button highscoreButton;
    private Button tutorialButton;
    private Button optionsButton;

    /**
     * Listener for clicks on the buttons.
     */
    private OnClickListener buttonClick = new OnClickListener() {
        /**
         * Overrides the method {@link OnClickListener#onClick(View)}.
         * Makes an intent. It starts the {@link GameActivity}, {@link StatisticsActivity}, the tutorial through {@link DialogHelper#openTutorialDialogFullscreen(FragmentManager)} or {@link OptionsActivity} according to which button was clicked.
         *
         * @param v the clicked button view, can be {@link StartActivity#playButton}, {@link StartActivity#highscoreButton}, {@link StartActivity#tutorialButton} or {@link StartActivity#optionsButton}
         */
        @Override
        public void onClick(View v) {
            Intent i = null;
            Context context = v.getContext();
            if (v == playButton) {
                i = new Intent(context, GameActivity.class);
                startActivity(i);
            } else if (v == highscoreButton) {
                i = new Intent(context, StatisticsActivity.class);
                startActivity(i);
            } else if (v == tutorialButton) {
                new DialogHelper().openTutorialDialogFullscreen(getSupportFragmentManager());
            } else if (v == optionsButton) {
                i = new Intent(context, OptionsActivity.class);
                startActivity(i);
            }
        }
    };

    /**
     * Overrides the method {@link AppCompatActivity#onCreate(Bundle, PersistableBundle)}.
     * Sets the layout.
     * Initializes the button {@link #playButton}, {@link #highscoreButton}, {@link #tutorialButton} and {@link #optionsButton} and sets the listener {@link #buttonClick} for all buttons.
     * Initializes the toolbar.
     *
     * @param savedInstanceState saved information about the activity given by the system
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        playButton = findViewById(R.id.activity_start_btn_play);
        playButton.setOnClickListener(buttonClick);

        highscoreButton = findViewById(R.id.activity_start_btn_statistics);
        highscoreButton.setOnClickListener(buttonClick);

        tutorialButton = findViewById(R.id.activity_start_btn_tutorial);
        tutorialButton.setOnClickListener(buttonClick);

        optionsButton = findViewById(R.id.activity_start_btn_options);
        optionsButton.setOnClickListener(buttonClick);

        // sets the toolbar
        Toolbar toolbar = findViewById(R.id.activity_start_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}