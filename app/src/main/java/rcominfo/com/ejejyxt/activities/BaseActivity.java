package rcominfo.com.ejejyxt.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Window;
import android.widget.EditText;


/**
 * Created by wly on 2016/11/25.
 */
    public class BaseActivity extends Activity {
        private Context mContext;
        private ProgressDialog pDialog;
        private static String message;
    private BroadcastReceiver mSamDataReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        mContext = this;
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("加载中...请稍后...");
        pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pDialog.setIndeterminate(true);
        pDialog.setCancelable(true);

    }

    public void setTextFocus(EditText ed){
        ed.setFocusable(true);
        ed.setFocusableInTouchMode(true);
        ed.requestFocus();
        ed.findFocus();
    }

    public String getVersionName() {
        //androidmanifest中获取版本名称
        PackageManager packageManager = getPackageManager();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    getPackageName(), 0);

            int versionCode = packageInfo.versionCode;
            String versionName = packageInfo.versionName;

            System.out.println("versionName=" + versionName + ";versionCode="
                    + versionCode);

            return versionName;
        } catch (PackageManager.NameNotFoundException e) {

            e.printStackTrace();
        }

        return "";
    }

    public int getVersionCode() {
        //androidmanifest中获取版本名称
        PackageManager packageManager = getPackageManager();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    getPackageName(), 0);

            int versionCode = packageInfo.versionCode;
            String versionName = packageInfo.versionName;

            System.out.println("versionName=" + versionName + ";versionCode="
                    + versionCode);

            return versionCode;
        } catch (PackageManager.NameNotFoundException e) {

            e.printStackTrace();
        }

        return 0;
    }

    public final void startNewActivity(Class<? extends Activity> target,
                                       int enterAnim, int exitAnim, boolean isFinish, Bundle mBundle) {
        Intent mIntent = new Intent(this, target);
        if (mBundle != null) {
            mIntent.putExtras(mBundle);
        }
        startActivity(mIntent);
        overridePendingTransition(enterAnim, exitAnim);
        if (isFinish) {
            finish();
        }
    }



}
