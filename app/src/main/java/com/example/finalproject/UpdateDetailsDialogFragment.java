package com.example.finalproject;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import static com.example.finalproject.FileHelper.FileToArray;
import static com.example.finalproject.SpinnerHelper.GetPosition;
import static com.example.finalproject.SpinnerHelper.SpinnerPhone;

public  class UpdateDetailsDialogFragment extends DialogFragment  {  //step 1 create class with default constructor

    private EditText updatefirstNamePersonDialog,updatelastNamePersonDialog,updateEmailPersonDialog,updatecontPhone;
    private Context context;
    private Spinner phonePrefixSpinner;
    private String orderNumber;
    private int orderIndex,numLineInOrder =3, orderPosFirstNm=0, orderPosLastNm=1, orderPosEmail=2, orderPosRestPhoneNm=3;
    private String title, personalDetails;
    private String[] ordersLines, personalDetailsSeperate,phoneNumber;

    public UpdateDetailsDialogFragment() {
        // Empty constructor required for DialogFragment
    }

    public static UpdateDetailsDialogFragment newInstance(String title, int orderIndex, String orderNumber) {
        UpdateDetailsDialogFragment frag = new UpdateDetailsDialogFragment();
        Bundle args = new Bundle();//create dictionary ->(key,value)
        args.putString("title", title);
        args.putString("orderNumber", orderNumber);
        args.putInt("orderIndex",orderIndex);
        frag.setArguments(args);//step 2
        return frag;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.update_person_frag, null);
        updatefirstNamePersonDialog =(EditText) v.findViewById(R.id.UpdatefirstNamePersonDialog);
        updatelastNamePersonDialog =(EditText) v.findViewById(R.id.UpdatelastNamePersonDialog);
        updateEmailPersonDialog =(EditText) v.findViewById(R.id.UpdateEmailPersonDialog);
        updatecontPhone =(EditText) v.findViewById(R.id.updatecontPhone);
        context =getContext();
        phonePrefixSpinner = v.findViewById(R.id.spinnerPhone2);

        SpinnerPhone(context, phonePrefixSpinner);

        title = getArguments().getString("title");
        orderIndex = getArguments().getInt("orderIndex");
        orderNumber = getArguments().getString("orderNumber");
        ordersLines = FileToArray();
        personalDetails = ordersLines[orderIndex*numLineInOrder+1];
        personalDetailsSeperate = personalDetails.split(" ",0);
        updatefirstNamePersonDialog.setText(personalDetailsSeperate[orderPosFirstNm]);
        updatelastNamePersonDialog.setText(personalDetailsSeperate[orderPosLastNm]);
        updateEmailPersonDialog.setText(personalDetailsSeperate[orderPosEmail]);
        phoneNumber = personalDetailsSeperate[orderPosRestPhoneNm].split("-",0);
        updatecontPhone.setText(phoneNumber[1]);//phone number [0] contains the prefix ,phone number[1] contains the rest of the phone number
        phonePrefixSpinner.setSelection(GetPosition(context, phonePrefixSpinner,phoneNumber[0]));

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setTitle(title+" "+orderNumber);
        alertDialogBuilder.setView(v);

        alertDialogBuilder.setPositiveButton("Update" , new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        alertDialogBuilder.setNegativeButton("Cancel Order", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                CancelOrderDialogFragment alertDialog =CancelOrderDialogFragment.newInstance("Cancel order number "+orderNumber,"Are you sure?", orderIndex, orderNumber, ordersLines);
                alertDialog.show(fm, "fragment_alert");
            }
        });

        alertDialogBuilder.setNeutralButton("Exit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (dialog != null ) {
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
        dialog.setCancelable(false);//with this we can't go back with the BACK Key
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String prefix = SpinnerHelper.prefixPicker;
                String errMsg;
                Person person = new Person(updatefirstNamePersonDialog.getText().toString(), updatelastNamePersonDialog.getText().toString(), updateEmailPersonDialog.getText().toString(), prefix + "-" + updatecontPhone.getText());
                errMsg = CheckIfValid.checkPersonDetails(person);
                if (!(errMsg.equals(""))) {
                    Toast.makeText(getActivity(), "You must fill a valid: " + errMsg, Toast.LENGTH_LONG).show();
                    return;
                } else {
                    FileHelper.ReplaceLine(person.getfirstName() + " " + person.getlastName() + " " + person.getEmail() + " " + person.getphoneNum(), orderIndex * numLineInOrder + 1);
                    FragmentManager fm = getActivity().getSupportFragmentManager();
                    MessageDialogFragment alertDialog = MessageDialogFragment.newInstance("Success!", "Order number " + orderNumber + " updated successfully!");
                    alertDialog.show(fm, "fragment_alert");
                    ((AlertDialog) getDialog()).dismiss();
                }
            }
        });
    }


}