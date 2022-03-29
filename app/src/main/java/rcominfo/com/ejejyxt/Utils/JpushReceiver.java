package rcominfo.com.ejejyxt.Utils;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;

import cn.jpush.android.api.JPushInterface;
import rcominfo.com.ejejyxt.activities.MainActivity;

/**
 * Created by 王璐阳 on 2017/3/13.
 */
public class JpushReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(final Context context, Intent intent) {
        if(intent.getAction().equals(JPushInterface.ACTION_MESSAGE_RECEIVED)){
            Bundle b = intent.getExtras();
            String title = b.getString(JPushInterface.EXTRA_TITLE);
            String message = b.getString(JPushInterface.EXTRA_MESSAGE);
            String code = ShareUtil.getInstance(context).getCode();
            Log.e("code",code);
            if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {

            } else if(title.equals("用户登录")&&!message.equals(code)&&!code.equals("0")){
                Log.e("code",code);
                JPushInterface.setAliasAndTags(context,"",null,null);
                ShareUtil.getInstance(context).setCode("0");
                AlertDialog.Builder ab = new AlertDialog.Builder(context);
                ab.setTitle("警告");
                ab.setMessage("你的账号在另外一台设备上登录");
                ab.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        Intent intent = new Intent(context, SingleLoginService.class);
                        context.stopService(intent);
                        Intent in = new Intent(context,MainActivity.class);
                        in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(in);
                    }
                });

                AlertDialog dialog = ab.create();
                dialog.getWindow()
                        .setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
                dialog.show();
            }else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {


            }else if(intent.getAction().equals(JPushInterface.ACTION_NOTIFICATION_OPENED)){
                Log.e("ACTION_NOTIFICATION_OPENED", "[MyReceiver] 用户点击打开了通知");
                openNotification(context,null);
            }
        }
    }

    private void openNotification(Context context, Bundle bundle){
        String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
        String myValue = "";
        try {

        } catch (Exception e) {
            return;
        }

    }
}
