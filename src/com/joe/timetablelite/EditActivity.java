package com.joe.timetablelite;

import java.util.ArrayList;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class EditActivity extends Activity {
	SQLiteDatabase db;
	TimeTableDbHelper mDbHelper;
	int noOfRows;
	Context context = this;
	ListView listView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_launch);
		mDbHelper =  new TimeTableDbHelper(context);
		int NoOfRows = getIntent().getExtras().getInt("noOfPeriods");
		noOfRows = NoOfRows;
		Constants.noOfPeriods = NoOfRows;
		Constants.monday = new ArrayList<String>();
		Initdatabase init = new Initdatabase();
		init.execute();
		
		
		
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
        	for(int i=0; i<Constants.noOfPeriods; i++) {
        		Constants.monday.add("+++");
        	}
        	CustomEditAdapter adapter= new CustomEditAdapter(context, R.layout.list_view_component_edit, R.id.subject, Constants.monday);
        	setContentView(R.layout.activity_edit);
        	final ListView lv = (ListView) findViewById(R.id.editListView);
        	lv.setAdapter(adapter);
        	listView = lv;
        	lv.setOnItemClickListener(new OnItemClickListener(){
    			public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
    		
    				ArrayAdapter<String> adapter1 = (ArrayAdapter<String>) lv.getAdapter();
    				Intent i = new Intent(context, AutoSuggest.class);
    				i.putExtra("period-id", position);
    				i.putExtra("day-id", 0);
    				startActivityForResult(i, 1);
    				
    				//adapter1.remove("+");
    				
    				//I don't exactly know how ArrayAdapter is used with List variable :(
    			 //   adapter1.remove(task);
    			    //lv.refreshDrawableState();
    				//adapter1.notifyDataSetChanged();
    			}
    			
    		});
        }
    }
	
	private class UpdateDb extends AsyncTask<Void, Void, Void> {
		 
        @Override
        protected Void doInBackground(Void... objects) {
        	
        	db = mDbHelper.getWritableDatabase();
        	//ContentValues values = new ContentValues();
        	ContentValues values= new ContentValues();
			
        	for(int i=0; i < Constants.monday.size(); i++) {
        		if(Constants.monday.get(i) != "+++") {
        		values.put("period"+(i+1), Constants.monday.get(i));
        		}
        		else {
        		values.put("period"+(i+1), "nil");	
        		}
        	}
        	db.update(TimeTableDbHelper.TABLE_NAME_TIMETABLE, values, "id = " + 1, null);
        	return null;
        }

        @Override
        protected void onPostExecute(final Void unused){
            //update UI with my objects
        	
           
        }
    }
	protected void onPause() {
		//new UpdateDb().execute();
		super.onPause();
		
	}
	
	public void goBack(View v) {
		super.finish();
	}
	
	public void saveThis(View v) {
		new UpdateDb().execute();
	}
	
	public void goNext(View v) {
		Intent intent = new Intent(this, EditActivityTuesday.class);
		startActivity(intent);
	}
	
	
	
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		  if (requestCode == 1) {

		     if(resultCode == RESULT_OK){      
		         String result=data.getStringExtra("result");   
		         int periodid = data.getExtras().getInt("period-id");
		         ArrayAdapter<String> adapter2 = (ArrayAdapter<String>) listView.getAdapter();
		         
		         Constants.monday.set(periodid, result);
		         
		         adapter2.notifyDataSetChanged();
		         
		     }
		     if (resultCode == RESULT_CANCELED) {    
		         //Write your code if there's no result
		     }
		  }
		}//onActivityResult
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.edit, menu);
		return true;
	}

}
