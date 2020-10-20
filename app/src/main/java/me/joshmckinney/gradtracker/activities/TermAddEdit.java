package me.joshmckinney.gradtracker.activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import me.joshmckinney.gradtracker.R;

public class TermAddEdit extends AppCompatActivity {
    public static final String EXTRA_TERM_ID =
            "me.joshmckinney.gradtracker.EXTRA_ID";
    public static final String EXTRA_TERM_TITLE =
            "me.joshmckinney.gradtracker.EXTRA_TITLE";
    public static final String EXTRA_TERM_START_DATE =
            "me.joshmckinney.gradtracker.EXTRA_START_DATE";
    public static final String EXTRA_TERM_END_DATE =
            "me.joshmckinney.gradtracker.EXTRA_END_DATE";

    final Calendar termCalendar = Calendar.getInstance();
    private EditText editTextTitle;
    private EditText editTextStartDate;
    private EditText editTextEndDate;
    DatePickerDialog.OnDateSetListener startDateListener;
    DatePickerDialog.OnDateSetListener endDateListener;
    ImageButton startCalPicker;
    ImageButton endCalPicker;
    String myFormat = "MM/dd/yy";
    SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addedit_term);
        // Enable close button for exiting without save
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        editTextTitle = findViewById(R.id.edit_text_term_title);
        editTextStartDate = findViewById(R.id.edit_text_start_date);
        editTextEndDate = findViewById(R.id.edit_text_end_date);
        startCalPicker = findViewById(R.id.startCalPicker);
        endCalPicker = findViewById(R.id.endCalPicker);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);

        Intent intent = getIntent();

        if (intent.hasExtra(EXTRA_TERM_TITLE)) {
            setTitle("Edit Term");
            editTextTitle.setText(intent.getStringExtra(EXTRA_TERM_TITLE));
            String startoutput = "";
            String endoutput = "";
            startoutput = sdf.format(Date.parse(intent.getStringExtra(EXTRA_TERM_START_DATE)));
            endoutput = sdf.format(Date.parse(intent.getStringExtra(EXTRA_TERM_END_DATE)));
            editTextStartDate.setText(startoutput);
            editTextEndDate.setText(endoutput);

            startDateListener = new DatePickerDialog.OnDateSetListener() {

                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear,
                                      int dayOfMonth) {
                    // TODO Auto-generated method stub
                    termCalendar.set(Calendar.YEAR, year);
                    termCalendar.set(Calendar.MONTH, monthOfYear);
                    termCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    setStartDate();
                }

            };
            endDateListener = new DatePickerDialog.OnDateSetListener() {

                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear,
                                      int dayOfMonth) {
                    // TODO Auto-generated method stub
                    termCalendar.set(Calendar.YEAR, year);
                    termCalendar.set(Calendar.MONTH, monthOfYear);
                    termCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    setEndDate();
                }

            };

            startCalPicker.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    new DatePickerDialog(TermAddEdit.this, startDateListener, termCalendar
                            .get(Calendar.YEAR), termCalendar.get(Calendar.MONTH),
                            termCalendar.get(Calendar.DAY_OF_MONTH)).show();
                }
            });
            endCalPicker.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    new DatePickerDialog(TermAddEdit.this, endDateListener, termCalendar
                            .get(Calendar.YEAR), termCalendar.get(Calendar.MONTH),
                            termCalendar.get(Calendar.DAY_OF_MONTH)).show();
                }
            });
        } else {
            setTitle("Add Term");
            startDateListener = new DatePickerDialog.OnDateSetListener() {

                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear,
                                      int dayOfMonth) {
                    // TODO Auto-generated method stub
                    termCalendar.set(Calendar.YEAR, year);
                    termCalendar.set(Calendar.MONTH, monthOfYear);
                    termCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    setStartDate();
                }

            };
            endDateListener = new DatePickerDialog.OnDateSetListener() {

                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear,
                                      int dayOfMonth) {
                    // TODO Auto-generated method stub
                    termCalendar.set(Calendar.YEAR, year);
                    termCalendar.set(Calendar.MONTH, monthOfYear);
                    termCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    setEndDate();
                }

            };

            startCalPicker.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    new DatePickerDialog(TermAddEdit.this, startDateListener, termCalendar
                            .get(Calendar.YEAR), termCalendar.get(Calendar.MONTH),
                            termCalendar.get(Calendar.DAY_OF_MONTH)).show();
                }
            });
            endCalPicker.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    new DatePickerDialog(TermAddEdit.this, endDateListener, termCalendar
                            .get(Calendar.YEAR), termCalendar.get(Calendar.MONTH),
                            termCalendar.get(Calendar.DAY_OF_MONTH)).show();
                }
            });
        }
    }

    // Sets the start date string from calendar picker
    private void setStartDate() {
        editTextStartDate.setText(sdf.format(termCalendar.getTime()));
    }

    private void setEndDate() {
        editTextEndDate.setText(sdf.format(termCalendar.getTime()));
    }

    public void saveTerm() {
        String title = editTextTitle.getText().toString();
        String startDateString = editTextStartDate.getText().toString();
        String endDateString = editTextEndDate.getText().toString();

        if (title.trim().isEmpty()) {
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
        data.putExtra(EXTRA_TERM_TITLE, title);
        data.putExtra(EXTRA_TERM_START_DATE, startDateString);
        data.putExtra(EXTRA_TERM_END_DATE, endDateString);

        int id = getIntent().getIntExtra(EXTRA_TERM_ID, -1);
        if (id != -1) {
            data.putExtra(EXTRA_TERM_ID, id);
        }

        setResult(RESULT_OK, data);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.addedit_term_menu, menu);
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
            case R.id.save_term:
                saveTerm();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}