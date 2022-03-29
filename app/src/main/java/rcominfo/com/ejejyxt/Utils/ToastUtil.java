package rcominfo.com.ejejyxt.Utils;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

public class ToastUtil {
	public static void Show(Context mContext,String msg){
		Toast toast = Toast.makeText(mContext,
				msg,200);

			   toast.setGravity(Gravity.CENTER, 0, 0);
			   toast.show();
	}
}
