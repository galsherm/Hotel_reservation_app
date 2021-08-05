package com.example.finalproject;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

public class MessageDialogFragment extends androidx.fragment.app.DialogFragment{
    public MessageDialogFragment() {

        // Empty constructor required for DialogFragment

    }
    public static MessageDialogFragment newInstance(String title, String msg) {
        MessageDialogFragment frag = new MessageDialogFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        args.putString("msg", msg);
        frag.setArguments(args);
        return frag;
    }
    @Override

    public Dialog onCreateDialog(Bundle savedInstanceState) {

        String title =getArguments().getString("title");
        String msg =getArguments().getString("msg");

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());

        alertDialogBuilder.setTitle(title);

        alertDialogBuilder.setMessage(msg);



        alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

            @Override

            public void onClick(DialogInterface dialog, int which) {

                if (dialog != null ) {

                    dialog.dismiss();
                    FragmentTransaction transaction =((AppCompatActivity) getContext()).getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.fragContainer,  new Home_frag()); // Add your fragment class
                    transaction.setReorderingAllowed(true);
                    transaction.commit();

                }

            }

        });
        return alertDialogBuilder.create();
    }

    @Override
    public void onStart() {
        super.onStart();
        AlertDialog dialog = (AlertDialog) getDialog();
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);//with this we can't go back with the BACK Key
    }
}
