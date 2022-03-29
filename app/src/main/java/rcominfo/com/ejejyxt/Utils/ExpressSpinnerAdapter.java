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
 * Created by 王璐阳 on 2017/2/14.
 */
public class ExpressSpinnerAdapter extends BaseAdapter {
    Context context;
    ArrayList<ExpressBean.ForwarderReturn> items;
    private ViewHolder viewHolder;

    public ExpressSpinnerAdapter(Context context, ArrayList<ExpressBean.ForwarderReturn> items) {
        this.context=context;
        this.items = items;

    }

    @Override
    public int getCount() {
        return items.size();
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
            viewHolder = new ViewHolder();
            view = LayoutInflater.from(context).inflate(R.layout.spinner_express,null);
            viewHolder.tv_express = (TextView)view.findViewById(R.id.tv_express);
            view.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) view.getTag();
        }

        viewHolder.tv_express.setText(items.get(i).ForwarderName);
        return view;
    }

    class ViewHolder{
        TextView tv_express;
    }
}
