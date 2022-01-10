package com.roadstar.customerr.app.business;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.roadstar.customerr.common.utils.NetworkUtil;

/**
 * Created by $Bilal on 4/6/2016.
 */
public class NetworkChangeListener extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent brodIntent = new Intent("network_status");
        brodIntent.putExtra("isConnected", NetworkUtil.isNetworkConnected(context));
        context.sendBroadcast(brodIntent);

        Log.d("Network", ""+ NetworkUtil.isNetworkConnected(context) +"----"+
        NetworkUtil.hasWLANConnection(context)+"-----"+ NetworkUtil.hasMobileConnection(context));
    }
}
