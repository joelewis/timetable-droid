package com.joe.timetablelite;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;



public class EditTimeTable extends Activity {
	Context context = this;
	TextView tvv;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_time_table);
		// Show the Up button in the action bar.
		setupActionBar();
		TextView tv = (TextView) findViewById(R.id.seekBarValue);
		TextView next = (TextView) findViewById(R.id.periods);
		tvv = tv;
		final TextView tv1 = tv;
		final TextView tv2 = next;
		//tv.setClickable(false);
		//tv.setBackgroundColor(color.background_light);
		SeekBar bar = (SeekBar) findViewById(R.id.seekBar1);
	
		bar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            int progressChanged = 0;
            
 
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser){
                progressChanged = progress;
            }
 
            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            	tv1.setText(progressChanged+"");
            }
 
            public void onStopTrackingTouch(SeekBar seekBar) {
                //Toast.makeText(context,"seek bar progress:"+progressChanged,
                //        Toast.LENGTH_SHORT).show();
            	if(progressChanged >= 4) {
            		tv2.setTextColor(0xff444444);
            		tv1.setText(progressChanged+"");
            		tv2.setClickable(true);
            	}
            	else {
            		tv2.setTextColor(0xff999999);
            		tv1.setText(progressChanged+"");
            		tv2.setClickable(true);
            		
            	}
            }
        });
		//Toast.makeText(context, bar.getProgress() + "", Toast.LENGTH_LONG).show();
		
	}
	
	
	
	public void toEditScreen(View v) {
		Intent intent = new Intent(context,MainEditActivity.class);
		String str = tvv.getText().toString();
		int val = Integer.parseInt(str);
		intent.putExtra("intention", 0);
		intent.putExtra("noOfPeriods", val);
		Log.i("value", "" + val);
		startActivity(intent);
	}

	/**
	 * Set up the {@link android.app.ActionBar}, if the API is available.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.edit_time_table, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
