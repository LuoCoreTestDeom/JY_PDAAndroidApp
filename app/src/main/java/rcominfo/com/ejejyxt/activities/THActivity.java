package rcominfo.com.ejejyxt.activities;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.rcominfo.tmjy.R;

import java.util.ArrayList;

import rcominfo.com.ejejyxt.Bean.GetBean.PostTypeMsg;
import rcominfo.com.ejejyxt.Bean.GetBean.THbean;
import rcominfo.com.ejejyxt.Utils.AsyncHttpPost;
import rcominfo.com.ejejyxt.Utils.InfoCode;
import rcominfo.com.ejejyxt.Utils.JsonCreate;
import rcominfo.com.ejejyxt.Utils.Langutil;
import rcominfo.com.ejejyxt.Utils.MediaPlayer;
import rcominfo.com.ejejyxt.Utils.ShareUtil;
import rcominfo.com.ejejyxt.Utils.SimpleAda;
import rcominfo.com.ejejyxt.Utils.ToastUtil;
import rcominfo.com.ejejyxt.Utils.VibratorUtil;

public class THActivity extends BaseActivity {

    private ImageView btn_back;
    private Spinner sp_kdyjd;
    private EditText ed_express_code;
    private String nickname;
    private String wavehouse;
    private THActivity mContext;
    public String str_sp;
    private ProgressDialog pDialog;
    private ArrayList<THbean.TH> container;
    private TextView num;
    int count;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_th);
        mContext = this;
        count = 0;
        initView();
        IntentFilter intentFilter = new IntentFilter(InfoCode.SCN_CUST_ACTION_SCODE);
        registerReceiver(mSamDataReceiver, intentFilter);
        container = new ArrayList<>();
        OnClick();
        OnEditorClick();
        getTHinfo();
    }

    public  void getTHinfo(){
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("加载中...请稍后...");
        pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pDialog.setIndeterminate(true);
        pDialog.setCancelable(true);
        pDialog.show();
        AsyncHttpPost.post(mContext, "PeerInfo", "{}", new AsyncHttpPost.Postinterface() {
            @Override
            public void onSuccess(PostTypeMsg mBean, AsyncHttpClient Client) {
                Gson g = new Gson();
                THbean tHbean = g.fromJson(mBean.ReturnJson,THbean.class);
                container.addAll(tHbean.PD);
                SimpleAda sada = new SimpleAda(mContext,container);
                sp_kdyjd.setAdapter(sada);
                sada.notifyDataSetChanged();
                pDialog.dismiss();
            }

            @Override
            public void onFailure(String Msg) {
                ToastUtil.Show(mContext,Msg);
                pDialog.dismiss();
                getTHinfo();
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
                str_sp = container.get(arg2).customerName;
                count = 0;
                ToastUtil.Show(mContext, str_sp);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });
    }
    private BroadcastReceiver mSamDataReceiver = new BroadcastReceiver() {
        public String message;

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


            String billcode;

            @Override
            public boolean onEditorAction(TextView arg0, int arg1, KeyEvent arg2) {
                // TODO Auto-generated method stub
                if (arg1 == 0 && arg2.getAction() == KeyEvent.ACTION_DOWN) {
                    billcode = ed_express_code.getText().toString();
                    ed_express_code.setEnabled(false);

                    AsyncHttpPost.post(mContext, "PeerDeliverBill", JsonCreate.KDJD(str_sp, billcode, wavehouse, nickname), new AsyncHttpPost.Postinterface() {
                        @Override
                        public void onSuccess(PostTypeMsg mBean, AsyncHttpClient Client) {
                            MediaPlayer.getInstance(mContext).ok();
                            VibratorUtil.Vibrate(mContext,500);
                            ed_express_code.setEnabled(true);
                            ed_express_code.setText("");
                            ToastUtil.Show(mContext, billcode + "交单成功");
                            count+=1;
                            num.setText(count+"");
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
    private void initView() {
        // TODO Auto-generated method stub
        TextView tv_info_menu = (TextView) findViewById(R.id.tv_info_menu);
        btn_back = (ImageView) findViewById(R.id.btn_back);
        sp_kdyjd = (Spinner) findViewById(R.id.sp_kdyjd);
        ed_express_code = (EditText) findViewById(R.id.ed_kdyjd_num);
        num = (TextView)findViewById(R.id.num);
        nickname = ShareUtil.getInstance(mContext).getNickName();
        wavehouse = ShareUtil.getInstance(mContext).getWaveHouse();
        tv_info_menu.setText(Langutil.langchange(THActivity.this, R.string.user) + nickname + ","
                + Langutil.langchange(THActivity.this, R.string.warehouse) + wavehouse);
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
