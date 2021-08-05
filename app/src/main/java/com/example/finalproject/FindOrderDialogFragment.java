package com.example.finalproject;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import static com.example.finalproject.FileHelper.*;
import static com.example.finalproject.TimeHelper.CheckIfDatePassed;

public class FindOrderDialogFragment extends DialogFragment {
    public EditText etOrderNumber;
    public Context context;
    private String orderNumber, date;
    private int orderIndex;
    private String[] ordersNumbers, ordersDetails, order;
    private boolean orderExist;
    private SimpleDateFormat format;

    public FindOrderDialogFragment() {
        // Empty constructor required for DialogFragment
    }

    public static FindOrderDialogFragment newInstance(String title) {
        FindOrderDialogFragment frag = new FindOrderDialogFragment();
        Bundle args = new Bundle();//create dictionary ->(key,value)
        args.putString("title", title);
        frag.setArguments(args);//step 2
        return frag;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.frag_find_order, null);
        etOrderNumber =(EditText) v.findViewById(R.id.etOrderNumber);
        context =getContext();
        String title = getArguments().getString("title");
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setTitle(title);
        alertDialogBuilder.setView(v);

        alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {


            }
        });

        alertDialogBuilder.setNeutralButton("Exit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        return alertDialogBuilder.create();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        AlertDialog dialog = (AlertDialog) getDialog();
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                orderNumber = etOrderNumber.getText().toString();
                ordersNumbers = GetOrdersNumbers();
                ordersDetails = GetOrdersDetails();
                orderExist = false;
                format = new SimpleDateFormat("dd/MM/yyyy");
                for (int i = 0; i < ordersNumbers.length; i++)        //check if order number is exist in raw file
                {
                    if (orderNumber.equals(ordersNumbers[i])) {
                        order = ordersDetails[i].split(" ",0);
                        date = order[order.length-3];
                        try {
                            if(!CheckIfDatePassed(format.parse(date))) {
                                orderIndex = i;
                                orderExist = true;
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        break;
                    }
                }
                if (orderExist) {
                    FragmentManager fm = getActivity().getSupportFragmentManager();
                    UpdateDetailsDialogFragment alertDialog = UpdateDetailsDialogFragment.newInstance("Update Order", orderIndex, orderNumber);
                    alertDialog.show(fm, "fragment_alert");//equal to transaction operation
                    dialog.dismiss();
                } else {
                    Toast.makeText(context, "Order number "+orderNumber+" not exist or already passed!", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}
