package rcominfo.com.ejejyxt.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.rcominfo.tmjy.R;

import java.util.ArrayList;
import java.util.Comparator;

import rcominfo.com.ejejyxt.Bean.GetBean.CheckListBean;
import rcominfo.com.ejejyxt.Bean.GetBean.ExpressBean;
import rcominfo.com.ejejyxt.Bean.GetBean.PostTypeMsg;
import rcominfo.com.ejejyxt.Bean.PostBean.Check_print_Bean;
import rcominfo.com.ejejyxt.Utils.AsyncHttpPost;
import rcominfo.com.ejejyxt.Utils.ExpressAdapter;
import rcominfo.com.ejejyxt.Utils.InfoCode;
import rcominfo.com.ejejyxt.Utils.JsonCreate;
import rcominfo.com.ejejyxt.Utils.MediaPlayer;
import rcominfo.com.ejejyxt.Utils.MyAdapter_check;
import rcominfo.com.ejejyxt.Utils.ShareUtil;
import rcominfo.com.ejejyxt.Utils.ToastUtil;
import rcominfo.com.ejejyxt.Utils.VibratorUtil;
import android.widget.SimpleAdapter;

public class CheckActivity extends BaseActivity {

    private RelativeLayout rl_all;
    private EditText ed_son_code;
    private TextView weight_total;
    private TextView weight_temp;
    private CheckActivity mContext;
    private EditText alert_ed;
    private TextView alert_tv_info;
    int flag_int = 1;
    private AlertDialog alertDialog;
    private String main_code;
    private ArrayList<CheckListBean.checkOrderInfoReturn> container;
    private GridView gridView;
    private MyAdapter_check ada;
    private String billcode;
    private double weight_to;
    private Spinner sp_check;
    private Button set_weight;
    private CommonListener commonListener;

    private TextView good_num;
    private String code;
    private ArrayList<Check_print_Bean.kdcom_billcode> temp_code;
    private int HM = 1;
    private int CF = 2;
    public String orderid;
    private Button post_package;
    private ArrayList<ExpressBean.ForwarderReturn> container_express;
    private Spinner sp_express;
    private int expressID;
    private double sizeOfThreeEdge;
    private double a1;
    private double b1;
    private double c1;
    private double max_weight;
    private double weight_tip;
    private double cf_max_size;
    private double cf_max_weight;
    private boolean flag = false;
    private String express_chosen;
    private double weight_to_last;
    private double zhouchang;
    private double longest_edge;
    private double a;
    private double b;
    private double c;
    private String[] size_edge;
    private boolean expressFlag;
    private TextView tv_count;
    private int count;
    private String express;
    private TextView timeBarName;
    private TextView tvPrintCarrier;
    private TextView tvPrintAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check);
        count = 0;
        IntentFilter intentFilter = new IntentFilter(InfoCode.SCN_CUST_ACTION_SCODE);
        registerReceiver(mSamDataReceiver, intentFilter);
        mContext = this;
        container = new ArrayList<>();
        container_express = new ArrayList<>();
        temp_code = new ArrayList<>();
        container_express = new ArrayList<>();
        expressFlag = true;
        express_chosen = "";
        weight_to = 0.0;
        initView();
        GetExprtess();
        GetSubCode();
        onEditorAction();

        OnBtnClick();
    }
    //初始化控件
    private void initView() {
        rl_all = (RelativeLayout) findViewById(R.id.rl_all);
        ed_son_code = (EditText) findViewById(R.id.ed_son_code);
        weight_temp = (TextView) findViewById(R.id.weight_temp);
        gridView = (GridView) findViewById(R.id.grView);
        good_num = (TextView) findViewById(R.id.good_num);
        tv_count = (TextView)findViewById(R.id.tv_count);
        weight_temp.setText(weight_to + "kg");
        post_package = (Button) findViewById(R.id.post_package);
        commonListener = new CommonListener();
        tv_count.setText("已扫描:"+count+"条");
        timeBarName=(TextView)findViewById(R.id.timeBarName);
        tvPrintCarrier=(TextView)findViewById(R.id.tvPrintCarrier);
        tvPrintAddress=(TextView)findViewById(R.id.tvPrintAddress);

    }
    //获取承运商
    private void GetExprtess() {
        AsyncHttpPost.post(mContext, "Forwarder", "{}", new AsyncHttpPost.Postinterface() {
            @Override
            public void onSuccess(PostTypeMsg mBean, AsyncHttpClient Client) {
                Gson gson = new Gson();
                ExpressBean eBean = gson.fromJson(mBean.ReturnJson, ExpressBean.class);
                container_express.addAll(eBean.ForwarderLiset);
            }

            @Override
            public void onFailure(String Msg) {
                ToastUtil.Show(mContext, Msg);
            }
        });
    }

    private BroadcastReceiver mSamDataReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(InfoCode.SCN_CUST_ACTION_SCODE)) {
                String message = intent.getStringExtra(InfoCode.SCN_CUST_EX_SCODE);
                if (ed_son_code.hasFocus()) {
                    ed_son_code.setText(message);
                    ed_son_code.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_ENTER));
                    ed_son_code.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_ENTER));
                } else if (alert_ed.hasFocus()) {
                    alert_ed.setText(message);
                    alert_ed.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_ENTER));
                    alert_ed.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_ENTER));
                }
            }
        }
    };

    private void onEditorAction() {
        ed_son_code.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                code = ed_son_code.getText().toString();
                if (i == 0 && keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                    if (code.equals("")) {
                        MediaPlayer.getInstance(mContext).error();
                        VibratorUtil.Vibrate(mContext, 1000);
                        ToastUtil.Show(mContext, "单号不能为空");
                        ed_son_code.setText("");
                        ed_son_code.setFocusable(true);
                        ed_son_code.setFocusableInTouchMode(true);
                        ed_son_code.requestFocus();
                        ed_son_code.findFocus();
                    } else {
                        AddCodeToContainer(code);
                    }
                }
                return true;
            }
        });
    }

    private void AddCodeToContainer(String code) {
        for (int i = 0; i < container.size(); i++) {
            if ((container.get(i).kd_billcode).equals(code)) {
                if (container.get(i).OrderWeight != null && !container.get(i).OrderWeight.equals("")) {
                    weight_to_last = Double.parseDouble(container.get(i).OrderWeight);
                } else {
                    ToastUtil.Show(mContext, "无法获取重量，请到系统查证");
                    weight_to_last = 0.0;
                }
                Check_print_Bean check_print_bean = new Check_print_Bean();
                Check_print_Bean.kdcom_billcode kdcom_billcode = check_print_bean.new kdcom_billcode();
                kdcom_billcode.billcode = container.get(i).kd_billcode;
                kdcom_billcode.kd_com = container.get(i).kd_com;
                temp_code.add(kdcom_billcode);
                setTextFocus(ed_son_code);
                ed_son_code.setText("");
                MediaPlayer.getInstance(mContext).ok();
                VibratorUtil.Vibrate(mContext, 500);
                weight_to = weight_to + weight_to_last;
                good_num.setText(temp_code.size()+"件");
                count++;
                tv_count.setText("已扫描:"+count+"条");
                weight_temp.setText(weight_to+"");
                container.remove(i);
                ada.notifyDataSetChanged();
                break;
            }else if((i+1)==container.size()){
                ed_son_code.setText("");
                ed_son_code.setFocusable(true);
                ed_son_code.setFocusableInTouchMode(true);
                ed_son_code.requestFocus();
                ed_son_code.findFocus();
                ToastUtil.Show(mContext, "无匹配单号");
                MediaPlayer.getInstance(mContext).error();
                VibratorUtil.Vibrate(mContext, 1000);
            }
        }
    }

    private void print(String ex, int isbig, final int where) {
        final ProgressDialog progressDialog = new ProgressDialog(mContext);
        progressDialog.setMessage("请稍后...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);
        progressDialog.show();
        AsyncHttpPost.post(mContext, "Print", JsonCreate.Print(ex, ShareUtil.getInstance(mContext).getNickName(),
                ShareUtil.getInstance(mContext).getWaveHouseID(), orderid, temp_code, String.valueOf(weight_to), isbig),
                new AsyncHttpPost.Postinterface() {
                    @Override
                    public void onSuccess(PostTypeMsg mBean, AsyncHttpClient Client) {
                        temp_code.clear();
                        flag = false;
                        count = 0;
                        tv_count.setText("已扫描:"+count+"条");
                        weight_to = 0.0;
                        good_num.setText(temp_code.size() + "件");
                        weight_temp.setText(weight_to + "kg");
                        MediaPlayer.getInstance(mContext).ok();
                        VibratorUtil.Vibrate(mContext, 500);
                        ToastUtil.Show(mContext, "打印成功");
                        progressDialog.dismiss();
                        expressFlag = true;
                        if (container.size() == 0) {
                            flag_int = 1;
                            GetSubCode();
                        }
                    }

                    @Override
                    public void onFailure(String Msg) {
                        ToastUtil.Show(mContext, Msg);
                        Log.e("printfalse", false + "");
                        MediaPlayer.getInstance(mContext).error();
                        VibratorUtil.Vibrate(mContext, 1000);
                        progressDialog.dismiss();
                        if (where == 0) {

                            weight_to = weight_to - weight_to_last;
                            weight_temp.setText(weight_to + "");
                            temp_code.remove(temp_code.size() - 1);
                            good_num.setText(temp_code.size() + "件");
                        }

                    }
                });
    }


    private void OnBtnClick() {
        post_package.setOnClickListener(commonListener);
    }

    class com<T> implements Comparator<T> {

        @Override
        public int compare(T t, T t1) {
            int i = Integer.parseInt(String.valueOf(t));
            int j = Integer.parseInt(String.valueOf(t1));
            if (i > j) return 1;
            if (i < j) return -1;
            return 0;
        }
    }

    private void GetSubCode() {
        AlertDialog.Builder ab2 = new AlertDialog.Builder(mContext);
        rl_all.setVisibility(View.GONE);
        View a = LayoutInflater.from(mContext).inflate(R.layout.alertview, null);
        alert_ed = (EditText) a.findViewById(R.id.alert_ed_queue_num);
        alert_tv_info = (TextView) a.findViewById(R.id.alert_tv_info);
        ab2.setCancelable(false);
        ab2.setTitle("请输入或扫描快递单号");
        ab2.setView(a);
        ab2.setPositiveButton("确定", null);
        alert_ed.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == 0 && keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                    alertRequest();
                }

                return true;
            }
        });
        ab2.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });

        ab2.create();
        alertDialog = ab2.create();
        alertDialog.show();
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                alertRequest();

            }
        });
    }

    private void alertRequest() {
        alert_ed.setEnabled(false);
        main_code = alert_ed.getText().toString();
        if (main_code.equals("")) {
            alert_tv_info.setText("单号不能为空");
            MediaPlayer.getInstance(mContext).error();
            VibratorUtil.Vibrate(mContext, 1000);
            alert_tv_info.setVisibility(View.VISIBLE);
            alert_ed.setEnabled(true);
        } else {
            AsyncHttpPost.post(mContext, "CheckOrderInfo", "{\"billcode\": " + "\"" + alert_ed.getText().toString() + "\"}",
                    new AsyncHttpPost.Postinterface() {
                        @Override
                        public void onSuccess(PostTypeMsg mBean, AsyncHttpClient Client) {
                            flag_int = 0;
                            container.clear();
                            Gson gson = new Gson();
                            CheckListBean bean = gson.fromJson(mBean.ReturnJson, CheckListBean.class);
                            if (bean.coir.size() > 0) {
                                express = bean.coir.get(0).cforhm;
                                MediaPlayer.getInstance(mContext).ok();
                                VibratorUtil.Vibrate(mContext, 500);
                                container.addAll(bean.coir);
                                ada = new MyAdapter_check(mContext, container);
                                orderid = bean.coir.get(0).OrderId;
                                timeBarName.setText("航班时效："+bean.coir.get(0).timeBarName);
                                tvPrintCarrier.setText("承运商："+bean.coir.get(0).Carrier);
                                tvPrintAddress.setText("收货地址："+bean.coir.get(0).DeliveryAddress);
                                gridView.setAdapter(ada);

                                ada.notifyDataSetChanged();
                                rl_all.setVisibility(View.VISIBLE);
                                alertDialog.dismiss();
                                setTextFocus(ed_son_code);
                                alert_ed.setEnabled(true);

                            } else {
                                MediaPlayer.getInstance(mContext).error();
                                VibratorUtil.Vibrate(mContext, 1000);
                                alert_tv_info.setVisibility(View.VISIBLE);
                                alert_tv_info.setText("获取数据失败");
                                setTextFocus(ed_son_code);
                                alert_ed.setEnabled(true);
                            }

                        }

                        @Override
                        public void onFailure(String Msg) {
                            MediaPlayer.getInstance(mContext).error();
                            VibratorUtil.Vibrate(mContext, 1000);
                            alert_tv_info.setVisibility(View.VISIBLE);
                            alert_tv_info.setText(Msg);
                            setTextFocus(alert_ed);
                            alert_ed.setText("");
                            alert_ed.setEnabled(true);
                        }
                    });
        }

    }



    private class CommonListener implements View.OnClickListener {

        private String ex;

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.post_package:
                    View v = LayoutInflater.from(mContext).inflate(R.layout.choose_express,null);
                    Spinner mSpinner = (Spinner)v.findViewById(R.id.sp_express_check);
                    final ExpressAdapter ada = new ExpressAdapter(mContext,container_express);
                    mSpinner.setAdapter(ada);
                    ada.notifyDataSetChanged();
                    for(int i = 0;i<container_express.size();i++){
                        if(container_express.get(i).ForwarderName.equals(express)){
                            mSpinner.setSelection(i);
                        }
                    }
                    mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            ex = (String) ada.getItem(i);
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {
                            ex = (String) ada.getItem(0);
                        }
                    });
                    AlertDialog.Builder ab = new AlertDialog.Builder(mContext);
                            ab.setTitle("请选择快递公司").setView(v)
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            print(ex,0,0);
                        }
                    }).setNegativeButton("取消",null).create().show();

                    break;
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            //TODO something
            AlertDialog.Builder a = new AlertDialog.Builder(mContext);
            a.setTitle("确认退出");
            a.setMessage("确认退出核单打包么？");
            a.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    finish();
                }
            });
            a.setNegativeButton("取消", null);
            a.create();
            a.show();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        Log.e("key", event + "");
        if (event.getKeyCode() == 23 && event.getAction() == KeyEvent.ACTION_DOWN) {
            Log.e("123", "123132321");
            if (ed_son_code.hasFocus()) {
                ed_son_code.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_ENTER));
                return false;

            } else if (alert_ed.hasFocus()) {
                alert_ed.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_ENTER));
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
