package me.joshmckinney.gradtracker.activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import me.joshmckinney.gradtracker.R;

public class AssessmentAddEdit extends AppCompatActivity {
    public static final String EXTRA_ASSESSMENT_ID =
            "me.joshmckinney.gradtracker.EXTRA_ASSESSMENT_ID";
    public static final String EXTRA_ASSESSMENT_TITLE =
            "me.joshmckinney.gradtracker.EXTRA_ASSESSMENT_ID";
    public static final String EXTRA_ASSESSMENT_GOAL =
            "me.joshmckinney.gradtracker.EXTRA_ASSESSMENT_GOAL";
    public static final String EXTRA_ASSESSMENT_TYPE =
            "me.joshmckinney.gradtracker.EXTRA_ASSESSMENT_TYPE";

    final Calendar assessmentCal = Calendar.getInstance();
    private EditText editTextTitle;
    private EditText editTextDueDate;
    private String typeString;
    private Spinner typeItems;
    DatePickerDialog.OnDateSetListener dueDateListener;
    ImageButton dueCalPicker;
    String myFormat = "MM/dd/yy";
    SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addedit_assessment);

        editTextTitle = findViewById(R.id.edit_text_assessment_title);
        editTextDueDate = findViewById(R.id.edit_text_due_date);
        dueCalPicker = findViewById(R.id.dueCalPicker);

        // Enable close button for exiting without save
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        List<String> typeArray = new ArrayList<String>();
        typeArray.add("OA");
        typeArray.add("PA");

        // Type Spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, typeArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner typeItems = (Spinner) findViewById(R.id.assessment_type_spinner);
        typeItems.setAdapter(adapter);
        typeItems.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                typeString = typeItems.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        Intent intent = getIntent();

        if (intent.hasExtra(EXTRA_ASSESSMENT_TITLE)) {
            setTitle("Edit Assessment");
            editTextTitle.setText(intent.getStringExtra(EXTRA_ASSESSMENT_TITLE));
            editTextDueDate.setText(intent.getStringExtra(EXTRA_ASSESSMENT_GOAL));
            dueDateListener = new DatePickerDialog.OnDateSetListener() {

                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear,
                                      int dayOfMonth) {
                    // TODO Auto-generated method stub
                    assessmentCal.set(Calendar.YEAR, year);
                    assessmentCal.set(Calendar.MONTH, monthOfYear);
                    assessmentCal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    editTextDueDate.setText(sdf.format(assessmentCal.getTime()));
                }

            };
            dueCalPicker.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    new DatePickerDialog(AssessmentAddEdit.this, dueDateListener, assessmentCal
                            .get(Calendar.YEAR), assessmentCal.get(Calendar.MONTH),
                            assessmentCal.get(Calendar.DAY_OF_MONTH)).show();
                }
            });
        } else {
            setTitle("Add Assessment");
            editTextTitle = findViewById(R.id.edit_text_assessment_title);
            dueDateListener = new DatePickerDialog.OnDateSetListener() {

                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear,
                                      int dayOfMonth) {
                    // TODO Auto-generated method stub
                    assessmentCal.set(Calendar.YEAR, year);
                    assessmentCal.set(Calendar.MONTH, monthOfYear);
                    assessmentCal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    editTextDueDate.setText(sdf.format(assessmentCal.getTime()));
                }

            };
            dueCalPicker.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    new DatePickerDialog(AssessmentAddEdit.this, dueDateListener, assessmentCal
                            .get(Calendar.YEAR), assessmentCal.get(Calendar.MONTH),
                            assessmentCal.get(Calendar.DAY_OF_MONTH)).show();
                }
            });
        }
    }

    public void saveAssessment() {
        String title = editTextTitle.getText().toString();
        String goalDate = editTextDueDate.getText().toString();

        if (title.trim().isEmpty()) {
            Toast.makeText(this, "Please enter a title", Toast.LENGTH_SHORT);
            return;
        }

        Intent data = new Intent();
        data.putExtra(EXTRA_ASSESSMENT_TITLE, title);
        data.putExtra(EXTRA_ASSESSMENT_GOAL, goalDate);
        data.putExtra(EXTRA_ASSESSMENT_TYPE, typeString);

        int id = getIntent().getIntExtra(EXTRA_ASSESSMENT_ID, -1);
        if (id != -1) {
            data.putExtra(EXTRA_ASSESSMENT_ID, id);
        }

        setResult(RESULT_OK, data);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.addedit_assessment_menu, menu);
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
            case R.id.save_assessment:
                saveAssessment();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}