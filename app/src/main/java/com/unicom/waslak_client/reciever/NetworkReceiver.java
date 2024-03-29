package com.unicom.waslak_client.reciever;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.unicom.waslak_client.utils.NetworkUtil;

import javax.inject.Inject;

public class NetworkReceiver extends BroadcastReceiver {
    NetworkUtil networkUtil;

    @Inject
    public NetworkReceiver(NetworkUtil networkUtil) {
        this.networkUtil = networkUtil;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast. throw new UnsupportedOperationException("Not yet implemented");
        String status = networkUtil.getConnectivityStatusString();
        if (status != null) {
            Toast.makeText(context, status, Toast.LENGTH_LONG).show();
        }

    }
}
