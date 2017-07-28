package niedermeyer.nonogram.activities;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.PopupWindow;
import android.widget.TextView;

import niedermeyer.nonogram.R;

/**
 * @author Elen Niedermeyer, last updated 2017-07-16
 */

public class Menu implements View.OnClickListener {

    private Activity activity;

    private LinearLayout rowsMenuLayout;
    private TextView numberOfRowsText;
    private LinearLayout columnsMenuLayout;
    private TextView numberOfColumnsText;

    private int maxNumberOfColumnsAndRows = 30;

    public Menu(Activity pActivity) {
        activity = pActivity;
    }

    public void showMenu(View v) {
        PopupWindow popup = makePopupWindow();
        popup.showAsDropDown(v, -40, 20);
    }

    @Override
    public void onClick(View v) {
        final View view = v;

        View layout = activity.getLayoutInflater().inflate(R.layout.menu_number_picker, null);
        final NumberPicker picker = (NumberPicker) layout.findViewById(R.id.menu_number_picker);
        picker.setMinValue(1);
        if (view == rowsMenuLayout) {
            picker.setMaxValue(maxNumberOfColumnsAndRows);
            picker.setValue(NonogramActivity.numberOfRows);
        } else if (view == columnsMenuLayout) {
            picker.setMaxValue(maxNumberOfColumnsAndRows);
            picker.setValue(NonogramActivity.numberOfColumns);
        }

        new AlertDialog.Builder(activity)
                .setView(layout)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int newValue = picker.getValue();
                        if (view == rowsMenuLayout) {
                            NonogramActivity.numberOfRows = newValue;
                            numberOfRowsText.setText(Integer.toString(newValue));
                        } else if (view == columnsMenuLayout) {
                            NonogramActivity.numberOfColumns = newValue;
                            numberOfColumnsText.setText(Integer.toString(newValue));
                        }

                        GameHandler game = new GameHandler(activity);
                        game.newGame();
                    }
                })
                .setNegativeButton(R.string.cancel, null)
                .setCancelable(true)
                .show();
    }

    private PopupWindow makePopupWindow() {
        // get the layout
        View view = activity.getLayoutInflater().inflate(R.layout.menu_popup, null);

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

        // make popup window
        PopupWindow popupWindow = new PopupWindow(activity);
        popupWindow.setFocusable(true);
        popupWindow.setWidth(WindowManager.LayoutParams.WRAP_CONTENT);
        popupWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        popupWindow.setContentView(view);
        popupWindow.setBackgroundDrawable(null);

        return popupWindow;
    }
}
