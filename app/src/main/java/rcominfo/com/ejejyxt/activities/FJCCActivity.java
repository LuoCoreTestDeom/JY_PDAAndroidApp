package rcominfo.com.ejejyxt.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.rcominfo.tmjy.R;

import rcominfo.com.ejejyxt.Bean.GetBean.PostTypeMsg;
import rcominfo.com.ejejyxt.Utils.AsyncHttpPost;
import rcominfo.com.ejejyxt.Utils.DBHelperUtil;
import rcominfo.com.ejejyxt.Utils.InfoCode;
import rcominfo.com.ejejyxt.Utils.JsonCreate;
import rcominfo.com.ejejyxt.Utils.Langutil;
import rcominfo.com.ejejyxt.Utils.MediaPlayer;
import rcominfo.com.ejejyxt.Utils.MySpinnerAdapter;
import rcominfo.com.ejejyxt.Utils.ShareUtil;
import rcominfo.com.ejejyxt.Utils.ToastUtil;
import rcominfo.com.ejejyxt.Utils.VibratorUtil;
import rcominfo.com.ejejyxt.Utils.WebAPI;


public class FJCCActivity extends BaseActivity {
    private ImageView btn_back;
    private TextView tv_info_menu;
    private Spinner sp_fjcc;
    private String sp_str;
    private EditText ed_fjcc_package;
    private EditText ed_fjcc_num;
    private TextView num_scanner;
    private String result;
    private String package_code;
    private FJCCActivity mContext;
    private String nickname;
    private String wavehouse;
    private CheckBox cb_fjcc;
    private String http;
    private boolean b;
    int num = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_fjcc);
        mContext = this;
        initView();
        IntentFilter intentFilter = new IntentFilter(InfoCode.SCN_CUST_ACTION_SCODE);
        registerReceiver(mSamDataReceiver, intentFilter);
        cbOnclick();
        OnEditorClick();
        OnClick();
    }

    private BroadcastReceiver mSamDataReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(InfoCode.SCN_CUST_ACTION_SCODE)) {
                String message = intent.getStringExtra(InfoCode.SCN_CUST_EX_SCODE);
                if (ed_fjcc_package.hasFocus()) {
                ed_fjcc_package.setText(message);
                ed_fjcc_package.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_ENTER));
                ed_fjcc_package.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_ENTER));
            } else if (ed_fjcc_num.hasFocus()) {
                ed_fjcc_num.setText(message);
                ed_fjcc_num.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_ENTER));
                ed_fjcc_num.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_ENTER));
            }


        }
        }
    };

    private void cbOnclick() {

        cb_fjcc.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
                // TODO Auto-generated method stub
                b = arg1;
                if (arg1) {

                } else {
                   setTextFocus(ed_fjcc_package);
                    ed_fjcc_package.setText("");
                }
            }
        });
    }

    private void OnEditorClick() {


        // TODO Auto-generated method stub
        ed_fjcc_package.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView arg0, int arg1, KeyEvent arg2) {
                Log.e("key", arg2.toString());
                Log.e("key", arg1 + "");
                // TODO Auto-generated method stub
                if (arg1 == 0 && arg2.getAction() == KeyEvent.ACTION_DOWN) {
                    package_code = ed_fjcc_package.getText().toString();
                    if (package_code.equals("")) {
                        MediaPlayer.getInstance(mContext).error();
                        VibratorUtil.Vibrate(mContext,1000);
                        ToastUtil.Show(mContext, "请输入包号");
                    } else {
                        MediaPlayer.getInstance(mContext).ok();
                        VibratorUtil.Vibrate(mContext,500);
                        setTextFocus(ed_fjcc_num);
                    }
                }
                return true;
            }
        });


        ed_fjcc_num.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView arg0, int arg1, KeyEvent arg2) {
                Log.e("key", arg2.toString());
                Log.e("key", arg1 + "");
                // TODO Auto-generated method stub
                if (arg1 == 0 && arg2.getAction() == KeyEvent.ACTION_DOWN) {
                    ed_fjcc_num.setEnabled(false);
                    ed_fjcc_package.setEnabled(false);
                    final String code = ed_fjcc_num.getText().toString();
                    package_code = ed_fjcc_package.getText().toString();
                    if (ed_fjcc_num.equals("")) {
                        MediaPlayer.getInstance(mContext).error();
                        VibratorUtil.Vibrate(mContext,1000);
                        ToastUtil.Show(mContext, "运单号不能为空");
                        ed_fjcc_num.setEnabled(true);
                        ed_fjcc_package.setEnabled(true);
                    } else if (ed_fjcc_package.equals("")) {
                        MediaPlayer.getInstance(mContext).error();
                        VibratorUtil.Vibrate(mContext,1000);
                        ToastUtil.Show(mContext, "袋号不能为空");
                        ed_fjcc_num.setEnabled(true);
                        ed_fjcc_package.setEnabled(true);
                    }

                    AsyncHttpPost.post(mContext, WebAPI.FJCC, JsonCreate.FJCC(code, wavehouse, nickname, sp_str, package_code), new AsyncHttpPost.Postinterface() {
                        @Override
                        public void onSuccess(PostTypeMsg mBean, AsyncHttpClient Client) {
                            ed_fjcc_num.setEnabled(true);
                            ed_fjcc_package.setEnabled(true);
                            MediaPlayer.getInstance(mContext).ok();
                            VibratorUtil.Vibrate(mContext,500);
                            ToastUtil.Show(mContext, "扫描成功");
                            num = num + 1;
                            num_scanner.setText(num + "");
                            if (b) {
                                setTextFocus(ed_fjcc_num);
                            } else {
                                ed_fjcc_num.setText("");
                                setTextFocus(ed_fjcc_package);
                                ed_fjcc_package.setText("");
                            }
                        }

                        @Override
                        public void onFailure(String Msg) {
                            ed_fjcc_num.setText("");
                            ed_fjcc_num.setEnabled(true);
                            ed_fjcc_package.setEnabled(true);
                            MediaPlayer.getInstance(mContext).error();
                            VibratorUtil.Vibrate(mContext,1000);
                            ToastUtil.Show(mContext, Msg);
                        }
                    });
                }
                return true;
            }
        });
    }

    private void OnClick() {
        // TODO Auto-generated method stub
        btn_back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                finish();
            }
        });

        sp_fjcc.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                // TODO Auto-generated method stub
                DBHelperUtil db = new DBHelperUtil(FJCCActivity.this);
                SQLiteDatabase dbwriter = db.getWritableDatabase();
                Cursor c = dbwriter.query(DBHelperUtil.TABLE_NAME, null, null, null, null, null, null);
                c.moveToPosition(arg2);
                sp_str = c.getString(c.getColumnIndex(DBHelperUtil.WAVEHOUSE_NAME));
                ToastUtil.Show(mContext, sp_str);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });
    }

    private void initView() {
        // TODO Auto-generated method stub
        btn_back = (ImageView) findViewById(R.id.btn_back);
        tv_info_menu = (TextView) findViewById(R.id.tv_info_menu);
        sp_fjcc = (Spinner) findViewById(R.id.sp_fjcc);
        ed_fjcc_package = (EditText) findViewById(R.id.ed_fjcc_package);
        ed_fjcc_num = (EditText) findViewById(R.id.ed_fjcc_num);
        num_scanner = (TextView) findViewById(R.id.num_scanner);
        cb_fjcc = (CheckBox) findViewById(R.id.cb_fjcc);
        nickname = ShareUtil.getInstance(mContext).getNickName();
        wavehouse = ShareUtil.getInstance(mContext).getWaveHouse();
        tv_info_menu.setText(Langutil.langchange(FJCCActivity.this, R.string.user) + nickname + ","
                + Langutil.langchange(FJCCActivity.this, R.string.warehouse) + wavehouse);
        DBHelperUtil db = new DBHelperUtil(FJCCActivity.this);
        SQLiteDatabase dbwriter = db.getWritableDatabase();
        Cursor cursor = dbwriter.query(DBHelperUtil.TABLE_NAME1, null, null, null, null, null, null);
        MySpinnerAdapter ada = new MySpinnerAdapter(FJCCActivity.this, cursor, DBHelperUtil.WAVEHOUSE_NAME);
        sp_fjcc.setAdapter(ada);

    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        Log.e("key",event+"");
        if(event.getKeyCode()==23&&event.getAction()==KeyEvent.ACTION_DOWN){
            if(ed_fjcc_package.hasFocus()){
                ed_fjcc_package.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_ENTER));
            }else if(ed_fjcc_num.hasFocus()){
                ed_fjcc_num.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_ENTER));
            }
            return false;
        }else if(event.getKeyCode()==23&&event.getAction()==KeyEvent.ACTION_UP){
            return false;
        }
        return super.dispatchKeyEvent(event);
    }
}
