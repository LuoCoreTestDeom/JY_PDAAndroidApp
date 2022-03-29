package rcominfo.com.ejejyxt.activities;

import android.app.AlertDialog;
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
import android.widget.AdapterView;
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


public class KDYJDActivity extends BaseActivity {
    private TextView tv_info_menu;
    private ImageView btn_back;
    private Spinner sp_kdyjd;
    private EditText ed_express_code;
    private TextView tv_kdyjd_num;
    private String result;
    private String billcode;
    private String str_sp;
    int num = 0;
    private KDYJDActivity mContext;
    private DBHelperUtil db;
    private SQLiteDatabase dbwriter;
    private Cursor cursor;
    private String nickname;
    private String wavehouse;
    private String http;
    private String message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kdyjd);
        mContext = this;
        initView();
        mContext = this;
        IntentFilter intentFilter = new IntentFilter(InfoCode.SCN_CUST_ACTION_SCODE);
        registerReceiver(mSamDataReceiver, intentFilter);

        OnClick();
        OnEditorClick();
    }

    private BroadcastReceiver mSamDataReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(InfoCode.SCN_CUST_ACTION_SCODE)) {
                message = intent.getStringExtra(InfoCode.SCN_CUST_EX_SCODE);
                if (ed_express_code.hasFocus()) {
                    Log.e("mSamDataReceiver",message);
                    ed_express_code.setText(message);
                    ed_express_code.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_ENTER));

                }
            }
        }
    };

    private void OnEditorClick() {
        // TODO Auto-generated method stub
        ed_express_code.setOnEditorActionListener(new TextView.OnEditorActionListener() {


            @Override
            public boolean onEditorAction(TextView arg0, int arg1, KeyEvent arg2) {
                // TODO Auto-generated method stub
                if (arg1 == 0 && arg2.getAction() == KeyEvent.ACTION_DOWN) {
                    billcode = ed_express_code.getText().toString();
                    ed_express_code.setEnabled(false);

                    AsyncHttpPost.post(mContext, WebAPI.KDJD, JsonCreate.KDJD(str_sp, billcode, wavehouse, nickname), new AsyncHttpPost.Postinterface() {
                        @Override
                        public void onSuccess(PostTypeMsg mBean, AsyncHttpClient Client) {
                            MediaPlayer.getInstance(mContext).ok();
                            VibratorUtil.Vibrate(mContext,500);
                            if(mBean.MsgText!=null&&!mBean.MsgText.equals("")){
                                AlertDialog.Builder ab=  new AlertDialog.Builder(mContext);
                                ab.setTitle("快递信息！");
                                ab.setMessage(mBean.getMsgText());
                                ab.create();
                                ab.show();
                            }
                            ed_express_code.setEnabled(true);
                            ed_express_code.setText("");
                            num = num + 1;
                            tv_kdyjd_num.setText("" + num);
                            ToastUtil.Show(mContext, billcode + "交单成功");
                        }

                        @Override
                        public void onFailure(String Msg) {
                            ed_express_code.setEnabled(true);
                            MediaPlayer.getInstance(mContext).error();
                            VibratorUtil.Vibrate(mContext,1000);
                            ed_express_code.setText("");
                            ToastUtil.Show(mContext,Msg);
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
                onBackPressed();
            }
        });

        sp_kdyjd.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                // TODO Auto-generated method stub
                cursor.moveToPosition(arg2);
                str_sp = cursor.getString(cursor.getColumnIndex(DBHelperUtil.WAVEHOUSE_NAME1));
                ToastUtil.Show(mContext, str_sp);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });
    }

    private void initView() {
        // TODO Auto-generated method stub
        tv_info_menu = (TextView) findViewById(R.id.tv_info_menu);
        btn_back = (ImageView) findViewById(R.id.btn_back);
        sp_kdyjd = (Spinner) findViewById(R.id.sp_kdyjd);
        ed_express_code = (EditText) findViewById(R.id.ed_kdyjd_num);
        tv_kdyjd_num = (TextView) findViewById(R.id.tv_kdyjd_num);
        db = new DBHelperUtil(KDYJDActivity.this);
        dbwriter = db.getWritableDatabase();
        cursor = dbwriter.query(DBHelperUtil.TABLE_NAME1, null, null, null, null, null, null);
        MySpinnerAdapter ada = new MySpinnerAdapter(KDYJDActivity.this, cursor, DBHelperUtil.WAVEHOUSE_NAME1);
        sp_kdyjd.setAdapter(ada);
        nickname = ShareUtil.getInstance(mContext).getNickName();
        wavehouse = ShareUtil.getInstance(mContext).getWaveHouse();
        tv_info_menu.setText(Langutil.langchange(KDYJDActivity.this, R.string.user) + nickname + ","
                + Langutil.langchange(KDYJDActivity.this, R.string.warehouse) + wavehouse);
    }
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        Log.e("key",event+"");
        if(event.getKeyCode()==23&&event.getAction()==KeyEvent.ACTION_DOWN){
            if(ed_express_code.hasFocus()){

                ed_express_code.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_ENTER));
                return false;
            }
            return false;

        }else if(event.getKeyCode()==23&&event.getAction()==KeyEvent.ACTION_UP){
                return false;
        }
        return super.dispatchKeyEvent(event);
    }
    @Override
    protected void onDestroy() {
        unregisterReceiver(mSamDataReceiver);
        super.onDestroy();
    }

}
