package com.roadstar.customerr.app.data.models;


import com.roadstar.customerr.app.business.BaseItem;

/**
 * Created by bilal on 29/05/2018.
 */

public class Progress implements BaseItem {
    @Override
    public int getItemType() {
        return BaseItem.ITEM_PROGRESS;
    }
}
