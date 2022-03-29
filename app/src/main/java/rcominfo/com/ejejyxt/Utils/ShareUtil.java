package rcominfo.com.ejejyxt.Utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by wly on 2016/11/26.
 */
public class ShareUtil {
    private static final String FILE_NAME = "JYXT";
    public static ShareUtil mInstance;
    private Context mContext=null;
    private final SharedPreferences shared;
    private final SharedPreferences.Editor editor;


    public synchronized static ShareUtil getInstance(Context mContext){
        if(mInstance == null)
            mInstance = new ShareUtil(mContext);
        return mInstance;
    }

    public ShareUtil(Context context) {
        this.mContext = context;
        shared = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        editor = shared.edit();
    }

    public void setNickName(String nickname){
        editor.putString("nickname",nickname);
        editor.commit();
    }
    public String getNickName(){
        return shared.getString("nickname","");
    }

    public  void  setTrackingUrl(String urlstr)
    {
        editor.putString("TrackingUrl",urlstr);
        editor.commit();
    }
    public  String getTrackingUrl(){return  shared.getString("TrackingUrl","");}

    public  void  setSingleLower(int urlstr)
    {
        editor.putInt("SingleLower",urlstr);
        editor.commit();
    }
    public  Integer getSingleLower(){return  shared.getInt("SingleLower",0);}

    public void setWaveHouse(String waveHouse){
        editor.putString("wavehouse",waveHouse);
        editor.commit();
    }
    public String getWaveHouse(){
        return shared.getString("wavehouse","");
    }
    public void setWaveHouseID(String waveHouseid){
        editor.putString("wavehouseid",waveHouseid);
        editor.commit();
    }
    public String getWaveHouseID(){
        return shared.getString("wavehouseid","");
    }
    public void setWaveHouseType(String waveHouseType){
        editor.putString("waveHouseType",waveHouseType);
        editor.commit();
    }
    public String getWaveHouseType(){
        return shared.getString("waveHouseType","");
    }

    public void setPermission(int permisssion){
        editor.putInt("permisssion",permisssion);
        editor.commit();
    }
    public int getPermisssion(){
        return shared.getInt("permisssion",0);
    }
    public void setareaCode(String areacode){
        editor.putString("areaCode",areacode);
        editor.commit();
    }
    public String getareaCode(){
        return shared.getString("areaCode","");
    }

    public void server(String serverIP,String serverPort){
        editor.putString("serverIP",serverIP);
        editor.putString("serverPort",serverPort);
        editor.commit();
    }
    public String getServerIP(){
        return shared.getString("serverIP","");
    }
    public String getServerPort(){
        return shared.getString("serverPort","");
    }
    public void new_server(String serverIP,String serverPort){
        editor.putString("new_serverIP",serverIP);
        editor.putString("new_serverPort",serverPort);
        editor.commit();
    }
    public String getnewServerIP(){
       return shared.getString("new_serverIP","");
    }
    public String getnewServerPort(){
       return shared.getString("new_serverPort","");
    }

    public String getServerall(){
        return "http://"+shared.getString("serverIP","")+":"+shared.getString("serverPort","")+"/PDA/AllTransfer";
    }
    public String getRequstUrl(){
        return "http://"+shared.getString("serverIP","")+":"+shared.getString("serverPort","")+"/PDA/";
    }
    public String getnewServerall(){
        return "http://"+shared.getString("new_serverIP","")+":"+shared.getString("new_serverPort","")+"/PDA/GetVosionNo";
    }

    public void Permission(String permission){
        editor.putString("permission",permission);
        editor.commit();
    }
    public String getPermission(){
        return shared.getString("permission","");
    }

    public void setCode(String code){
        editor.putString("code",code);
        editor.commit();
    }
    public String getCode(){
        return shared.getString("code","");
    }
}
