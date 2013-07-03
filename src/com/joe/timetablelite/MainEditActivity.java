package com.joe.timetablelite;

import java.util.ArrayList;
import java.util.Calendar;

import android.app.Activity;
import android.content.ContentValues;
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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MainEditActivity extends Activity {
	TimeTableDbHelper mDbHelper;
	Context  context = this;
	SQLiteDatabase db; 
	ListView listView;
	ArrayList<String> today = new ArrayList<String>();
	int dow;
	int noOfRows;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_edit);
		mDbHelper =  new TimeTableDbHelper(context);
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
    	
    	if(getIntent().getExtras().getInt("intention") == 0) {
			int NoOfRows = getIntent().getExtras().getInt("noOfPeriods");
			noOfRows = NoOfRows;
			Constants.noOfPeriods = NoOfRows;
			Initdatabase init = new Initdatabase();
			init.execute();
		}
    	else {
    		new QueryDb().execute(dayofweek);
    		new UpdateAuto().execute();
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
        	//Calendar cal_now = Calendar.getInstance();
        	
        	Log.i("dayofweek", ""+dayofweek);
        	db = mDbHelper.getWritableDatabase();
        	
        	Cursor cursor = db.query(TimeTableDbHelper.TABLE_NAME_TIMETABLE, TimeTableDbHelper.allColumns_mod, "id = " + dayofweek, null, null, null, null);
        	cursor.moveToFirst();
        	Log.i("fetched", cursor.getCount() + "of rows and " + cursor.getColumnCount() + "  of columns");
        	
        	for(int i=1; i<cursor.getColumnCount(); i++) {
        		Log.i("data", cursor.getString(i));
        		if(cursor.getString(i).equalsIgnoreCase("nil")) {
        			today.add("+");
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
        	listView = lv;
        	lv.setOnItemClickListener(new OnItemClickListener(){
    			public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
    				ArrayAdapter<String> adapter1 = (ArrayAdapter<String>) lv.getAdapter();
    				Intent i = new Intent(context, AutoSuggest.class);
    				i.putExtra("period-id", position);
    				i.putExtra("day-id", dow);
    				startActivityForResult(i, 1);
    			}
    			
    		});
        	
        	
           
        }
    }
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		  if (requestCode == 1) {

		     if(resultCode == RESULT_OK){      
		         String result=data.getStringExtra("result");   
		         int periodid = data.getExtras().getInt("period-id");
		         ArrayAdapter<String> adapter2 = (ArrayAdapter<String>) listView.getAdapter();		         
		         today.set(periodid, result);		         
		         adapter2.notifyDataSetChanged();
		         
		     }
		     if (resultCode == RESULT_CANCELED) {    
		         //Write your code if there's no result
		     }
		  }
		}//onActivityResult
	
	private class UpdateDb extends AsyncTask<Integer, Void, Void> {
		
		int updateday;
		
        @Override
        protected Void doInBackground(Integer... objects) {
        	
        	updateday = (Integer) objects[0];
        	db = mDbHelper.getWritableDatabase();
        	//ContentValues values = new ContentValues();
        	ContentValues values= new ContentValues();
			
        	for(int i=0; i < today.size(); i++) {
        		if(today.get(i).equalsIgnoreCase("+++")) {
        			values.put("period"+(i+1), "nil");	
        		}
        		else {
        			values.put("period"+(i+1), today.get(i));
        		}
        	}
        	Log.i("dow", updateday+"");
        	db.update(TimeTableDbHelper.TABLE_NAME_TIMETABLE, values, "id = " + dow, null);
        	Log.i("dow", updateday+"");
        	return null;
        }

        
        @Override
        protected void onPostExecute(final Void unused){
            //update UI with my objects
        	new QueryDb().execute(updateday);
           
        }
    }
	
	private class Initdatabase extends AsyncTask<Void, Void, Void> {
		 
        @Override
        protected Void doInBackground(Void... objects) {
        	
        	db = mDbHelper.getWritableDatabase();
        	TimeTableDbHelper.inittable(db, noOfRows);
        	return null;
        }

        @Override
        protected void onPostExecute(final Void unused){
            //update UI with my objects
        	new QueryDb().execute(1);
        }
    }
	
	private class UpdateAuto extends AsyncTask<Void, Void, Void> {
		 
        @Override
        protected Void doInBackground(Void... objects) {
        	Constants.getSuggestions();       	
        	return null;
        }

        @Override
        protected void onPostExecute(final Void unused){
            //update UI with my objects
        }
    }
	
	public void update_db() {
    	db = mDbHelper.getWritableDatabase();
    	//ContentValues values = new ContentValues();
    	ContentValues values= new ContentValues();
		
    	for(int i=0; i < today.size(); i++) {
    		if(today.get(i).equalsIgnoreCase("+++")) {
    			values.put("period"+(i+1), "nil");	
    		}
    		else {
    			values.put("period"+(i+1), today.get(i));
    		}
    	}
    	db.update(TimeTableDbHelper.TABLE_NAME_TIMETABLE, values, "id = " + dow, null);
    }
    
	
	public void mon(View v) {
		setTitle("Monday");
		if(dow != 1) {
			new UpdateDb().execute(1);
			
		}
	}
	
	public void tue(View v) {
		
		setTitle("Tuesday");
		if(dow != 2) {
			new UpdateDb().execute(2);
		}
	}
	
	public void wed(View v) {
		setTitle("Wednesday");
		if(dow != 3) {
			new UpdateDb().execute(3);
		}
	}
	
	public void thu(View v) {
		setTitle("Thursday");
		if(dow != 4) {
			new UpdateDb().execute(4);
		}
	}
	
	public void fri(View v) {
		setTitle("Friday");
		if(dow != 5) {
			new UpdateDb().execute(5);
		}
	}
	

	
		@Override
		public boolean onCreateOptionsMenu(Menu menu) {
			// Inflate the menu; this adds items to the action bar if it is present.
			if(getIntent().getExtras().getInt("intention") == 1) {
				getMenuInflater().inflate(R.menu.main_edit, menu);
			}
			else {
				getMenuInflater().inflate(R.menu.secondary_edit, menu);
			}
			return true;
		}
	
	
	
		@Override
		public boolean onOptionsItemSelected(MenuItem item) {
			// Handle item selection
			if(getIntent().getExtras().getInt("intention") == 1) {
			switch (item.getItemId()) {
	        	case R.id.exit:
	        		update_db();
	        		super.finish();
	         	   Intent intent = new Intent(this, LaunchActivity.class);
	         	   startActivity(intent);
	         	   return true;
	     	   case R.id.changeslots:
	     		   super.finish();
	     		   Intent i = new Intent(this, EditTimeTable.class);
	        		startActivity(i);
	        		
	     	  default:
		       		 return super.onOptionsItemSelected(item);
				}
			} else {
				switch (item.getItemId()) {	
	     	    case R.id.done:
	     		   super.finish();
	     		   Intent done = new Intent(this, DoneActivity.class);
	     		   done.putExtra("from", 1);
	     		   startActivity(done);
	     	    default:
	       		   return super.onOptionsItemSelected(item);
			}
		  }
		}
	

}
