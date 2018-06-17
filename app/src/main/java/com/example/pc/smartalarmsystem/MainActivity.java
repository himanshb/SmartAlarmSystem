package com.example.pc.smartalarmsystem;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    AlarmManager manager;
    PendingIntent pendingIntent;
    TimePicker timePicker;
    Calendar calendar;
    LevelDatabase levelDatabase;
    AlarmDatabase alarmDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        manager=(AlarmManager)this.getSystemService(ALARM_SERVICE);
        timePicker=(TimePicker)this.findViewById(R.id.timePicker);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.floatingActionButton2);
        fab.setOnClickListener(this);
        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                calendar=Calendar.getInstance();
                calendar.set(Calendar.HOUR_OF_DAY,hourOfDay);
                calendar.set(Calendar.MINUTE,minute);
            }
        });
        levelDatabase=new LevelDatabase(this);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.id_level_Easy) {
            boolean check=levelDatabase.updateTable("easy");
            if (check)
                Toast.makeText(this, "Level Easy Selected!", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(this, "Failed!!", Toast.LENGTH_SHORT).show();
            return true;
        }
        if (id == R.id.id_level_Medium) {
            boolean check=levelDatabase.updateTable("medium");
            if (check)
                Toast.makeText(this, "Level Medium Selected!", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(this, "Failed!!", Toast.LENGTH_SHORT).show();
            return true;
        }
        if (id == R.id.id_level_Hard) {
            boolean check=levelDatabase.updateTable("hard");
            if (check)
                Toast.makeText(this, "Level Hard Selected!", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(this, "Failed!!", Toast.LENGTH_SHORT).show();
            return true;
        }
        if (id == R.id.id_cancelAlarm) {
            startActivity(new Intent(this,AlarmsActivity.class));
            return true;
        }
        if (id == R.id.id_reminder) {
            startActivity(new Intent(this,ReminderActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        int flag=0;
        if (calendar!=null) {
        if(calendar.getTimeInMillis()<(System.currentTimeMillis()-5000)){
            long updatedTime=calendar.getTimeInMillis()+86400000;
            calendar.setTimeInMillis(updatedTime);
            flag=1;
        }
           // Toast.makeText(this, "system= "+systemTime, Toast.LENGTH_SHORT).show();
            alarmDatabase=new AlarmDatabase(this);

            final long alarmID=calendar.getTimeInMillis();
            //String timeStamp = new SimpleDateFormat("hh_mm_ss", Locale.getDefault()).format(calendar.getTimeInMillis());
            String timeStamp=timeConversion(alarmID);
            alarmDatabase.insertAlarm(Long.toString(alarmID),timeStamp);
            //Toast.makeText(this, timeStamp, Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, AlarmBroadcastReceiver.class);
            intent.putExtra("alarm_id",alarmID);
            pendingIntent = PendingIntent.getBroadcast(this, (int) alarmID, intent, PendingIntent.FLAG_ONE_SHOT);
            manager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),pendingIntent);
            //manager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 86400000, pendingIntent);

            if (flag==1){
                Toast.makeText(this, "Alarm will ring at "+timeStamp+" tomorrow", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(this, "Alarm will ring at "+timeStamp, Toast.LENGTH_SHORT).show();
            }
        }
        else {
            Toast.makeText(this, "Set Time First", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (alarmDatabase!=null){
            alarmDatabase.close();
        }
        if(levelDatabase!=null){
            levelDatabase.close();
        }
    }

      String timeConversion(long time){
        Date d = new Date(time);
        Calendar c = Calendar.getInstance();
        c.setTime(d);
        int hour=c.get(Calendar.HOUR_OF_DAY);
        int minute=c.get(Calendar.MINUTE);
          if (hour>12){
              return new String(hour-12 +" : "+minute+" pm");
          }
        return new String(hour +" : "+minute+" am");
    }

    boolean doubleBackToExitPressedOnce = false;
    @Override
    public void onBackPressed() {

            //Press Back again to exit
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                return;
            }
            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Please press BACK again to exit", Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 2000);
        }

}
