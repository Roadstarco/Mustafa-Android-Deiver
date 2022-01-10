package com.roadstar.customerr.app.module.ui.chat.model;


import java.io.Serializable;

public class User implements Serializable {
    public String name;
    public String email;
    public String avata;
    public String id;
    public String memberIds;
    public String type;
    public Message message;



    public User(){
//        status = new Status();
        message = new Message();
//        status.isOnline = false;
//        status.timestamp = 0;
        message.idReceiver = "0";
        message.idSender = "0";
        message.text = "";
        message.timestamp = 0;
    }
}
