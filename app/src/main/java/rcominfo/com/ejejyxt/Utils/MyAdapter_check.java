package rcominfo.com.ejejyxt.Utils;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.rcominfo.tmjy.R;

import org.w3c.dom.Text;

import java.util.ArrayList;

import rcominfo.com.ejejyxt.Bean.GetBean.CheckListBean;


/**
 * Created by dell on 2016/12/29.
 */
public class MyAdapter_check extends BaseAdapter {
    Context context;
    ArrayList<CheckListBean.checkOrderInfoReturn> items;
    private ViewHolder vh;
    int selectItem;

    public MyAdapter_check(Context context, ArrayList<CheckListBean.checkOrderInfoReturn> items) {
        this.context = context;
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size()+1;
    }

    @Override
    public Object getItem(int i) {
        return items.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(view==null){
            view = LayoutInflater.from(context).inflate(R.layout.item_jh_error,null);
            vh = new ViewHolder();
            vh.tv_code = (TextView)view.findViewById(R.id.tv_code);
            vh.tv_person = (TextView)view.findViewById(R.id.tv_person);
            vh.tv_goods=(TextView)view.findViewById(R.id.tv_goods);
            view.setTag(vh);
        }else{
            vh = (ViewHolder) view.getTag();
        }

        if(i==0){
            vh.tv_code.setText("单号");
            view.setBackgroundColor(view.getResources().getColor(android.R.color.darker_gray));
            vh.tv_person.setText("重量(kg)");
            vh.tv_goods.setText("品名");
            view.setEnabled(false);
        }else{
            vh.tv_code.setText(items.get(i-1).kd_billcode);
            vh.tv_person.setText(items.get(i-1).OrderWeight);
            vh.tv_goods.setText(items.get(i-1).goods);
        }

        if(i==selectItem&&i!=0){
            view.setBackgroundColor(Color.BLUE);
            vh.tv_code.setSelected(true);
            vh.tv_person.setSelected(true);
            vh.tv_code.setPressed(true);
            vh.tv_person.setPressed(true);
            vh.tv_code.setTextColor(Color.WHITE);
            vh.tv_person.setTextColor(Color.WHITE);
        }else if(i!=selectItem&&i!=0){
            view.setBackgroundColor(Color.TRANSPARENT);
            vh.tv_code.setSelected(false);
            vh.tv_person.setSelected(false);
            vh.tv_code.setPressed(false);
            vh.tv_person.setPressed(false);
            vh.tv_code.setTextColor(Color.BLACK);
            vh.tv_person.setTextColor(Color.BLACK);
        }
        return view;
    }

    public  void setSelectItem(int selectItem) {
        this.selectItem = selectItem;
    }
    class ViewHolder{
        TextView tv_code;
        TextView tv_person;
        TextView tv_goods;
    }
}
