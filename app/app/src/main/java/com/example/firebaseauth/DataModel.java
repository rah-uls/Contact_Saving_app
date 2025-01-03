package com.example.firebaseauth;
//recyclerview item setup data model
public class DataModel {
   String name,mobile;
   public DataModel(){}

    public DataModel(String name, String mobile) {
        this.name = name;
        this.mobile = mobile;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }


}

