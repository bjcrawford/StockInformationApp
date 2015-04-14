package edu.temple.cis4350.bc.sia.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RadioGroup;

import edu.temple.cis4350.bc.sia.MainActivity;
import edu.temple.cis4350.bc.sia.R;

public class SettingsDialogFragment extends DialogFragment {

    private static final String TAG = "SettingsDialogFragment";

    private View dialogView;

    /**
     * Called when the Dialog is created. Handles defining button event
     * handling and building of the Dialog
     * @param savedInstanceState The saved instance state
     * @return The Dialog
     */
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        // Inflate the DialogFragment's view using the layout
        dialogView = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_settings, null);

        String strRate = PreferenceManager.getDefaultSharedPreferences(getActivity())
                .getString(MainActivity.KEY_PREF_REFRESH_RATE, MainActivity.VALUE_PREF_REFRESH_15);

        int checkId = -1;

        switch (strRate) {
            case MainActivity.VALUE_PREF_REFRESH_5:
                checkId = R.id.settings_refresh_radio_1;
                break;
            case MainActivity.VALUE_PREF_REFRESH_15:
                checkId = R.id.settings_refresh_radio_2;
                break;
            case MainActivity.VALUE_PREF_REFRESH_30:
                checkId = R.id.settings_refresh_radio_3;
                break;
            case MainActivity.VALUE_PREF_REFRESH_45:
                checkId = R.id.settings_refresh_radio_4;
                break;
            case MainActivity.VALUE_PREF_REFRESH_60:
                checkId = R.id.settings_refresh_radio_5;
                break;
            case MainActivity.VALUE_PREF_REFRESH_0:
                checkId = R.id.settings_refresh_radio_6;
                break;
        }

        ((RadioGroup) dialogView.findViewById(R.id.settings_refresh_radio_group)).check(checkId);


        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.settings_dialog_title);
        builder.setView(dialogView);
        builder.setNeutralButton(R.string.settings_back_button, null);
        builder.setCancelable(true);

        // Create the AlertDialog object and return it
        return builder.create();
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);

        int checkedId = ((RadioGroup) dialogView.findViewById(R.id.settings_refresh_radio_group)).getCheckedRadioButtonId();
        String strRate = MainActivity.VALUE_PREF_REFRESH_15;

        switch (checkedId) {
            case R.id.settings_refresh_radio_1:
                strRate = MainActivity.VALUE_PREF_REFRESH_5;
                break;
            case R.id.settings_refresh_radio_3:
                strRate = MainActivity.VALUE_PREF_REFRESH_30;
                break;
            case R.id.settings_refresh_radio_4:
                strRate = MainActivity.VALUE_PREF_REFRESH_45;
                break;
            case R.id.settings_refresh_radio_5:
                strRate = MainActivity.VALUE_PREF_REFRESH_60;
                break;
            case R.id.settings_refresh_radio_6:
                strRate = MainActivity.VALUE_PREF_REFRESH_0;
                break;
        }

        PreferenceManager.getDefaultSharedPreferences(getActivity()).edit()
                .putString(MainActivity.KEY_PREF_REFRESH_RATE, strRate)
                .commit();
    }
}
