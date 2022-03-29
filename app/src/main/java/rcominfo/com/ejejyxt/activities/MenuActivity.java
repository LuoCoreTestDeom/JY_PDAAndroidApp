package rcominfo.com.ejejyxt.activities;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.rcominfo.tmjy.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rcominfo.com.ejejyxt.Bean.GetBean.KD_comBean;
import rcominfo.com.ejejyxt.Bean.GetBean.PostTypeMsg;
import rcominfo.com.ejejyxt.Bean.GetBean.WavehouseGetBean;
import rcominfo.com.ejejyxt.Bean.PostBean.BeanAll;
import rcominfo.com.ejejyxt.Utils.AsyncHttpPost;
import rcominfo.com.ejejyxt.Utils.DBHelperUtil;
import rcominfo.com.ejejyxt.Utils.Langutil;
import rcominfo.com.ejejyxt.Utils.ShareUtil;
import rcominfo.com.ejejyxt.Utils.ToastUtil;
import rcominfo.com.ejejyxt.Utils.WebAPI;

public class MenuActivity extends BaseActivity {
    private GridView gv_menu;
    private ArrayList<Map<String, Object>> data_list;
    private Context mContext;
    private ProgressDialog pDialog;
    private Gson gson;
    private SQLiteDatabase dbwriter;
    private BeanAll.ztPriceList bean;
    private WavehouseGetBean wavehouseGetBean;
    private ImageView btn_back;
    private ArrayList<String> container_title;
    private ArrayList<Integer> container_icon;
    private ArrayList<Integer> container_flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        mContext = this;
        initView();
        gson = new Gson();
        container_title = new ArrayList<>();
        container_icon  = new ArrayList<>();
        container_flag = new ArrayList<>();
        pDialog = new ProgressDialog(this);
        pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pDialog.setIndeterminate(true);
        pDialog.setCancelable(false);
        DBHelperUtil db = new DBHelperUtil(mContext);
        dbwriter = db.getWritableDatabase();
        SetGridByPermission();
        if(container_icon.size()>0&&container_title.size()>0){
            addView();
        }

        downloadExpresscom();
        downloadWaveHouseInfo();
        downloadZTpoint();
        onItemClick();
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


    }

    private void onItemClick() {
        gv_menu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i){
                    case 0:
                        GetinActivity(0);
                        break;
                    case 1:
                        GetinActivity(1);
                        break;
                    case 2:
                        GetinActivity(2);
                        break;
                    case 3:
                        GetinActivity(3);
                        break;
                    case 4:
                        GetinActivity(4);
                        break;
                    case 5:
                        GetinActivity(5);
                        break;
                    case 6:
                        GetinActivity(6);
                        break;
                    case 7:
                        GetinActivity(7);
                        break;
                    case 8:
                        GetinActivity(8);
                        break;
                    case 9:
                        GetinActivity(9);
                        break;
                    case 10:
                        GetinActivity(10);
                        break;
                }
            }
        });
    }

    private void SetGridByPermission() {
        String permission  = ShareUtil.getInstance(mContext).getPermission();
        if(permission.equals("")){
            AlertDialog.Builder ab = new AlertDialog.Builder(mContext);
                    ab.setTitle("权限");
                    ab.setMessage("对不起，你没有权限使用任何功能，请联系管理员");
                    ab.create();
                    ab.show();
        }else{
            String[] allow_to_use = permission.split(",");

            if(allow_to_use.length>0){
                if(allow_to_use.length==1&&allow_to_use[0].equals("0")){
                    AlertDialog.Builder ab = new AlertDialog.Builder(mContext);
                    ab.setTitle("权限");
                    ab.setMessage("对不起，你没有权限使用任何功能，请联系管理员");
                    ab.create();
                    ab.show();
                }else{
                    for (String anAllow_to_use : allow_to_use) {
                        switch (anAllow_to_use) {
                            case "0":
                                ToastUtil.Show(mContext, "用户." + ShareUtil.getInstance(mContext).getNickName() + "欢迎您使用PDA仓库管理系统");
                                break;
                            case "1":
                                container_icon.add(R.drawable.img_new);
                                container_title.add("入库上架");
                                container_flag.add(1);
                                break;
                            case "2":
                                container_icon.add(R.drawable.zt);
                                container_title.add("拣货下架");
                                container_flag.add(2);
                                break;
                            case "3":
                                container_icon.add(R.drawable.img_send);
                                container_title.add("核单打包");
                                container_flag.add(3);
                                break;
                            case "4":
                                container_icon.add(R.drawable.img_search);
                                container_title.add("查件跟踪");
                                container_flag.add(4);
                                break;
                            case "5":
                                container_icon.add(R.drawable.check);
                                container_title.add("盘点");
                                container_flag.add(5);
                                break;
                            case "6":
                                container_title.add("快递员交单");
                                container_icon.add(R.drawable.kuaidiyuan);
                                container_flag.add(6);
                                break;
                            case "7":
                                container_icon.add(R.drawable.print_again);
                                container_title.add("补打运单");
                                container_flag.add(7);
                                break;
                            case "8":
                                container_title.add("异常拣货");
                                container_icon.add(R.drawable.zt);
                                container_flag.add(8);
                                break;
                            case "9":
                                container_title.add("指定单拣货");
                                container_icon.add(R.drawable.check);
                                container_flag.add(9);
                                break;
                            case "10":
                                container_title.add("同行件交单");
                                container_icon.add(R.drawable.kuaidiyuan);
                                container_flag.add(10);
                                break;
                            case "11":
                                container_title.add("店铺拣货下架");
                                container_icon.add(R.drawable.kuaidiyuan);
                                container_flag.add(11);
                                break;
                            default:

                                break;
                        }

                    }
                }
            }}

        }




    private void GetinActivity(int position){
        if(container_flag.get(position)==1){
            startNewActivity(RKSJActivity.class, R.anim.activity_fade_in, R.anim.activity_fade_out, false, null);
        }else if(container_flag.get(position)==2){
            startNewActivity(TaskActivity.class, R.anim.activity_fade_in, R.anim.activity_fade_out, false, null);
        }else if(container_flag.get(position)==3){
            startNewActivity(CheckActivity.class, R.anim.activity_fade_in, R.anim.activity_fade_out, false, null);
        }else if(container_flag.get(position)==4){
            startNewActivity(CJGZActivity.class, R.anim.activity_fade_in, R.anim.activity_fade_out, false, null);
        }else if(container_flag.get(position)==5){
            startNewActivity(PDActivity.class, R.anim.activity_fade_in, R.anim.activity_fade_out, false, null);
        }else if(container_flag.get(position)==6){
            startNewActivity(KDYJDActivity.class, R.anim.activity_fade_in, R.anim.activity_fade_out, false, null);
        }else if(container_flag.get(position)==7){
            startNewActivity(PrintAgainActivity.class, R.anim.activity_fade_in, R.anim.activity_fade_out, false, null);
        }else if(container_flag.get(position)==8){
            startNewActivity(ErrorJHActivity.class,R.anim.activity_fade_in, R.anim.activity_fade_out, false, null);
        }else if(container_flag.get(position)==9){
            startNewActivity(GetTaskBynumActivity.class,R.anim.activity_fade_in, R.anim.activity_fade_out, false, null);
        }else if(container_flag.get(position)==10){
            startNewActivity(THActivity.class,R.anim.activity_fade_in, R.anim.activity_fade_out, false, null);
        }else if(container_flag.get(position)==11){
            startNewActivity(ShopTaskActivity.class,R.anim.activity_fade_in, R.anim.activity_fade_out, false, null);
        }
    }
    private void downloadZTpoint() {
        AsyncHttpPost.post(mContext, WebAPI.ZT_PRICELIST, "{}", new AsyncHttpPost.Postinterface() {
            @Override
            public void onSuccess(PostTypeMsg mBean, AsyncHttpClient Client) {
                bean = gson.fromJson(mBean.ReturnJson, BeanAll.ztPriceList.class);
                ContentValues cv = new ContentValues();
                for (int i = 0; i < bean.ztPrice.size(); i++) {
                    cv.put(DBHelperUtil.WAVEHOUSE_NAME2, bean.ztPrice.get(i).type_name);
                    dbwriter.insert(DBHelperUtil.TABLE_NAME2, null, cv);

                }
            }

            @Override
            public void onFailure(String Msg) {

            }
        });

    }

    private void downloadWaveHouseInfo() {
        pDialog.setMessage("正在下载仓库信息...");
        pDialog.show();
        AsyncHttpPost.post(mContext, WebAPI.WAVEHOUSELIST, "{}", new AsyncHttpPost.Postinterface() {
            @Override
            public void onSuccess(PostTypeMsg mBean, AsyncHttpClient Client) {
                wavehouseGetBean = gson.fromJson(mBean.ReturnJson, WavehouseGetBean.class);
                ContentValues cv = new ContentValues();
                Log.e("wavehouseGetBean", wavehouseGetBean.houseInfo.size() + "");
                for (int i = 0; i < wavehouseGetBean.houseInfo.size(); i++) {
                    cv.put(DBHelperUtil.WAVEHOUSE_ID, wavehouseGetBean.houseInfo.get(i).ID);
                    cv.put(DBHelperUtil.WAVEHOUSE_NAME, wavehouseGetBean.houseInfo.get(i).house_name);
                    cv.put(DBHelperUtil.WAVEHOUSE_TYPE, wavehouseGetBean.houseInfo.get(i).house_type);
                    dbwriter.insert(DBHelperUtil.TABLE_NAME, null, cv);
                    pDialog.dismiss();
                }

            }

            @Override
            public void onFailure(String Msg) {
                ToastUtil.Show(mContext, Msg);
                pDialog.dismiss();
            }
        });


    }

    private void downloadExpresscom() {
        pDialog.setMessage("下载快递公司信息");
        pDialog.show();

        AsyncHttpPost.post(mContext, WebAPI.KD_COM, "{}", new AsyncHttpPost.Postinterface() {
            @Override
            public void onSuccess(PostTypeMsg mBean, AsyncHttpClient Client) {
                pDialog.dismiss();

                KD_comBean kd_comBean = gson.fromJson(mBean.ReturnJson, KD_comBean.class);
                Log.e("KD_comBean", kd_comBean.Kd_comInfo.size() + "");
                ContentValues cv = new ContentValues();
                for (int i = 0; i < kd_comBean.Kd_comInfo.size(); i++) {
                    cv.put(DBHelperUtil.WAVEHOUSE_ID1, kd_comBean.Kd_comInfo.get(i).id);
                    cv.put(DBHelperUtil.WAVEHOUSE_NAME1, kd_comBean.Kd_comInfo.get(i).Kd_com);
                    cv.put(DBHelperUtil.WAVEHOUSE_TYPE1, kd_comBean.Kd_comInfo.get(i).country_id);
                    cv.put(DBHelperUtil.WAVEHOUSE_ABBR, kd_comBean.Kd_comInfo.get(i).jym);
                    dbwriter.insert(DBHelperUtil.TABLE_NAME1, null, cv);
                }

            }

            @Override
            public void onFailure(String Msg) {
                pDialog.dismiss();
                ToastUtil.Show(mContext, Msg);
            }
        });

    }

    private void initView() {
        gv_menu = (GridView) findViewById(R.id.gv_menu);
        TextView tv_info_menu = (TextView) findViewById(R.id.tv_info_menu);
        btn_back = (ImageView)findViewById(R.id.btn_back);
        String nickname = ShareUtil.getInstance(mContext).getNickName();
        String wavehouse = ShareUtil.getInstance(mContext).getWaveHouse();
        tv_info_menu.setText(Langutil.langchange(MenuActivity.this, R.string.user) + nickname + ","
                + Langutil.langchange(MenuActivity.this, R.string.warehouse) + wavehouse);

    }

    private void addView() {
        data_list = new ArrayList<>();
        getData();
        String[] from = {"image", "text"};
        int[] to = {R.id.iv_gridimg, R.id.iv_gridtext};
        SimpleAdapter sim_adapter = new SimpleAdapter(this, data_list, R.layout.gridviewlayout, from, to);
        gv_menu.setAdapter(sim_adapter);
    }

    public List<Map<String, Object>> getData() {

            for (int i = 0; i < container_icon.size(); i++) {
                Map<String, Object> map = new HashMap<>();
                map.put("image", container_icon.get(i));
                map.put("text", container_title.get(i));
                data_list.add(map);
                Log.e("error","in");
            }
        return data_list;
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        Log.e("key",event+"");
        Log.e("error","in");
        return super.dispatchKeyEvent(event);
    }
}
