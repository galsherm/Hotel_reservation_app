package com.example.finalproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Date;
import java.util.List;

public  class HotelAdapter extends  RecyclerView.Adapter<HotelAdapter.ViewHolder> {

    // Store a member variable for the contacts
    private List<Hotel> mHotels;
    private Order tempOrder;

    //sp
    //public static String s1;
    //
    // Pass in the contact array into the constructor
    public HotelAdapter(List<Hotel> hotel, Order tempOrder) {

        this.mHotels = hotel;
        this.tempOrder = tempOrder;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View hotelView = inflater.inflate(R.layout.hotel_item, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(hotelView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull HotelAdapter.ViewHolder holder, final int position) {
        // Get the data model based on position
        Hotel hotel = mHotels.get(position);
        holder.BindData(holder,tempOrder,hotel);

    }

    @Override
    public int getItemCount() {
        return mHotels.size();
    }


    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    public  class ViewHolder extends RecyclerView.ViewHolder {
        private long totalPrice;

        public  void BindData(@NonNull HotelAdapter.ViewHolder holder, Order tempOrder, Hotel hotel){
            // Set item views based on your views and data model
            Context context = holder.picture.getContext();
            int id = context.getResources().getIdentifier(hotel.getPicture(),"drawable",context.getPackageName());
            holder.picture.setImageResource(id);
            holder.picture.setClipToOutline(true);
            holder.hotelName.setText(hotel.getName());
            holder.location.setText(hotel.getCity()+", "+hotel.getCountry());
            System.out.println("$$$ tempOrder: "+tempOrder);
            totalPrice = Long.parseLong(hotel.getPrice())*tempOrder.getNights()*tempOrder.getRooms();
            holder.price.setText("US$"+totalPrice);
            holder.rate.setText(hotel.getRate());
            holder.amount.setText(tempOrder.getNights()+" nights, "+tempOrder.getParticipants()+" people");
            System.out.println(" holder hotel name "+holder.hotelName);
            Order order = new Order(tempOrder,hotel,totalPrice);
            holder.itemView.setOnClickListener((view)->{    //coockbook 3
                FragmentActivity activity = (FragmentActivity)(context);
                FragmentManager fm = activity.getSupportFragmentManager();
                HotelDetails_frag alertDialog = new HotelDetails_frag(order);
                alertDialog.show(fm, "fragment_alert");
                }
            );
        }
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        View itemView;
        public ImageView picture;
        public TextView hotelName;
        public TextView  location;
        public TextView  price;
        public TextView  rate;
        public TextView  amount;


        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.

            super(itemView);
            this.itemView = itemView;
            itemView.setClickable(true);

            picture =(ImageView)itemView.findViewById(R.id.imPicture);
            hotelName =(TextView)itemView.findViewById(R.id.tvName);
            location =(TextView)itemView.findViewById(R.id.tvLocation);
            price = (TextView)itemView.findViewById(R.id.tvTotalPrice);
            rate = (TextView)itemView.findViewById(R.id.tvRate);
            amount = (TextView)itemView.findViewById(R.id.tvAmount);
        }
    }

}
