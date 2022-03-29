package rcominfo.com.ejejyxt.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
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

import rcominfo.com.ejejyxt.Utils.ShareUtil;
import rcominfo.com.ejejyxt.Utils.ToastUtil;


public class SettingActivity extends BaseActivity {

    private Context mContext;
    private EditText ed_enter;
    private EditText ed_port;
    private Button save;
    private Button btn_wifi;
    private Button btn_getNewVersion;
    private Button btn_exit;
    private CommonListener commonListener;
    private int name;
    private String url_down;
    private ProgressDialog pDialog;
    private Button btn_server_select;
    private Button test;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        mContext = this;
        initView();
        Onclick();
    }

    private void initView() {
        btn_wifi = (Button)findViewById(R.id.btn_wifi);
        btn_getNewVersion = (Button)findViewById(R.id.btn_getNewVersion);
        btn_exit = (Button)findViewById(R.id.btn_exit);
        btn_server_select = (Button)findViewById(R.id.btn_server_select);
        test = (Button)findViewById(R.id.test);
        commonListener = new CommonListener();
        test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    private void Onclick(){
        btn_wifi.setOnClickListener(commonListener);
        btn_getNewVersion.setOnClickListener(commonListener);
        btn_exit.setOnClickListener(commonListener);
        btn_server_select.setOnClickListener(commonListener);
        test.setOnClickListener(commonListener);
    }

    class CommonListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.btn_wifi:
                    setWifi();
                    break;

                case R.id.btn_getNewVersion:
                    getnewVersion();
                    break;

                case R.id.btn_exit:
                    exit();
                    break;

                case R.id.btn_server_select:
                    startNewActivity(ServerActivity.class,R.anim.activity_fade_in,R.anim.activity_fade_out,false,null);
                    break;
                case R.id.test:
                    startNewActivity(TestActivity.class,R.anim.activity_fade_in,R.anim.activity_fade_out,false,null);
            }
        }
    }



    private void exit() {
        AlertDialog.Builder ab =new AlertDialog.Builder(mContext);
        ab.setTitle("输入管理员密码");
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_alert_dialog,null);
        final TextView tv_error = (TextView)v.findViewById(R.id.tv_error);
        final EditText ed_admin_pwd = (EditText)v.findViewById(R.id.ed_admin_pwd);
        ab.setView(v);
        ab.setNegativeButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String pwd = ed_admin_pwd.getText().toString();
                if(pwd.equals("123654")){
                    System.exit(0);

                }else {
                    ToastUtil.Show(mContext,"密码错误");
                }
            }
        });
        ab.setPositiveButton("取消",null);
        ab.create();
        ab.show();
    }

    private void getnewVersion() {
        AsyncHttpClient client = new AsyncHttpClient();

        client.post(ShareUtil.getInstance(mContext).getnewServerall(), null, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                try {

                    Log.e("json", new String(bytes));
                    JSONObject jo = new JSONObject(new String(bytes));
                    JSONArray temp = jo.getJSONArray("tmjyAndroidPDA");
                    for (int j = 0; j < temp.length(); j++) {
                        JSONObject jo1 = (JSONObject) temp.get(j);
                        try {
                            name = Integer.parseInt(jo1.getString("vosoin"));
                        }catch (NumberFormatException e){
                            ToastUtil.Show(mContext,"获取版本号失败");
                        }
                        url_down = jo1.getString("geturl");
                        if (name>getVersionCode()) {
                            download(url_down);
                        } else {
                            ToastUtil.Show(mContext, "无新版本");
                        }
                    }
                    System.out.println(name + url_down);
                }catch(JSONException e){
                    ToastUtil.Show(mContext, "数据解析失败");
                }

            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {

                ToastUtil.Show(mContext,"网络错误或服务器IP有误");


            }
        });
    }




    private void setWifi() {
        ToastUtil.Show(mContext,"123");
        Intent in = new Intent(Settings.ACTION_WIFI_SETTINGS);
        startActivity(in);
    }


    protected void download(String url_down) {

        pDialog = new ProgressDialog(mContext);
        pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pDialog.setTitle("下载中");
        pDialog.show();
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
                    ToastUtil.Show(mContext, "下载失败");
                    pDialog.dismiss();
                }


            });
        } else {
            ToastUtil.Show(mContext, "正在安装");
            pDialog.dismiss();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        ToastUtil.Show(mContext,"更新完成");
        super.onActivityResult(requestCode, resultCode, data);
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
        startNewActivity(MainActivity.class,R.anim.activity_slide_left_in,R.anim.activity_slide_left_out,true,null);
        }
        return false;
    }
}
