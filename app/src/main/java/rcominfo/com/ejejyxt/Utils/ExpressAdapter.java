package rcominfo.com.ejejyxt.Utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.rcominfo.tmjy.R;

import java.util.ArrayList;

import rcominfo.com.ejejyxt.Bean.GetBean.ExpressBean;

/**
 * Created by 王璐阳 on 2017/5/25.
 */

public class ExpressAdapter extends BaseAdapter {
    Context context;
    ArrayList<ExpressBean.ForwarderReturn> items;
    private ViewHolder viewHolder;

    public ExpressAdapter(Context context, ArrayList<ExpressBean.ForwarderReturn> items) {
        this.context = context;
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int i) {
        return items.get(i).ForwarderName;
    }

    @Override
    public long getItemId(int i) {
        return items.get(i).id;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(view==null){
            view = LayoutInflater.from(context).inflate(R.layout.spinner_express,null);
            viewHolder = new ViewHolder();
            viewHolder.tv_express = (TextView)view.findViewById(R.id.tv_express);
            view.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) view.getTag();
        }

        viewHolder.tv_express.setText(items.get(i).ForwarderName);
        return view;
    }

    private class ViewHolder{
        TextView tv_express;
    }
}
