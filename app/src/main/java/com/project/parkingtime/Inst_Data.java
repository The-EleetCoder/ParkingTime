package com.project.parkingtime;

public class Inst_Data {
    public String name,city,state,email,password;
    public int slots,price,count;

    public Inst_Data(){

    }

    public Inst_Data(String name, String city, String state, int slots, int price, String email, String password, int count)
    {
        this.name=name;
        this.city=city;
        this.state=state;
        this.slots=slots;
        this.price=price;
        this.email=email;
        this.password=password;
        this.count=count;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}