package com.tstudioz.sportsman.app;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

/**
 * Created by Tomáš Zahálka on 12. 5. 2014.
 */
public class DialogHelper {

    public DialogHelper(Context context, CommandListener commandListener) {
        this.context = context;
        this.commandListener = commandListener;
    }

    private final Context context;
    private final CommandListener commandListener;
    private int command;

    static interface CommandListener {
        public void onCommandSelected(int command);
    }

    public void showYesNoDialog(String message, int command) {
        this.command = command;
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message)
                .setPositiveButton(R.string.yes, dialogClickListener)
                .setNegativeButton(R.string.no, dialogClickListener)
                .show();
    }

    private DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which){
                case DialogInterface.BUTTON_POSITIVE:
                    commandListener.onCommandSelected(command);
                    break;
                case DialogInterface.BUTTON_NEGATIVE:
                    break;
            }
        }
    };
}
