package com.joe.timetablelite;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

public class AutoSuggest extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_auto_suggest);
		// Get a reference to the AutoCompleteTextView in the layout
		AutoCompleteTextView textView = (AutoCompleteTextView) findViewById(R.id.autocomplete_subject);
		// Get the string array
		
		int dayid = getIntent().getExtras().getInt("day-id");
		ArrayList<String> suggestions = new ArrayList<String>();
		Log.i("value", " : "+dayid);
		switch(dayid) {
		case 0: 
			Log.i("into", "monday");
			suggestions = Constants.monday;break; 
		case 1: 
			 suggestions = Constants.tuesday;break;
		case 2: 
			 suggestions = Constants.wednesday;break;
		case 3: 
			 suggestions = Constants.thursday;break;
		case 4: 
			 suggestions = Constants.friday;break;
		}
		 
		// Create the adapter and set it to the AutoCompleteTextView 
		ArrayAdapter<String> adapter = 
		        new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, suggestions);
		textView.setAdapter(adapter);
	}
	
	public void returnresult(View v) {
		 Intent returnIntent = new Intent();
		 String result = "+++";
	     if(((AutoCompleteTextView) findViewById(R.id.autocomplete_subject)).getText().toString() != "" ) {
	    	 result = ((AutoCompleteTextView) findViewById(R.id.autocomplete_subject)).getText().toString();
	     }
	     int periodid = getIntent().getExtras().getInt("period-id");
		 returnIntent.putExtra("result", result);
		 returnIntent.putExtra("period-id", periodid);
		 setResult(RESULT_OK,returnIntent);     
		 finish();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.auto_suggest, menu);
		return true;
	}

}
