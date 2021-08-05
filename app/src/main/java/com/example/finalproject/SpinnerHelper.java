package com.example.finalproject;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;

public class SpinnerHelper {
    public static String MonthPicker, YearPicker, prefixPicker;

    public  static String Picke;
    public static void SpinnerCreditCardInit(Context context, Spinner month, Spinner year , TextView expireDateTv ) {    // spinner for payment fragment

        int curYear = Calendar.getInstance().get(Calendar.YEAR);
        int numOfYears = 10;
        ArrayList<String> yearArray = new ArrayList<String>();

            ArrayAdapter<CharSequence> monthAdapter = ArrayAdapter.createFromResource(context, R.array.months, android.R.layout.simple_spinner_item);
            monthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            month.setAdapter(monthAdapter);
            month.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    MonthPicker = (String) parent.getItemAtPosition(position);// take the data from what we choose from the spinner
                    month.setSelection(position); //sets the position of the spinner

                    //use to notify to the spinner if we choose one of the item in the spinner-if true un payment fragment it will flip the credit card
                    month.setFocusableInTouchMode(true);
                    month.requestFocus();
                    month.setFocusableInTouchMode(false);
                    month.clearFocus();
                    ////////////////////////////
                    if(Payment_frag.backCreditCard==false) {///if we in the front of the card view then show the exipre dates
                        expireDateTv.setText(MonthPicker + "/" + YearPicker);
                    }

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    month.setSelection(0);
                }
            });

            for (int i = 0; i < numOfYears; i++) {
                yearArray.add(String.valueOf(curYear + i));
            }
            ArrayAdapter<String> yearAdapter = new ArrayAdapter(context, android.R.layout.simple_spinner_dropdown_item, yearArray);
            yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            year.setAdapter(yearAdapter);
            year.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    YearPicker = (String) parent.getItemAtPosition(position);// take the data from what we choose from the spinner
                    year.setSelection(position);    //sets the position of the spinner
                    //use to notify to the spinner if we choose one of the item in the spinner-if true un payment fragment it will flip the credit card
                    year.setFocusableInTouchMode(true);
                    year.requestFocus();
                    year.setFocusableInTouchMode(false);
                    year.clearFocus();
                    /////////////////////////////
                    if(Payment_frag.backCreditCard==false) {///if we in the front of the card view then show the exipre dates
                        expireDateTv.setText(MonthPicker + "/" + YearPicker);
                    }


                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    year.setSelection(0);
                }
            });

       // }

    }

    public static void SpinnerPhone(Context context,Spinner phonePrefix ) {//used both in UpdateDetailsAlertDialogFragment and in Order_frag

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(context, R.array.numbers, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        phonePrefix.setAdapter(adapter);
        phonePrefix.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                prefixPicker = (String) parent.getItemAtPosition(position);
                phonePrefix.setSelection(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                phonePrefix.setSelection(0);

            }
        });
    }

    public static int GetPosition(Context context,Spinner phonePrefix, String value)
    {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(context, R.array.numbers, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        phonePrefix.setAdapter(adapter);
        int pos = adapter.getPosition(value);
        return pos;
    }

}