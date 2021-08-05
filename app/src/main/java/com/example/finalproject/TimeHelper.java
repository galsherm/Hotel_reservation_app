package com.example.finalproject;

// Java program to find the difference between two dates

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class TimeHelper {
    public static long timeMi;
    public static int HOUR = 22, MINUTE=25;
    public static String checkinTime = HOUR + ":" + MINUTE + ":" + "00";

    // Function to print difference in time start_date and end_date
    public static ArrayList<Object> findDifference(Date start_date, Date end_date)
    {
        ArrayList difference = new ArrayList<>();

        Date d1 = start_date;
        Date d2=end_date;
        // Calucalte time difference in milliseconds
        long difference_In_Time= d2.getTime() - d1.getTime();

        // Calucalte time difference in seconds, minutes, hours, years, and days
        long difference_In_Seconds
                = TimeUnit.MILLISECONDS
                .toSeconds(difference_In_Time)
                % 60;
        long difference_In_Minutes
                = TimeUnit
                .MILLISECONDS
                .toMinutes(difference_In_Time)
                % 60;

        long difference_In_Hours
                = TimeUnit
                .MILLISECONDS
                .toHours(difference_In_Time)
                % 24;

        long difference_In_Days
                = TimeUnit
                .MILLISECONDS
                .toDays(difference_In_Time)
                % 365;

        long difference_In_Years
                = TimeUnit
                .MILLISECONDS
                .toDays(difference_In_Time)
                / 365l;

        if(difference_In_Hours<0 || difference_In_Days <0 || difference_In_Minutes<0 || difference_In_Years <0 || difference_In_Seconds<0)
        {
            System.out.println("less then");
            difference.add("missed");
            return difference;
        }
        else {
            difference.add(difference_In_Years);
            difference.add(difference_In_Days);
            difference.add(difference_In_Hours);
            difference.add(difference_In_Minutes);
            difference.add(difference_In_Seconds);
            timeMi = TimeUnit.DAYS.toMillis((difference_In_Days)) + TimeUnit.HOURS.toMillis((difference_In_Hours)) + TimeUnit.MINUTES.toMillis((difference_In_Minutes)) + TimeUnit.SECONDS.toMillis((difference_In_Seconds));
        }
        return difference;
    }


    public static boolean CheckIfDatePassed(Date d)
    {
        boolean pass;
        Calendar calendar1, calendar2;
        Date today;
        today = new Date();

        calendar1 = Calendar.getInstance();
        calendar1.setTime(d);
        calendar2 = Calendar.getInstance();
        calendar2.setTime(today);

        pass = false;
        if (calendar1.get(Calendar.YEAR) == calendar2.get(Calendar.YEAR)
                && calendar1.get(Calendar.MONTH) == calendar2.get(Calendar.MONTH)
                && calendar1.get(Calendar.DAY_OF_MONTH) == calendar2.get(Calendar.DAY_OF_MONTH)) {
            if (calendar2.get(Calendar.HOUR_OF_DAY) < HOUR || (calendar2.get(Calendar.HOUR_OF_DAY) == HOUR && calendar2.get(Calendar.MINUTE) < MINUTE)) {
                pass = false;
            } else {
                pass = true;
            }
        } else if (d.before(today)) {
            pass = true;
        }
        return pass;
    }

    public static Date SetDate(Date c)
    {
        Calendar cal = Calendar.getInstance();
        cal.setTime(c);
        cal.set(Calendar.HOUR_OF_DAY, HOUR);
        cal.set(Calendar.MINUTE, MINUTE);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        Date d = cal.getTime();
        return d;
    }

}

