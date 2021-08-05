package com.example.finalproject;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import com.wajahatkarim3.easyflipview.EasyFlipView;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static com.example.finalproject.FileHelper.*;
import static com.example.finalproject.SpinnerHelper.*;
import static com.example.finalproject.TimeHelper.SetDate;
import static com.example.finalproject.TimeHelper.findDifference;
import static com.example.finalproject.TimeHelper.timeMi;

public class Payment_frag extends Fragment {

    private Context context;
    private EditText ownerName, digits1_4, digits5_8, digits9_12, digits13_16, verification;
    private Button btnPlaceOrder;
    private TextView price,cnnTv;
    private String yearPicked, monthPicked, creditCardNumber, verificationCode, cardHolderName, expirationDate;
    private Spinner month, year;
    private Person person;
    private String pattern = "dd/MM/yyyy";
    private int creditCardSegmentlength = 4, cnnLength = 3;
    private Order order;
    private TextView cardNumTV,expireDateTv,cardOwnerTv;
    private EasyFlipView easyFlipView;
    public static boolean backCreditCard =false;
    public Payment_frag(Order order, Person person) {
        this.order = order;
        this.person = person;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frag_payment, container, false);//need create frag
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        context = getContext();
        ownerName = (EditText) view.findViewById(R.id.etOwnerName);
        digits1_4 = (EditText) view.findViewById(R.id.etCardNumber1_);
        digits5_8 = (EditText) view.findViewById(R.id.etCardNumber5_);
        digits9_12 = (EditText) view.findViewById(R.id.etCardNumber9_);
        digits13_16 = (EditText) view.findViewById(R.id.etCardNumber13_);
        verification = (EditText) view.findViewById(R.id.etVerification);
        month = (Spinner) view.findViewById(R.id.month);
        year = (Spinner) view.findViewById(R.id.year);

        btnPlaceOrder = (Button) view.findViewById(R.id.btnPlaceOrder);
        price = (TextView) view.findViewById(R.id.tvTotalPrice1);
        cardNumTV = (TextView) view.findViewById(R.id.cardNumTv1);
        expireDateTv = (TextView) view.findViewById(R.id.expireDateTv1);
        cardOwnerTv = (TextView) view.findViewById(R.id.cardOwnerTv1);
        cnnTv= (TextView) view.findViewById(R.id.cnnTv1);
        easyFlipView = (EasyFlipView) view.findViewById(R.id.easyFlipView1);
        cnnTv.setText("");
        DateFormat df = new SimpleDateFormat(pattern);
        price.setText("US$ "+order.getPrice());

        SpinnerCreditCardInit(context, month, year,expireDateTv);

        digits1_4.addTextChangedListener(createTextWatcher());
        digits5_8.addTextChangedListener(createTextWatcher());
        digits9_12.addTextChangedListener(createTextWatcher());
        digits13_16.addTextChangedListener(createTextWatcher());

        ownerName.addTextChangedListener(createTextWatcher());
        verification.addTextChangedListener(createTextWatcher());
        verification.setOnFocusChangeListener(focusListener);
        monthPicked = MonthPicker;
        yearPicked = YearPicker;
        creditCardNumber = digits1_4.getText().toString() + digits5_8.getText().toString() + digits9_12.getText().toString() + digits13_16.getText().toString();

        easyFlipView.setOnFlipListener(new EasyFlipView.OnFlipAnimationListener() {
            @Override
            public void onViewFlipCompleted(EasyFlipView flipView, EasyFlipView.FlipState newCurrentSide)
            {
                if(easyFlipView.getCurrentFlipState()== EasyFlipView.FlipState.FRONT_SIDE) {
                    backCreditCard =false;
                    System.out.println("FRONT_SIDE");
                                   cnnTv.setText("");
                        cardNumTV.setText("xxxx - xxxx - xxxx - xxxx");
                        cardOwnerTv.setText("Owner Name");

                        if (digits1_4.getText().length() != 0 || digits5_8.getText().length() != 0 || digits9_12.getText().length() != 0 || digits13_16.getText().length() != 0) {
                            cardNumTV.setText(digits1_4.getText() + " - " + digits5_8.getText() + " - " + digits9_12.getText() + " - " + digits13_16.getText());
                        }
                        if (ownerName.getText().length() != 0) {
                            cardOwnerTv.setText(ownerName.getText());
                        }

                            expireDateTv.setText(SpinnerHelper.MonthPicker + " / " + SpinnerHelper.YearPicker);

                                }
                if (easyFlipView.getCurrentFlipState() == EasyFlipView.FlipState.BACK_SIDE) {
                    backCreditCard = true;
                    cnnTv.setText("CNN");
                    if (verification.getText().length() != 0) {
                        cnnTv.setText(verification.getText());
                    }
                    cardNumTV.setText("");
                    cardOwnerTv.setText("");
                    expireDateTv.setText("");
                }
            }
        });
        btnPlaceOrder.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 monthPicked = SpinnerHelper.MonthPicker;
                 yearPicked = SpinnerHelper.YearPicker;

                if (digits1_4.getText().length() != creditCardSegmentlength || digits5_8.getText().length() != creditCardSegmentlength || digits9_12.getText().length() != creditCardSegmentlength || digits13_16.getText().length() != creditCardSegmentlength) {
                     Toast.makeText(context, "You must fill all the card number details", Toast.LENGTH_SHORT).show();
                     return;
                } else {
                    creditCardNumber = digits1_4.getText().toString() + digits5_8.getText().toString() + digits9_12.getText().toString() + digits13_16.getText().toString();
                    if (CheckIfValid.isValidCreditCard( Long.parseLong(creditCardNumber) )==false)
                             {
                        Toast.makeText(context, "Your Credit Card Number is illegal", Toast.LENGTH_SHORT).show();
                        return;
                    }


                }
                 if (ownerName.getText().toString().isEmpty()) {
                     Toast.makeText(context, "You must fill cardholder name", Toast.LENGTH_SHORT).show();
                     return;
                 } else {
                     cardHolderName = ownerName.getText().toString();
                 }
                if (verification.getText().length() != cnnLength) {
                    Toast.makeText(context, "Security number must be 3 digit number", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    verificationCode = verification.getText().toString();
                }

                expirationDate = monthPicked + "/" + yearPicked;
                writeData(order.getOrderNumber() + " " + order.getHotel().getName() + " " + df.format(order.getFrom()) + " " + df.format(order.getTo()) + " " + order.getRooms());
                writeData(person.getfirstName() + " " + person.getlastName() + " " + person.getEmail() + " " + person.getphoneNum());
                writeData(cardHolderName + " " + creditCardNumber + " " + verificationCode + " " + expirationDate);

                FragmentManager fm;
                fm = getActivity().getSupportFragmentManager();
                String msg = "Your payment was successfully processed.\n\nOrder Number: " + order.getOrderNumber() + "\nHotel: " + order.getHotel().getName() + "\nDates: " + df.format(order.getFrom()) + "-" + df.format(order.getTo()) + "\n\nEnjoy your vacation!";
                MessageDialogFragment alertDialog = MessageDialogFragment.newInstance("Thank you!", msg);
                alertDialog.show(fm, "fragment_alert");

                UpdateRooms(order);//function in FILE HELPER that update rooms count in shared preferences file
             }
        });
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    private View.OnFocusChangeListener focusListener = new View.OnFocusChangeListener() {   //very important do not change it!

        public void onFocusChange(View v, boolean hasFocus) {
            if (hasFocus){
                System.out.println("hasFocus"+hasFocus);
                easyFlipView.flipTheView();
                backCreditCard =true;
            } else {
                System.out.println("hasFocus2 "+hasFocus);
                backCreditCard =false;
                easyFlipView.flipTheView();
            }
        }
    };
    private TextWatcher createTextWatcher() {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                cnnTv.setText("");
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(backCreditCard ==true){
                    if(verification.getText().length()==0){
                        cnnTv.setText("CNN");
                    }else {
                        cnnTv.setText(verification.getText());
                    }
                }
                else {
                    backCreditCard =false;

                    if (digits1_4.getText().length() != 0 || digits5_8.getText().length() != 0 || digits9_12.getText().length() != 0 || digits13_16.getText().length() != 0) {
                        cardNumTV.setText(digits1_4.getText() + " - " + digits5_8.getText() + " - " + digits9_12.getText() + " - " + digits13_16.getText());
                    }
                    if (digits1_4.getText().length() == 0 && digits5_8.getText().length() == 0 && digits9_12.getText().length() == 0 && digits13_16.getText().length() == 0) {
                        cardNumTV.setText("xxxx - xxxx - xxxx - xxxx");
                    }
                                        if (ownerName.getText().length() != 0) {
                        cardOwnerTv.setText(ownerName.getText());
                    }
                    if(ownerName.getText().length() == 0){
                        cardOwnerTv.setText("");
                    }

                    if (digits1_4.getText().length() == creditCardSegmentlength) {
                        digits5_8.requestFocus();
                    }
                    if (digits5_8.getText().length() == creditCardSegmentlength && digits1_4.getText().length() == creditCardSegmentlength) {
                        digits9_12.requestFocus();
                    }
                    if (digits9_12.getText().length() == creditCardSegmentlength && digits1_4.getText().length() == creditCardSegmentlength && digits5_8.getText().length() == creditCardSegmentlength) {
                        digits13_16.requestFocus();
                    }
                    if (digits9_12.getText().length() == creditCardSegmentlength && digits1_4.getText().length() == creditCardSegmentlength && digits5_8.getText().length() == creditCardSegmentlength) {
                        digits13_16.requestFocus();
                    }
                    if (digits9_12.getText().length() == creditCardSegmentlength && digits1_4.getText().length() == creditCardSegmentlength && digits5_8.getText().length() == creditCardSegmentlength && digits13_16.getText().length() == creditCardSegmentlength) {
                        ownerName.requestFocus();
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        };
    }


}




