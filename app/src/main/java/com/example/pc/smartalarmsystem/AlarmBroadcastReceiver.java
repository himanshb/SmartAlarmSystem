package com.example.pc.smartalarmsystem;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by sparsh on 07-Apr-18.
 */

public class AlarmBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        long alarmId=intent.getExtras().getLong("alarm_id");
        Intent intent1=new Intent(context,QuestionsActivity.class);
        intent1.putExtra("alarm_id",alarmId);
        intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent1);
    }
}
