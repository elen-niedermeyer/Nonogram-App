package niedermeyer.nonogram;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;

/**
 * @author Elen Niedermeyer, last updated 2017-05-12
 */
public class NonogramActivity extends AppCompatActivity {

    public static int numberOfRows;
    public static int numberOfColumns;

    private GameHandler game = new GameHandler(this);
    private Menu menu = new Menu(this);

    private ImageButton menuButton;
    private Button newGameButton;
    private Button resetGameButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_nonogram);

        // initialize new game button
        newGameButton = (Button) findViewById(R.id.new_game_button);
        newGameButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                game.newGame();
            }
        });

        // initialize reset game button
        resetGameButton = (Button) findViewById(R.id.reset_button);
        resetGameButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                game.resetGame();
            }
        });

        // initialize menu button
        menuButton = (ImageButton) findViewById(R.id.menu_button);
        menuButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v == menuButton) {
                    menu.showMenu(v);
                }
            }
        });

        initializeFieldSizes();

        game.newGame();
    }

    private void initializeFieldSizes() {
        SharedPreferences prefs = getPreferences(Context.MODE_PRIVATE);
        numberOfRows = prefs.getInt(getString(R.string.prefs_rows), 3);
        numberOfColumns = prefs.getInt(getString(R.string.prefs_columns), 3);
    }
}
