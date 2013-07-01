package com.joe.timetablelite;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;

public class LaunchActivity extends Activity {
	TimeTableDbHelper mDbHelper;
	Context  context = this;
	SQLiteDatabase db; 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mDbHelper =  new TimeTableDbHelper(context);
		setContentView(R.layout.activity_launch);
		new UpdateLaunchScreen().execute();
		
		
	}
	
	public void goBack(View v) {
		finish();
	}
	
	public void addTimeTable(View v) {
		Intent intent = new Intent(context, EditTimeTable.class);
		startActivity(intent);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.launch, menu);
		return true;
	}
	
	private class UpdateLaunchScreen extends AsyncTask<Void, Void, Void> {
		String[] compareThis; 
		
        //String[] toThis = {"1", "nil", "nil", "nil", "nil", "nil", "nil", "nil"};
        @Override
        protected Void doInBackground(Void... objects) {
        	db = mDbHelper.getWritableDatabase();
            Cursor cursor = db.query(mDbHelper.TABLE_NAME_TIMETABLE, mDbHelper.allColumns, mDbHelper.COLUMN_NAME_ID + "=" + 1, null, null, null, null);
            //compareThis = { cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6)};
            cursor.moveToFirst();
            compareThis = new String[cursor.getColumnCount()];
            Log.i("cursor", cursor.getColumnCount() + "");
            for(int i=0; i<compareThis.length; i++) {
            	compareThis[i] = cursor.getString(i);
            	Log.i("comparethis", compareThis[i]+"");
            }
                       
            return null;
        }

        @Override
        protected void onPostExecute(final Void unused){
            //update UI with my objects
        	//Log.i("async", toThis+"");
        	Log.i("async", compareThis+"");
            for(int i=0; i<compareThis.length; i++) {
            	if( compareThis[i] != "nil" ) {
            		Log.i("async","yes");
            		setContentView(R.layout.activity_index); 
            		//TextView tv = (TextView) findViewById(R.id.updatetimetable);
            		//Typeface font = Typeface.createFromAsset(getAssets(), "segoeui.ttf");
            		//tv.setTypeface(font);
            		break;
            	}	
            	else {
            		setContentView(R.layout.activity_today); break;
            		
            	}
            }
        }
    }

}
