package niedermeyer.nonogram;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.PopupWindow;
import android.widget.TextView;

/**
 * @author Elen Niedermeyer, last updated 2017-05-12
 */

public class Menu implements View.OnClickListener {

    private Activity activity;

    private LinearLayout rowsMenuLayout;
    private TextView numberOfRowsText;
    private LinearLayout columnsMenuLayout;
    private TextView numberOfColumnsText;

    public Menu(Activity pActivity) {
        activity = pActivity;
    }

    public void showMenu(View v) {
        PopupWindow popup = makePopupWindow();
        popup.showAsDropDown(v, -40, 18);
    }

    @Override
    public void onClick(View v) {
        final View view = v;

        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.menu_number_picker, null);
        final NumberPicker picker = (NumberPicker) layout.findViewById(R.id.menu_number_picker);
        if (view == rowsMenuLayout) {
            picker.setValue(NonogramActivity.numberOfRows);
            picker.setMaxValue(10);
        } else if (view == columnsMenuLayout) {
            picker.setValue(NonogramActivity.numberOfColumns);
            picker.setMaxValue(10);
        }

        new AlertDialog.Builder(activity)
                .setView(layout)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int newValue = picker.getValue();
                        SharedPreferences prefs = activity.getPreferences(Context.MODE_PRIVATE);
                        SharedPreferences.Editor prefsEdit = prefs.edit();
                        if (view == rowsMenuLayout) {
                            prefsEdit.putInt(activity.getString(R.string.prefs_rows), newValue);
                            NonogramActivity.numberOfRows = newValue;
                            numberOfRowsText.setText(Integer.toString(newValue));
                        } else if (view == columnsMenuLayout) {
                            prefsEdit.putInt(activity.getString(R.string.prefs_columns), newValue);
                            NonogramActivity.numberOfColumns = newValue;
                            numberOfColumnsText.setText(Integer.toString(newValue));
                        }
                        prefsEdit.commit();

                        GameHandler game = new GameHandler(activity);
                        game.newGame();
                    }
                })
                .setNegativeButton(R.string.cancel, null)
                .setCancelable(true)
                .show();
    }

    private PopupWindow makePopupWindow() {
        PopupWindow popupWindow = new PopupWindow(activity);

        // get the layout
        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.menu_popup, null);

        // set the on click listener for the row menu item
        rowsMenuLayout = (LinearLayout) view.findViewById(R.id.menu_size_row);
        rowsMenuLayout.setOnClickListener(this);
        // set the actual number of rows
        numberOfRowsText = (TextView) view.findViewById(R.id.menu_size_row_text_number);
        numberOfRowsText.setText(Integer.toString(NonogramActivity.numberOfRows));

        // set the on click listener for the column menu item
        columnsMenuLayout = (LinearLayout) view.findViewById(R.id.menu_size_column);
        columnsMenuLayout.setOnClickListener(this);
        // set the actual number of columns
        numberOfColumnsText = (TextView) view.findViewById(R.id.menu_size_column_text_number);
        numberOfColumnsText.setText(Integer.toString(NonogramActivity.numberOfColumns));

        // set popup window attributes
        popupWindow.setFocusable(true);
        popupWindow.setWidth(WindowManager.LayoutParams.WRAP_CONTENT);
        popupWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        popupWindow.setContentView(view);

        return popupWindow;
    }
}