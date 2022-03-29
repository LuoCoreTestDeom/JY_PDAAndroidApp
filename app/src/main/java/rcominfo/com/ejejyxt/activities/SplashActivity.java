package rcominfo.com.ejejyxt.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.EditText;
import android.widget.TextView;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.rcominfo.tmjy.R;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

import de.greenrobot.event.EventBus;
import rcominfo.com.ejejyxt.Bean.EventMsg;
import rcominfo.com.ejejyxt.Utils.InfoCode;
import rcominfo.com.ejejyxt.Utils.ShareUtil;
import rcominfo.com.ejejyxt.Utils.ToastUtil;
import rcominfo.com.ejejyxt.Utils.VibratorUtil;


public class SplashActivity extends BaseActivity {

    private SplashActivity mContext;
    private TextView tv_version_code;
    private TextView tv_progress;
    private EditText ed_enter_sp;
    private EditText ed_port_sp;
    private ShareUtil sha;
    private int name;
    private String url_down;
    private ProgressDialog pDialog;

    Handler myHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        mContext = this;
        EventBus.getDefault().register(mContext);
        sha = ShareUtil.getInstance(mContext);

        intiView();
        checkVerson();
    }

    public void onEventMainThread(EventMsg msg) {
        switch (msg.getFlag()) {
            case InfoCode.CODE_UPDATE_DIALOG:
                download();
                break;
            case InfoCode.CODE_JSON_ERROR:
                enterHome();
                ToastUtil.Show(mContext,"获取数据格式有误");
                break;
            case InfoCode.CODE_NET_ERROR:
                enterHome();
                ToastUtil.Show(mContext,"网络错误或服务器IP有误");
                break;
            case InfoCode.CODE_ENTER_HOME:
                enterHome();
                ToastUtil.Show(mContext,"无新版本");

        }
    }

    private void intiView() {
        tv_version_code = (TextView) findViewById(R.id.tv_version_code);
        tv_progress = (TextView) findViewById(R.id.tv_progress);

        setdata();
    }

    private void setdata() {
        tv_version_code.setText(getVersionName());
        Log.e("versionname", getVersionName());

    }



    private void checkVerson() {
        //比对版本号进行更新
        Log.e("check","1");
        AsyncHttpClient client = new AsyncHttpClient();

        client.post(ShareUtil.getInstance(mContext).getnewServerall(), null, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                Log.e("check","2");
                Log.e("check",new String(bytes));

                try {
                    JSONObject jo = new JSONObject(new String(bytes));
                    JSONArray temp = jo.getJSONArray("tmjyAndroidPDA");
                    for (int j = 0; j < temp.length(); j++) {
                        JSONObject jo1 = (JSONObject) temp.get(j);
                        try {
                            name = Integer.parseInt(jo1.getString("vosoin"));
                        }catch (NumberFormatException e){
                            enterHome();
                            ToastUtil.Show(mContext,"获取版本号失败");
                        }

                        url_down = jo1.getString("geturl");
                        if (name>getVersionCode()) {
                            Log.e("check","3");
                            EventBus.getDefault().post(new EventMsg(InfoCode.CODE_UPDATE_DIALOG,
                                    null));
                        } else {
                            EventBus.getDefault().post(new EventMsg(InfoCode.CODE_ENTER_HOME, null));
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



    protected void download() {

        pDialog = new ProgressDialog(mContext);
        pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pDialog.setTitle("下载中");
        pDialog.show();

        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {


            String target = Environment.getExternalStorageDirectory()
                    + "/TMandroidpda.apk";
            HttpUtils utils = new HttpUtils();
            
            utils.download(url_down, target, new RequestCallBack<File>() {

                @Override
                public void onLoading(long total, long current,
                                      boolean isUploading) {
                    super.onLoading(total, current, isUploading);
                    System.out.println("下载进度:" + current + "/" + total);
                    pDialog.setProgress((int) (current*100/total));
                }

                @Override
                public void onSuccess(ResponseInfo<File> arg0) {
                    pDialog.dismiss();
                    System.out.println("下载完成");
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.addCategory(Intent.CATEGORY_DEFAULT);
                    intent.setDataAndType(Uri.fromFile(arg0.result),
                            "application/vnd.android.package-archive");
                    startActivityForResult(intent, 0);
                }

                @Override
                public void onFailure(com.lidroid.xutils.exception.HttpException arg0, String arg1) {
                    pDialog.dismiss();
                    ToastUtil.Show(mContext, "下载失败");
                    enterHome();
                }


            });
        } else {
            pDialog.dismiss();
            ToastUtil.Show(mContext, "正在安装");

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        enterHome();
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void enterHome() {
//        MediaPlayer.getInstance(mContext).welcome();

                Intent intent = new Intent(mContext, MainActivity.class);
                startActivity(intent);
                finish();
            }




    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0){
            VibratorUtil.Vibrate(mContext,2000);
            ToastUtil.Show(mContext,"不能退出");
        }
        return super.onKeyDown(keyCode, event);
    }

}

