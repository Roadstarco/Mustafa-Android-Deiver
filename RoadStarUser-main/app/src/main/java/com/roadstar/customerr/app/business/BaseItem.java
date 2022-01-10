package com.roadstar.customerr.app.business;

public interface BaseItem {
    int ITEM_HEADER = 50;
    int ITEM_EMPTY = 60;
    int ITEM_PROGRESS = 70;
    int ITEM_HISTORY = 1;
    int getItemType();
}
