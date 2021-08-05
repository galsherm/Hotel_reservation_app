package com.example.finalproject;

import android.content.Context;
import android.os.Bundle;
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
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import static com.example.finalproject.SpinnerHelper.SpinnerPhone;
import static com.example.finalproject.SpinnerHelper.prefixPicker;


public class Order_frag extends Fragment {

    private Context context;
    private EditText firstName,lastName,email,continuePhone;
    private Button btnContinue;
    private TextView hotelName, from, to, price;
    private Spinner phonePrefix;
    private String prefix,pattern = "dd/MM/yyyy";
    private Order order;
    private  Person person;
    public Order_frag(Order o)
    {
        this.order = o;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frag_order, container, false);//need create frag
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        context = getContext();
        //person details////
        firstName = (EditText) view.findViewById(R.id.etFirstName);
        lastName = (EditText) view.findViewById(R.id.etLastName);
        email = (EditText) view.findViewById(R.id.etEmail);
        continuePhone =(EditText) view.findViewById(R.id.continuePhone);
        btnContinue = (Button) view.findViewById(R.id.btnContinue);
        hotelName = (TextView) view.findViewById(R.id.tvHotelPicked2);
        from = (TextView) view.findViewById(R.id.tvArrivalPicked);
        to = (TextView) view.findViewById(R.id.tvLeavingPicked);
        price = (TextView) view.findViewById(R.id.tvTotalPrice2);
        phonePrefix = view.findViewById(R.id.spinnerPhone);
        SpinnerPhone(context, phonePrefix);
        hotelName.setText(order.getHotel().getName());
        DateFormat df = new SimpleDateFormat(pattern);
        from.setText(df.format(order.getFrom()));
        to.setText(df.format(order.getTo()));
        price.setText("US$ "+order.getPrice());
        btnContinue.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   String errMsg = "";
                   prefix = prefixPicker;//take the prefix from the spinner
                   person =new Person(firstName.getText().toString(),lastName.getText().toString(),email.getText().toString(),prefix+"-"+continuePhone.getText());
                 System.out.println("person "+person.getphoneNum()  +"yepp"+person.phoneNum +" continuePhone " +continuePhone.getText().toString());
                   errMsg =CheckIfValid.checkPersonDetails(person);
                   if( !(errMsg.equals(""))){
                       Toast.makeText(getActivity(), "You must fill a valid: " + errMsg,
                               Toast.LENGTH_LONG).show();
                       return;
                   }

                   FragmentTransaction transaction =((AppCompatActivity) context).getSupportFragmentManager().beginTransaction();
                   transaction.add(R.id.fragContainer,  new Payment_frag(order,person)); // Add your fragment class
                   transaction.setReorderingAllowed(true);
                   transaction.addToBackStack("FFF");
                   transaction.commit();
               }


    });
    }

}