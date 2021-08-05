package com.example.finalproject;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentManager;
import static com.example.finalproject.FileHelper.*;

public class
CancelOrderDialogFragment extends androidx.fragment.app.DialogFragment{

    private int lineToDelete,numLineInOrder =3;
    private String orderNumber;
    private String[] ordersLines;

    public CancelOrderDialogFragment() {
        // Empty constructor required for DialogFragment
    }

    public static CancelOrderDialogFragment newInstance(String title,String msg, int lineToDelete, String orderNumber, String[] ordersLines) {
        CancelOrderDialogFragment frag = new CancelOrderDialogFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        args.putString("msg", msg);
        args.putInt("lineToDelete", lineToDelete);
        args.putString("orderNumber", orderNumber);
        args.putStringArray("ordersLines",ordersLines);
        frag.setArguments(args);
        return frag;
    }
    @Override

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String title =getArguments().getString("title");
        String msg =getArguments().getString("msg");
        lineToDelete = getArguments().getInt("lineToDelete");
        orderNumber = getArguments().getString("orderNumber");
        ordersLines = getArguments().getStringArray("ordersLines");

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setTitle(title);
        alertDialogBuilder.setMessage(msg);

        alertDialogBuilder.setNegativeButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                DeleteOrderSp(lineToDelete);//when delete invataion update hotel rooms number in raw file
                DeleteOrder(lineToDelete * numLineInOrder);//delete order inventation in raw file
                FragmentManager fm = getActivity().getSupportFragmentManager();
                MessageDialogFragment alertDialog = MessageDialogFragment.newInstance("Success!", "Order number " + orderNumber + " canceled successfully!");
                alertDialog.show(fm, "fragment_alert");
                ((AlertDialog) getDialog()).dismiss();

            }
        }).setPositiveButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        return alertDialogBuilder.create();
    }


}
