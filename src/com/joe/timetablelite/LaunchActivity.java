package com.joe.timetablelite;


import java.util.ArrayList;
import java.util.Calendar;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

public class LaunchActivity extends Activity {
	TimeTableDbHelper mDbHelper;
	Context  context = this;
	SQLiteDatabase db; 
	ListView listView;
	ArrayList<String> today = new ArrayList<String>();
	int dow;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Constants.context = this;
		if (getIntent().getBooleanExtra("EXIT", false)) {
	         finish();
	    }
		
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
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
	    switch (item.getItemId()) {
	        case R.id.edit:
	            Intent intent = new Intent(this, MainEditActivity.class);
	            intent.putExtra("intention", 1);
	            //intent.putExtra("intention", 1);
	            startActivity(intent);
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
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
            if(compareThis[1].equalsIgnoreCase("nil")) {
        		setContentView(R.layout.activity_index);
            	}	
            	else {
            		setContentView(R.layout.activity_today);
            		
            		int dayofweek;
                	switch(Calendar.getInstance().get(Calendar.DAY_OF_WEEK)) {
                	case 1:
                		dayofweek = 1; break;
                	case 2:
                		dayofweek = 1; break;
                	case 3:
                		dayofweek = 2; break;
                	case 4:
                		dayofweek = 3; break;
                	case 5:
                		dayofweek = 4; break;
                	case 6:
                		dayofweek = 5; break;
                	case 7:
                		dayofweek = 1; break;   
                	default:
                		dayofweek = 1; break;
                	}
            		
            		new QueryDb().execute(dayofweek);
	
            	}
            }
        
        }
	
	public void mon(View v) {
		setTitle("Monday");
		if(dow != 1) {
			new QueryDb().execute(1);
		}
	}
	
	public void tue(View v) {
		
		setTitle("Tuesday");
		if(dow != 2) {
			new QueryDb().execute(2);
		}
	}
	
	public void wed(View v) {
		setTitle("Wednesday");
		if(dow != 3) {
			new QueryDb().execute(3);
		}
	}
	
	public void thu(View v) {
		setTitle("Thursday");
		if(dow != 4) {
			new QueryDb().execute(4);
		}
	}
	
	public void fri(View v) {
		setTitle("Friday");
		if(dow != 5) {
			new QueryDb().execute(5);
		}
	}
	
	class QueryDb extends AsyncTask<Integer, Void, Void> {
		 
        @Override
        protected Void doInBackground(Integer... objects) {
        	
        	
        	
        	int dayofweek = objects[0];
        	dow = dayofweek;
        	/*
        	switch(dayofweek) {
        	case 1:
        		dayofweek = 1; break;
        	case 2:
        		dayofweek = 1; break;
        	case 3:
        		dayofweek = 2; break;
        	case 4:
        		dayofweek = 3; break;
        	case 5:
        		dayofweek = 4; break;
        	case 6:
        		dayofweek = 5; break;
        	case 7:
        		dayofweek = 1; break;   
        	default:
        		dayofweek = 1; break;
        	}*/
    		
        	today.clear();
        	Calendar cal_now = Calendar.getInstance();
        	
        	Log.i("dayofweek", ""+dayofweek);
        	db = mDbHelper.getWritableDatabase();
        	
        	Cursor cursor = db.query(TimeTableDbHelper.TABLE_NAME_TIMETABLE, TimeTableDbHelper.allColumns_mod, "id = " + dayofweek, null, null, null, null);
        	cursor.moveToFirst();
        	Log.i("fetched", cursor.getCount() + "of rows and " + cursor.getColumnCount() + "  of columns");
        	
        	for(int i=1; i<cursor.getColumnCount(); i++) {
        		Log.i("data", cursor.getString(i));
        		if(cursor.getString(i).equalsIgnoreCase("nil")) {
        			today.add("...");
        		} else {
        		    today.add(cursor.getString(i));
        		}
        	}
        	
        	
        	return null;
        }
        
        @Override
        protected void onPostExecute(final Void unused){
        	
            //update UI with my objects
        	CustomEditAdapter adapter= new CustomEditAdapter(context, R.layout.list_view_component_edit, R.id.subject, today);        	
        	final ListView lv = (ListView) findViewById(R.id.editListView);
        	lv.setAdapter(adapter);
        	
        	
           
        }
    }
	
    

}
