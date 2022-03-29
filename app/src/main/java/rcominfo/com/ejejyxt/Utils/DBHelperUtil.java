package rcominfo.com.ejejyxt.Utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelperUtil extends SQLiteOpenHelper{

	public static final String TABLE_NAME = "wave_house";
	public static final String WAVEHOUSE_NAME = "wh_name";
	public static final String WAVEHOUSE_TYPE = "wh_type";
	public static final String WAVEHOUSE_ID = "wh_id";
	public static final String ID = "_id";
	public static final String TABLE_NAME1 = "express_company";
	public static final String WAVEHOUSE_NAME1 = "express";
	public static final String WAVEHOUSE_TYPE1 = "countryid";
	public static final String WAVEHOUSE_ID1 = "id";
	public static final String WAVEHOUSE_ABBR = "abbr";
	public static final String ID1 = "_id";
	public static final String TABLE_NAME2 = "ZT_point";
	public static final String WAVEHOUSE_NAME2 = "zt";
	public static final String ID2 = "_id";

	public DBHelperUtil(Context context) {
		super(context, "data", null, 1);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase arg0) {
		// TODO Auto-generated method stub
		arg0.execSQL("CREATE TABLE " + TABLE_NAME + " (" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + WAVEHOUSE_NAME
				+ " Varchar(50)   UNIQUE," + WAVEHOUSE_TYPE + " Varchar(50)  NULL," +  WAVEHOUSE_ID
				+ " Varchar(50)  )");
		arg0.execSQL("CREATE TABLE " + TABLE_NAME1 + " (" + ID1 + " INTEGER PRIMARY KEY AUTOINCREMENT," + WAVEHOUSE_NAME1
				+ " Varchar(50)  NULL UNIQUE," + WAVEHOUSE_TYPE1 + " Varchar(50)  NULL,"+ WAVEHOUSE_ABBR + " Varchar(50)  NULL," +  WAVEHOUSE_ID1
				+ " Varchar(50)  NULL)");
		arg0.execSQL("CREATE TABLE " + TABLE_NAME2 + " (" + ID2 + " INTEGER PRIMARY KEY AUTOINCREMENT," + WAVEHOUSE_NAME2
				+ " Varchar(50)  NULL UNIQUE)");
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {

		
	}

}
