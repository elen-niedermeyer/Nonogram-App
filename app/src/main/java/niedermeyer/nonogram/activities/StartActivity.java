package niedermeyer.nonogram.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import niedermeyer.nonogram.R;


/**
 * @author Elen Niedermeyer, last modified 2017-08-27
 */
public class StartActivity extends AppCompatActivity implements OnClickListener {

    /**
     * Buttons in the start activity
     */
    private Button playButton;
    private Button highscoreButton;
    private Button howToPlayButton;
    private Button optionsButton;

    /**
     * Overrides the method {@link OnClickListener#onClick(View)}.
     * Makes an intent. It starts the {@link NonogramActivity}, {@link StatisticsActivity}, {@link HowToPlayActivity} or {@link OptionsActivity} according to which button was clicked.
     *
     * @param v the clicked button view, can be {@link #playButton}, {@link #highscoreButton}, {@link #howToPlayButton} or {@link #optionsButton}
     */
    @Override
    public void onClick(View v) {
        Intent i = null;
        if (v == playButton) {
            i = new Intent(this, NonogramActivity.class);
        } else if (v == highscoreButton) {
            i = new Intent(this, StatisticsActivity.class);
        } else if (v == howToPlayButton) {
            i = new Intent(this, HowToPlayActivity.class);
        } else if (v == optionsButton) {
            i = new Intent(this, OptionsActivity.class);
        }
        startActivity(i);
    }

    /**
     * Overrides the method {@link AppCompatActivity#onCreate(Bundle, PersistableBundle)}.
     * Sets the layout.
     * Initializes the button {@link #playButton}, {@link #highscoreButton}, {@link #howToPlayButton} and {@link #optionsButton} and sets the listener {@link #onClick(View)} for all buttons.
     *
     * @param savedInstanceState saved information about the activity given by the system
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        playButton = (Button) findViewById(R.id.activity_start_buttons_play);
        playButton.setOnClickListener(this);

        highscoreButton = (Button) findViewById(R.id.activity_start_buttons_highscore);
        highscoreButton.setOnClickListener(this);

        howToPlayButton = (Button) findViewById(R.id.activity_start_buttons_howto);
        howToPlayButton.setOnClickListener(this);

        optionsButton = (Button) findViewById(R.id.activity_start_buttons_options);
        optionsButton.setOnClickListener(this);
    }
}