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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import me.joshmckinney.gradtracker.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CardView cardTerm = findViewById(R.id.card_terms);
        cardTerm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, TermList.class);
                startActivity(intent);
            }
        });
        CardView cardCourse = findViewById(R.id.card_courses);
        cardCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CourseList.class);
                startActivity(intent);
            }
        });
        CardView cardAssessment = findViewById(R.id.card_assessments);
        cardAssessment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AssessmentList.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_about:
                AlertDialog aboutDialog = new AlertDialog.Builder(this).create();
                aboutDialog.setIcon(R.mipmap.ic_launcher);
                aboutDialog.setTitle("About Grad Tracker");
                aboutDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                    }
                });
                aboutDialog.show();
                Button yesButton = aboutDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                LinearLayout.LayoutParams aboutLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
                yesButton.setLayoutParams(aboutLayoutParams);
                return true;
            case R.id.menu_help:
                AlertDialog helpDialog= new AlertDialog.Builder(this).create();
                helpDialog.setIcon(R.mipmap.ic_launcher);
                helpDialog.setTitle("Help");
                helpDialog.setMessage("Add items by choosing a category and hitting the plus(+) button.\n\n" +
                        "Edit existing items by choosing a category and long pressing the item.\n\n" +
                        "View existing item details by choosing a category and clicking the item.");
                helpDialog.setButton(AlertDialog.BUTTON_POSITIVE,"Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                    }
                });
                helpDialog.show();
                Button helpButton = helpDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                LinearLayout.LayoutParams helpLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
                helpButton.setLayoutParams(helpLayoutParams);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}