package com.joe.timetablelite;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.util.Log;

public class HabitatorBootReceiver extends BroadcastReceiver {

   @Override
   public void onReceive(Context context, Intent intent) {
      Log.i(Constants.LOG_TAG, "DealBootReceiver invoked, configuring AlarmManager");
      AlarmManager alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
      PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, new Intent(context, HabitatorAlarmReceiver.class), 0);
      Log.i("bootReceiver", " ");
      // use inexact repeating which is easier on battery (system can phase events and not wake at exact times)
      Date dat  = new Date();//initializes to now
      Calendar cal_alarm = Calendar.getInstance();
      Calendar cal_now = Calendar.getInstance();
      cal_now.setTime(dat);
      cal_alarm.setTime(dat);
      //cal_alarm.setTimeZone(TimeZone.getTimeZone("GMT"));
      cal_alarm.set(Calendar.HOUR_OF_DAY, Constants.hourOfDay);//set the alarm time
      cal_alarm.set(Calendar.MINUTE, Constants.minutes);
      cal_alarm.set(Calendar.SECOND, Constants.seconds);
      if(cal_alarm.before(cal_now)){//if its in the past increment
          cal_alarm.add(Calendar.DATE,1);
    	  Log.i("","cal_alarm is in past");
      }
      alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, cal_alarm.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
      Log.i("alarm", "alarm set "+ (SystemClock.elapsedRealtime()+cal_now.getTimeInMillis()));
   }
}
