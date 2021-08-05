package com.example.finalproject;

import java.util.Date;

public class Order {

    int orderNumber;
    Hotel hotel;
    long price;
    Date from;
    Date to;
    int participants;
    int rooms;
    long nights;

    public Order(int orderNumber, Date from, Date to, int participants, int rooms, long nights) {
        this.orderNumber = orderNumber;
        this.from = from;
        this.to = to;
        this.participants = participants;
        this.rooms = rooms;
        this.nights = nights;
    }

    public Order(Order order, Hotel hotel, long price) {
        this.orderNumber = order.getOrderNumber();
        this.hotel = hotel;
        this.price = price;
        this.from = order.getFrom();
        this.to = order.getTo();
        this.participants = order.getParticipants();
        this.rooms = order.getRooms();
        this.nights = order.getNights();
    }

    public long getNights() { return nights; }

    public void setNights(long nights) { this.nights = nights; }

    public int getOrderNumber() { return orderNumber; }

    public void setOrderNumber(int orderNumber) { this.orderNumber = orderNumber; }

    public Hotel getHotel() { return hotel; }

    public void setHotel(Hotel hotel) { this.hotel = hotel; }

    public long getPrice() { return price; }

    public void setPrice(long price) { this.price = price; }

    public Date getFrom() { return from; }

    public void setFrom(Date from) { this.from = from; }

    public Date getTo() { return to; }

    public void setTo(Date to) { this.to = to; }

    public int getParticipants() { return participants; }

    public void setParticipants(int participants) { this.participants = participants; }

    public int getRooms() { return rooms; }

    public void setRooms(int rooms) { this.rooms = rooms; }

    @Override
    public String toString() {
        return "Order{" +
                "orderNumber=" + orderNumber +
                ", from=" + from +
                ", to=" + to +
                ", participants=" + participants +
                ", rooms=" + rooms +
                ", nights=" + nights +
                '}';
    }
}