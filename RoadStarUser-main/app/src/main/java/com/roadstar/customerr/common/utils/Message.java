package com.roadstar.customerr.common.utils;

import com.google.gson.annotations.SerializedName;

public class Message<T> {

    public enum MsgId {
        CARD_ADDED,
    }

    @SerializedName("msg_id")
    private MsgId msg_id;
    @SerializedName("data")
    private T data;

    public MsgId getMsg_id() {
        return msg_id;
    }

    public Message setMsg_id(MsgId msg_id) {
        this.msg_id = msg_id;
        return this;
    }

    public T getData() {
        return data;
    }

    public Message setData(T data) {
        this.data = data;
        return this;
    }
}
