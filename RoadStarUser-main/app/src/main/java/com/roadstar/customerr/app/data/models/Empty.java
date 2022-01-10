package com.roadstar.customerr.app.data.models;


import com.roadstar.customerr.app.business.BaseItem;

/**
 * Created by bilal on 01/03/2018.
 */

public class Empty implements BaseItem {

    String message = "";

    public Empty(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public int getItemType() {
        return BaseItem.ITEM_EMPTY;
    }
}
