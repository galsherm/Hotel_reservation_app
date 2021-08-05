package com.example.finalproject;

import android.app.DatePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import static com.example.finalproject.FileHelper.*;
import static com.example.finalproject.TimeHelper.*;

// we inport here the file helper and TimerHelper class
public class Home_frag extends Fragment implements DatePickerDialog.OnDateSetListener, View.OnClickListener {

    private EditText from, to, participants;
    private Button btnCalendarFrom, btnCalendarTo, btnSearch, btnAdd, btnDec;
    private String picked;
    private boolean toButtonPressed = false, fromButtonPressed = false;
    private int oldParticipants;
    private Date startDate;
    private SimpleDateFormat sdf;
    private home_frag_listener listener;
    private TextView time, timerTv, vacation;
    private StringBuilder hotelName;
    private int  newParticipants = 1, orderNum = 1000;
    public static Date fromDateTotTimer;
    private boolean back = false;
    private Order tempOrderBack;

    public Home_frag() { }

    public Home_frag(Order order) {
        this.tempOrderBack = order;//it use to restore the data of the order
        back = true;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        try {
            this.listener = (home_frag_listener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException("the class " +
                    context.getClass().getName() +
                    " must implements the interface 'home_frag_listener'");
        }
        super.onAttach(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frag_home, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        startAlarm();//start service
        sdf = new SimpleDateFormat("dd/MM/yyyy"); // here set the pattern as you date in string was containing like date/month/year

        time = (TextView) view.findViewById(R.id.timerTv2);
        vacation = (TextView) view.findViewById(R.id.tvVacation);
        timerTv = (TextView) view.findViewById(R.id.timerTv);

        from = (EditText) view.findViewById(R.id.etFrom);
        to = (EditText) view.findViewById(R.id.etTo);
        participants = (EditText) view.findViewById(R.id.etParticipants);

        btnCalendarFrom = (Button) view.findViewById(R.id.btnCalendarFrom);
        btnCalendarTo = (Button) view.findViewById(R.id.btnCalendarTo);
        btnAdd = (Button) view.findViewById(R.id.btnAdd);
        btnDec = (Button) view.findViewById(R.id.btnRemove);
        btnSearch = (Button) view.findViewById(R.id.btnSearch);

        //disable the option that the user write to edittext if the dates//
        from.setEnabled(false);
        from.setCursorVisible(false);
        to.setEnabled(false);
        to.setCursorVisible(false);
        ////////////////////////////////////////////////

        clearTimer();                       //clear timer textViews -the counter ,the hotel name

        if (isFileExist() && FileNotEmpty()) {//check in raw file
            orderNum = GetOrderNumber();    //generate the next order number
                updateTimer();                  //update timer

        }

        if (back) {                         //if back from the hotels_frag set the previous selection
          //  if(!(from.getText().equals(to.getText()))) {
                from.setText(sdf.format(tempOrderBack.getFrom())); //tempOrderBack is the value we define in the constructor
                to.setText(sdf.format(tempOrderBack.getTo()));
                newParticipants = tempOrderBack.getParticipants();
                participants.setText(newParticipants + "");
           // }
        }

        btnCalendarFrom.setOnClickListener(new View.OnClickListener() { //CookBook 3
            @Override
            public void onClick(View v) {
                fromButtonPressed = true;//sets flag fromButtonPressed in the showDatePickerDailog
                showDatePickerDailog();
            }
        });

        btnCalendarTo.setOnClickListener(new View.OnClickListener() { //CookBook 3
            @Override
            public void onClick(View v) {
                toButtonPressed = true;//sets flag toButtonPressed in the showDatePickerDailog
                showDatePickerDailog();
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {       //CookBook 3
            @Override
            public void onClick(View v) {

                oldParticipants = Integer.valueOf(participants.getText().toString());
                newParticipants = oldParticipants + 1;
                participants.setText("" + newParticipants);
            }
        });

        btnDec.setOnClickListener(new View.OnClickListener() {      //CookBook 3
            @Override
            public void onClick(View v) {
                oldParticipants = Integer.valueOf(participants.getText().toString());
                if (oldParticipants > 0) {
                    newParticipants = oldParticipants - 1;
                    participants.setText("" + newParticipants);
                }
            }
        });

        btnSearch.setOnClickListener(this);                        //CookBook 4 ,the on click method we can check in the buttom of this fragment code
    }

    public void updateTimer()
    {
        fromDateTotTimer=startDate;
        startDate = FindStartDate();                               //find the next nearest vacation date
        if(startDate!=null) {
            hotelName = GetHotelNameNext(startDate);               //find the next nearest vacation hotel name
                vacation.setText("Your vacation at " + hotelName + "begins in:");

            try {
                updateTimeText(startDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        else
        {
            clearTimer();           //clear timer textViews
        }
    }


    private void showDatePickerDailog() {
        Calendar myCalendar = Calendar.getInstance();
        Calendar minDay = Calendar.getInstance();
        Calendar maxDay = Calendar.getInstance();
        String focusDate;
        String[] focusVal;
        if (toButtonPressed) {
            if (!from.getText().toString().isEmpty()) {           //disable all dates before selected from date
                String minDate = from.getText().toString();
                String[] dateVal = minDate.split("/", 0);//dateVal[0] -> day //  dateVal[1] -> month //dateVal[2] -> year
                minDay.set(Integer.parseInt(dateVal[2]), Integer.parseInt(dateVal[1]) - 1, Integer.parseInt(dateVal[0]));
                minDay.add(Calendar.DATE, 1);       // here we add another day that show dates from
            }

            if (to.getText().toString().isEmpty()) {           //set previous date as the next date after from selection
                myCalendar = minDay;//sets the minimal date that the user can pick
            }
            if (!to.getText().toString().isEmpty()) {           //set focus date as the previous to selection
                focusDate = to.getText().toString();
                focusVal = focusDate.split("/", 0);
                myCalendar.set(Integer.parseInt(focusVal[2]), Integer.parseInt(focusVal[1]) - 1, Integer.parseInt(focusVal[0]));
            }
        }

        if (fromButtonPressed) {
            if (!from.getText().toString().isEmpty()) {             //set focus date as the previous from selected
                focusDate = from.getText().toString();
                focusVal = focusDate.split("/", 0);
                myCalendar.set(Integer.parseInt(focusVal[2]), Integer.parseInt(focusVal[1]) - 1, Integer.parseInt(focusVal[0]));
            }
            if (!to.getText().toString().isEmpty()) {               //disable all dates after selected to date
                String maxDate = to.getText().toString();
                String[] dateVal = maxDate.split("/", 0);
                maxDay.set(Integer.parseInt(dateVal[2]), Integer.parseInt(dateVal[1]) - 1, Integer.parseInt(dateVal[0]));
                maxDay.add(Calendar.DATE, -1);
            }
        }

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                getContext(),
                this,
                myCalendar.get(Calendar.YEAR),      //the regular constructor of DatePickerDialog
                myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)
        );

        datePickerDialog.getDatePicker().setMinDate(minDay.getTimeInMillis());//here we actually sets the min day that repesnts on the date picker
        if (fromButtonPressed && !to.getText().toString().isEmpty()) {
            datePickerDialog.getDatePicker().setMaxDate(maxDay.getTimeInMillis());
        }
        datePickerDialog.show();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) { //implements datePickerDialog
        month = month + 1;  // month start with 0 so we add 1
        picked = dayOfMonth + "/" + month + "/" + year;
        if (fromButtonPressed) {
            from.setText(picked);//change edit text
            fromButtonPressed = false;
        } else if (toButtonPressed) {
            to.setText(picked);
            toButtonPressed = false;
        }
    }

    @Override
    public void onClick(View v) {
        Date d1 = null, d2 = null, now;
        ArrayList<Object> different;
        long diff, diffDays;
        int rooms;
        Order tempOrder;


        SimpleDateFormat format;
        if(Integer.parseInt(participants.getText().toString())==0  ){
            Toast.makeText(getActivity(),"You need to choose at least one participant", Toast.LENGTH_LONG).show();
            return;
        }

        format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

        if (!(from.getText().toString().isEmpty() || to.getText().toString().isEmpty())) {
            try {
                d1 = format.parse(from.getText() + " " + checkinTime);
                d2 = format.parse(to.getText() + " 00:00:00");

            } catch (ParseException e) {
                e.printStackTrace();
            }

            now = new Date();
            different = findDifference(now, d1);
            if (different.get(0).equals("missed")) {
                Toast.makeText(getActivity(),"You missed check in time today, please choose another day", Toast.LENGTH_LONG).show();
                return;
            } else {
                diff = d2.getTime() - d1.getTime();
                diffDays = 1 + diff / (24 * 60 * 60 * 1000);
                rooms = ((newParticipants - 1) / 3) + 1;
                tempOrder = new Order(orderNum, d1, d2, newParticipants, rooms, diffDays);

                try {
                    listener.OnSearch(tempOrder);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        } else
            Toast.makeText(getActivity(), "You must insert Two dates", Toast.LENGTH_LONG).show();
    }

    private void updateTimeText(Date c) throws ParseException {
        Date rnow = new Date();
        Date d = SetDate(c);
        findDifference(rnow, d);
        new CountDownTimer(timeMi, 1000) {
            public void onTick(long millisUntilFinished) {
                if(MyBroadcastReceiver.TherIsInventation ==1) {
                    updateCountDownText(d);
                }
            }
            public void onFinish() {
                if(MyBroadcastReceiver.TherIsInventation ==1) {
                    updateTimer();
                }
            }
        }.start();
    }

    private void updateCountDownText(Date c) {
        ArrayList li;
        int years, days, hours, minutes, seconds;
        String yearsString, daysString, hoursString, minutesString, secondsString;
        Date rnow;
        rnow = new Date();
        li = findDifference(rnow, c);

        years = Integer.parseInt(li.get(0).toString());           //transfer time parameters to int
        days = Integer.parseInt(li.get(1).toString());
        hours = Integer.parseInt(li.get(2).toString());
        minutes = Integer.parseInt(li.get(3).toString());
        seconds = Integer.parseInt(li.get(4).toString());

        yearsString = intToString(years);                       //transfer time parameters to String
        daysString = intToString(days);
        hoursString = intToString(hours);
        minutesString = intToString(minutes);
        secondsString = intToString(seconds);

        if (years > 0) {                                        //update timer textViews
            timerTv.setText(yearsString + ":" + daysString + ":" + hoursString + ":" + minutesString + ":" + secondsString);
            time.setText("     years  :   days   :  hours  : minutes : seconds");
        } else if (days > 0) {
            timerTv.setText(daysString + ":" + hoursString + ":" + minutesString + ":" + secondsString);
            time.setText("     days  :  hours  : minutes : seconds");
        } else {
            timerTv.setText(hoursString + ":" + minutesString + ":" + secondsString);
            time.setText("    hours : minutes : seconds");
        }
    }

    private String intToString(int num) {           //function for timer String
        if (num < 10) { //if we want to show one dighit (it means zero in the left)
            return "0" + num;
        }
        return num + "";
    }

    private void clearTimer()
    {
        timerTv.setText("");
        time.setText("");
        vacation.setText("");
    }

    private void startAlarm() {//service trigger the broadcast recierver
        if(isFileExist() && FileNotEmpty()) {
            Intent intentService = new Intent(getContext(), MyService.class);
            Integer integerTimeSet = Integer.parseInt("15");
            intentService.putExtra("TimeValue", integerTimeSet);

            getActivity().startForegroundService(intentService);
        }
    }

    public interface home_frag_listener {
        public void OnSearch(Order order) throws ParseException;
    }

}
