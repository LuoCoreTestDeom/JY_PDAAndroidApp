package rcominfo.com.ejejyxt.activities;

import android.app.usage.UsageEvents;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.rcominfo.tmjy.R;

import java.util.regex.Pattern;

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


public class RKSJActivity extends BaseActivity {
    private ImageView btn_back;
    private String kuwei_num;
    private EditText ed_rksj_kuwei;
    private CheckBox cb_fix;
    private EditText ed_rksj_kuaijian;
    private String data;
    private TextView num;
    private boolean check_box;
    int num1 = 0;
    private String result;
    private RKSJActivity mContext;
    private String http;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_rksj);
        initView();
        if(cb_fix.isChecked())
        {
            setTextFocus(ed_rksj_kuaijian);
        }
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
                String message = intent.getStringExtra(InfoCode.SCN_CUST_EX_SCODE);
                if (ed_rksj_kuwei.hasFocus()) {
                    Log.e("执行广播", "执行");
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
        // TODO Auto-generated method stub

        cb_fix.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
                // TODO Auto-generated method stub
                check_box = arg1;
                if (arg1) {
                    ed_rksj_kuwei.setEnabled(!arg1);
                    setTextFocus(ed_rksj_kuaijian);
                }
                else {
                    ed_rksj_kuwei.setEnabled(!arg1);
                    setTextFocus(ed_rksj_kuwei);
                    ed_rksj_kuwei.setText("");
                }

            }
        });
        ed_rksj_kuwei.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView arg0, int arg1, KeyEvent arg2) {

                // TODO Auto-generated method stub
                if (arg1 == EditorInfo.IME_ACTION_UNSPECIFIED && arg2.getAction() == KeyEvent.ACTION_DOWN&&arg2.getKeyCode()==KeyEvent.KEYCODE_ENTER) {
                    kuwei_num = ed_rksj_kuwei.getText().toString();
                    if (kuwei_num.equals("")) {
                        MediaPlayer.getInstance(mContext).error();
                        VibratorUtil.Vibrate(mContext, 1000);
                        setTextFocus(ed_rksj_kuwei);
                        Toast.makeText(RKSJActivity.this, Langutil.langchange(RKSJActivity.this, R.string.kuiwei_notnull), Toast.LENGTH_SHORT).show();
                    } else {
                        if (kuwei_num.length() != 0) {
                            MediaPlayer.getInstance(mContext).ok();
                            VibratorUtil.Vibrate(mContext, 500);
                            setTextFocus(ed_rksj_kuaijian);
                        } else {
                            MediaPlayer.getInstance(mContext).error();
                            VibratorUtil.Vibrate(mContext, 1000);
                            ed_rksj_kuwei.setText("");
                            setTextFocus(ed_rksj_kuwei);
                        }
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
                Log.e("key", arg2.toString());
                Log.e("key", arg1 + "");
                if (arg1 == EditorInfo.IME_ACTION_UNSPECIFIED && arg2.getKeyCode() == KeyEvent.KEYCODE_ENTER && arg2.getAction()==KeyEvent.ACTION_DOWN) {
                    ed_rksj_kuaijian.setEnabled(false);
                    ed_rksj_kuwei.setEnabled(false);

                    kuwei_num = ed_rksj_kuwei.getText().toString();
                    billnum = ed_rksj_kuaijian.getText().toString();

                    if(billnum.matches("^[a-zA-Z]{1}[0-9]{2}$")||billnum.matches("^[0-9]{4}$"))
                    {
                        ToastUtil.Show(mContext, "本次为库位");
                        ed_rksj_kuwei.setText(billnum);
                        ed_rksj_kuaijian.setText("");
                        ed_rksj_kuaijian.setEnabled(true);
                        MediaPlayer.getInstance(mContext).ok();
                        return true;
                    }

                    if (billnum.equals("")) {
                        ed_rksj_kuaijian.setEnabled(true);
                        ToastUtil.Show(mContext, "运单号不能为空");
                    } else if (kuwei_num.equals("")) {
                        ed_rksj_kuaijian.setEnabled(true);
                        ed_rksj_kuaijian.clearFocus();
                        ed_rksj_kuaijian.setFocusable(false);
                        ToastUtil.Show(mContext, "库位号不能为空");
                        if(!cb_fix.isChecked())
                        {
                            ed_rksj_kuwei.setEnabled(true);
                            ed_rksj_kuwei.requestFocus();//让editText2获取焦点
                            ed_rksj_kuwei.setSelection(ed_rksj_kuwei.getText().length());
                            ed_rksj_kuwei.setFocusableInTouchMode(true);
                        }

                    } else {
                        AsyncHttpPost.post(mContext, WebAPI.RKSJPD, JsonCreate.RKSJ_json(kuwei_num, billnum, "0", ShareUtil.getInstance(mContext).getNickName(),
                                ShareUtil.getInstance(mContext).getWaveHouse()), new AsyncHttpPost.Postinterface() {
                            @Override
                            public void onSuccess(PostTypeMsg mBean, AsyncHttpClient Client) {
                                ToastUtil.Show(mContext, "上架成功");
                                MediaPlayer.getInstance(mContext).ok();
                                VibratorUtil.Vibrate(mContext, 500);
                                ed_rksj_kuaijian.setEnabled(true);
                                ed_rksj_kuwei.setEnabled(true);
                                num1 = num1 + 1;
                                num.setText("" + num1);
                                if (check_box) {
                                    ed_rksj_kuaijian.setText("");
                                    setTextFocus(ed_rksj_kuaijian);
                                } else {
                                    ed_rksj_kuwei.setText("");
                                    ed_rksj_kuaijian.setText("");
                                    setTextFocus(ed_rksj_kuwei);
                                }
                            }

                            @Override
                            public void onFailure(String Msg) {
                                ToastUtil.Show(mContext, Msg);
                                MediaPlayer.getInstance(mContext).error();
                                VibratorUtil.Vibrate(mContext, 1000);
                                ed_rksj_kuaijian.setEnabled(true);
                                ed_rksj_kuwei.setEnabled(!cb_fix.isChecked());
                                ed_rksj_kuaijian.setText("");


                            }
                        });
                    }
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
    }

    private void initView() {
        // TODO Auto-generated method stub
        btn_back = (ImageView) findViewById(R.id.btn_back);
        TextView tv_info_menu = (TextView) findViewById(R.id.tv_info_menu);
        ed_rksj_kuwei = (EditText) findViewById(R.id.ed_rksj_kuwei);
        cb_fix = (CheckBox) findViewById(R.id.cb_fix);
        ed_rksj_kuaijian = (EditText) findViewById(R.id.ed_rksj_kuaijian);
        num = (TextView) findViewById(R.id.num);
        String nickname = ShareUtil.getInstance(mContext).getNickName();
        String wavehouse = ShareUtil.getInstance(mContext).getWaveHouse();
        tv_info_menu.setText(Langutil.langchange(RKSJActivity.this, R.string.user) + nickname + ","
                + Langutil.langchange(RKSJActivity.this, R.string.warehouse) + wavehouse);
    }


    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        Log.e("key", event + "");
        if (event.getKeyCode() == 23 && event.getAction() == KeyEvent.ACTION_DOWN) {
            if (ed_rksj_kuwei.hasFocus()) {
                ed_rksj_kuwei.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_ENTER));
                return false;
            } else if (ed_rksj_kuaijian.hasFocus()) {
                ed_rksj_kuaijian.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_ENTER));
                return false;
            }
            return false;
        } else if (event.getKeyCode() == 23 && event.getAction() == KeyEvent.ACTION_UP) {
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
