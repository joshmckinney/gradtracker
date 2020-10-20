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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Calendar;
import java.util.Date;

import me.joshmckinney.gradtracker.R;
import me.joshmckinney.gradtracker.adapter.AssessmentAdapter;
import me.joshmckinney.gradtracker.adapter.MentorAdapter;
import me.joshmckinney.gradtracker.adapter.NoteAdapter;
import me.joshmckinney.gradtracker.database.TrackerRepository;
import me.joshmckinney.gradtracker.model.Assessment;
import me.joshmckinney.gradtracker.model.Course;
import me.joshmckinney.gradtracker.model.Note;
import me.joshmckinney.gradtracker.util.AlertReceiver;
import me.joshmckinney.gradtracker.util.DateFormatter;
import me.joshmckinney.gradtracker.util.NotificationHelper;
import me.joshmckinney.gradtracker.viewmodel.AssessmentViewModel;
import me.joshmckinney.gradtracker.viewmodel.CourseViewModel;
import me.joshmckinney.gradtracker.viewmodel.MentorViewModel;
import me.joshmckinney.gradtracker.viewmodel.NoteViewModel;

public class CourseDetail extends AppCompatActivity {
    public static final String EXTRA_COURSE_ID =
            "me.joshmckinney.gradtracker.EXTRA_COURSE_ID";
    public static final String EXTRA_COURSE_TITLE =
            "me.joshmckinney.gradtracker.EXTRA_COURSE_TITLE";
    public static final String EXTRA_COURSE_START_DATE =
            "me.joshmckinney.gradtracker.EXTRA_COURSE_START_DATE";
    public static final String EXTRA_COURSE_END_DATE =
            "me.joshmckinney.gradtracker.EXTRA_COURSE_END_DATE";
    public static final String EXTRA_COURSE_STATUS =
            "me.joshmckinney.gradtracker.EXTRA_COURSE_STATUS";

    private Course course;
    private CourseViewModel courseViewModel;
    private MentorViewModel mentorViewModel;
    private AssessmentViewModel assessmentViewModel;
    private NoteViewModel noteViewModel;
    private TextView textViewStartDate;
    private TextView textViewEndDate;
    private TextView textViewStatus;
    private String alarmType = "-1";
    private Calendar calendar;
    private Date startDate = new Date();
    private Date endDate = new Date();
    private String courseTitle;
    private int courseId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_detail);
        textViewStartDate = findViewById(R.id.text_view_start);
        textViewEndDate = findViewById(R.id.text_view_end);
        textViewStatus = findViewById(R.id.text_view_status);

        // Set and enable back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Intent intent = getIntent();
        course = (Course) intent.getSerializableExtra("Course");
        courseId = course.getCourseId();
        courseTitle = course.getCourseName();
        setTitle(courseTitle);
        textViewStatus.setText(course.getStatus());
        startDate = course.getStartDate();
        endDate = course.getEndDate();
        textViewStartDate.setText(DateFormatter.toDate(startDate));
        textViewEndDate.setText(DateFormatter.toDate(endDate));

        calendar = Calendar.getInstance();
        // then use -- calendar.setTime(date);

        TrackerRepository.setCourseId(courseId);

        RecyclerView recyclerViewMentor = findViewById(R.id.recycler_view_mentor);
        recyclerViewMentor.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewMentor.setHasFixedSize(true);

        RecyclerView recyclerViewAssessment = findViewById(R.id.recycler_view_assessment);
        recyclerViewAssessment.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerViewAssessment.setHasFixedSize(true);

        RecyclerView recyclerViewNote = findViewById(R.id.recycler_view_note);
        recyclerViewNote.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewNote.setHasFixedSize(true);

        final MentorAdapter mentorAdapter = new MentorAdapter();
        recyclerViewMentor.setAdapter(mentorAdapter);
        courseViewModel = ViewModelProviders.of(this).get(CourseViewModel.class);
        mentorViewModel = ViewModelProviders.of(this).get(MentorViewModel.class);
        mentorViewModel.getCourseMentors(courseId).observe(this, mentors -> {
            mentorAdapter.submitList(mentors);
        });

        final AssessmentAdapter assessmentAdapter = new AssessmentAdapter();
        recyclerViewAssessment.setAdapter(assessmentAdapter);
        assessmentViewModel = ViewModelProviders.of(this).get(AssessmentViewModel.class);
        assessmentViewModel.getCourseAssessments(courseId).observe(this, assessments -> {
            assessmentAdapter.submitList(assessments);
        });

        final NoteAdapter noteAdapter = new NoteAdapter();
        recyclerViewNote.setAdapter(noteAdapter);
        noteViewModel = ViewModelProviders.of(this).get(NoteViewModel.class);
        noteViewModel.getCourseNotes(courseId).observe(this, notes -> {
            noteAdapter.submitList(notes);
        });

        assessmentAdapter.setOnItemClickListener(new AssessmentAdapter.onItemClickListener() {
            @Override
            public void onItemClick(Assessment assessment) {
                Intent assessmentDetailIntent = new Intent(CourseDetail.this, AssessmentDetail.class);
                assessmentDetailIntent.putExtra("Assessment", assessment);
                startActivity(assessmentDetailIntent);
            }
        });
        noteAdapter.setOnItemClickListener(new NoteAdapter.onItemClickListener() {
            @Override
            public void onItemClick(Note note) {
                sendMail(note.getNoteString());
            }
        });

    }

    private void sendMail(String note) {
        String subject = "Course Notes - " + courseTitle;
        String message = note;
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, message);
        intent.setType("message/rfc822");
        startActivity(Intent.createChooser(intent, "Choose an email client"));
    }

    private void setStartAlarm(Calendar c) {
        // Set alarm type to start date
        alarmType = "1";
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent alarmIntent = new Intent(this, AlertReceiver.class);
        // Put intents BEFORE calling pendingIntent
        alarmIntent.putExtra(NotificationHelper.EXTRA_ALARM_TITLE, courseTitle);
        alarmIntent.putExtra(NotificationHelper.EXTRA_ALARM_TYPE, alarmType);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, courseId+1000, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        if (c.after(Calendar.getInstance())) {
            c.add(Calendar.DATE, -1);
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);
            Toast.makeText(this, "Alarm Set", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Alarm not set! Check if future date.", Toast.LENGTH_LONG).show();
        }
    }

    private void cancelStartAlarm() {
        // Set alarm type to start date
        alarmType = "1";
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent alarmIntent = new Intent(this, AlertReceiver.class);
        // Put intents BEFORE calling pendingIntent
        alarmIntent.putExtra(NotificationHelper.EXTRA_ALARM_TITLE, courseTitle);
        alarmIntent.putExtra(NotificationHelper.EXTRA_ALARM_TYPE, alarmType);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, courseId+1000, alarmIntent, 0);
        alarmManager.cancel(pendingIntent);
        Toast.makeText(this, "Alarm Canceled", Toast.LENGTH_SHORT).show();
    }

    private void setEndAlarm(Calendar c) {
        // Set alarm type to end date
        alarmType = "2";
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent alarmIntent = new Intent(this, AlertReceiver.class);
        // Put intents BEFORE calling pendingIntent
        alarmIntent.putExtra(NotificationHelper.EXTRA_ALARM_TITLE, courseTitle);
        alarmIntent.putExtra(NotificationHelper.EXTRA_ALARM_TYPE, alarmType);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, courseId+1001, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        if (c.after(Calendar.getInstance())) {
            c.add(Calendar.DATE, -1);
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);
            Toast.makeText(this, "Alarm Set", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Alarm not set! Check if future date.", Toast.LENGTH_LONG).show();
        }
    }

    private void cancelEndAlarm() {
        // Set alarm type to end date
        alarmType = "2";
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent alarmIntent = new Intent(this, AlertReceiver.class);
        // Put intents BEFORE calling pendingIntent
        alarmIntent.putExtra(NotificationHelper.EXTRA_ALARM_TITLE, courseTitle);
        alarmIntent.putExtra(NotificationHelper.EXTRA_ALARM_TYPE, alarmType);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, courseId+1001, alarmIntent, 0);
        alarmManager.cancel(pendingIntent);
        Toast.makeText(this, "Alarm Canceled", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.detail_course_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        switch (item.getItemId()) {
            case R.id.set_start_alarm:
                calendar.setTime(startDate);
                setStartAlarm(calendar);
                return true;
            case R.id.cancel_start_alarm:
                calendar.setTime(startDate);
                cancelStartAlarm();
                return true;
            case R.id.set_end_alarm:
                calendar.setTime(endDate);
                setEndAlarm(calendar);
                return true;
            case R.id.cancel_end_alarm:
                calendar.setTime(endDate);
                cancelEndAlarm();
                return true;
            case R.id.delete_course:
            case R.id.delete_term:
                new AlertDialog.Builder(CourseDetail.this)
                        .setTitle("Delete Term")
                        .setMessage("Are you sure you want to delete '" + courseTitle + "'?")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                Toast.makeText(CourseDetail.this, "Course Deleted", Toast.LENGTH_SHORT).show();
                                courseViewModel.delete(course);
                                finish();
                            }
                        })
                        .setNegativeButton(android.R.string.no, null).show();
        }
        return super.onOptionsItemSelected(item);
    }
}