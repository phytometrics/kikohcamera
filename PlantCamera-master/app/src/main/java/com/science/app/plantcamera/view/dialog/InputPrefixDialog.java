package com.science.app.plantcamera.view.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.fragment.app.DialogFragment;

import com.science.app.plantcamera.R;

public class InputPrefixDialog extends DialogFragment {

    public interface InputPrefixDialogListener {
        void onDialogPositiveClick(String prefix);
        void onDialogNegativeClick();
    }

    private InputPrefixDialogListener listener = null;
    private EditText inputPrefix;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        Bundle args = getArguments();
        String prefix = "";
        if (args != null) {
            prefix = args.getString("FILE_PREFIX", prefix);
        }
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View root = inflater.inflate(R.layout.dialog_input_prefix, null);
        inputPrefix = root.findViewById(R.id.text_prefix);
        inputPrefix.setText(prefix);

        builder.setView(root)
            .setMessage(R.string.message_input_prefix)
            .setPositiveButton(R.string.text_ok, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    String text = inputPrefix.getText().toString();
                    listener.onDialogPositiveClick(text);
                }
            })
            .setNegativeButton(R.string.text_cancel, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    listener.onDialogNegativeClick();
                }
            }
        );
        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (InputPrefixDialogListener)context;
        } catch (ClassCastException e) {

        }
    }
}
