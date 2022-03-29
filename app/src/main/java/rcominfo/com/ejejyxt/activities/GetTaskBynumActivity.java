package rcominfo.com.ejejyxt.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.rcominfo.tmjy.R;

import java.util.ArrayList;

import rcominfo.com.ejejyxt.Bean.GetBean.PostTypeMsg;
import rcominfo.com.ejejyxt.Bean.PostBean.BeanAll;
import rcominfo.com.ejejyxt.Utils.AsyncHttpPost;
import rcominfo.com.ejejyxt.Utils.InfoCode;
import rcominfo.com.ejejyxt.Utils.JsonCreate;
import rcominfo.com.ejejyxt.Utils.MediaPlayer;
import rcominfo.com.ejejyxt.Utils.MyAdapter_task;
import rcominfo.com.ejejyxt.Utils.ShareUtil;
import rcominfo.com.ejejyxt.Utils.ToastUtil;
import rcominfo.com.ejejyxt.Utils.VibratorUtil;
import rcominfo.com.ejejyxt.Utils.WebAPI;

public class GetTaskBynumActivity extends BaseActivity {

    private GridView gvTask;
    private EditText ed_code;
    private Button btn_stop;
    private CommonListener commonListener;
    private GetTaskBynumActivity mContext;
    private ProgressDialog pDialog;
    private Gson gson;
    private ArrayList<BeanAll.OffShelfRuturn> container;
    public MyAdapter_task ada;
    private BeanAll.pickingShelvesList bean;
    private String out_barcode;
    private EditText alert_ed;
    private AlertDialog alertDialog;
    int flag = 1;
    private TextView alert_tv_info;
    private LinearLayout ll_gone;

    private TextView username;
    private TextView jsNum;
    private TextView wjhNum;
    private TextView goodsType;
    private TextView orderGoods;
    private  TextView tv_Carrier;
    private  TextView tv_Rem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_task_bynum);
        mContext = this;
        gson = new Gson();
        container = new ArrayList<>();
        IntentFilter intentFilter = new IntentFilter(InfoCode.SCN_CUST_ACTION_SCODE);
        registerReceiver(mSamDataReceiver, intentFilter);
        initView();
        requestTask(ShareUtil.getInstance(mContext).getNickName());
        setOnClick();
        OnEditor();
        OnItemclick();


    }

    private void OnItemclick() {
        gvTask.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                AlertDialog.Builder ab = new AlertDialog.Builder(mContext);
                View view1 = LayoutInflater.from(mContext).inflate(R.layout.ab_task_view, null);
                ab.setTitle("详细信息");
                ab.setView(view1);
                TextView tv_weight = (TextView) view1.findViewById(R.id.tv_weight);
                TextView tv_guige = (TextView) view1.findViewById(R.id.tv_guige);
                tv_weight.setText("重量：" + container.get(i - 1).dd_weight2);
                tv_guige.setText("长*宽*高：" + container.get(i - 1).guige);
                ab.setNegativeButton("OK", null);
                ab.create();
                ab.show();



            }
        });
    }

    private BroadcastReceiver mSamDataReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(InfoCode.SCN_CUST_ACTION_SCODE)) {
                String message = intent.getStringExtra(InfoCode.SCN_CUST_EX_SCODE);
                if (ed_code.hasFocus()) {
                    ed_code.setText(message);
                    ed_code.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_ENTER));
                }else if(alert_ed.hasFocus()){
                    alert_ed.setText(message);
                    alert_ed.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_ENTER));
                }
            }
        }
    };

    private void postCode(final String code) {
        ed_code.setEnabled(false);
        AsyncHttpPost.post(mContext, WebAPI.ERROR_JHXJ, JsonCreate.JHXJfind(container.get(0).out_barcode, code, ShareUtil.getInstance(mContext).getNickName()), new AsyncHttpPost.Postinterface() {
            @Override
            public void onSuccess(PostTypeMsg mBean, AsyncHttpClient Client) {
                ed_code.setEnabled(true);
                MediaPlayer.getInstance(mContext).ok();
                VibratorUtil.Vibrate(mContext, 500);
                ed_code.setText("");
                setTextFocus(ed_code);

                if (container.size() > 1) {
                    for (int i = 0; i < container.size(); i++) {
                        if (container.get(i).kd_billcode.equals(code)) {
                            container.remove(i);
                            ada.notifyDataSetChanged();
                            ed_code.setText("");
                            setTextFocus(ed_code);
                        }
                    }
                } else if (container.size() == 1) {
                    container.clear();
                    ada.notifyDataSetChanged();
                    finishtask();

                }

            }

            @Override
            public void onFailure(String Msg) {
                MediaPlayer.getInstance(mContext).error();
                VibratorUtil.Vibrate(mContext, 1000);
                ToastUtil.Show(mContext, Msg);
                ed_code.setEnabled(true);
                ed_code.setText("");
                setTextFocus(ed_code);
            }
        });
    }

    private void initView() {
        gvTask = (GridView) findViewById(R.id.gvTask);
        ed_code = (EditText) findViewById(R.id.ed_code);
        btn_stop = (Button) findViewById(R.id.btn_stop);
        ll_gone = (LinearLayout)findViewById(R.id.ll_gone);
        commonListener = new CommonListener();

        username=(TextView)findViewById(R.id.textUserName);
        jsNum=(TextView)findViewById(R.id.jsNum);
        wjhNum=(TextView)findViewById(R.id.wjhNum);
        goodsType=(TextView)findViewById(R.id.goodsType);
        orderGoods=(TextView)findViewById(R.id.OrderGoods);
        orderGoods.setMovementMethod(ScrollingMovementMethod.getInstance());
        tv_Carrier=(TextView)findViewById(R.id.tv_Carrier);
        tv_Rem=(TextView)findViewById(R.id.tv_Rem);
    }

    private void OnEditor() {
        ed_code.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == 0 && keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                    String code = ed_code.getText().toString().replace("\n","");
                    if (TextUtils.isEmpty(code))
                    {
                        MediaPlayer.getInstance(mContext).error();
                        ToastUtil.Show(mContext, "请输入单号！");
                        ed_code.setText("");
                        setTextFocus(ed_code);
                        return true;
                    }
                    if(ShareUtil.getInstance(mContext).getSingleLower()==1)
                    {
                        for(int re = 0;re<container.size();re++) {
                            if (container.get(re).kd_billcode.equals(code)) {
                                MediaPlayer.getInstance(mContext).ok();
                                ed_code.setText("");
                                setTextFocus(ed_code);
                                container.remove(re);
                                wjhNum.setText("未拣货：" + container.size());
                                ada.notifyDataSetChanged();
                                ed_code.setText("");
                                setTextFocus(ed_code);
                                if (container.size()<1)
                                {
                                    finishtask();
                                }
                                break;
                            }
                            else if (re==container.size()-1)
                            {
                                MediaPlayer.getInstance(mContext).error();
                                ToastUtil.Show(mContext, "无匹配单号！");
                                ed_code.setText("");
                                setTextFocus(ed_code);
                            }
                        }


                    }
                    else {
                        postCode(code);
                    }

                }
                return false;
            }
        });
    }

    private void setOnClick() {
        btn_stop.setOnClickListener(commonListener);
    }

    private void finishtask() {
        AsyncHttpPost.post(mContext, WebAPI.SZJHXJ, JsonCreate.FinishTask(out_barcode, ShareUtil.getInstance(mContext).getNickName(), ShareUtil.getInstance(mContext).getWaveHouse()), new AsyncHttpPost.Postinterface() {
            @Override
            public void onSuccess(PostTypeMsg mBean, AsyncHttpClient Client) {
                ToastUtil.Show(mContext, "当前任务完成");
                MediaPlayer.getInstance(mContext).ok();
                flag =1;
                requestTask(ShareUtil.getInstance(mContext).getWaveHouse());
                ll_gone.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(String Msg) {
                ToastUtil.Show(mContext, Msg);
                MediaPlayer.getInstance(mContext).error();
                AlertDialog.Builder ab = new AlertDialog.Builder(mContext);
                ab.setTitle("拣货失败").setMessage("是否重试");
                ab.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finishtask();
                    }
                });
                ab.setCancelable(false);
                ab.create();
                ab.show();

            }
        });
    }

    private void requestTask(final String waveHouseID) {
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("加载中...请稍后...");
        pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pDialog.setIndeterminate(true);
        pDialog.setCancelable(true);
        AlertDialog.Builder ab = new AlertDialog.Builder(mContext);
        ab.setTitle("输入指定单号");
        View a = LayoutInflater.from(mContext).inflate(R.layout.alertview, null);
        alert_ed = (EditText) a.findViewById(R.id.alert_ed_queue_num);
        alert_ed.setHint("扫描货输入订单ID");
        alert_tv_info = (TextView) a.findViewById(R.id.alert_tv_info);
        ab.setTitle("请输入或扫描订单号");
        ab.setView(a);
        ab.setPositiveButton("确定", null);

        alert_ed.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == 0 && keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                String orderid = alert_ed.getText().toString();
                    if(orderid.equals("")){
                        orderid = "-1";
                        get(waveHouseID,orderid);
                    }else{
                        get(waveHouseID,orderid);
                    }

                }

                return false;
            }
        });
        ab.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });

        ab.create();
        alertDialog = ab.create();
        alertDialog.show();
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String orderid = alert_ed.getText().toString();
                if(orderid.equals("")){
                    orderid = "-1";
                    get(waveHouseID,orderid);
                }else{
                    get(waveHouseID,orderid);
                }

            }
        });

    }

    private void get(String waveHouseID,String orderID) {
        pDialog.show();
        AsyncHttpPost.post(mContext, WebAPI.JHXJ_PARENT, JsonCreate.requestPick(ShareUtil.getInstance(mContext).getareaCode(), waveHouseID,orderID,ShareUtil.getInstance(mContext).getWaveHouse(),0), new AsyncHttpPost.Postinterface() {

            @Override
            public void onSuccess(PostTypeMsg mBean, AsyncHttpClient Client) {
                pDialog.dismiss();
                container.clear();
                MediaPlayer.getInstance(mContext).ok();
                VibratorUtil.Vibrate(mContext, 500);
                bean = gson.fromJson(mBean.ReturnJson, BeanAll.pickingShelvesList.class);
                container.addAll(bean.OffShelfRuturn);
                out_barcode = container.get(0).out_barcode;

                ada = new MyAdapter_task(mContext, container);
                gvTask.setAdapter(ada);
                alertDialog.dismiss();
                flag = 0;
                ada.notifyDataSetChanged();
                ed_code.setText("");
                setTextFocus(ed_code);
                ll_gone.setVisibility(View.VISIBLE);

                username.setText("会员名称："+container.get(0).username);
                goodsType.setText("类型："+container.get(0).goodsType);
                jsNum.setText("总件数："+container.size());
                wjhNum.setText("未拣货："+container.size());
                orderGoods.setText(container.get(0).OrderGoods);
                tv_Carrier.setText("承运商:"+container.get(0).carrier);
                tv_Rem.setText("备注:"+container.get(0).rem);
            }

            @Override
            public void onFailure(String Msg) {
                pDialog.dismiss();
                alert_tv_info.setText(Msg);
                alert_tv_info.setVisibility(View.VISIBLE);
                MediaPlayer.getInstance(mContext).error();
                VibratorUtil.Vibrate(mContext, 1000);
            }
        });
    }

    private class CommonListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.btn_stop:
                    VibratorUtil.Vibrate(mContext, 2000);
                    if (container.size() > 0) {
                        AsyncHttpPost.post(mContext, WebAPI.PICKING_STOP, JsonCreate.StopPicking(container.get(0).out_barcode, ShareUtil.getInstance(mContext).getNickName()), new AsyncHttpPost.Postinterface() {
                            @Override
                            public void onSuccess(PostTypeMsg mBean, AsyncHttpClient Client) {
                                ToastUtil.Show(mContext, "成功");
                                MediaPlayer.getInstance(mContext).ok();
                                VibratorUtil.Vibrate(mContext, 500);
                                container.clear();
                                ada.notifyDataSetChanged();
                                ed_code.setText("");
                                requestTask(ShareUtil.getInstance(mContext).getWaveHouse());

                                ll_gone.setVisibility(View.GONE);


                            }
                            @Override
                            public void onFailure(String Msg) {
                                ToastUtil.Show(mContext, Msg);
                                MediaPlayer.getInstance(mContext).error();
                                VibratorUtil.Vibrate(mContext, 1000);
                            }
                        });
                    } else {
                        finish();
                    }
                    break;
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            //TODO something
//            if(flag ==1){
//
//            }else if(flag==0){
                AlertDialog.Builder a = new AlertDialog.Builder(mContext);
                a.setTitle("确认退出");
                a.setMessage("确认退出核单打包么？");
                a.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                });
                a.setNegativeButton("取消",null);
                a.create();
                a.show();
//            }

            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        Log.e("key", event + "");
        if (event.getKeyCode() == 23 && event.getAction() == KeyEvent.ACTION_DOWN) {
            if (ed_code.hasFocus()) {
                ed_code.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_ENTER));
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

