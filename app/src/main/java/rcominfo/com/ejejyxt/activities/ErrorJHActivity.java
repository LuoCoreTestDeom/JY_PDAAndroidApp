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
import android.view.Window;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.rcominfo.tmjy.R;

import java.util.ArrayList;

import rcominfo.com.ejejyxt.Bean.GetBean.ErrorJHBean;
import rcominfo.com.ejejyxt.Bean.GetBean.PostTypeMsg;
import rcominfo.com.ejejyxt.Bean.PostBean.BeanAll;
import rcominfo.com.ejejyxt.Utils.AsyncHttpPost;
import rcominfo.com.ejejyxt.Utils.InfoCode;
import rcominfo.com.ejejyxt.Utils.JsonCreate;
import rcominfo.com.ejejyxt.Utils.MediaPlayer;
import rcominfo.com.ejejyxt.Utils.MyAdapter_errorjh;
import rcominfo.com.ejejyxt.Utils.MyAdapter_task;
import rcominfo.com.ejejyxt.Utils.ShareUtil;
import rcominfo.com.ejejyxt.Utils.ToastUtil;
import rcominfo.com.ejejyxt.Utils.VibratorUtil;
import rcominfo.com.ejejyxt.Utils.WebAPI;

public class ErrorJHActivity extends BaseActivity {

    private FrameLayout fl_lv_error;
    private ListView lv_error_jh;
    private FrameLayout fl_lv_error_info;
    private ListView lv_error_info;
    private EditText ed_code_error_jh;
    private ErrorJHActivity mContext;
    private Gson gson;
    private ArrayList<ErrorJHBean.AbnormalPickingRequest_Return> container;
    private ArrayList<BeanAll.OffShelfRuturn> container_1;
    private MyAdapter_task adapter_info;
    private MyAdapter_errorjh ada;
    private String parent_code;
    private ProgressDialog progressDialog;
int flag = 0;
    private String out_barcode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_error_jh);
        mContext = this ;
        IntentFilter intentFilter = new IntentFilter(InfoCode.SCN_CUST_ACTION_SCODE);
        registerReceiver(mSamDataReceiver, intentFilter);
        gson = new Gson();
        progressDialog = new ProgressDialog(mContext);
        progressDialog.setMessage("请稍后...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setIndeterminate(true);
        initView();
        LoadData();
        OnitemClick();
        onEditorAction();

    }
    private BroadcastReceiver mSamDataReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(InfoCode.SCN_CUST_ACTION_SCODE)) {
                String message = intent.getStringExtra(InfoCode.SCN_CUST_EX_SCODE);
                if (ed_code_error_jh.hasFocus()) {
                    ed_code_error_jh.setText(message);
                    ed_code_error_jh.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_ENTER));
                    ed_code_error_jh.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_ENTER));
                }
            }
        }
    };


    private void onEditorAction() {
        ed_code_error_jh.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if(i==0&&keyEvent.getAction()==KeyEvent.ACTION_DOWN){
                    String code = ed_code_error_jh.getText().toString();
                    for (int kdCount=0;kdCount<container_1.size();kdCount++){
                        if (container_1.get(kdCount).kd_billcode.equals(code)){
                                getResultBybillcode(code,kdCount);
                            break;
                        }
                        else if ((kdCount+1)==container_1.size()){
                            ToastUtil.Show(mContext,"请输入正确得单号");
                            MediaPlayer.getInstance(mContext).error();
                            VibratorUtil.Vibrate(mContext,1000);
                            ed_code_error_jh.selectAll();
                        }
                    }
                }
                return false;
            }
        });
    }

    private void getResultBybillcode(final String code,final int kdCount) {
        ed_code_error_jh.setEnabled(false);
        AsyncHttpPost.post(mContext, WebAPI.ERROR_JHXJ, JsonCreate.JHXJfind(parent_code,code, ShareUtil.getInstance(mContext).getNickName()), new AsyncHttpPost.Postinterface() {
            @Override
            public void onSuccess(PostTypeMsg mBean, AsyncHttpClient Client) {

                if ((container_1.size()-1)==0){
                    ToastUtil.Show(mContext,"拣货完成");
                    finishtask();
                    LoadData();
                    flag = 0;
                    fl_lv_error_info.setVisibility(View.GONE);
                    fl_lv_error.setVisibility(View.VISIBLE);
                }
                else {
                    container_1.remove(kdCount);
                    MediaPlayer.getInstance(mContext).ok();
                    VibratorUtil.Vibrate(mContext,500);
                }
                adapter_info.notifyDataSetChanged();
                ed_code_error_jh.setEnabled(true);
                ed_code_error_jh.setText("");
            }
            @Override
            public void onFailure(String Msg) {

                ToastUtil.Show(mContext,Msg);
                MediaPlayer.getInstance(mContext).error();
                VibratorUtil.Vibrate(mContext,1000);
                ed_code_error_jh.setEnabled(true);
                ed_code_error_jh.setText("");
            }

        });


    }

    private void OnitemClick() {
        lv_error_jh.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    if(i!=0) {

                        progressDialog.show();
                        parent_code = container.get(i - 1).billcode;
                        requestErrorCode(container.get(i - 1).billcode);
                    }


            }
        });

        lv_error_info.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                AlertDialog.Builder ab = new AlertDialog.Builder(mContext);
                View view1 = LayoutInflater.from(mContext).inflate(R.layout.ab_task_view, null);
                ab.setTitle("详细信息");
                ab.setView(view1);
                TextView tv_weight = (TextView)view1.findViewById(R.id.tv_weight);
                TextView tv_guige = (TextView)view1.findViewById(R.id.tv_guige);
                tv_weight.setText("重量："+container_1.get(i-1).dd_weight2);
                tv_guige.setText("长*宽*高："+container_1.get(i-1).guige);
                ab.setNegativeButton("OK",null);
                ab.create();
                ab.show();
            }
        });
    }

    private void requestErrorCode(String billcode) {
        container_1.clear();
        out_barcode = billcode;
        AsyncHttpPost.post(mContext, WebAPI.ERROR_JHXJ_FINISH, "{\"out_barcode\":\"" +billcode+ "\"}", new AsyncHttpPost.Postinterface() {
            @Override
            public void onSuccess(PostTypeMsg mBean, AsyncHttpClient Client) {
                progressDialog.dismiss();
                MediaPlayer.getInstance(mContext).ok();
                VibratorUtil.Vibrate(mContext,500);
                BeanAll.pickingShelvesList bean = gson.fromJson(mBean.ReturnJson,BeanAll.pickingShelvesList.class);
                container_1.addAll(bean.OffShelfRuturn);
                adapter_info = new MyAdapter_task(mContext,container_1);
                lv_error_info.setAdapter(adapter_info);
                adapter_info.notifyDataSetChanged();
                flag = 1;
                fl_lv_error.setVisibility(View.GONE);
                fl_lv_error_info.setVisibility(View.VISIBLE);
                setTextFocus(ed_code_error_jh);

            }

            @Override
            public void onFailure(String Msg) {
                progressDialog.dismiss();
                ToastUtil.Show(mContext,Msg);
                MediaPlayer.getInstance(mContext).error();
                VibratorUtil.Vibrate(mContext,1000);
            }
        });
    }
    private void finishtask() {
        AsyncHttpPost.post(mContext, WebAPI.PICKING, JsonCreate.FinishTask(out_barcode,ShareUtil.
                getInstance(mContext).getNickName(),ShareUtil.getInstance(mContext).getWaveHouse()),
                new AsyncHttpPost.Postinterface() {
            @Override
            public void onSuccess(PostTypeMsg mBean, AsyncHttpClient Client) {
                ToastUtil.Show(mContext,"完成");
                VibratorUtil.Vibrate(mContext,500);
                container_1.clear();
            }

            @Override
            public void onFailure(String Msg) {
                ToastUtil.Show(mContext, Msg);
                MediaPlayer.getInstance(mContext).error();
                VibratorUtil.Vibrate(mContext,1000);
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
    private void LoadData() {
        container.clear();
        progressDialog.show();
        AsyncHttpPost.post(mContext, WebAPI.INNORMALPICKING, JsonCreate.YCJHList(ShareUtil.getInstance(mContext).getNickName(),ShareUtil.getInstance(mContext).getWaveHouse()), new AsyncHttpPost.Postinterface() {
            @Override
            public void onSuccess(PostTypeMsg mBean, AsyncHttpClient Client) {
                MediaPlayer.getInstance(mContext).ok();
                VibratorUtil.Vibrate(mContext,500);
                progressDialog.dismiss();
                ErrorJHBean.AbnormalPickingList bean = gson.fromJson(mBean.ReturnJson,ErrorJHBean.AbnormalPickingList.class);
                container.addAll(bean.AbnormalPickingListReturn);
                ada = new MyAdapter_errorjh(ErrorJHActivity.this,container);
                lv_error_jh.setAdapter(ada);
                ada.notifyDataSetChanged();
            }

            @Override
            public void onFailure(String Msg) {
                MediaPlayer.getInstance(mContext).error();
                VibratorUtil.Vibrate(mContext,1000);
                ToastUtil.Show(mContext,Msg);
                progressDialog.dismiss();
            }
        });
    }

    private void initView() {
        fl_lv_error = (FrameLayout)findViewById(R.id.fl_lv_error);
        fl_lv_error_info=(FrameLayout)findViewById(R.id.fl_lv_error_info);
        lv_error_jh = (ListView)findViewById(R.id.lv_error_jh);
        lv_error_info = (ListView)findViewById(R.id.lv_error_info);
        ed_code_error_jh = (EditText)findViewById(R.id.ed_code_error_jh);
        container = new ArrayList<>();
        container_1 = new ArrayList<>();
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK){
           if(flag == 1){

               fl_lv_error_info.setVisibility(View.GONE);
               fl_lv_error.setVisibility(View.VISIBLE);
               LoadData();
               flag = 0;
           }else if(flag == 0){
               onBackPressed();
           }
        }
        return true;
    }
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        Log.e("key",event+"");
        if(event.getKeyCode()==23&&event.getAction()==KeyEvent.ACTION_DOWN){
            if(ed_code_error_jh.hasFocus()){
                ed_code_error_jh.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_ENTER));
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
