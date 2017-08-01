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

import java.util.logging.Level;
import java.util.logging.Logger;

import niedermeyer.nonogram.R;
import niedermeyer.nonogram.persistence.GameSizeHandler;

/**
 * @author Elen Niedermeyer, last updated 2017-07-16
 */
public class Menu {

    private static final int MIN_NUMBER_OF_COLUMNS_AND_ROWS = 3;
    private static final int MAX_NUMBER_OF_COLUMNS_AND_ROWS = 30;

    private Activity activity;

    public Menu(Activity pActivity) {
        activity = pActivity;
    }

    public void showPopupMenu(View v) {
        PopupWindow popup = makePopupWindow();
        popup.showAsDropDown(v, -40, 20);
    }

    public AlertDialog makeNumberPickerForGameSize(final boolean isRow) {
        View layout = activity.getLayoutInflater().inflate(R.layout.dialog_number_picker, null);
        final NumberPicker picker = (NumberPicker) layout.findViewById(R.id.menu_number_picker);
        picker.setMinValue(MIN_NUMBER_OF_COLUMNS_AND_ROWS);
        picker.setMaxValue(MAX_NUMBER_OF_COLUMNS_AND_ROWS);
        if (isRow) {
            picker.setValue(GameSizeHandler.numberOfRows);
        } else {
            picker.setValue(GameSizeHandler.numberOfColumns);
        }

        AlertDialog dialog = new AlertDialog.Builder(activity)
                .setView(layout)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int newValue = picker.getValue();
                        if (isRow) {
                            GameSizeHandler.numberOfRows = newValue;
                        } else {
                            GameSizeHandler.numberOfColumns = newValue;
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
        final TextView numberOfRowsText = (TextView) view.findViewById(R.id.menu_popup_size_row_number);
        numberOfRowsText.setText(Integer.toString(GameSizeHandler.numberOfRows));

        // set the on click listener for the row menu item
        LinearLayout rowsMenuLayout = (LinearLayout) view.findViewById(R.id.menu_popup_size_row);
        rowsMenuLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int saveNumber = GameSizeHandler.numberOfRows;
                AlertDialog dialog = makeNumberPickerForGameSize(true);
                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        if (saveNumber != GameSizeHandler.numberOfRows) {
                            numberOfRowsText.setText(Integer.toString(GameSizeHandler.numberOfRows));
                            if (activity instanceof NonogramActivity) {
                                NonogramActivity nonogramActivity = (NonogramActivity) activity;
                                GameHandler game = nonogramActivity.getGameHandler();
                                game.newGame();
                            } else {
                                Logger.getLogger(NonogramActivity.class.getName()).log(Level.FINER, null, "New game should be started from another activity than NonogramActivity");
                            }
                        }
                    }
                });
                dialog.show();
            }
        });


        // set the actual number of columns
        final TextView numberOfColumnsText = (TextView) view.findViewById(R.id.menu_popup_size_column_number);
        numberOfColumnsText.setText(Integer.toString(GameSizeHandler.numberOfColumns));

        // set the on click listener for the column menu item
        LinearLayout columnsMenuLayout = (LinearLayout) view.findViewById(R.id.menu_popup_size_column);
        columnsMenuLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int saveNumber = GameSizeHandler.numberOfColumns;
                AlertDialog dialog = makeNumberPickerForGameSize(false);
                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        if (saveNumber != GameSizeHandler.numberOfColumns) {
                            numberOfColumnsText.setText(Integer.toString(GameSizeHandler.numberOfColumns));
                            if (activity instanceof NonogramActivity) {
                                NonogramActivity nonogramActivity = (NonogramActivity) activity;
                                GameHandler game = nonogramActivity.getGameHandler();
                                game.newGame();
                            } else {
                                Logger.getLogger(NonogramActivity.class.getName()).log(Level.FINER, null, "New game should be started from another activity than NonogramActivity");
                            }
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
