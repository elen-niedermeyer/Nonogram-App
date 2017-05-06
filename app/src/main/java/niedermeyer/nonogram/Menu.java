package niedermeyer.nonogram;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * @author Elen Niedermeyer, last updated 2017-05-06
 */

public class Menu {

    private Activity activity;

    public Menu(Activity pActivity) {
        activity = pActivity;
    }

    public void showMenu(View v) {
        PopupWindow popup = makePopupWindow();
        popup.showAsDropDown(v, -40, 18);
    }


    public PopupWindow makePopupWindow() {

        PopupWindow popupWindow = new PopupWindow(activity);

        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.menu_popup, null);

        // set numbers of rows and columns
        TextView numberOfRows = (TextView) view.findViewById(R.id.menu_size_row_text_number);
        numberOfRows.setText(Integer.toString(NonogramActivity.numberOfRows));

        TextView numberOfColumns = (TextView) view.findViewById(R.id.menu_size_column_text_number);
        numberOfColumns.setText(Integer.toString(NonogramActivity.numberOfColumns));

        popupWindow.setFocusable(true);
        popupWindow.setWidth(WindowManager.LayoutParams.WRAP_CONTENT);
        popupWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        popupWindow.setContentView(view);

        return popupWindow;
    }

}
