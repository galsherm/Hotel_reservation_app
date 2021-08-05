package com.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import static com.example.finalproject.FileHelper.*;
import static com.example.finalproject.TimeHelper.CheckIfDatePassed;

public class MainActivity extends AppCompatActivity implements Home_frag.home_frag_listener  {
    private static String file_path;
    private Date  today, lastDate;
    private String[] dates;
    private SharedPreferences preferences;
    MyBroadcastReceiver MybroadCastReceiver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        file_path=this.getFilesDir().getAbsolutePath();
        preferences = this.getSharedPreferences("SharedPreferences", Context.MODE_PRIVATE);
        FileHelper fileHelper = new FileHelper(file_path,preferences);
        today = Calendar.getInstance().getTime();
        setContentView(R.layout.activity_main);
        MybroadCastReceiver =new MyBroadcastReceiver() ;



    }




    /*************broadcast**************////////////
@Override
protected void onResume() {
    // IntentFilter intentFilter = new IntentFilter();
    IntentFilter intentFilter = new IntentFilter("android.permission.FOREGROUND_SERVICE");
    intentFilter.addAction("Counter");

    this.registerReceiver(MybroadCastReceiver, intentFilter);//activly listen to event


    super.onResume();
}
    @Override
    protected void onStop() {
        unregisterReceiver(MybroadCastReceiver);
        super.onStop();
    }

////////////////////////////////////////////////////
    @Override
    public void OnSearch(Order tempOrder) throws ParseException {
        Hotels_frag hotelsFrag;

            getSupportFragmentManager().beginTransaction()		//This block create fragb dynamically in the stack
                    .setReorderingAllowed(true)
                    .add(R.id.fragContainer, Hotels_frag.class, null,"HOTELSFRAG")	//frag container is the host ,fragB is the new container
                    .addToBackStack("BBB")
                    .commit();
            getSupportFragmentManager().executePendingTransactions();
            hotelsFrag = (Hotels_frag) getSupportFragmentManager().findFragmentByTag("HOTELSFRAG");	// if we are in landscape oriantation  //find frag b in stack

        hotelsFrag.onNewSearch(tempOrder);
    }

    //create menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.orderMenu:

                    getSupportFragmentManager().beginTransaction()		//This block create fragb dynamically in the stack
                            .setReorderingAllowed(true)
                            .replace(R.id.fragContainer, Home_frag.class, null,"HOTELSFRAG")	//frag container is the host, fragB is the new container
                            .commit();
                    getSupportFragmentManager().executePendingTransactions();

                return true;

            case R.id.updateMenu:
                boolean pass;
                if(isFileExist() && FileNotEmpty())
                {
                        dates = GetDates();                 //sorted array of orders dates
                        try {
                            lastDate = new SimpleDateFormat("dd/MM/yyyy").parse(dates[dates.length - 1]);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        pass = CheckIfDatePassed(lastDate);
                        if (lastDate.before(today) && pass) {
                            Toast.makeText(this, "There are no orders to update!", Toast.LENGTH_SHORT).show();
                        } else {
                            showAlertSettingDialog();
                        }
                }
                else {
                    Toast.makeText(this, "There are no orders to update!", Toast.LENGTH_SHORT).show();
                }
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showAlertSettingDialog() {
        FragmentManager fm = this.getSupportFragmentManager();
        FindOrderDialogFragment alertDialog = FindOrderDialogFragment.newInstance("Edit Order");
        alertDialog.show(fm, "fragment_alert");//eqal to transaction operation
    }

}
