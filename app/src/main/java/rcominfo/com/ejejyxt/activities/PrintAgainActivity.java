package rcominfo.com.ejejyxt.activities;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.rcominfo.tmjy.R;

import rcominfo.com.ejejyxt.Bean.GetBean.PostTypeMsg;
import rcominfo.com.ejejyxt.Utils.AsyncHttpPost;
import rcominfo.com.ejejyxt.Utils.InfoCode;
import rcominfo.com.ejejyxt.Utils.JsonCreate;
import rcominfo.com.ejejyxt.Utils.MediaPlayer;
import rcominfo.com.ejejyxt.Utils.ShareUtil;
import rcominfo.com.ejejyxt.Utils.ToastUtil;
import rcominfo.com.ejejyxt.Utils.VibratorUtil;
import rcominfo.com.ejejyxt.Utils.WebAPI;

public class PrintAgainActivity extends BaseActivity {

    private EditText ed_print_num;
    private Button btn_print_again;
    private PrintAgainActivity mContext;
    private String code_print_again;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_print_again);
        IntentFilter intentFilter = new IntentFilter(InfoCode.SCN_CUST_ACTION_SCODE);
        registerReceiver(mSamDataReceiver, intentFilter);
        mContext = this;
        initView();
        editAction();
        Onclick();
    }

    private void Onclick() {
        btn_print_again.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                code_print_again = ed_print_num.getText().toString();
                alert(code_print_again);
            }
        });
    }

    private void editAction() {
        ed_print_num.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == 0 && keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                    code_print_again = ed_print_num.getText().toString();
                    alert(code_print_again);
                }
                return true;
            }
        });
    }

    private void alert(final String code_print_again){
        AlertDialog.Builder ab = new AlertDialog.Builder(mContext);
        ab.setTitle("确认打印");
        ab.setMessage("确认重新打印运单号："+code_print_again+"?");
        ab.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Print(code_print_again);
            }
        });
        ab.setNegativeButton("取消",null);
        ab.create();
        ab.show();
    }

    private void Print(String code_print_again) {
        AsyncHttpPost.post(mContext, WebAPI.PRINT, JsonCreate.PrintAgain(ShareUtil.getInstance(mContext).getNickName(),
                ShareUtil.getInstance(mContext).getWaveHouseID(), code_print_again), new AsyncHttpPost.Postinterface() {
            @Override
            public void onSuccess(PostTypeMsg mBean, AsyncHttpClient Client) {
                MediaPlayer.getInstance(mContext).ok();
                VibratorUtil.Vibrate(mContext,500);
                ToastUtil.Show(mContext,"打印成功");
                ed_print_num.setText("");
            }

            @Override
            public void onFailure(String Msg) {
                MediaPlayer.getInstance(mContext).error();
                VibratorUtil.Vibrate(mContext,1000);
                ed_print_num.setText("");
                ToastUtil.Show(mContext,Msg);
            }
        });
    }

    private BroadcastReceiver mSamDataReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(InfoCode.SCN_CUST_ACTION_SCODE)) {
                String message = intent.getStringExtra(InfoCode.SCN_CUST_EX_SCODE);
                if (ed_print_num.hasFocus()) {
                    ed_print_num.setText(message);
                    ed_print_num.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_ENTER));
                    ed_print_num.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_ENTER));
                }
            }
        }
    };

    private void initView() {

        ed_print_num = (EditText)findViewById(R.id.ed_print_num);
        btn_print_again = (Button)findViewById(R.id.btn_print_again);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        Log.e("key",event+"");
        if(event.getKeyCode()==23&&event.getAction()==KeyEvent.ACTION_DOWN){
            if(ed_print_num.hasFocus()){
                ed_print_num.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_ENTER));
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
