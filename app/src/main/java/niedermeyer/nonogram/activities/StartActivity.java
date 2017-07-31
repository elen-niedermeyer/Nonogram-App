package niedermeyer.nonogram.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import niedermeyer.nonogram.R;


/**
 * @author Elen Niedermeyer
 *         Last update 2017-07-16
 */

public class StartActivity extends AppCompatActivity implements OnClickListener {

    private Button playButton;
    private Button optionsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        playButton = (Button) findViewById(R.id.activity_start_buttons_play);
        playButton.setOnClickListener(this);

        optionsButton = (Button) findViewById(R.id.activity_start_buttons_options);
        optionsButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == playButton) {
            Intent i = new Intent(this, NonogramActivity.class);
            startActivity(i);
        } else if (v == optionsButton) {
            Intent i = new Intent(this, OptionsActivity.class);
            startActivity(i);
        }
    }
}
