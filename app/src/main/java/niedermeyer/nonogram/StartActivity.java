package niedermeyer.nonogram;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class StartActivity extends AppCompatActivity implements OnClickListener {

    private Button playButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        playButton = (Button) findViewById(R.id.activity_start_buttons_play);
        playButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == playButton) {
            Intent i = new Intent(this, NonogramActivity.class);
            startActivity(i);
        }
    }
}
