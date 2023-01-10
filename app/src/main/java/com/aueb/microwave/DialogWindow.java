package com.aueb.microwave;

import android.content.Context;
import android.content.DialogInterface;

import androidx.appcompat.app.AlertDialog;

import static android.content.DialogInterface.BUTTON_POSITIVE;

public class DialogWindow {

    public static void showDialog(final Context context, String title, String message, String button_text, final Runnable runnable) {
        AlertDialog dialog = new AlertDialog.Builder(context).create();

        // Setting Dialog Title
        dialog.setTitle(title);

        // Setting Dialog Message
        dialog.setMessage(message);
        dialog.setButton(BUTTON_POSITIVE, button_text, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        // Setting OK Button
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                runnable.run();
            }
        });
        dialog.setCanceledOnTouchOutside(false);

        // Showing Alert Message
        dialog.show();
    }
}
