package com.emanuel.shotgun;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Layout;
import android.view.LayoutInflater;

/**
 * Created by Daniel on 2/20/2016.
 */
public class DateTimePickerFragment extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        builder.setView(inflater.inflate(R.layout.date_time_picker, null))
                .setPositiveButton(R.string.set, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO GET THE SELECTED DATE AND TIME

                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DateTimePickerFragment.this.getDialog().cancel();
                    }
                });
        return builder.create();
    }
}
