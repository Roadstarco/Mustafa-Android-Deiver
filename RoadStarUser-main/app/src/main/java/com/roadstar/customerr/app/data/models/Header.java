package com.roadstar.customerr.app.data.models;


import com.roadstar.customerr.app.business.BaseItem;

/**
 * Created by bilal on 27/02/2018.
 */

public class Header implements BaseItem {

    String title = "";

    public Header(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public int getItemType() {
        return BaseItem.ITEM_HEADER;
    }
}
