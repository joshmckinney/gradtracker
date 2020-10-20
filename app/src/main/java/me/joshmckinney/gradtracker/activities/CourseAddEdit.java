package me.joshmckinney.gradtracker.activities;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import me.joshmckinney.gradtracker.R;
import me.joshmckinney.gradtracker.adapter.AssessmentAdapter;
import me.joshmckinney.gradtracker.adapter.MentorAdapter;
import me.joshmckinney.gradtracker.adapter.NoteAdapter;
import me.joshmckinney.gradtracker.database.TrackerRepository;
import me.joshmckinney.gradtracker.model.Assessment;
import me.joshmckinney.gradtracker.model.Course;
import me.joshmckinney.gradtracker.model.Mentor;
import me.joshmckinney.gradtracker.model.Note;
import me.joshmckinney.gradtracker.model.Term;
import me.joshmckinney.gradtracker.util.DateFormatter;
import me.joshmckinney.gradtracker.viewmodel.AssessmentViewModel;
import me.joshmckinney.gradtracker.viewmodel.MentorViewModel;
import me.joshmckinney.gradtracker.viewmodel.NoteViewModel;
import me.joshmckinney.gradtracker.viewmodel.TermViewModel;

import static android.view.View.GONE;
import static me.joshmckinney.gradtracker.activities.AssessmentAddEdit.EXTRA_ASSESSMENT_GOAL;
import static me.joshmckinney.gradtracker.activities.AssessmentAddEdit.EXTRA_ASSESSMENT_TITLE;
import static me.joshmckinney.gradtracker.activities.AssessmentAddEdit.EXTRA_ASSESSMENT_TYPE;
import static me.joshmckinney.gradtracker.activities.MentorAddEdit.EXTRA_MENTOR_EMAIL;
import static me.joshmckinney.gradtracker.activities.MentorAddEdit.EXTRA_MENTOR_NAME;
import static me.joshmckinney.gradtracker.activities.MentorAddEdit.EXTRA_MENTOR_PHONE;

public class CourseAddEdit extends AppCompatActivity {
    public static final String EXTRA_COURSE_ID =
            "me.joshmckinney.gradtracker.EXTRA_COURSE_ID";
    public static final String EXTRA_COURSE_TITLE =
            "me.joshmckinney.gradtracker.EXTRA_COURSE_TITLE";
    public static final String EXTRA_COURSE_STATUS =
            "me.joshmckinney.gradtracker.EXTRA_COURSE_STATUS";
    public static final String EXTRA_COURSE_START_DATE =
            "me.joshmckinney.gradtracker.EXTRA_COURSE_START_DATE";
    public static final String EXTRA_COURSE_END_DATE =
            "me.joshmckinney.gradtracker.EXTRA_COURSE_END_DATE";
    public static final String EXTRA_COURSE_TERM_ID =
            "me.joshmckinney.gradtracker.EXTRA_COURSE_TERM_ID";
    private static final int ADD_MENTOR_REQUEST = 1;
    private static final int EDIT_MENTOR_REQUEST = 2;
    private static final int ADD_ASSESSMENT_REQUEST = 3;
    private static final int EDIT_ASSESSMENT_REQUEST = 4;


    final Calendar courseCalendar = Calendar.getInstance();
    private Course course;
    private int courseId;
    private String titleString;
    private String statusString;
    private String intentStatusString;
    private int termId;
    private TextView textViewMentors;
    private TextView textViewAssessments;
    private TextView textViewNotes;
    private ImageButton buttonAddMentor;
    private ImageButton buttonAddAssessment;
    private ImageButton buttonAddNote;
    private RecyclerView recyclerViewMentor;
    private RecyclerView recyclerViewAssessment;
    private RecyclerView recyclerViewNote;
    private EditText editTextTitle;
    private EditText editTextStartDate;
    private EditText editTextEndDate;
    private Spinner termItems;

    DatePickerDialog.OnDateSetListener startDateListener;
    DatePickerDialog.OnDateSetListener endDateListener;
    ImageButton startCalPicker;
    ImageButton endCalPicker;
    String myFormat = "MM/dd/yy";
    SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

    TermViewModel termViewModel;
    MentorViewModel mentorViewModel;
    AssessmentViewModel assessmentViewModel;
    NoteViewModel noteViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addedit_course);
        Intent addEditIntent = getIntent();
        // Enable close button for exiting without save
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        editTextTitle = findViewById(R.id.edit_text_course_title);
        editTextStartDate = findViewById(R.id.edit_text_start_date);
        editTextEndDate = findViewById(R.id.edit_text_end_date);
        startCalPicker = findViewById(R.id.startCalPicker);
        endCalPicker = findViewById(R.id.endCalPicker);
        textViewMentors = findViewById(R.id.text_view_mentor);
        textViewAssessments = findViewById(R.id.text_view_assessment);
        textViewNotes = findViewById(R.id.text_view_note);
        buttonAddMentor = findViewById(R.id.button_add_mentor);
        buttonAddAssessment = findViewById(R.id.button_add_assessment);
        buttonAddNote = findViewById(R.id.button_add_note);
        recyclerViewMentor = findViewById(R.id.recycler_view_mentor);
        recyclerViewAssessment = findViewById(R.id.recycler_view_assessment);
        recyclerViewNote = findViewById(R.id.recycler_view_note);

        List<String> statusArray = new ArrayList<String>();
        statusArray.add("Plan to take");
        statusArray.add("In Progress");
        statusArray.add("Completed");
        statusArray.add("Dropped");

        // Status Spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, statusArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner statusItems = (Spinner) findViewById(R.id.course_status_spinner);
        statusItems.setAdapter(adapter);
        statusItems.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                statusString = statusItems.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        // Term Spinner
        termItems = (Spinner) findViewById(R.id.course_term_spinner);
        List<String> termsList = new ArrayList<String>();
        List<Integer> termIdList = new ArrayList<Integer>();
        ArrayAdapter<String> termAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, termsList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        termViewModel = ViewModelProviders.of(this).get(TermViewModel.class);
        termItems.setAdapter(termAdapter);
        termViewModel.getAllTerms().observe(this, terms -> {
            termsList.clear();
            for (Term term : terms) {
                termsList.add(term.getTermName());
                termIdList.add(term.getTermId());
            }
            //notifyDataSetChanged after update termsList variable here
            termAdapter.notifyDataSetChanged();
        });
        termItems.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                termId = termIdList.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        if (addEditIntent.hasExtra(EXTRA_COURSE_TITLE)) {
            setTitle("Edit Course");
            courseId = addEditIntent.getIntExtra(EXTRA_COURSE_ID,-1);
            TrackerRepository.setCourseId(courseId);
            editTextTitle.setText(addEditIntent.getStringExtra(EXTRA_COURSE_TITLE));
            Date startDate = new Date();
            startDate.setTime(addEditIntent.getLongExtra(EXTRA_COURSE_START_DATE, -1));
            editTextStartDate.setText(DateFormatter.toDate(startDate));
            Date endDate = new Date();
            endDate.setTime(addEditIntent.getLongExtra(EXTRA_COURSE_END_DATE, -1));
            editTextEndDate.setText(DateFormatter.toDate(endDate));

            startDateListener = new DatePickerDialog.OnDateSetListener() {

                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear,
                                      int dayOfMonth) {
                    courseCalendar.set(Calendar.YEAR, year);
                    courseCalendar.set(Calendar.MONTH, monthOfYear);
                    courseCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    setStartDate();
                }

            };
            endDateListener = new DatePickerDialog.OnDateSetListener() {

                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear,
                                      int dayOfMonth) {
                    courseCalendar.set(Calendar.YEAR, year);
                    courseCalendar.set(Calendar.MONTH, monthOfYear);
                    courseCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    setEndDate();
                }

            };

            startCalPicker.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    new DatePickerDialog(CourseAddEdit.this, startDateListener, courseCalendar
                            .get(Calendar.YEAR), courseCalendar.get(Calendar.MONTH),
                            courseCalendar.get(Calendar.DAY_OF_MONTH)).show();
                }
            });
            endCalPicker.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    new DatePickerDialog(CourseAddEdit.this, endDateListener, courseCalendar
                            .get(Calendar.YEAR), courseCalendar.get(Calendar.MONTH),
                            courseCalendar.get(Calendar.DAY_OF_MONTH)).show();
                }
            });

            RecyclerView recyclerViewMentor = findViewById(R.id.recycler_view_mentor);
            recyclerViewMentor.setLayoutManager(new LinearLayoutManager(this));
            recyclerViewMentor.setHasFixedSize(true);

            RecyclerView recyclerViewAssessment = findViewById(R.id.recycler_view_assessment);
            recyclerViewAssessment.setLayoutManager(new LinearLayoutManager(this));
            recyclerViewAssessment.setHasFixedSize(true);

            RecyclerView recyclerViewNote = findViewById(R.id.recycler_view_note);
            recyclerViewNote.setLayoutManager(new LinearLayoutManager(this));
            recyclerViewNote.setHasFixedSize(true);

            final MentorAdapter mentorAdapter = new MentorAdapter();
            recyclerViewMentor.setAdapter(mentorAdapter);
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

            // Handle clicks for editing mentors, assessments and notes
            mentorAdapter.setOnItemClickListener(new MentorAdapter.onItemClickListener() {
                @Override
                public void onItemClick(Mentor mentor) {
                    Intent editMentorIntent = new Intent(CourseAddEdit.this, MentorAddEdit.class);
                    editMentorIntent.putExtra(EXTRA_MENTOR_NAME, mentor.getMentorName());
                    editMentorIntent.putExtra(EXTRA_MENTOR_EMAIL, mentor.getMentorEmail());
                    editMentorIntent.putExtra(EXTRA_MENTOR_PHONE, mentor.getMentorPhone());
                    startActivityForResult(editMentorIntent, EDIT_MENTOR_REQUEST);
                }
            });
            assessmentAdapter.setOnItemClickListener(new AssessmentAdapter.onItemClickListener() {
                @Override
                public void onItemClick(Assessment assessment) {
                    Intent editAssessmentIntent = new Intent(CourseAddEdit.this, AssessmentAddEdit.class);
                    editAssessmentIntent.putExtra(EXTRA_ASSESSMENT_TITLE, assessment.getAssessmentName());
                    editAssessmentIntent.putExtra(EXTRA_ASSESSMENT_TYPE, assessment.getAssessmentType());
                    editAssessmentIntent.putExtra(EXTRA_ASSESSMENT_GOAL, assessment.getGoalDate());
                    startActivityForResult(editAssessmentIntent, EDIT_ASSESSMENT_REQUEST);
                }
            });
            noteAdapter.setOnItemClickListener(new NoteAdapter.onItemClickListener() {
                @Override
                public void onItemClick(Note note) {
                    int noteId = note.getNoteId();
                    AlertDialog alertDialog = new AlertDialog.Builder(CourseAddEdit.this).create();
                    alertDialog.setIcon(android.R.drawable.ic_dialog_alert);
                    alertDialog.setTitle("Edit Note");
                    alertDialog.setMessage("Please edit existing note:");
                    final EditText existingNoteString = new EditText(CourseAddEdit.this);
                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.MATCH_PARENT);
                    existingNoteString.setLayoutParams(lp);
                    alertDialog.setView(existingNoteString);
                    existingNoteString.setText(note.getNoteString());
                    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            String newNoteString = existingNoteString.getText().toString();
                            Note note = new Note(courseId,newNoteString);
                            note.setNoteId(noteId);
                            noteViewModel.update(note);
                            noteAdapter.notifyDataSetChanged();
                        }
                    });
                    alertDialog.show();
                    Button okButton = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                    LinearLayout.LayoutParams aboutLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
                    okButton.setLayoutParams(aboutLayoutParams);
                }
            });
            // Handle clicks for adding mentors, assessments and notes
            buttonAddMentor.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent addMentorIntent = new Intent(CourseAddEdit.this, MentorAddEdit.class);
                    startActivityForResult(addMentorIntent, ADD_MENTOR_REQUEST);
                }
            });
            buttonAddAssessment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent addAssessmentIntent = new Intent(CourseAddEdit.this, AssessmentAddEdit.class);
                    startActivityForResult(addAssessmentIntent, ADD_ASSESSMENT_REQUEST);
                }
            });
            buttonAddNote.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog alertDialog = new AlertDialog.Builder(CourseAddEdit.this).create();
                    alertDialog.setIcon(android.R.drawable.ic_dialog_alert);
                    alertDialog.setTitle("Add Note");
                    alertDialog.setMessage("Please add a new note:");
                    final EditText editTextNoteString = new EditText(CourseAddEdit.this);
                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.MATCH_PARENT);
                    editTextNoteString.setLayoutParams(lp);
                    alertDialog.setView(editTextNoteString);
                    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            String newNoteString = editTextNoteString.getText().toString();
                            Note note = new Note(courseId,newNoteString);
                            noteViewModel.insert(note);
                            noteAdapter.notifyDataSetChanged();
                        }
                    });
                    alertDialog.show();
                    Button okButton = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                    LinearLayout.LayoutParams aboutLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
                    okButton.setLayoutParams(aboutLayoutParams);
                }
            });
        } else {
            setTitle("Add Course");
            // Hide sub items, to be added AFTER initial course creation
            textViewMentors.setVisibility(GONE);
            recyclerViewMentor.setVisibility(GONE);
            buttonAddMentor.setVisibility(GONE);
            textViewAssessments.setVisibility(GONE);
            recyclerViewAssessment.setVisibility(GONE);
            buttonAddAssessment.setVisibility(GONE);
            textViewNotes.setVisibility(GONE);
            recyclerViewNote.setVisibility(GONE);
            buttonAddNote.setVisibility(GONE);

            startDateListener = new DatePickerDialog.OnDateSetListener() {

                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear,
                                      int dayOfMonth) {
                    courseCalendar.set(Calendar.YEAR, year);
                    courseCalendar.set(Calendar.MONTH, monthOfYear);
                    courseCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    setStartDate();
                }

            };
            endDateListener = new DatePickerDialog.OnDateSetListener() {

                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear,
                                      int dayOfMonth) {
                    courseCalendar.set(Calendar.YEAR, year);
                    courseCalendar.set(Calendar.MONTH, monthOfYear);
                    courseCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    setEndDate();
                }

            };

            startCalPicker.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    new DatePickerDialog(CourseAddEdit.this, startDateListener, courseCalendar
                            .get(Calendar.YEAR), courseCalendar.get(Calendar.MONTH),
                            courseCalendar.get(Calendar.DAY_OF_MONTH)).show();
                }
            });
            endCalPicker.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    new DatePickerDialog(CourseAddEdit.this, endDateListener, courseCalendar
                            .get(Calendar.YEAR), courseCalendar.get(Calendar.MONTH),
                            courseCalendar.get(Calendar.DAY_OF_MONTH)).show();
                }
            });

            View parentLayout = findViewById(android.R.id.content);
            Snackbar.make(parentLayout, "Please save course first before adding or editing mentors, assessments or notes.", Snackbar.LENGTH_INDEFINITE)
                    .setAction("Ok", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                        }
                    })
                    .show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        String myFormat = "MM/dd/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        Date goalDate = null;
        int type = -1;

        if(requestCode == ADD_MENTOR_REQUEST && resultCode == RESULT_OK) {
            String name = data.getStringExtra(MentorDetail.EXTRA_MENTOR_NAME);
            String phone = data.getStringExtra(MentorDetail.EXTRA_MENTOR_PHONE);
            String email = data.getStringExtra(MentorDetail.EXTRA_MENTOR_EMAIL);

            Mentor mentor = new Mentor(name, phone, email,courseId);
            MentorViewModel.insert(mentor);
            //mentorAdapter.notifyDataSetChanged();
            Toast.makeText(this, "Mentor saved", Toast.LENGTH_LONG).show();
        } else if (requestCode == EDIT_MENTOR_REQUEST && resultCode == RESULT_OK) {
            int id = data.getIntExtra(MentorDetail.EXTRA_MENTOR_ID, -1);
            if (id == -1) {
                Toast.makeText(this, "Couldn't be updated", Toast.LENGTH_SHORT).show();
                return;
            }

            String name = data.getStringExtra(MentorDetail.EXTRA_MENTOR_NAME);
            String phone = data.getStringExtra(MentorDetail.EXTRA_MENTOR_PHONE);
            String email = data.getStringExtra(MentorDetail.EXTRA_MENTOR_EMAIL);

            Mentor mentor = new Mentor(name,phone,email,courseId);
            mentor.setMentorId(id);
            mentorViewModel.update(mentor);
            //mentorAdapter.notifyDataSetChanged();
            Toast.makeText(this,"Mentor Updated", Toast.LENGTH_SHORT).show();
        } else if (requestCode == ADD_ASSESSMENT_REQUEST && resultCode == RESULT_OK) {
            String assessmentName = data.getStringExtra(EXTRA_ASSESSMENT_TITLE);
            String typeString = data.getStringExtra(AssessmentAddEdit.EXTRA_ASSESSMENT_TYPE);
            String goalDateString = data.getStringExtra(AssessmentAddEdit.EXTRA_ASSESSMENT_GOAL);

            try {
                goalDate = sdf.parse(goalDateString);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            switch(typeString) {
                case "OA":
                    type = 0;
                    break;
                case "PA":
                    type = 1;
                    break;
            }

            Assessment assessment = new Assessment(courseId,assessmentName,type,goalDate);
            AssessmentViewModel.insert(assessment);
            //assessmentAdapter.notifyDataSetChanged();
            Toast.makeText(this, "Assessment saved", Toast.LENGTH_LONG).show();
        } else if (requestCode == EDIT_ASSESSMENT_REQUEST && resultCode == RESULT_OK) {
            int id = data.getIntExtra(AssessmentDetail.EXTRA_ASSESSMENT_ID, -1);
            if (id == -1) {
                Toast.makeText(this, "Couldn't be updated", Toast.LENGTH_SHORT).show();
                return;
            }

            int courseId = Integer.parseInt(data.getStringExtra(AssessmentDetail.EXTRA_ASSESSMENT_COURSE_ID));
            String title = data.getStringExtra(AssessmentDetail.EXTRA_ASSESSMENT_TITLE);
            String typeString = data.getStringExtra(AssessmentDetail.EXTRA_ASSESSMENT_TYPE);
            String goalDateString = data.getStringExtra(AssessmentDetail.EXTRA_ASSESSMENT_GOAL);

            try {
                goalDate = sdf.parse(goalDateString);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            switch (typeString) {
                case "OA":
                    type = 0;
                    break;
                case "PA":
                    type = 1;
                    break;
            }
            Assessment assessment = new Assessment(courseId,title,type,goalDate);
            assessment.setAssessmentId(id);
            assessmentViewModel.update(assessment);
            //assessmentAdapter.notifyDataSetChanged();
        } else {
            Toast.makeText(this, "Not saved!", Toast.LENGTH_LONG).show();
        }
    }

    private void setStartDate() {
        editTextStartDate.setText(sdf.format(courseCalendar.getTime()));
    }

    private void setEndDate() {
        editTextEndDate.setText(sdf.format(courseCalendar.getTime()));
    }

    public void saveCourse() {
        titleString = editTextTitle.getText().toString();
        String startDateString = editTextStartDate.getText().toString();
        String endDateString = editTextEndDate.getText().toString();

        if (titleString.trim().isEmpty()) {
            Toast.makeText(this, "Please enter a title", Toast.LENGTH_LONG);
            return;
        }
        if (startDateString.trim().isEmpty()) {
            Toast.makeText(this, "Please enter a start date", Toast.LENGTH_LONG);
            return;
        }
        if (endDateString.trim().isEmpty()) {
            Toast.makeText(this, "Please enter an end date", Toast.LENGTH_LONG);
            return;
        }

        Intent data = new Intent();
        data.putExtra(EXTRA_COURSE_TITLE, titleString);
        data.putExtra(EXTRA_COURSE_START_DATE, startDateString);
        data.putExtra(EXTRA_COURSE_END_DATE, endDateString);
        data.putExtra(EXTRA_COURSE_STATUS, statusString);
        data.putExtra(EXTRA_COURSE_TERM_ID, termId);

        int id = getIntent().getIntExtra(EXTRA_COURSE_ID, -1);
        if (id != -1) {
            data.putExtra(EXTRA_COURSE_ID, id);
        }

        setResult(RESULT_OK, data);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.addedit_course_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle close click
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        // Handle save click
        switch (item.getItemId()) {
            case R.id.save_course:
                saveCourse();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}