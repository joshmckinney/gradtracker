package me.joshmckinney.gradtracker.activities;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import java.util.Calendar;
import java.util.Date;

import me.joshmckinney.gradtracker.R;
import me.joshmckinney.gradtracker.model.Assessment;
import me.joshmckinney.gradtracker.util.AlertReceiver;
import me.joshmckinney.gradtracker.util.DateFormatter;
import me.joshmckinney.gradtracker.util.NotificationHelper;
import me.joshmckinney.gradtracker.viewmodel.AssessmentViewModel;

public class AssessmentDetail extends AppCompatActivity {
    public static final String EXTRA_ASSESSMENT_ID =
            "me.joshmckinney.gradtracker.EXTRA_ASSESSMENT_ID";
    public static final String EXTRA_ASSESSMENT_COURSE_ID =
            "me.joshmckinney.gradtracker.EXTRA_ASSESSMENT_COURSE_ID";
    public static final String EXTRA_ASSESSMENT_TITLE =
            "me.joshmckinney.gradtracker.EXTRA_ASSESSMENT_TITLE";
    public static final String EXTRA_ASSESSMENT_TYPE =
            "me.joshmckinney.gradtracker.EXTRA_ASSESSMENT_TYPE";
    public static final String EXTRA_ASSESSMENT_GOAL =
            "me.joshmckinney.gradtracker.EXTRA_ASSESSMENT_GOAL";

    private Assessment assessment;
    private Date goalDate = new Date();
    private int assessmentId;
    private int assessmentType;
    private String alarmType = "0";
    private String assessmentTitle;
    private Calendar calendar;
    private AssessmentViewModel assessmentViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessment_detail);
        // Set and enable back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        assessmentViewModel = ViewModelProviders.of(this).get(AssessmentViewModel.class);

        TextView textViewGoalDate = findViewById(R.id.text_view_goal_date);
        TextView textViewType = findViewById(R.id.text_view_type);

        Intent intent = getIntent();
        assessment = (Assessment) intent.getSerializableExtra("Assessment");
        assessmentId = assessment.getAssessmentId();
        assessmentTitle = assessment.getAssessmentName();
        assessmentType = assessment.getAssessmentType();
        switch(assessmentType){
            case 0:
                textViewType.setText("Type: Objective");
                break;
            case 1:
                textViewType.setText("Type: Performance");
        }
        setTitle(assessmentTitle);
        goalDate = assessment.getGoalDate();
        textViewGoalDate.setText(DateFormatter.toDate(goalDate));

        calendar = Calendar.getInstance();
        calendar.setTime(goalDate);
    }

    private void startAlarm(Calendar c) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent alarmIntent = new Intent(this, AlertReceiver.class);
        // Put intents BEFORE calling pendingIntent
        alarmIntent.putExtra(NotificationHelper.EXTRA_ALARM_TITLE, assessmentTitle);
        alarmIntent.putExtra(NotificationHelper.EXTRA_ALARM_TYPE, alarmType);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, assessmentId, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        if (c.after(Calendar.getInstance())) {
            c.add(Calendar.DATE, -1);
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);
            Toast.makeText(this, "Alarm Set", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Alarm not set! Check if future date.", Toast.LENGTH_LONG).show();
        }
    }
    private void cancelAlarm() {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent alarmIntent = new Intent(AssessmentDetail.this, AlertReceiver.class);
        // Put intents BEFORE calling pendingIntent
        alarmIntent.putExtra(NotificationHelper.EXTRA_ALARM_TITLE, assessmentTitle);
        alarmIntent.putExtra(NotificationHelper.EXTRA_ALARM_TYPE, alarmType);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(AssessmentDetail.this, assessmentId, alarmIntent, 0);
        alarmManager.cancel(pendingIntent);
        Toast.makeText(this, "Alarm Canceled", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.detail_assessment_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        switch (item.getItemId()) {
            case R.id.set_alarm:
                startAlarm(calendar);
                return true;
            case R.id.cancel_alarm:
                cancelAlarm();
                return true;
            case R.id.delete_assessment:
                new AlertDialog.Builder(AssessmentDetail.this)
                        .setTitle("Delete Assessment")
                        .setMessage("Are you sure you want to delete '" + assessmentTitle + "'?")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                Toast.makeText(AssessmentDetail.this, "Assessment Deleted", Toast.LENGTH_SHORT).show();
                                assessmentViewModel.delete(assessment);
                                finish();
                            }
                        })
                        .setNegativeButton(android.R.string.no, null).show();
        }
        return super.onOptionsItemSelected(item);
    }
}