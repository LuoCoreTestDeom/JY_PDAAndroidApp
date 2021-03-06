package rcominfo.com.ejejyxt.Utils;


import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.rcominfo.tmjy.R;


/**
 * @author Administrator
 *
 */
public class MySpinnerAdapter extends BaseAdapter{
	
	private Context context;
	private Cursor cursor;
	private String content;
	private LayoutInflater mInflater;

	
	public MySpinnerAdapter(Context context, Cursor cursor,String content) {
		this.context = context;
		this.cursor = cursor;
		this.content = content;
	}

	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return cursor.getCount();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return cursor.getPosition();
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		// TODO Auto-generated method stub
		ViewHolder holder;

		if(arg1==null){
			mInflater = LayoutInflater.from(context);
			arg1 = mInflater.inflate(R.layout.layout_listview_num, null);


			holder = new ViewHolder();
			holder.text_num = (TextView) arg1.findViewById(R.id.lv_text_num);
			arg1.setTag(holder);
		}else{
			holder = (ViewHolder) arg1.getTag();
		}
		cursor.moveToFirst();
		cursor.moveToPosition(arg0);
		String s = cursor.getString(cursor.getColumnIndex(content));
		holder.text_num.setTextSize(30);
		holder.text_num.setText(s);
		holder.text_num.setBackgroundColor(Color.WHITE);
		return arg1;
	}
	
	static class ViewHolder{
		TextView text_num;
	}

}
