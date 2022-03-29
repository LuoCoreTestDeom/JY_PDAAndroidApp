package rcominfo.com.ejejyxt.Utils;

import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

import de.greenrobot.event.EventBus;
import rcominfo.com.ejejyxt.Bean.EventMsg;

/**
 * Created by 王璐阳 on 2017/2/13.
 */
public class NewServive extends Service {
    private String url_down;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        checkVerson();
        return super.onStartCommand(intent, flags, startId);
    }
    private void checkVerson() {
        //比对版本号进行更新



        AsyncHttpClient client = new AsyncHttpClient();

        client.post(ShareUtil.getInstance(this).getnewServerall(), null, new AsyncHttpResponseHandler() {
            public String name;

            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                try {
                    JSONObject jo = new JSONObject(new String(bytes));
                    JSONArray temp = jo.getJSONArray("EJEAndroidPDA");
                    for (int j = 0; j < temp.length(); j++) {
                        JSONObject jo1 = (JSONObject) temp.get(j);
                        name = jo1.getString("vosoin");
                        url_down = jo1.getString("geturl");
                        if (!name.equals(getVersionName())) {

                        } else {

                        }
                    }
                    System.out.println(name + url_down);
                } catch (JSONException e) {
                    EventBus.getDefault().post(new EventMsg(InfoCode.CODE_JSON_ERROR, null));
                }

            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                EventBus.getDefault().post(new EventMsg(InfoCode.CODE_NET_ERROR, null));
            }
        });
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

    protected void download() {

        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {

            String target = Environment.getExternalStorageDirectory()
                    + "/JYandroidpda.apk";
            HttpUtils utils = new HttpUtils();
            utils.download(url_down, target, new RequestCallBack<File>() {

                @Override
                public void onLoading(long total, long current,
                                      boolean isUploading) {
                    super.onLoading(total, current, isUploading);
                    System.out.println("下载进度:" + current + "/" + total);

                }

                @Override
                public void onSuccess(ResponseInfo<File> arg0) {

                    System.out.println("下载完成");
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.addCategory(Intent.CATEGORY_DEFAULT);
                    intent.setDataAndType(Uri.fromFile(arg0.result),
                            "application/vnd.android.package-archive");

                }

                @Override
                public void onFailure(com.lidroid.xutils.exception.HttpException arg0, String arg1) {

                    ToastUtil.Show(NewServive.this, "下载失败");

                }


            });
        } else {

            ToastUtil.Show(this, "正在安装");

        }
    }
}
