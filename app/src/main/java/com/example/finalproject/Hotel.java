package com.example.finalproject;

public class Hotel {
    String name;
    String city;
    String country;
    String picture;
    int rooms;
    String rate;
    String price;
    String details;

    public Hotel()
    {

    }

    public Hotel(String name, String city, String country, String picture, int rooms, String rate, String price, String details) {
        this.name = name;
        this.city = city;
        this.country = country;
        this.picture = picture;
        this.rooms = rooms;
        this.rate = rate;
        this.price = price;
        this.details = "";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public int getRooms() {
        return rooms;
    }

    public void setRooms(int rooms) {
        this.rooms = rooms;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    @Override
    public String toString() {
        return name;
    }

    public int compare(Hotel other) {
        return  this.name.compareTo(other.name);
    }

}
