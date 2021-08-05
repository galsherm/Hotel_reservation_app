package com.example.finalproject;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentTransaction;

import java.util.Date;

public class HotelDetails_frag extends DialogFragment {

    private Button btnOrder;
    private TextView tvName, tvLocation, tvDetails, tvPrice, tvAmount;
    private Hotel hotel;
    private Dialog builder;
    private ImageView imv1, imv2, imv3;
    private  String hotelSuffixPic2="2",hotelSuffixPic3="3";
    private int id,id2,id3;
    Order tempOrder;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = getActivity().getLayoutInflater().inflate(R.layout.frag_details, new LinearLayout(getActivity()), false);
        // Build dialog
        builder = new Dialog(getActivity());
        builder.requestWindowFeature(Window.FEATURE_NO_TITLE);
        builder.setContentView(view);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(builder.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;

        builder.getWindow().setAttributes(lp);
        return builder;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frag_details, container, false);
    }

    public HotelDetails_frag(Order o)
    {
        this.tempOrder = o;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btnOrder = (Button) view.findViewById(R.id.btnOrder);
        tvName = (TextView) view.findViewById(R.id.tvHName);
        tvLocation = (TextView) view.findViewById(R.id.tvHLocation);
        tvDetails = (TextView) view.findViewById(R.id.tvDetails);
        tvPrice = (TextView) view.findViewById(R.id.tvHPrice);
        tvAmount = (TextView) view.findViewById(R.id.tvHAmount);
        imv1 = (ImageView) view.findViewById(R.id.imageView);
        imv2 = (ImageView) view.findViewById(R.id.imageView2);
        imv3 = (ImageView) view.findViewById(R.id.imageView3);
        hotel = tempOrder.getHotel();
        tvName.setText(hotel.getName());
        tvLocation.setText(hotel.getCity()+", "+hotel.getCountry());
        tvDetails.setText(hotel.getDetails());
        tvPrice.setText("US$ "+tempOrder.getPrice());
        Context context = getContext();
        id = context.getResources().getIdentifier(hotel.getPicture(),"drawable",context.getPackageName());
        id2 = context.getResources().getIdentifier( hotel.getName().replaceAll("\\s+","").toLowerCase()+hotelSuffixPic2,"drawable",context.getPackageName());
        id3 = context.getResources().getIdentifier( hotel.getName().replaceAll("\\s+","").toLowerCase()+hotelSuffixPic3,"drawable",context.getPackageName());

        imv1.setImageResource(id);
        imv2.setImageResource(id2);
        imv3.setImageResource(id3);


        tvAmount.setText(tempOrder.getNights()+" nights, "+tempOrder.getParticipants()+" people");
        btnOrder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FragmentTransaction transaction =((AppCompatActivity) getContext()).getSupportFragmentManager().beginTransaction();
                    transaction.add(R.id.fragContainer,  new Order_frag(tempOrder)); // Add your fragment class
                    transaction.setReorderingAllowed(true);
                    transaction.addToBackStack("EEE");
                    transaction.commit();
                    builder.dismiss();
                }
            }
        );
    }


}
