package com.example.finalproject;


import android.content.Context;
import android.content.res.AssetManager;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class HotelXMLParser {
    final static String KEY_HOTEL="hotel";
    final static String KEY_NAME="name";
    final static String KEY_CITY="city";
    final static String KEY_COUNTRY="country";
    final static String KEY_PICTURE="picture";
    final static String KEY_ROOMS="rooms";
    final static String KEY_RATE="rate";
    final static String KEY_PRICE="price";
    final static String KEY_DETAILS="details";

    public static ArrayList<Hotel> parseHotels(Context context){
        ArrayList<Hotel> data = null;
        InputStream in = openHotelsFile(context);
        XmlPullParserFactory xmlFactoryObject;
        try {
            xmlFactoryObject = XmlPullParserFactory.newInstance();
            XmlPullParser parser = xmlFactoryObject.newPullParser();

            parser.setInput(in, null);
            int eventType = parser.getEventType();
            Hotel currentHotel = null;
            String inTag = "";
            String strTagText = null;

            while (eventType != XmlPullParser.END_DOCUMENT){
                inTag = parser.getName();
                switch (eventType){
                    case XmlPullParser.START_DOCUMENT:
                        data = new ArrayList<Hotel>();
                        break;
                    case XmlPullParser.START_TAG:
                        if (inTag.equalsIgnoreCase(KEY_HOTEL))
                            currentHotel = new Hotel();
                        break;
                    case XmlPullParser.TEXT:
                        strTagText = parser.getText();
                        break;
                    case XmlPullParser.END_TAG:
                        if (inTag.equalsIgnoreCase(KEY_HOTEL))
                            data.add(currentHotel);
                        else if (inTag.equalsIgnoreCase(KEY_NAME))
                            currentHotel.name = strTagText;
                        else if (inTag.equalsIgnoreCase(KEY_CITY))
                            currentHotel.city =strTagText;
                        else if (inTag.equalsIgnoreCase(KEY_COUNTRY))
                            currentHotel.country =strTagText;
                        else if (inTag.equalsIgnoreCase(KEY_PICTURE))
                            currentHotel.picture =strTagText;
                        else if (inTag.equalsIgnoreCase(KEY_ROOMS))
                            currentHotel.rooms = Integer.parseInt(strTagText);
                        else if (inTag.equalsIgnoreCase(KEY_RATE))
                            currentHotel.rate = strTagText;
                        else if (inTag.equalsIgnoreCase(KEY_PRICE))
                            currentHotel.price = strTagText;
                        else if (inTag.equalsIgnoreCase(KEY_DETAILS))
                            currentHotel.details = strTagText;
                        else if (inTag.equalsIgnoreCase(KEY_PRICE))
                            currentHotel.setDetails(strTagText);
                        inTag ="";
                        break;
                }//switch
                eventType = parser.next();
            }//while
        } catch (Exception e) {e.printStackTrace();}
        return data;
    }

    private static InputStream openHotelsFile(Context context){
        AssetManager assetManager = context.getAssets();
        InputStream in =null;
        try {
            in = assetManager.open("hotels.xml");
        } catch (IOException e) {e.printStackTrace();}
        return in;
    }
}
