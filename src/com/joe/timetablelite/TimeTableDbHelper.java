package com.joe.timetablelite;


import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;



public class TimeTableDbHelper extends SQLiteOpenHelper {
	public static final String name = "TimeTable";
	public static final int version = 1;
	
	public static final String TABLE_NAME_TIMETABLE = "timetable";
	public static final String COLUMN_NAME_ID = "id";
	public static final String COLUMN_NAME_PERIOD1 = "period1";
	public static final String COLUMN_NAME_PERIOD2 = "period2";
	public static final String COLUMN_NAME_PERIOD3 = "period3";
	public static final String COLUMN_NAME_PERIOD4 = "period4";
	public static final String COLUMN_NAME_PERIOD5 = "period5";
	public static final String COLUMN_NAME_PERIOD6 = "period6";
	public static final String COLUMN_NAME_PERIOD7 = "period7";
	public static final String COLUMN_NAME_PERIOD8 = "period8";
	public static final String COLUMN_NAME_PERIOD9 = "period9";
	public static final String COLUMN_NAME_PERIOD10 = "period10";
	
	public static  String[] allColumns = { COLUMN_NAME_ID, COLUMN_NAME_PERIOD1, COLUMN_NAME_PERIOD2, COLUMN_NAME_PERIOD3, COLUMN_NAME_PERIOD4 };
	
	public static  String[] allColumns_mod = null;
	
	private static final String TEXT_TYPE = " TEXT";
	private static final String COMMA_SEP = ",";
	private static final String SQL_CREATE_ENTRIES =
	    "CREATE TABLE " + TABLE_NAME_TIMETABLE + " (" +
	    COLUMN_NAME_ID + " INTEGER PRIMARY KEY," +
	    COLUMN_NAME_PERIOD1 + TEXT_TYPE + COMMA_SEP +
	    COLUMN_NAME_PERIOD2 + TEXT_TYPE + COMMA_SEP +
	    COLUMN_NAME_PERIOD3 + TEXT_TYPE + COMMA_SEP +
	    COLUMN_NAME_PERIOD4 +  
	    " );";
	
	private static String SQL_CREATE_ENTRIES_MOD = "";
	
	private static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + TABLE_NAME_TIMETABLE;
		
	public TimeTableDbHelper(Context context) {
		super(context, name, null, version);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL(SQL_CREATE_ENTRIES);
		Log.i("into","oncreate");
			  
		for(int i=0; i<5; i++) {
			Log.i("into","intialising tables");
			ContentValues values= new ContentValues();
			values.put(COLUMN_NAME_ID, i+1);
			values.put(COLUMN_NAME_PERIOD1, "nil");
			values.put(COLUMN_NAME_PERIOD4, "nil");
			values.put(COLUMN_NAME_PERIOD3, "nil");
			values.put(COLUMN_NAME_PERIOD2, "nil");
			db.insert(TABLE_NAME_TIMETABLE, null, values);
		}		
	}
	
	public static void inittable(SQLiteDatabase db, int noOfRows) {
		db.execSQL(SQL_DELETE_ENTRIES);
		String statement = formsSqlStatement(noOfRows);
		db.execSQL(SQL_CREATE_ENTRIES_MOD);
		for(int i=0; i<5; i++) {
			Log.i("into","intialising tables");
			ContentValues values= new ContentValues();
			values.put(COLUMN_NAME_ID, i+1);
			for(int j=0; j<noOfRows; j++) {
				values.put("period"+(j+1), "nil");
			}
			db.insert(TABLE_NAME_TIMETABLE, null, values);
		}		
	}
	
	public static String formsSqlStatement(int noOfRows) {
		String pre_statement = "CREATE TABLE " + TABLE_NAME_TIMETABLE + " (" +
	    COLUMN_NAME_ID + " INTEGER PRIMARY KEY ";
		String statement = "";
		for(int i=0; i<noOfRows; i++) {
			statement = statement + ", period" + (i+1) + " TEXT ";
		}
		String post_statement = ");";
		SQL_CREATE_ENTRIES_MOD = pre_statement + statement + post_statement;
		allColumns_mod = new String[noOfRows+1];
		allColumns_mod[0] = COLUMN_NAME_ID;
		for(int j=0; j<noOfRows; j++) {
			allColumns_mod[j+1] = "period" +(j+1);
		}
		return SQL_CREATE_ENTRIES_MOD;
	}
	
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
		// TODO Auto-generated method stub
		db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
	}

}
