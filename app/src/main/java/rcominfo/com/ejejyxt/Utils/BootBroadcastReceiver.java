package rcominfo.com.ejejyxt.Utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import rcominfo.com.ejejyxt.activities.SplashActivity;


/**
 * Created by dell on 2016/12/8.
 */
public class BootBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {     // boot
            Intent intent2 = new Intent(context, SplashActivity.class);
            intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent2);

        }
    }
}
