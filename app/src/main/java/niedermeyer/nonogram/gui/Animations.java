package niedermeyer.nonogram.gui;

import android.app.Activity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import niedermeyer.nonogram.R;

/**
 * @author Elen Niedermeyer, last modified 2017-09-30
 */
public class Animations {

    /**
     * Shows the won animation.
     * Brings an new view in foreground. It's invisible again if there's a click on it.
     *
     * @param pActivity an instance of {@link NonogramActivity}
     */
    public void showWonAnimation(final NonogramActivity pActivity) {
        final ScrollView gameView = (ScrollView) pActivity.findViewById(R.id.activity_nonogram_scroll_vertical);
        final LinearLayout animatedView = (LinearLayout) pActivity.findViewById(R.id.activity_nonogram_animation_won);
        animatedView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameView.setVisibility(View.VISIBLE);
                animatedView.setVisibility(View.GONE);
                pActivity.getGameHandler().displayNewGame();
            }
        });

        // now make the animation and fade in the animation layout
        crossOver(pActivity, animatedView);
    }

    /**
     * Makes a new view visible. It's in foreground then.
     *
     * @param pActivity the activity
     * @param pGoIn     the view that should be made visible
     */
    private void crossOver(Activity pActivity, View pGoIn) {
        int longAnimationDuration = pActivity.getResources().getInteger(android.R.integer.config_longAnimTime);

        pGoIn.setVisibility(View.VISIBLE);
        pGoIn.animate()
                .alpha(1f)
                .setDuration(longAnimationDuration);
    }
}
