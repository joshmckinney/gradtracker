package me.joshmckinney.gradtracker.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import me.joshmckinney.gradtracker.R;
import me.joshmckinney.gradtracker.adapter.CourseAdapter;
import me.joshmckinney.gradtracker.model.Course;
import me.joshmckinney.gradtracker.viewmodel.CourseViewModel;

public class CourseList extends AppCompatActivity {
    public static final int ADD_COURSE_REQUEST = 1;
    public static final int EDIT_COURSE_REQUEST = 2;

    private CourseViewModel courseViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_list);
        setTitle("Courses");
        // Set and enable back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        FloatingActionButton buttonAddCourse = findViewById(R.id.button_add_course);
        buttonAddCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CourseList.this, CourseAddEdit.class);
                startActivityForResult(intent, ADD_COURSE_REQUEST);
            }
        });

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        final CourseAdapter adapter = new CourseAdapter();
        recyclerView.setAdapter(adapter);

        courseViewModel = ViewModelProviders.of(this).get(CourseViewModel.class);
        courseViewModel.getAllCourses().observe(this, new Observer<List<Course>>() {
            @Override
            public void onChanged(@Nullable List<Course> courses) {
                adapter.submitList(courses);
                // Wait for courses to populate after on changed and alert if empty -- 1000ms
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (adapter.getCurrentList().isEmpty()) {
                            AlertDialog alertDialog = new AlertDialog.Builder(CourseList.this).create();
                            alertDialog.setIcon(android.R.drawable.ic_dialog_alert);
                            alertDialog.setTitle("No Courses Found");
                            alertDialog.setMessage("Please add a course via courses.");
                            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface arg0, int arg1) {
                                }
                            });
                            alertDialog.show();
                            Button okButton = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                            LinearLayout.LayoutParams aboutLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
                            okButton.setLayoutParams(aboutLayoutParams);
                        }
                    }
                }, 1000);
            }
        });

        // Course item click loads course details
        adapter.setOnItemClickListener(new CourseAdapter.onItemClickListener() {
            @Override
            public void onItemClick(Course course) {
                Intent intent = new Intent(CourseList.this, CourseDetail.class);
                intent.putExtra(CourseDetail.EXTRA_COURSE_ID, String.valueOf(course.getCourseId()));
                intent.putExtra(CourseDetail.EXTRA_COURSE_TITLE, course.getCourseName());
                intent.putExtra(CourseDetail.EXTRA_COURSE_STATUS, course.getStatus());
                intent.putExtra(CourseDetail.EXTRA_COURSE_START_DATE, course.getStartDate().getTime());
                intent.putExtra(CourseDetail.EXTRA_COURSE_END_DATE, course.getEndDate().getTime());
                intent.putExtra("Course", course);
                startActivity(intent);
            }
        });
        // Course item long click loads course edit
        adapter.setOnLongClickListener(new CourseAdapter.onLongClickListener() {
            @Override
            public void onItemLongClick(Course course) {
                Intent intent = new Intent(CourseList.this, CourseAddEdit.class);
                intent.putExtra(CourseAddEdit.EXTRA_COURSE_ID, course.getCourseId());
                intent.putExtra(CourseAddEdit.EXTRA_COURSE_TITLE, course.getCourseName());
                intent.putExtra(CourseAddEdit.EXTRA_COURSE_STATUS, course.getStatus());
                intent.putExtra(CourseAddEdit.EXTRA_COURSE_START_DATE, course.getStartDate().getTime());
                intent.putExtra(CourseAddEdit.EXTRA_COURSE_END_DATE, course.getEndDate().getTime());
                intent.putExtra("Course", course);
                startActivityForResult(intent, EDIT_COURSE_REQUEST);
            }
        });

        View parentLayout = findViewById(android.R.id.content);
        Snackbar.make(parentLayout, "Click a course to view details or long click to edit.", Snackbar.LENGTH_SHORT)
                .setAction("Ok", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                })
                .show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String title = null;
        String status = null;
        Date startDate = null;
        Date endDate = null;
        int termId;
        String mentorId;

        String myFormat = "MM/dd/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        if(requestCode == ADD_COURSE_REQUEST && resultCode == RESULT_OK) {
            title = data.getStringExtra(CourseAddEdit.EXTRA_COURSE_TITLE);
            status = data.getStringExtra(CourseAddEdit.EXTRA_COURSE_STATUS);
            String startDateString = data.getStringExtra(CourseAddEdit.EXTRA_COURSE_START_DATE);
            String endDateString = data.getStringExtra(CourseAddEdit.EXTRA_COURSE_END_DATE);
            termId = data.getIntExtra(CourseAddEdit.EXTRA_COURSE_TERM_ID, -1);

            try {
                startDate = sdf.parse(startDateString);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            try {
                endDate = sdf.parse(endDateString);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            Course course = new Course(title,startDate,endDate,status,termId);
            CourseViewModel.insert(course);

            Toast.makeText(this, "Course saved", Toast.LENGTH_SHORT).show();
        } else if (requestCode == EDIT_COURSE_REQUEST && resultCode == RESULT_OK) {
            int id = data.getIntExtra(CourseAddEdit.EXTRA_COURSE_ID, -1);
            if (id == -1) {
                Toast.makeText(this, "Couldn't be updated", Toast.LENGTH_SHORT).show();
                return;
            }

            title = data.getStringExtra(CourseAddEdit.EXTRA_COURSE_TITLE);
            String startDateString = data.getStringExtra(CourseAddEdit.EXTRA_COURSE_START_DATE);
            String endDateString = data.getStringExtra(CourseAddEdit.EXTRA_COURSE_END_DATE);
            status = data.getStringExtra(CourseAddEdit.EXTRA_COURSE_STATUS);
            termId = data.getIntExtra(CourseAddEdit.EXTRA_COURSE_TERM_ID, -1);

            try {
                startDate = sdf.parse(startDateString);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            try {
                endDate = sdf.parse(endDateString);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            Course course = new Course(title,startDate,endDate,status,termId);
            course.setCourseId(id);
            courseViewModel.update(course);

            Toast.makeText(this,"Course Updated", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Course not saved!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
