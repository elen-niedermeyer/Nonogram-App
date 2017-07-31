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

public class Menu {

    private NonogramActivity activity;

    private LinearLayout rowsMenuLayout;
    private TextView numberOfRowsText;
    private LinearLayout columnsMenuLayout;
    private TextView numberOfColumnsText;

    private int minNumberOfColumnsAndRows = 3;
    private int maxNumberOfColumnsAndRows = 30;

    public static int dialogResult = 0;

    public Menu(NonogramActivity pActivity) {
        activity = pActivity;
    }

    public void showPopupMenu(View v) {
        PopupWindow popup = makePopupWindow();
        popup.showAsDropDown(v, -40, 20);
    }

    public AlertDialog makeNumberPickerForGameSize(final boolean isRow, final boolean isColumn, int value) {
        View layout = activity.getLayoutInflater().inflate(R.layout.menu_number_picker, null);
        final NumberPicker picker = (NumberPicker) layout.findViewById(R.id.menu_number_picker);
        picker.setMinValue(minNumberOfColumnsAndRows);
        picker.setMaxValue(maxNumberOfColumnsAndRows);
        picker.setValue(value);

        AlertDialog dialog = new AlertDialog.Builder(activity)
                .setView(layout)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int newValue = picker.getValue();
                        if (isRow) {
                            NonogramActivity.numberOfRows = newValue;
                        } else if (isColumn) {
                            NonogramActivity.numberOfColumns = newValue;
                        }
                        dialog.dismiss();
                    }
                })
                .setNegativeButton(R.string.cancel, null)
                .create();

        return dialog;
    }

    private PopupWindow makePopupWindow() {
        // get the layout
        View view = activity.getLayoutInflater().inflate(R.layout.menu_popup, null);

        // set the actual number of rows
        numberOfRowsText = (TextView) view.findViewById(R.id.menu_popup_size_row_number);
        numberOfRowsText.setText(Integer.toString(NonogramActivity.numberOfRows));

        // set the on click listener for the row menu item
        rowsMenuLayout = (LinearLayout) view.findViewById(R.id.menu_popup_size_row);
        rowsMenuLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int saveNumber = NonogramActivity.numberOfRows;
                AlertDialog dialog = makeNumberPickerForGameSize(true, false, NonogramActivity.numberOfRows);
                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        if (saveNumber != NonogramActivity.numberOfRows) {
                            numberOfRowsText.setText(Integer.toString(NonogramActivity.numberOfRows));
                            GameHandler game = new GameHandler(activity);
                            game.newGame();
                        }
                    }
                });
                dialog.show();
            }
        });


        // set the actual number of columns
        numberOfColumnsText = (TextView) view.findViewById(R.id.menu_popup_size_column_number);
        numberOfColumnsText.setText(Integer.toString(NonogramActivity.numberOfColumns));

        // set the on click listener for the column menu item
        columnsMenuLayout = (LinearLayout) view.findViewById(R.id.menu_popup_size_column);
        columnsMenuLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int saveNumber = NonogramActivity.numberOfColumns;
                AlertDialog dialog = makeNumberPickerForGameSize(false, true, NonogramActivity.numberOfColumns);
                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        if (saveNumber != NonogramActivity.numberOfColumns) {
                            numberOfColumnsText.setText(Integer.toString(NonogramActivity.numberOfColumns));
                            GameHandler game = new GameHandler(activity);
                            game.newGame();
                        }
                    }
                });
                dialog.show();
            }
        });

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
