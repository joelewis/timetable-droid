package com.joe.timetablelite;

import java.util.Calendar;
import java.util.Date;



import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;

public class DoneActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_done);
		scheduleAlarmReceiver();
	}
	
	private void scheduleAlarmReceiver() {
	      AlarmManager alarmMgr = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
	      PendingIntent pendingIntent =  PendingIntent.getBroadcast(this, 0, new Intent(this, HabitatorAlarmReceiver.class),
		                        PendingIntent.FLAG_CANCEL_CURRENT);
	      // Use inexact repeating which is easier on battery (system can phase events and not wake at exact times)
	     
	      
	    /*  Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.add(Calendar.SECOND, 10);
        alarmMgr.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);  */
	      Date dat  = new Date();//initializes to now
	      Calendar cal_alarm = Calendar.getInstance();
	      Calendar cal_now = Calendar.getInstance();
	      cal_now.setTime(dat);
	      cal_alarm.setTime(dat);
	      //cal_alarm.setTimeZone(TimeZone.getTimeZone("GMT"));
	      cal_alarm.set(Calendar.HOUR_OF_DAY,Constants.hourOfDay);//set the alarm time
	      cal_alarm.set(Calendar.MINUTE, Constants.minutes);
	      cal_alarm.set(Calendar.SECOND, Constants.seconds);
	      if(cal_alarm.before(cal_now)){//if its in the past increment
	          cal_alarm.add(Calendar.DATE,1);
	    	  Log.i("","cal_alarm is in past");
	    	  
	      }
	      alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, cal_alarm.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
	      Log.i("alarm", "alarm set@ "+ "now hour|day: "+ cal_now.get(Calendar.HOUR_OF_DAY)+"|"+ cal_now.get(Calendar.DATE) + "alarm hour|min|date: "+ cal_alarm.get(Calendar.HOUR_OF_DAY) +"|"+cal_alarm.get(Calendar.MINUTE) +"|"+cal_alarm.get(Calendar.DATE) );
	       
	}
	
	public void toExit(View v) {
		Constants.monday = null;
		Constants.tuesday = null;
	    Constants.wednesday = null;
		Constants.thursday = null;
		Constants.friday = null;
		Intent intent = new Intent(getApplicationContext(), LaunchActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intent.putExtra("EXIT", true);
		startActivity(intent);
		super.finish();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.done, menu);
		return true;
	}

}
