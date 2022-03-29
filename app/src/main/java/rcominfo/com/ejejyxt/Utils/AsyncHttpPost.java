package rcominfo.com.ejejyxt.Utils;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;

import rcominfo.com.ejejyxt.Bean.GetBean.PostTypeMsg;


/**
 * Created by wly on 2016/11/7.
 *
 */
public class AsyncHttpPost {

    private static PostTypeMsg mBean;

    public static void post(final Context context, final String FunctionName , final String Json, final Postinterface postinterface){
        final String url = ShareUtil.getInstance(context).getServerall();
        Log.e("url_base",url);
        String MD5Result = MD5.getMD5(Json+WebAPI.KEY);
        final AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.add("FunctionName",FunctionName);
        params.add("JsonData",Json);
        params.add("CusID", WebAPI.CUSID);
        params.add("KeyMd5",MD5Result);
        client.post(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                if(!(new String(bytes)==null)&&!new String(bytes).equals("")){
                    try {
                        mBean = new Gson().fromJson(new String(bytes), PostTypeMsg.class);
                        if (mBean.State) {
                            postinterface.onSuccess(mBean, client);
                            Log.e("PostError",FunctionName +" "+ Json);
                            Log.e("PostError",FunctionName +" "+ new String(bytes));
                        }
                        else {
                            Log.e("PostError",FunctionName +" "+ Json);
                            Log.e("PostError",FunctionName +" "+ new String(bytes));
                            postinterface.onFailure(mBean.MsgText);
                        }
                    }catch (Exception e){
                        ToastUtil.Show(context,"数据解析失败");

                    }


                }else{
                    ToastUtil.Show(context,"获取数据失败");
                    postinterface.onFailure(mBean.MsgText);
                }

            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                postinterface.onFailure("网络错误");
                Log.e("PostError",FunctionName +" "+ Json);
            }
        });
    }
    public interface Postinterface
    {
        public void onSuccess(PostTypeMsg mBean, AsyncHttpClient Client);
        public void onFailure(String Msg);
    }

}

