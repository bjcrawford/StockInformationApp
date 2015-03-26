package edu.temple.cis4350.bc.sia;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

public class AddStockDialogFragment extends DialogFragment {

    public interface OnAddStockListener {
        public void onAddStock(String msg);
    }

    private AutoCompleteTextView stockName;

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

        stockName = (AutoCompleteTextView) dialogView.findViewById(R.id.stock_name_input);

        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.add_stock_dialog_title);
        builder.setView(dialogView);
        builder.setPositiveButton(R.string.add_stock_button,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        AddStockDialogFragment.this.doAddClick();
                    }
                }
        );
        builder.setNegativeButton(R.string.cancel_button,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        AddStockDialogFragment.this.doCancel();
                    }
                }
        );

        // Create the AlertDialog object and return it
        return builder.create();
    }

    public void doAddClick() {
        String stockSymbol = stockName.getText().toString();
        ((OnAddStockListener) getActivity()).onAddStock(stockSymbol);
    }

    public void doCancel() {

    }
}
