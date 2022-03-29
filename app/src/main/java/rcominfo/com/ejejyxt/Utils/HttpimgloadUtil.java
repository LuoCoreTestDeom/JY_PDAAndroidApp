package rcominfo.com.ejejyxt.Utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpimgloadUtil {
	
	public static Bitmap getHttpBitmap(String url){  
        URL myFileURL;  
        Bitmap bitmap=null;  
        try{  
            myFileURL = new URL(url);  
            //�������  
            HttpURLConnection conn=(HttpURLConnection)myFileURL.openConnection();  
            //���ó�ʱʱ��Ϊ6000���룬conn.setConnectionTiem(0);��ʾû��ʱ������  
            conn.setConnectTimeout(6000);  
            //�������û��������  
            conn.setDoInput(true);  
            //��ʹ�û���  
            conn.setUseCaches(false);  
            //�����п��ޣ�û��Ӱ��  
            //conn.connect();  
            //�õ�������  
            InputStream is = conn.getInputStream();  
            //�����õ�ͼƬ  
            bitmap = BitmapFactory.decodeStream(is);  
            //�ر�������  
            is.close();  
        }catch(Exception e){  
            e.printStackTrace();  
        }  
          
        return bitmap;  
          
    }  
}
