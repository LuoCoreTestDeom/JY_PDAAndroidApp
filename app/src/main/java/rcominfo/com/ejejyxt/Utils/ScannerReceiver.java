package rcominfo.com.ejejyxt.Utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by dell on 2016/12/8.
 */
public class ScannerReceiver {

    public static BroadcastReceiver mSamDataReceiver;

    public static void getresult(final doSome doSome){
        mSamDataReceiver = new BroadcastReceiver() {
            public String message;

            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(InfoCode.SCN_CUST_ACTION_SCODE)) {
                    message = intent.getStringExtra(InfoCode.SCN_CUST_EX_SCODE);
                    Log.e("message",message);
                   doSome.dosomething(message);

                }
            }
        };
    }

    public interface doSome{
         void dosomething(String message);


    }
}
