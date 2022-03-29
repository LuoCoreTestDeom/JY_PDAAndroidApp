package rcominfo.com.ejejyxt.Utils;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.rcominfo.tmjy.R;

import java.util.ArrayList;
import java.util.List;

import rcominfo.com.ejejyxt.Bean.PostBean.BeanAll;



public class MyAdapter_taopai extends BaseAdapter{
	List<String> billnum;
	Context context;
	List<String> kuwei;
	List<String> num;
	List<String> agent;
	List<String> weight;
	List<String> shangjia;
	ArrayList<BeanAll.OffShelfRuturn> items;

	private ViewHolder vh;
	private int wid;
	public MyAdapter_taopai(Context context, ArrayList<BeanAll.OffShelfRuturn> items) {
		this.context = context;
		this.items = items;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return items.size()+1;
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		// TODO Auto-generated method stub
		if(arg1 ==null){
			arg1 = LayoutInflater.from(context).inflate(R.layout.list_layout, null);
			 vh = new ViewHolder();
			 vh.express_num =(TextView)arg1.findViewById(R.id.express_num);
			 vh.kuwei =(TextView)arg1.findViewById(R.id.kuwei);
			 vh.num =(TextView)arg1.findViewById(R.id.num);
			 vh.agent =(TextView)arg1.findViewById(R.id.agent);
			 vh.weight =(TextView)arg1.findViewById(R.id.weight);
			 vh.shangjia =(TextView)arg1.findViewById(R.id.shangjia);

			 wid = arg2.getWidth();
			 arg1.setTag(vh);
		}else{
			vh = (ViewHolder) arg1.getTag();
		}
		 if (arg0 == 0) {//�����о���
			 arg1.setBackgroundColor(arg1.getResources().getColor(android.R.color.darker_gray));
			  } else if (arg0 % 2 == 0) {
				  arg1.setBackgroundColor(arg1.getResources().getColor(android.R.color.holo_blue_light));
			  }else {
				  arg1.setBackgroundColor(arg1.getResources().getColor(android.R.color.holo_green_light));
			  }
		if(arg0 == 0){
			vh.express_num.setText("快递单号");
			vh.kuwei.setText("库位号");
			vh.num.setText("数量");
			vh.agent.setText("客户");
			vh.weight.setText("实重");
			vh.shangjia.setText("上架");
		}else{
			vh.express_num.setText(items.get(arg0-1).kd_billcode);
			vh.kuwei.setText(items.get(arg0-1).stock_area);
			vh.num.setText(items.get(arg0-1).number);
			vh.agent.setText(items.get(arg0-1).username);
			vh.weight.setText(items.get(arg0-1).dd_weight2);
			if(TextUtils.isEmpty(items.get(arg0-1).is_inplace))
			{
				vh.shangjia.setText(0);
			}
			else
				{
					vh.shangjia.setText(items.get(arg0-1).is_inplace);
				}

		}


		return arg1;
	}
	
	
	
	
	 private static class ViewHolder
	    {
	        TextView express_num;
	        TextView kuwei;
	        TextView num;
	        TextView agent;
	        TextView weight;
	        TextView shangjia;
	       
	    }


}
