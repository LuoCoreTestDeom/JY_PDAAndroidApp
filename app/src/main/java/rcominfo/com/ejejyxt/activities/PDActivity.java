package rcominfo.com.ejejyxt.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.rcominfo.tmjy.R;

import rcominfo.com.ejejyxt.Bean.GetBean.PostTypeMsg;
import rcominfo.com.ejejyxt.Utils.AsyncHttpPost;
import rcominfo.com.ejejyxt.Utils.InfoCode;
import rcominfo.com.ejejyxt.Utils.JsonCreate;
import rcominfo.com.ejejyxt.Utils.Langutil;
import rcominfo.com.ejejyxt.Utils.MediaPlayer;
import rcominfo.com.ejejyxt.Utils.ShareUtil;
import rcominfo.com.ejejyxt.Utils.ToastUtil;
import rcominfo.com.ejejyxt.Utils.VibratorUtil;
import rcominfo.com.ejejyxt.Utils.WebAPI;


public class PDActivity extends BaseActivity {
    private ImageView btn_back;
    private TextView tv_info_menu;
    private String kuwei_num;
    private EditText ed_rksj_kuwei;
    private CheckBox cb_fix;
    private EditText ed_rksj_kuaijian;

    private boolean check_box;

    private String result;
    private PDActivity mContext;
    private String http;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_pd);
        IntentFilter intentFilter = new IntentFilter(InfoCode.SCN_CUST_ACTION_SCODE);
        registerReceiver(mSamDataReceiver, intentFilter);
        mContext = this;
        initView();
        OnClick();
        OnEditorClick();
    }

    private BroadcastReceiver mSamDataReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(InfoCode.SCN_CUST_ACTION_SCODE)) {
                String message = intent.getStringExtra(InfoCode.SCN_CUST_EX_SCODE);
                if (ed_rksj_kuwei.hasFocus()) {
                    ed_rksj_kuwei.setText(message);
                    ed_rksj_kuwei.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_ENTER));
                    ed_rksj_kuwei.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_ENTER));
                } else if (ed_rksj_kuaijian.hasFocus()) {
                    ed_rksj_kuaijian.setText(message);
                    ed_rksj_kuaijian.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_ENTER));
                    ed_rksj_kuaijian.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_ENTER));
                }
            }
        }
    };

    private void OnEditorClick() {
        cb_fix.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
                // TODO Auto-generated method stub
                check_box = arg1;
                if (arg1) {

                } else {
                    ed_rksj_kuwei.setText("");
                    ed_rksj_kuaijian.setText("");
                    setTextFocus(ed_rksj_kuwei);
                }
            }
        });

        ed_rksj_kuwei.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView arg0, int arg1, KeyEvent arg2) {

                // TODO Auto-generated method stub
                if (arg1 == 0 && arg2.getAction() == KeyEvent.ACTION_DOWN) {
                    kuwei_num = ed_rksj_kuwei.getText().toString();
                    if (kuwei_num.equals("")) {
                        MediaPlayer.getInstance(mContext).error();
                        VibratorUtil.Vibrate(mContext,1000);
                       setTextFocus(ed_rksj_kuwei);
                        Toast.makeText(PDActivity.this, Langutil.langchange(PDActivity.this, R.string.kuiwei_notnull), Toast.LENGTH_SHORT).show();
                    } else {
                        MediaPlayer.getInstance(mContext).ok();
                       setTextFocus(ed_rksj_kuaijian);
                    }

                }
                return true;
            }
        });

        ed_rksj_kuaijian.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            private String billnum;

            @Override
            public boolean onEditorAction(TextView arg0, int arg1, KeyEvent arg2) {
                // TODO Auto-generated method stub

                if (arg1 == 0 && arg2.getAction() == KeyEvent.ACTION_DOWN) {
                    ed_rksj_kuaijian.setEnabled(false);
                    ed_rksj_kuwei.setEnabled(false);
                    billnum = ed_rksj_kuaijian.getText().toString();
                    kuwei_num = ed_rksj_kuwei.getText().toString();

                    AsyncHttpPost.post(mContext, WebAPI.RKSJPD, JsonCreate.RKSJ_json(kuwei_num, billnum,
                            "1", ShareUtil.getInstance(mContext).getNickName(),
                            ShareUtil.getInstance(mContext).getWaveHouse()), new AsyncHttpPost.Postinterface() {
                        @Override
                        public void onSuccess(PostTypeMsg mBean, AsyncHttpClient Client) {
                            ToastUtil.Show(mContext, "单号对应正确");
                            MediaPlayer.getInstance(mContext).ok();
                            VibratorUtil.Vibrate(mContext,500);
                            ed_rksj_kuaijian.setEnabled(true);
                            ed_rksj_kuwei.setEnabled(true);
                            if(check_box ==true){
                                ed_rksj_kuaijian.setText("");
                               setTextFocus(ed_rksj_kuaijian);
                            }else{
                                ed_rksj_kuwei.setText("");
                                ed_rksj_kuaijian.setText("");
                                setTextFocus(ed_rksj_kuwei);
                            }
                        }

                        @Override
                        public void onFailure(String Msg) {
                            ToastUtil.Show(mContext,Msg);
                            ed_rksj_kuaijian.setText("");
                            MediaPlayer.getInstance(mContext).error();
                            VibratorUtil.Vibrate(mContext,1000);
                            ed_rksj_kuaijian.setEnabled(true);
                            ed_rksj_kuwei.setEnabled(true);
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
    }

    private void initView() {
        // TODO Auto-generated method stub
        btn_back = (ImageView) findViewById(R.id.btn_back);
        tv_info_menu = (TextView) findViewById(R.id.tv_info_menu);
        ed_rksj_kuwei = (EditText) findViewById(R.id.ed_rksj_kuwei);
        cb_fix = (CheckBox) findViewById(R.id.cb_fix);
        ed_rksj_kuaijian = (EditText) findViewById(R.id.ed_rksj_kuaijian);


        String nickname = ShareUtil.getInstance(mContext).getNickName();
        String wavehouse = ShareUtil.getInstance(mContext).getWaveHouse();
        tv_info_menu.setText(Langutil.langchange(PDActivity.this, R.string.user) + nickname + ","
                + Langutil.langchange(PDActivity.this, R.string.warehouse) + wavehouse);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        Log.e("key",event+"");
        if(event.getKeyCode()==23&&event.getAction()==KeyEvent.ACTION_DOWN){
            if(ed_rksj_kuaijian.hasFocus()){
                ed_rksj_kuaijian.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_ENTER));
                return false;
            }else if(ed_rksj_kuwei.hasFocus()){
                ed_rksj_kuwei.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_ENTER));
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
