package com.example.pc.smartalarmsystem;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

public class AlarmsActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    ListView lvAlarms;
    AlarmDatabase alarmDatabase;
    List<String> alarms;
    ArrayAdapter<String> myAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarms);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        alarmDatabase=new AlarmDatabase(this);
        alarms=alarmDatabase.getAlarms();
        lvAlarms=(ListView)findViewById(R.id.listViewAlarms);
        myAdapter=new ArrayAdapter<>(this,R.layout.alarm_list_layout,R.id.textView2,alarms);
        lvAlarms.setAdapter(myAdapter);
        lvAlarms.setOnItemClickListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        alarmDatabase.close();
    }


    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        final AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setMessage("Do you want to delete this alarm?");
        final AdapterView<?> finalParent = parent;
        final int finalPosition = position;
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                function(finalParent, finalPosition);
            }
        });
        builder.setCancelable(false);
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }
    void function(AdapterView<?> parent, int position){
        String alarmTimeStamp=parent.getItemAtPosition(position).toString();
        AlarmManager manager=(AlarmManager)this.getSystemService(ALARM_SERVICE);
        String time=alarmDatabase.getAlarm(alarmTimeStamp);
        //Canceling alarm from AlarmManager.
        Intent intent = new Intent(this, AlarmBroadcastReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, (int)Long.parseLong(time), intent, PendingIntent.FLAG_ONE_SHOT);
        manager.cancel(pendingIntent);

        //delete alarm from DB.
        boolean check=alarmDatabase.deleteAlarm(alarmTimeStamp);
        if (check){
            alarms.remove(position);
            myAdapter.notifyDataSetChanged();
            Toast.makeText(this, "Alarm Deleted", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(this, "Something went wrong!!", Toast.LENGTH_SHORT).show();
        }
    }
}
