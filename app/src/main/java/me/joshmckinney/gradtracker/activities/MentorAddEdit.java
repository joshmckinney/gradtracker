package me.joshmckinney.gradtracker.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import me.joshmckinney.gradtracker.R;

public class MentorAddEdit extends AppCompatActivity {
    public static final String EXTRA_MENTOR_ID =
            "me.joshmckinney.gradtracker.EXTRA_MENTORID";
    public static final String EXTRA_MENTOR_COURSE_ID =
            "me.joshmckinney.gradtracker.EXTRA_MENTOR_COURSE_ID";
    public static final String EXTRA_MENTOR_NAME =
            "me.joshmckinney.gradtracker.EXTRA_MENTOR_NAME";
    public static final String EXTRA_MENTOR_PHONE =
            "me.joshmckinney.gradtracker.EXTRA_MENTOR_PHONE";
    public static final String EXTRA_MENTOR_EMAIL =
            "me.joshmckinney.gradtracker.EXTRA_MENTOR_EMAIL";

    private EditText editTextMentorName;
    private EditText editTextMentorPhone;
    private EditText editTextMentorEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mentor_detail);

        // Enable close button for exiting without save
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        editTextMentorName = findViewById(R.id.edit_text_mentor_name);
        editTextMentorPhone = findViewById(R.id.edit_text_mentor_phone);
        editTextMentorEmail = findViewById(R.id.edit_text_mentor_email);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);

        Intent intent = getIntent();

        if (intent.hasExtra(EXTRA_MENTOR_NAME)) {
            setTitle("Edit Mentor");
            editTextMentorName.setText(intent.getStringExtra(EXTRA_MENTOR_NAME));
            editTextMentorPhone.setText(intent.getStringExtra(EXTRA_MENTOR_PHONE));
            editTextMentorEmail.setText(intent.getStringExtra(EXTRA_MENTOR_EMAIL));
        } else {
            setTitle("Add Mentor");
        }
    }

    public void saveMentor() {
        String name = editTextMentorName.getText().toString();
        String phone = editTextMentorPhone.getText().toString();
        String email = editTextMentorEmail.getText().toString();

        if (name.trim().isEmpty()) {
            Toast.makeText(this, "Please enter a name", Toast.LENGTH_LONG);
            return;
        } else if (phone.trim().isEmpty()){
            Toast.makeText(this, "Please enter a phone number", Toast.LENGTH_LONG);
            return;
        } else if (email.trim().isEmpty()){
            Toast.makeText(this, "Please enter an email", Toast.LENGTH_LONG);
            return;
        } else {
            Toast.makeText(this, "Unknown error occurred", Toast.LENGTH_LONG);
        }

        Intent data = new Intent();
        data.putExtra(EXTRA_MENTOR_NAME, name);
        data.putExtra(EXTRA_MENTOR_PHONE, phone);
        data.putExtra(EXTRA_MENTOR_EMAIL, email);

        int id = getIntent().getIntExtra(EXTRA_MENTOR_ID, -1);
        if (id != -1) {
            data.putExtra(EXTRA_MENTOR_ID, id);
        }

        setResult(RESULT_OK, data);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.addedit_mentor_menu, menu);
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
            case R.id.save_mentor:
                saveMentor();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}