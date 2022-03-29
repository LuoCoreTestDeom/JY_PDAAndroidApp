package rcominfo.com.ejejyxt.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.rcominfo.tmjy.R;

import java.text.SimpleDateFormat;
import java.util.Date;

import rcominfo.com.ejejyxt.Bean.GetBean.LoginGetBean;
import rcominfo.com.ejejyxt.Bean.GetBean.PostTypeMsg;
import rcominfo.com.ejejyxt.Utils.AsyncHttpPost;
import rcominfo.com.ejejyxt.Utils.DBHelperUtil;
import rcominfo.com.ejejyxt.Utils.InfoCode;
import rcominfo.com.ejejyxt.Utils.Langutil;
import rcominfo.com.ejejyxt.Utils.MediaPlayer;
import rcominfo.com.ejejyxt.Utils.ShareUtil;
import rcominfo.com.ejejyxt.Utils.ToastUtil;
import rcominfo.com.ejejyxt.Utils.WebAPI;


public class CJGZActivity extends BaseActivity {
    private ImageView btn_back;
    private TextView tv_info_menu;
    private EditText ed_search;
    private WebView web_search;
    private String url;
    private Context mContext;
    private Button  btnQuery;
    Handler myHandler = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cjgz);
        mContext = this;
        IntentFilter intentFilter = new IntentFilter(InfoCode.SCN_CUST_ACTION_SCODE);
        registerReceiver(mSamDataReceiver, intentFilter);
        initView();
        OnClick();
        OnEditorActionListener();



    }

    private BroadcastReceiver mSamDataReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(InfoCode.SCN_CUST_ACTION_SCODE)) {
                String message = intent.getStringExtra(InfoCode.SCN_CUST_EX_SCODE);
                if (ed_search.hasFocus()) {
                    ed_search.setText(message);
                    ed_search.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_ENTER));
                }
            }
        }
    };

    private void OnEditorActionListener() {
        ed_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView arg0, int arg1, KeyEvent arg2) {
                // TODO Auto-generated method stub
                if(arg1==0&&arg2.getAction()==KeyEvent.ACTION_DOWN){
                    web_search.loadUrl(ShareUtil.getInstance(mContext).getTrackingUrl() +ed_search.getText().toString()+
                            "&date=\n"+getTime());
                    Log.e("url",ShareUtil.getInstance(mContext).getTrackingUrl() +ed_search.getText().toString()+
                            "&date=\n"+getTime());
                    web_search.setWebViewClient(new WebViewClient(){
                        @Override
                        public boolean shouldOverrideUrlLoading(WebView view, String url) {
                            view.loadUrl(url);
                            return true;
                        }
                    });
                }
                return true;
            }
        });
    }

    private String getTime() {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        Date curDate = new Date();
        String str = format.format(curDate);
        return str;
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
        btn_back = (ImageView)findViewById(R.id.btn_back);
        tv_info_menu= (TextView)findViewById(R.id.tv_info_menu);
        ed_search= (EditText)findViewById(R.id.ed_search);
        web_search = (WebView)findViewById(R.id.web_search);
        btnQuery=(Button)findViewById(R.id.BtnQuery);
        WebSettings webSettings = web_search.getSettings();
        webSettings.setUseWideViewPort(true);
        webSettings.setUseWideViewPort(true);//设定支持viewport
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setSupportZoom(true);
        webSettings.setTextSize(WebSettings.TextSize.LARGER);
        webSettings.setBuiltInZoomControls(true);

        String nickname = ShareUtil.getInstance(mContext).getNickName();
        String wavehouse = ShareUtil.getInstance(mContext).getWaveHouse();
        tv_info_menu.setText(Langutil.langchange(CJGZActivity.this, R.string.user) + nickname + ","
                + Langutil.langchange(CJGZActivity.this, R.string.warehouse) + wavehouse);
        OnQueryClike();
    }
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        Log.e("key",event+"");
        if(event.getKeyCode()==23&&event.getAction()==KeyEvent.ACTION_DOWN){
            if(ed_search.hasFocus()){
                ed_search.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_ENTER));
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

    private  void OnQueryClike()
    {
        btnQuery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                web_search.loadUrl(ShareUtil.getInstance(mContext).getTrackingUrl() +ed_search.getText().toString()+
                        "&date=\n"+getTime());
                Log.e("url",ShareUtil.getInstance(mContext).getTrackingUrl() +ed_search.getText().toString()+
                        "&date=\n"+getTime());
                web_search.setWebViewClient(new WebViewClient(){
                    @Override
                    public boolean shouldOverrideUrlLoading(WebView view, String url) {
                        view.loadUrl(url);
                        return true;
                    }
                });
            }
        });
    }
}
