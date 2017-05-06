package niedermeyer.nonogram;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

/**
 * @author Elen Niedermeyer, last updated 2017-05-06
 */
public class NonogramActivity extends AppCompatActivity implements OnClickListener {

    public static int numberOfRows;
    public static int numberOfColumns;

    private GameHandler game = new GameHandler(this);

    private ImageButton menuButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_nonogram);

        menuButton = (ImageButton) findViewById(R.id.menu_button);
        menuButton.setOnClickListener(this);

        initializeFieldSizes();

        Message msg = new Message();
        int[] gameSize = {numberOfRows, numberOfColumns};
        msg.obj = gameSize;
        game.sendMessage(msg);
    }

    @Override
    public void onClick(View v) {
        if (v == menuButton) {
            Menu menu = new Menu(this);
            menu.showMenu(v);
        }
    }

    private void initializeFieldSizes() {
        SharedPreferences prefs = getPreferences(Context.MODE_PRIVATE);
        numberOfRows = prefs.getInt(getString(R.string.prefs_rows), 3);
        numberOfColumns = prefs.getInt(getString(R.string.prefs_columns), 3);
    }
}
