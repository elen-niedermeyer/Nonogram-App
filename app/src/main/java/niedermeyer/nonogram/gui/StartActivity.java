package niedermeyer.nonogram.gui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import niedermeyer.nonogram.R;

/**
 * @author Elen Niedermeyer, last modified 2017-09-21
 */
public class StartActivity extends AppCompatActivity {

    /**
     * Buttons in the start activity
     */
    private Button playButton;
    private Button highscoreButton;
    private Button howToPlayButton;
    private Button optionsButton;

    /**
     * Listener for clicks on the buttons.
     */
    private OnClickListener buttonClick = new OnClickListener() {
        /**
         * Overrides the method {@link OnClickListener#onClick(View)}.
         * Makes an intent. It starts the {@link NonogramActivity}, {@link StatisticsActivity}, {@link InstructionActivity} or {@link OptionsActivity} according to which button was clicked.
         *
         * @param v the clicked button view, can be {@link StartActivity#playButton}, {@link StartActivity#highscoreButton}, {@link StartActivity#howToPlayButton} or {@link StartActivity#optionsButton}
         */
        @Override
        public void onClick(View v) {
            Intent i = null;
            Context context = v.getContext();
            if (v == playButton) {
                i = new Intent(context, NonogramActivity.class);
            } else if (v == highscoreButton) {
                i = new Intent(context, StatisticsActivity.class);
            } else if (v == howToPlayButton) {
                i = new Intent(context, InstructionActivity.class);
            } else if (v == optionsButton) {
                i = new Intent(context, OptionsActivity.class);
            }
            startActivity(i);
        }
    };

    /**
     * Overrides the method {@link AppCompatActivity#onCreate(Bundle, PersistableBundle)}.
     * Sets the layout.
     * Initializes the button {@link #playButton}, {@link #highscoreButton}, {@link #howToPlayButton} and {@link #optionsButton} and sets the listener {@link #buttonClick} for all buttons.
     *
     * @param savedInstanceState saved information about the activity given by the system
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        playButton = (Button) findViewById(R.id.activity_start_buttons_play);
        playButton.setOnClickListener(buttonClick);

        highscoreButton = (Button) findViewById(R.id.activity_start_buttons_highscore);
        highscoreButton.setOnClickListener(buttonClick);

        howToPlayButton = (Button) findViewById(R.id.activity_start_buttons_howto);
        howToPlayButton.setOnClickListener(buttonClick);

        optionsButton = (Button) findViewById(R.id.activity_start_buttons_options);
        optionsButton.setOnClickListener(buttonClick);
    }
}