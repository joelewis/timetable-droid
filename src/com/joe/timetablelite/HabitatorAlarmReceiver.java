package com.joe.timetablelite;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class HabitatorAlarmReceiver extends BroadcastReceiver {

   // onReceive must be very quick and not block, so it just fires up a Service
   @Override
   public void onReceive(Context context, Intent intent) {
      Log.i(Constants.LOG_TAG, "DealAlarmReceiver invoked, starting DealService in background");
      Log.i("AlarmReceiver", " ");

      context.startService(new Intent(context, NotificationService.class));
   }
}
