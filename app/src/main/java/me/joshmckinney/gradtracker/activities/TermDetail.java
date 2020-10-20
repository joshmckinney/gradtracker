package me.joshmckinney.gradtracker.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import me.joshmckinney.gradtracker.R;
import me.joshmckinney.gradtracker.adapter.CourseAdapter;
import me.joshmckinney.gradtracker.database.TrackerRepository;
import me.joshmckinney.gradtracker.model.Course;
import me.joshmckinney.gradtracker.model.Term;
import me.joshmckinney.gradtracker.util.DateFormatter;
import me.joshmckinney.gradtracker.viewmodel.CourseViewModel;
import me.joshmckinney.gradtracker.viewmodel.TermViewModel;

public class TermDetail extends AppCompatActivity {

    public static final String EXTRA_TERM_ID =
            "me.joshmckinney.gradtracker.EXTRA_TERM_ID";
    public static final String EXTRA_TERM_TITLE =
            "me.joshmckinney.gradtracker.EXTRA_TERM_TITLE";
    public static final String EXTRA_TERM_START_DATE =
            "me.joshmckinney.gradtracker.EXTRA_TERM_TITLE";
    public static final String EXTRA_TERM_END_DATE =
            "me.joshmckinney.gradtracker.EXTRA_TERM_TITLE";


    public static final int ADD_COURSE_REQUEST = 1;
    public static final int EDIT_COURSE_REQUEST = 2;

    private CourseViewModel courseViewModel;
    private TermViewModel termViewModel;
    private String termTitle;
    private CourseAdapter courseAdapter;
    private Term term;
    private boolean isEmpty = false;
    private TextView textViewStartDate;
    private TextView textViewEndDate;
    private Date startDate = new Date();
    private Date endDate = new Date();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_detail);
        textViewStartDate = findViewById(R.id.text_view_start);
        textViewEndDate = findViewById(R.id.text_view_end);
        // Set and enable back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        FloatingActionButton buttonAddCourse = findViewById(R.id.button_add_course);
        buttonAddCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TermDetail.this, CourseAddEdit.class);
                startActivityForResult(intent, ADD_COURSE_REQUEST);
            }
        });

        Intent intent = getIntent();
        term = (Term) intent.getSerializableExtra("Term");
        int termId = term.getTermId();
        TrackerRepository.setTermId(termId);
        termTitle = term.getTermName();
        setTitle(termTitle);
        startDate = term.getStartDate();
        endDate = term.getEndDate();
        textViewStartDate.setText(DateFormatter.toDate(startDate));
        textViewEndDate.setText(DateFormatter.toDate(endDate));

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        courseAdapter = new CourseAdapter();
        recyclerView.setAdapter(courseAdapter);

        termViewModel = ViewModelProviders.of(this).get(TermViewModel.class);
        courseViewModel = ViewModelProviders.of(this).get(CourseViewModel.class);
        courseViewModel.getTermCourses(termId).observe(this, courses -> {
            courseAdapter.submitList(courses);
            if(courseAdapter.getItemCount() == 0) {
                isEmpty = true;
                AlertDialog alertDialog = new AlertDialog.Builder(this).create();
                alertDialog.setIcon(android.R.drawable.ic_dialog_alert);
                alertDialog.setTitle("No Courses Found");
                alertDialog.setMessage("Please add a course to term '" + termTitle + "'.");
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
        });

        courseAdapter.setOnItemClickListener(new CourseAdapter.onItemClickListener() {
            @Override
            public void onItemClick(Course course) {
                Intent intent = new Intent(TermDetail.this, CourseDetail.class);
                intent.putExtra(CourseDetail.EXTRA_COURSE_ID, String.valueOf(course.getCourseId()));
                intent.putExtra(CourseDetail.EXTRA_COURSE_TITLE, course.getCourseName());
                intent.putExtra(CourseDetail.EXTRA_COURSE_STATUS, course.getStatus());
                intent.putExtra(CourseDetail.EXTRA_COURSE_START_DATE, course.getStartDate().getTime());
                intent.putExtra(CourseDetail.EXTRA_COURSE_END_DATE, course.getEndDate().getTime());
                intent.putExtra("Course", course);
                startActivity(intent);
            }
        });

        courseAdapter.setOnLongClickListener(new CourseAdapter.onLongClickListener() {
            @Override
            public void onItemLongClick(Course course) {
                Intent intent = new Intent(TermDetail.this, CourseAddEdit.class);
                intent.putExtra(CourseAddEdit.EXTRA_COURSE_ID, String.valueOf(course.getCourseId()));
                intent.putExtra(CourseAddEdit.EXTRA_COURSE_TITLE, course.getCourseName());
                intent.putExtra(CourseAddEdit.EXTRA_COURSE_STATUS, course.getStatus());
                intent.putExtra(CourseAddEdit.EXTRA_COURSE_START_DATE, course.getStartDate().toString());
                intent.putExtra(CourseAddEdit.EXTRA_COURSE_END_DATE, course.getEndDate().toString());
                startActivityForResult(intent, EDIT_COURSE_REQUEST);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String title = null;
        String status = null;
        Date startDate = null;
        Date endDate = null;
        String termId = null;
        String mentorId;

        String myFormat = "MM/dd/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        if(requestCode == ADD_COURSE_REQUEST && resultCode == RESULT_OK) {
            title = data.getStringExtra(CourseAddEdit.EXTRA_COURSE_TITLE);
            status = data.getStringExtra(CourseAddEdit.EXTRA_COURSE_STATUS);
            String startDateString = data.getStringExtra(CourseAddEdit.EXTRA_COURSE_START_DATE);
            String endDateString = data.getStringExtra(CourseAddEdit.EXTRA_COURSE_END_DATE);
            termId = data.getStringExtra(CourseAddEdit.EXTRA_COURSE_TERM_ID);

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

            Course course = new Course(title,startDate,endDate,status,Integer.parseInt(termId));
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
            termId = data.getStringExtra(CourseAddEdit.EXTRA_COURSE_TERM_ID);

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

            Course course = new Course(title,startDate,endDate,status,Integer.parseInt(termId));
            course.setCourseId(id);
            courseViewModel.update(course);

            Toast.makeText(this,"Course Updated", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Course not saved!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.detail_term_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        switch (item.getItemId()) {
            case R.id.delete_term:
                if (isEmpty) {
                    new AlertDialog.Builder(TermDetail.this)
                            .setTitle("Delete Term")
                            .setMessage("Are you sure you want to delete '" + termTitle + "'?")
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    Toast.makeText(TermDetail.this, "Term Deleted", Toast.LENGTH_SHORT).show();
                                    termViewModel.delete(term);
                                    finish();
                                }
                            })
                            .setNegativeButton(android.R.string.no, null).show();
                } else {
                    AlertDialog alertDialog = new AlertDialog.Builder(TermDetail.this).create();
                    alertDialog.setIcon(android.R.drawable.ic_dialog_alert);
                    alertDialog.setTitle("Error Deleting");
                    alertDialog.setMessage("Term '" + termTitle + "' still has associated classes, please delete these first.");
                    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            alertDialog.dismiss();
                        }
                    });
                    alertDialog.show();
                    Button okButton = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                    LinearLayout.LayoutParams aboutLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
                    okButton.setLayoutParams(aboutLayoutParams);
                }
        }
        return super.onOptionsItemSelected(item);
    }
}