package rcominfo.com.ejejyxt.Utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.rcominfo.tmjy.R;

import java.util.ArrayList;

import rcominfo.com.ejejyxt.Bean.GetBean.ErrorJHBean;


/**
 * Created by dell on 2016/12/29.
 */
public class MyAdapter_errorjh extends BaseAdapter {
    Context context;
    ArrayList<ErrorJHBean.AbnormalPickingRequest_Return> items;
    private ViewHolder vh;

    public MyAdapter_errorjh(Context context, ArrayList<ErrorJHBean.AbnormalPickingRequest_Return> items) {
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
            view.setTag(vh);
        }else{
            vh = (ViewHolder) view.getTag();
        }

        if(i==0){
            vh.tv_code.setText("异常订单号");
            view.setBackgroundColor(view.getResources().getColor(android.R.color.darker_gray));
            vh.tv_person.setText("异常提交人");
            view.setEnabled(false);
        }else{
            if(items.get(i-1).billcode!=null&&items.get(i-1).AbnormalEmp!=null){
            vh.tv_code.setText(items.get(i-1).billcode);
            vh.tv_person.setText(items.get(i-1).AbnormalEmp);}else{
                ToastUtil.Show(context,"fuck 骆工又传null");}

        }
        return view;
    }

    class ViewHolder{
        TextView tv_code;
        TextView tv_person;
    }
}
