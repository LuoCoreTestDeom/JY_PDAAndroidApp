package rcominfo.com.ejejyxt.activities;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.rcominfo.tmjy.R;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import rcominfo.com.ejejyxt.Bean.GetBean.LoginGetBean;
import rcominfo.com.ejejyxt.Bean.GetBean.PostTypeMsg;
import rcominfo.com.ejejyxt.Bean.PostBean.ConfigureInfo;
import rcominfo.com.ejejyxt.Utils.AsyncHttpPost;
import rcominfo.com.ejejyxt.Utils.DBHelperUtil;
import rcominfo.com.ejejyxt.Utils.JsonCreate;
import rcominfo.com.ejejyxt.Utils.LuoAsyncHttpGet;
import rcominfo.com.ejejyxt.Utils.MediaPlayer;
import rcominfo.com.ejejyxt.Utils.ShareUtil;
import rcominfo.com.ejejyxt.Utils.ToastUtil;
import rcominfo.com.ejejyxt.Utils.VibratorUtil;
import rcominfo.com.ejejyxt.Utils.WebAPI;

import static android.content.ContentValues.TAG;


public class MainActivity extends BaseActivity {
    private EditText ed_login_num;
    private EditText ed_login_pass;
    private Button btn_login;
    private SQLiteDatabase dbwriter;
    private Button btn_set;
    private DBHelperUtil db;
    private MainActivity mContext;
    private ProgressDialog pDialog;
    private Gson gson;
    private LoginGetBean loginGetBean;
    private ConfigureInfo configureInfo;
    private ImageView LogoID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        mContext = this;

        initView();
        ShareUtil.getInstance(mContext).setCode("0");
        gson = new Gson();
        db = new DBHelperUtil(mContext);
        dbwriter = db.getWritableDatabase();
        pDialog = new ProgressDialog(this);
        pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pDialog.setIndeterminate(true);
//        loadLogo();

        Onclick();


    }

    private void Onclick() {
        OnSetClick();
        DoLoginClick();
    }

    private void DoLoginClick() {
        ed_login_num.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == 0 && keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                    if (!ed_login_num.getText().toString().equals("")) {
                        ed_login_pass.setFocusable(true);
                        ed_login_pass.setFocusableInTouchMode(true);
                        ed_login_pass.requestFocus();
                        ed_login_pass.findFocus();
                        return false;
                    } else {
                        ToastUtil.Show(mContext, "输入有误");
                        MediaPlayer.getInstance(mContext).error();
                        return false;
                    }

                } else if (i == 0 && keyEvent.getAction() == KeyEvent.ACTION_UP) {
                    return false;
                }

                return true;
            }
        });

        ed_login_pass.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == 0 && keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                    dologin();
                    return false;
                } else if (i == 0 && keyEvent.getAction() == KeyEvent.ACTION_UP) {
                    return false;
                }
                return true;
            }
        });

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dologin();

            }
        });
    }


    private void OnSetClick() {
        btn_set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startNewActivity(SettingActivity.class, R.anim.activity_fade_in, R.anim.activity_fade_out, true, null);
            }
        });

    }


    private void initView() {
        TextView tv_version = (TextView) findViewById(R.id.tv_version);
        ed_login_num = (EditText) findViewById(R.id.ed_login_num);
        ed_login_pass = (EditText) findViewById(R.id.ed_login_pass);
        btn_login = (Button) findViewById(R.id.btn_login);
        btn_set = (Button) findViewById(R.id.btn_setting);
        tv_version.setText("版本号：" + getVersionName());
        db = new DBHelperUtil(MainActivity.this);
        dbwriter = db.getWritableDatabase();
        LogoID = (ImageView) findViewById(R.id.logoID);
        new Thread(runnable).start();

    }

    private void dologin() {
        pDialog.setMessage("正在登陆...");
        pDialog.show();
        final String user = ed_login_num.getText().toString();
        final String pass = ed_login_pass.getText().toString();


        int random = new Random().nextInt(999999999);
        final String code = getTime() + random;

        String json = JsonCreate.Json_login(user, pass, code);
        AsyncHttpPost.post(mContext, WebAPI.LOGIN, json, new AsyncHttpPost.Postinterface() {
            @Override
            public void onSuccess(PostTypeMsg mBean, AsyncHttpClient Client) {
                pDialog.dismiss();
                ShareUtil.getInstance(mContext).setCode(code);
                dbwriter.delete(DBHelperUtil.TABLE_NAME, null, null);
                dbwriter.delete(DBHelperUtil.TABLE_NAME1, null, null);
                dbwriter.delete(DBHelperUtil.TABLE_NAME2, null, null);
                MediaPlayer.getInstance(mContext).ok();
                loginGetBean = gson.fromJson(mBean.ReturnJson, LoginGetBean.class);
                ShareUtil.getInstance(mContext).setNickName(loginGetBean.nickname);
                ShareUtil.getInstance(mContext).setWaveHouse(loginGetBean.house_name);
                ShareUtil.getInstance(mContext).setWaveHouseID(loginGetBean.houseid);
                ShareUtil.getInstance(mContext).setWaveHouseType(loginGetBean.house_type);
                ShareUtil.getInstance(mContext).setPermission(loginGetBean.permission);
                ShareUtil.getInstance(mContext).setareaCode(loginGetBean.areaCode);
                ShareUtil.getInstance(mContext).Permission(loginGetBean.PDA_role);
                startNewActivity(MenuActivity.class, R.anim.activity_fade_in, R.anim.activity_fade_out, false, null);
            }

            @Override
            public void onFailure(String Msg) {
                pDialog.dismiss();
                ToastUtil.Show(mContext, Msg);
                MediaPlayer.getInstance(mContext).error();
            }
        });
        AsyncHttpPost.post(mContext, "ConfigureInfo","{}", new AsyncHttpPost.Postinterface() {
            @Override
            public void onSuccess(PostTypeMsg mBean, AsyncHttpClient Client) {
                configureInfo = gson.fromJson(mBean.ReturnJson, ConfigureInfo.class);
                ShareUtil.getInstance(mContext).setTrackingUrl(configureInfo.TrackingUrl);
                ShareUtil.getInstance(mContext).setSingleLower(configureInfo.SingleLower);
                ToastUtil.Show(mContext,  ShareUtil.getInstance(mContext).getSingleLower()+"配置"+ShareUtil.getInstance(mContext).getTrackingUrl());
            }

            @Override
            public void onFailure(String Msg) {
                ToastUtil.Show(mContext, Msg);
            }
        });
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            VibratorUtil.Vibrate(mContext, 2000);
        }
        return true;
    }

    private String getTime() {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        Date curDate = new Date();
        return format.format(curDate);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        Log.e("key", event + "");
        if (event.getKeyCode() == 23 && event.getAction() == KeyEvent.ACTION_DOWN) {
            Log.e("123", "123132321");
            if (ed_login_num.hasFocus()) {
                ed_login_num.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_ENTER));
                return false;

            } else if (ed_login_pass.hasFocus()) {
                ed_login_pass.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_ENTER));
                return false;
            }
            return false;
        } else if (event.getKeyCode() == 23 && event.getAction() == KeyEvent.ACTION_UP) {
            return false;
        }
        return super.dispatchKeyEvent(event);
    }

    //加载网络图片
    public Bitmap returnBitMap() {
        URL myFileUrl = null;
        Bitmap bitmap = null;
        try
        {
            myFileUrl = new URL("http://www.rcominfo.com/File/00000/Logo/2017040516/20170405165534_7309.png");
        }
        catch (MalformedURLException e) {
            e.printStackTrace();
        }
        try
        {
            HttpURLConnection conn = (HttpURLConnection) myFileUrl.openConnection();
            conn.setDoInput(true);
            conn.connect();
            InputStream is = conn.getInputStream();
            bitmap = BitmapFactory.decodeStream(is);
            is.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return bitmap;
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            // TODO: http request.
            Message msg = new Message();
            msg.obj=returnBitMap();
            handler.sendMessage(msg);
        }
    };
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            LogoID.setImageBitmap((Bitmap)msg.obj);
        }
    };
}
