package com.example.finalproject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.text.ParseException;
import java.util.ArrayList;
import static com.example.finalproject.FileHelper.*;


public class Hotels_frag extends Fragment {
    ArrayList<Hotel> hotels;
    RecyclerView rvHotels;
    private Order tempOrder;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.frag_hotels, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rvHotels = (RecyclerView) getActivity().findViewById(R.id.recyclerView);
        hotels = HotelXMLParser.parseHotels(getContext());
    }

    //the activity informs fragB about new click in fragA
    public void onNewSearch(Order tempOrder) throws ParseException {
        this.tempOrder = tempOrder;
        checkValidRoomsAssets(hotels,tempOrder); //check in the assets if the number of rooms that we want is greater than the number of rooms in the file then no represent room
        decreaseHotelRooms(hotels,tempOrder);//sp file decrease rooms number
        if (hotels.size() == 0) {
            Toast.makeText(getContext(), "There are no hotels to represent, please try diffrents dates", Toast.LENGTH_LONG).show();
            FragmentTransaction transaction = ((AppCompatActivity) getContext()).getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragContainer, new Home_frag(tempOrder)); // Add your fragment class
            transaction.setReorderingAllowed(true);
            transaction.commit();
        }

        else {
            HotelAdapter adapter = new HotelAdapter(hotels, tempOrder);
            rvHotels.setAdapter(adapter);
            rvHotels.setLayoutManager(new LinearLayoutManager(getContext()));
        }
    }


    ////menu
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {

        inflater.inflate(R.menu.menu2, menu);
        super.onCreateOptionsMenu(menu, inflater);


    }
}