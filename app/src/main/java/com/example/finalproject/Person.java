package com.example.finalproject;



public class Person {
    String firstName;
    String lastName;
    String Email;
    String phoneNum;

    public String getlastName() {
        return lastName;
    }

    public void setlastName(String lastName) {
        this.lastName = lastName;
    }

    public String getfirstName() {
        return firstName;
    }

    public void setfirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getphoneNum() {
        return phoneNum;
    }

    public void setphoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }


    public Person(String firstName, String lastName,String Email,String phoneNum){
        this.firstName = firstName;
        this.lastName = lastName;
        this.Email =Email;
        this.phoneNum=phoneNum;


    }


    public Person() {
        // TODO Auto-generated constructor stub
    }

    public void setEmail(String securityCode){
        this.Email = securityCode;
    }
    public String getEmail(){
        return this.Email;
    }

    /*public int compare(Order other) {
        return  this.firstName.compareTo(other.cardOwner);
    }
*/
    @Override
    public String toString() {
        return firstName;
    }
}

