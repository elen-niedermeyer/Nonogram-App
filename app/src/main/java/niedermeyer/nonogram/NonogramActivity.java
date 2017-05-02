package niedermeyer.nonogram;

import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class NonogramActivity extends AppCompatActivity {

    GameHandler game = new GameHandler(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_nonogram);

        Message msg = new Message();
        int[] gameSize = {3, 2};
        msg.obj = gameSize;
        game.sendMessage(msg);
    }
}
