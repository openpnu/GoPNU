package org.openpnu.gopnu.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.widget.Toast;

public class LocationReceiver extends BroadcastReceiver {

    public LocationReceiver() {

    }

    @Override
    public void onReceive(Context context, Intent intent) {

       Toast.makeText(context,"근접!",Toast.LENGTH_LONG).show();

        if (intent != null) {
            String content=intent.getStringExtra("content");

            Toast.makeText(context,content,Toast.LENGTH_LONG).show();
            Toast.makeText(context,"근접ㅜㅜ!",Toast.LENGTH_LONG).show();

        }

    }

}
