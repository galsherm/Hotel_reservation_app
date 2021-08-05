package com.example.finalproject;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.finalproject.TimeHelper.CheckIfDatePassed;

public class FileHelper {
    private static final String FILE_NAME = "orders.txt";
    private static SharedPreferences sharedpreferences = null;
    private static String file_path;
    private static int numberOfRawOrder=3;

    public FileHelper(String file_path, SharedPreferences preferences) {
        this.file_path = file_path;
        this.sharedpreferences = preferences;
    }

    /**
     * function to write data into raw file
     * @param data
     */
    public static void writeData(String data) {
        File directory = new File(file_path);
        if (!directory.exists()) {
            directory.mkdir();
        }
        File newFile = new File(file_path, File.separator + FILE_NAME);
        try {
            if (!newFile.exists())
                newFile.createNewFile();

            FileOutputStream fOut = new FileOutputStream(newFile, true);
            OutputStreamWriter outputWriter = new OutputStreamWriter(fOut);

            outputWriter.write(data + "\n");
            outputWriter.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * function to read data from raw file
     * @return
     */
    public static String readFile() {
        StringBuilder text = null;
        String line;
        //Get the text file
        File file = new File(file_path, File.separator + FILE_NAME);
        try {
            if (!file.exists())
                file.createNewFile();

            //Read text from file
            InputStream inputStream = new FileInputStream(file);
            text = new StringBuilder();
            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));

            while ((line = br.readLine()) != null) {
                text.append(line);
                text.append('\n');
            }
            inputStream.close();
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return text.toString();
    }

    /**
     * return if the file exist
     * @return true if file exist, false if file not exist
     */
    public static boolean isFileExist() {
        File file = new File(file_path, File.separator + FILE_NAME);
        try {
            if (!file.exists())
                return false;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    /**
     * function to replace line in raw file
     * @param newLine
     * @param position
     */
    public  static void ReplaceLine(String newLine, int position) {
        Path path = Paths.get(file_path+File.separator+FILE_NAME);
        List<String> lines = null;
        try {
            lines = Files.readAllLines(path, StandardCharsets.UTF_8);
            lines.remove(position);
            lines.add(position, newLine);
            Files.write(path, lines, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * function to delete order from raw file
     * @param position
     */
    public  static void DeleteOrder(int position) {
        Path path = Paths.get(file_path+ File.separator+FILE_NAME);
        List<String> lines = null;
        try {
            lines = Files.readAllLines(path, StandardCharsets.UTF_8);
            lines.remove(position);
            lines.remove(position);
            lines.remove(position);
            Files.write(path, lines, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * function to update rooms count in shared preferences file
     * @param order
     */
    public static  void UpdateRooms (Order order) {
        String pattern = "dd/MM/yyyy";
        DateFormat df = new SimpleDateFormat(pattern);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        Calendar startCalendar = Calendar.getInstance();
        startCalendar.setTime(order.getFrom());
        Calendar endCalendar = Calendar.getInstance();
        endCalendar.setTime(order.getTo());

        int currentRooms, newRooms=0;
        while (startCalendar.before(endCalendar)) {
            currentRooms = order.getHotel().getRooms();
            Date result = startCalendar.getTime();

            String key = order.getHotel().getName() + " " + df.format(result);
            if (sharedpreferences.getAll().size() == 0 || !(sharedpreferences.getAll().containsKey(key))) {
                newRooms = currentRooms - order.getRooms();
            } else {
                for (Map.Entry<String, ?> entry : sharedpreferences.getAll().entrySet()) {
                    if (entry.getValue() instanceof String) {
                        if (((String) entry.getKey()).equals(key)) {
                            currentRooms = Integer.parseInt(((String) entry.getValue()));
                            newRooms = currentRooms - order.getRooms();
                        }
                    }
                }
            }
            editor.putString(key, String.valueOf(newRooms));
            editor.apply();
            startCalendar.add(Calendar.DATE, 1);
        }
    }

    public static void decreaseHotelRooms(ArrayList<Hotel> hotels,Order tempOrder){
        String key, val, pattern = "dd/MM/yyyy";
        int size, i;
        Calendar start, end;
        Date targetDay;
        DateFormat df = new SimpleDateFormat(pattern);
        ArrayList<String> hotelsIndex;
        Map<String, String> removeHotel = new HashMap<String, String>();
        hotelsIndex = new ArrayList<>();

        for (i = 0; i < hotels.size(); i++) {
            hotelsIndex.add(hotels.get(i).toString());
        }
        start = Calendar.getInstance();
        start.setTime(tempOrder.getFrom());
        end = Calendar.getInstance();
        end.setTime(tempOrder.getTo());

        // shared preferences
        while (!start.after(end)) {
            targetDay = start.getTime();
            for (Map.Entry<String, ?> entry : sharedpreferences.getAll().entrySet()) {
                for (i = 0; i < hotelsIndex.size(); i++) {
                    key = hotelsIndex.get(i) + " " + df.format(targetDay);
                    val = (sharedpreferences.getString(entry.getKey(), null));
                    if (entry.getKey().equals(key) && Integer.parseInt(val) < tempOrder.getRooms()) {
                        removeHotel.put(hotelsIndex.get(i), hotelsIndex.get(i));
                    }
                }
            }
            start.add(Calendar.DATE, 1);
        }

        size = hotels.size();
        for (Map.Entry<String, ?> entry : removeHotel.entrySet()) {
            for (i = 0; i < size; i++) {
                if (hotels.get(i).getName().equals(entry.getKey())) {
                    if (i == size - 1) {
                        hotels.remove(size - 1);
                        size = size - 1;
                    } else {
                        hotels.remove(i);
                        size = size - 1;
                    }
                }
            }
        }
    }

    public static void DeleteOrderSp( int lineToDelete) {
        int rooms, newRooms, currentRooms;
        String  from, to, day, key, val, pattern = "dd/MM/yyyy";
        DateFormat df = new SimpleDateFormat(pattern);
        String[] ordersLines, order;
        StringBuilder hotel;
        Date targetDay, fromDate = null, toDate = null;
        Calendar start, end;
        ordersLines = FileToArray();
        order = ordersLines[lineToDelete * numberOfRawOrder].split(" ", 0);
        from = order[order.length - 3];
        to = order[order.length - 2];
        rooms = Integer.parseInt(order[order.length-1]);
        hotel = getHotelName(order);

        try {
            fromDate = df.parse(from);
            toDate = df.parse(to);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        start = Calendar.getInstance();
        start.setTime(fromDate);
        end = Calendar.getInstance();
        end.setTime(toDate);
        end.add(Calendar.DATE,-1);
        while (!start.after(end) && !end.before(start)) {
            targetDay = start.getTime();
            day = df.format(targetDay);
            key = hotel+day;
            val = (sharedpreferences.getString(key, null));
            currentRooms = Integer.parseInt(val);
            newRooms = currentRooms + rooms;
            sharedpreferences.edit()
                    .putString(key, newRooms+"")
                    .apply();
            start.add(Calendar.DATE, 1);
        }
    }

    //Assets
    public static  void checkValidRoomsAssets (ArrayList<Hotel> hotels ,Order tempOrder) {
        for (int i = 0; i < hotels.size(); i++) {
            if (hotels.get(i).getRooms() < tempOrder.getRooms()) {
                hotels.remove(i);
                i--;
            }
        }
    }

    /**
     * file to get array of all the lines in raw file
     * @return
     */
    public static String[] FileToArray()
    {
        String ordersData = null;
        try {
            ordersData = FileHelper.readFile();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ordersData.split("\n", 0);
    }

    public static boolean FileNotEmpty()
    {
        return (FileToArray().length % numberOfRawOrder == 0);
    }

    /**
     * file to to sorted array of vacation dates from raw file
     * @return
     */
    public static String[] GetDates()
    {
        String[] ordersDetailsSeperate, dates, ordersLines, ordersDetails;
        Date[] arrayOfDates;
        int i, j = 0;
        ordersLines = FileToArray();
        ordersDetails = GetOrdersDetails();
        dates = new String[ordersLines.length / 3];

        for (i = 0; i < ordersDetails.length; i++) {
            ordersDetailsSeperate = ordersDetails[j].split(" ", 0);
            dates[j] = ordersDetailsSeperate[ordersDetailsSeperate.length - 3];
            j++;
        }

        arrayOfDates = new Date[dates.length];
        for (int index = 0; index < dates.length; index++) {
            try {
                arrayOfDates[index] = new SimpleDateFormat("dd/MM/yyyy").parse(dates[index]);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        Arrays.sort(arrayOfDates);
        for (int index = 0; index < dates.length; index++) {
            dates[index] = new SimpleDateFormat("dd/MM/yyyy").format(arrayOfDates[index]);
        }

        return dates;//returns only sorted from dates
    }

    /**
     * function to get the orders details (order_number hotel_name start_date end_date rooms) from raw file
     * @return
     */
    public static String[] GetOrdersDetails()
    {
        String[] ordersLines, ordersDetails;
        int i, j = 0;
        ordersLines = FileToArray();
        ordersDetails = new String[ordersLines.length / 3];

        for (i = 0; i < ordersLines.length; i++) {
            if (i % 3 == 0) {
                ordersDetails[j] = ordersLines[i];
                j++;
            }
        }
        return ordersDetails;
    }

    /**
     * function to get the next available order number from raw file
     * @return
     */
    public static int GetOrderNumber()
    {
        int orderNum;
        String[] lastOrderDetails, ordersLines;
        ordersLines = FileToArray();

        lastOrderDetails = ordersLines[ordersLines.length - numberOfRawOrder].split(" ", 0);
        orderNum = Integer.parseInt(lastOrderDetails[0]) + 1;

        return orderNum;
    }

    /**
     * function to get the next vacation hotel name from raw file
     * @param starDate
     * @return
     */
    public static StringBuilder GetHotelNameNext(Date starDate)
    {
        StringBuilder hotelName;
        String[] ordersDetailsSeperate, ordersDetails;
        int i, hotelIndex = 0;
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        ordersDetails = GetOrdersDetails();

        for (i = 0; i < ordersDetails.length; i++) {
            ordersDetailsSeperate = ordersDetails[i].split(" ", 0);
            if (ordersDetailsSeperate[ordersDetailsSeperate.length - 3].equals(df.format(starDate))) {
                hotelIndex = i;
                break;
            }
        }
        ordersDetails = GetOrdersDetails();
        ordersDetailsSeperate = ordersDetails[hotelIndex].split(" ", 0);
        hotelName = getHotelName(ordersDetailsSeperate);
        return hotelName;
    }

    public static StringBuilder getHotelName(String[] order)
    {
        StringBuilder hotel;
        hotel = new StringBuilder();
        for (int i = 1; i < order.length - 3; i++) {
            hotel.append(order[i]+" ");
        }
        return hotel;
    }

    /**
     * function to get the next vacation start date from raw file
     * @return
     */
    public static Date FindStartDate()
    {
        Date startDate = null, today;
        boolean pass;
        int i;
        String[] dates;
        dates = GetDates();
        today = Calendar.getInstance().getTime();

        for (i = 0; i < dates.length; i++) {
            try {
                startDate = new SimpleDateFormat("dd/MM/yyyy").parse(dates[i]);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            pass = CheckIfDatePassed(startDate);
            if (startDate.after(today) || !pass) {
                try {
                    startDate = new SimpleDateFormat("dd/MM/yyyy").parse(dates[i]);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                return startDate;
            }
        }
        return null;
    }

    /**
     * function to get the orders numbers from raw file
     * @return
     */
    public static String[] GetOrdersNumbers()
    {
        String[] ordersDetails, orderNumbers, ordersDetailsSeperate;
        ordersDetails = GetOrdersDetails();
        orderNumbers = new String[ordersDetails.length];
        for(int i = 0; i<ordersDetails.length ; i++)
        {
            ordersDetailsSeperate =ordersDetails[i].split(" ",0);
            orderNumbers[i] = ordersDetailsSeperate[0];
        }
        return orderNumbers;
    }


}
