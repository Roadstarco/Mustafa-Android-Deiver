package com.roadstar.customerr.app.module.ui.chat.model;


import java.io.Serializable;

public class Message implements Serializable {
    public String idSender;
    public String nameSender;
    public String idReceiver;
    public String text;
    public String status;
    public String type;
    public boolean isFuture;
    public long timestamp;
}