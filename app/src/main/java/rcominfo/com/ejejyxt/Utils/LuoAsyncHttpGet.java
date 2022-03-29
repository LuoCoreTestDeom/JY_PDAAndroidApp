package rcominfo.com.ejejyxt.Utils;

import android.content.Context;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;

import rcominfo.com.ejejyxt.Bean.GetBean.PostTypeMsg;

public class  LuoAsyncHttpGet{
    public  static  void   getRequest(final Context context, final String FunctionName,final  TrackingUrl trackingUrl)
    {
        final String url = ShareUtil.getInstance(context).getRequstUrl()+FunctionName;
        Log.e("url_base",url);
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                if (bytes==null||bytes.length<1)
                {
                    ToastUtil.Show(context,"无法获取数据！");
                }
                else  if (new String(bytes)==null||new String(bytes).isEmpty())
                {
                    ToastUtil.Show(context,"数据解析失败！");
                }
                else {
                    trackingUrl.onSuccess(new String(bytes));
                }
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                ToastUtil.Show(context,"网络错误！");
            }
        });

    }

    public  interface  TrackingUrl
    {
         void onSuccess(String url);
    }

}
