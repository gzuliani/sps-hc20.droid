package org.jolly_handball.sps_hc20;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class BulletinDialogFragment extends AutoExpandingDialogFragment {

    // delays (in ms) associated to the scrolling speeds,
    // see resource `bulletin_speed_array`
    private int[] delays = {500, 250, 125, 50,};

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        final View view = inflater.inflate(R.layout.bulletin_dialog, null);
        final EditText textWidget = view.findViewById(R.id.bulletin_text);
        final Spinner speedWidget = view.findViewById(R.id.bulletin_speed);
        final Button showHideButton = view.findViewById(R.id.bulletin_show_hide);

        textWidget.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence text, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence text, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().length() == 0) {
                    showHideButton.setEnabled(false);
                } else {
                    showHideButton.setEnabled(true);
                }
            }
        });

        speedWidget.setSelection(1); // @string/bulletin_speed_medium

        showHideButton.setEnabled(false);
        showHideButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (showHideButton.getText().equals(getResources().getString(R.string.bulletin_show))) {
                    if (Globals.scoreboard != null)
                        Globals.scoreboard.showScrollingText(textWidget.getText().toString(), delays[(int) speedWidget.getSelectedItemId()]);
                    textWidget.setEnabled(false);
                    speedWidget.setEnabled(false);
                    showHideButton.setText(R.string.bulletin_hide);
                } else {
                    if (Globals.scoreboard != null)
                        Globals.scoreboard.hideScrollingText();
                    textWidget.setEnabled(true);
                    speedWidget.setEnabled(true);
                    showHideButton.setText(R.string.bulletin_show);
                }
            }
        });

        builder.setView(view)
                .setTitle(R.string.bulletin_dialog_title)
                .setMessage(R.string.bulletin_dialog_message)
                .setPositiveButton(
                        android.R.string.ok,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                if (Globals.scoreboard != null)
                                    Globals.scoreboard.hideScrollingText();
                            }
                        });

        return builder.create();
    }
}
