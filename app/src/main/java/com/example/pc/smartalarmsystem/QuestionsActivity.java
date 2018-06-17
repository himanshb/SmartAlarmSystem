package com.example.pc.smartalarmsystem;

import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.DateUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class QuestionsActivity extends AppCompatActivity implements View.OnClickListener {

    FloatingActionButton fab,snoozFab;

    DatabaseAccess myDatabase;
    RadioGroup radioGroup;
    RadioButton selectedRadioButton,radioButton1,radioButton2,radioButton3,radioButton4;
    TextView textView;

    Ringtone ringtone;
    Vibrator vibrator;

    LevelDatabase levelDatabase;
    List<Question> questions;
    Question ques;
    String level;

    long alarmId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON| WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        setContentView(R.layout.activity_questions);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        alarmId=this.getIntent().getExtras().getLong("alarm_id");

        vibrator= (Vibrator) this.getSystemService(VIBRATOR_SERVICE);
        Uri uri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        ringtone = RingtoneManager.getRingtone(this,uri);
        ringtone.play();
        // Start without a delay
        // Each element then alternates between vibrate, sleep, vibrate, sleep...
        long[] pattern = {0, 100, 1000, 300, 200, 100, 500, 200, 100};

        // The '-1' here means to vibrate once, as '-1' is out of bounds in the pattern array
        vibrator.vibrate(pattern, 0);

        radioGroup=(RadioGroup)this.findViewById(R.id.radioGroup);
        radioButton1=(RadioButton)findViewById(R.id.radioButton);
        radioButton2=(RadioButton)findViewById(R.id.radioButton2);
        radioButton3=(RadioButton)findViewById(R.id.radioButton3);
        radioButton4=(RadioButton)findViewById(R.id.radioButton4);
        textView=(TextView)findViewById(R.id.textViewQues);

        levelDatabase=new LevelDatabase(this);
        //levelDatabase.insert();
        level =levelDatabase.getLevel();
        levelDatabase.close();

        myDatabase=new DatabaseAccess(this);
        myDatabase.open();
        questions=myDatabase.getQuestions(level);
        Collections.shuffle(questions);
        ques=questions.get(0);
        textView.setText(ques.getQuestion());
        List<String> options=ques.getOptions();
        radioButton1.setText(options.get(0));
        radioButton2.setText(options.get(1));
        radioButton3.setText(options.get(2));
        radioButton4.setText(options.get(3));

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(this);

        snoozFab = (FloatingActionButton) findViewById(R.id.fab_snooz);
        snoozFab.setOnClickListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (myDatabase!=null){
            myDatabase.close();
        }
    }

    @Override
    public void onClick(View v) {
        if (v==fab){
            int selectedId = radioGroup.getCheckedRadioButtonId();
            selectedRadioButton = (RadioButton) findViewById(selectedId);
            if (selectedRadioButton != null) {
                boolean check = myDatabase.checkAns(ques.getQuestion(), selectedRadioButton.getText().toString());
                if (check) {
                    vibrator.cancel();
                    ringtone.stop();
                    finish();
                    Toast.makeText(QuestionsActivity.this, "Correct answer", Toast.LENGTH_SHORT).show();
                } else{
                    questions=myDatabase.getQuestions(level);
                    Collections.shuffle(questions);
                    ques=questions.get(0);
                    textView.setText(ques.getQuestion());
                    List<String> options=ques.getOptions();
                    radioButton1.setText(options.get(0));
                    radioButton2.setText(options.get(1));
                    radioButton3.setText(options.get(2));
                    radioButton4.setText(options.get(3));
                    Toast.makeText(QuestionsActivity.this, "wrong answer", Toast.LENGTH_SHORT).show();
                }
            }
            else
                Toast.makeText(this, "Please Select Any Of The Above!", Toast.LENGTH_SHORT).show();
        }
        else if (v==snoozFab){
            int selectedId = radioGroup.getCheckedRadioButtonId();
            selectedRadioButton = (RadioButton) findViewById(selectedId);
            if (selectedRadioButton != null) {
                boolean check = myDatabase.checkAns(ques.getQuestion(), selectedRadioButton.getText().toString());
                if (check) {
                    vibrator.cancel();
                    ringtone.stop();

                    long currentTimeMillis = System.currentTimeMillis();
                    long nextUpdateTimeMillis = currentTimeMillis + 10 * DateUtils.MINUTE_IN_MILLIS;
                    Calendar snoozCal=Calendar.getInstance();
                    snoozCal.setTime(new Date(nextUpdateTimeMillis));
                    AlarmManager manager= (AlarmManager) this.getSystemService(ALARM_SERVICE);
                    Intent intent = new Intent(this, AlarmBroadcastReceiver.class);
                    intent.putExtra("alarm_id",alarmId);
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(this, (int) alarmId, intent, PendingIntent.FLAG_ONE_SHOT);
                    manager.setExact(AlarmManager.RTC_WAKEUP, snoozCal.getTimeInMillis(),pendingIntent);
                    Toast.makeText(this, "Alarm will be snoozed for 10 minutes", Toast.LENGTH_SHORT).show();
                    finish();
                    Toast.makeText(QuestionsActivity.this, "Correct answer", Toast.LENGTH_SHORT).show();
                } else{
                    questions=myDatabase.getQuestions(level);
                    Collections.shuffle(questions);
                    ques=questions.get(0);
                    textView.setText(ques.getQuestion());
                    List<String> options=ques.getOptions();
                    radioButton1.setText(options.get(0));
                    radioButton2.setText(options.get(1));
                    radioButton3.setText(options.get(2));
                    radioButton4.setText(options.get(3));
                    Toast.makeText(QuestionsActivity.this, "wrong answer", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(this, "Back is disabled please attempt the question!!", Toast.LENGTH_SHORT).show();
        //super.onBackPressed();
    }

    //This for disabling the recent panel button
    @Override
    protected void onPause() {
        super.onPause();

        ActivityManager activityManager = (ActivityManager) getApplicationContext()
                .getSystemService(ACTIVITY_SERVICE);

        activityManager.moveTaskToFront(getTaskId(), 0);
        Toast.makeText(this, "Recent button is disabled please attempt the question!!", Toast.LENGTH_SHORT).show();
    }
}
