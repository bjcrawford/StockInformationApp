package edu.temple.cis4350.bc.sia.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;
import android.widget.Spinner;

import java.util.ArrayList;

import edu.temple.cis4350.bc.sia.R;
import edu.temple.cis4350.bc.sia.coloritem.ColorItem;
import edu.temple.cis4350.bc.sia.coloritem.ColorItemAdapter;

public class AddStockDialogFragment extends DialogFragment {

    private static final String TAG = "AddStockDialogFragment";

    private AutoCompleteTextView stockName;
    private Spinner colorSpinner;

    /**
     * Called when the Dialog is created. Handles defining button event
     * handling and building of the Dialog
     * @param savedInstanceState The saved instance state
     * @return The Dialog
     */
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        // Inflate the DialogFragment's view using the layout
        View dialogView = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_add_stock, null);

        // Set up the auto complete text field
        stockName = (AutoCompleteTextView) dialogView.findViewById(R.id.stock_name_input);

        // Warning ugly hack ahead
        stockName.setAdapter(
                new ArrayAdapter<String>(
                        getActivity(),
                        android.R.layout.simple_list_item_1,
                        getResources().getStringArray(R.array.stocks)
                ) {
                    // Ugly, ugly hack to get the scrollbar on the auto complete
                    // dropdown to not fade away
                    private boolean attrChanged = false;

                    @Override
                    public View getView(int position, View convertView, ViewGroup parent) {
                        if (!attrChanged) {
                            attrChanged = true;

                            ListView list = (ListView) parent;
                            list.setVerticalScrollBarEnabled(true);
                            list.setScrollbarFadingEnabled(false);
                        }

                        return super.getView(position, convertView, parent);
                    }
                }
        );

        // Set up the color spinner
        colorSpinner = (Spinner) dialogView.findViewById(R.id.stock_color_input);

        int[] colorCode = getResources().getIntArray(R.array.stock_colors);
        String[] colorText = getResources().getStringArray(R.array.stock_colors_text);

        ArrayList<ColorItem> colorItems = new ArrayList<ColorItem>();
        for (int i = 0; i < colorCode.length; i++) {
            colorItems.add(i, new ColorItem(colorText[i], colorCode[i]));
        }

        colorSpinner.setAdapter(new ColorItemAdapter(getActivity(), colorItems));

        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.add_stock_dialog_title);
        builder.setView(dialogView);
        builder.setPositiveButton(R.string.add_stock_button,
                new DialogInterface.OnClickListener() {
                    public void onClick (DialogInterface dialog,int whichButton){
                        AddStockDialogFragment.this.doAddClick();
                    }
                }

        );
        builder.setNegativeButton(R.string.cancel_button,
                new DialogInterface.OnClickListener() {
                    public void onClick (DialogInterface dialog,int whichButton){
                        AddStockDialogFragment.this.doCancel();
                    }
                }

        );

        // Create the AlertDialog object and return it
        return builder.create();
    }

    public void doAddClick() {
        String stockName = this.stockName.getText().toString();
        int stockColor = ((ColorItem) colorSpinner.getSelectedItem()).getItemColorCode();
        ((OnAddStockListener) getActivity()).onAddStock(stockName, stockColor);
    }

    public void doCancel() {

    }

    public interface OnAddStockListener {
        public void onAddStock(String stockName, int stockColor);
    }
}
