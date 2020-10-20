package me.joshmckinney.gradtracker.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import me.joshmckinney.gradtracker.R;
import me.joshmckinney.gradtracker.adapter.AssessmentAdapter;
import me.joshmckinney.gradtracker.model.Assessment;
import me.joshmckinney.gradtracker.viewmodel.AssessmentViewModel;

public class AssessmentList extends AppCompatActivity {
    public static final int ADD_ASSESSMENT_REQUEST = 1;
    public static final int EDIT_ASSESSMENT_REQUEST = 2;

    private AssessmentViewModel assessmentViewModel;
    final AssessmentAdapter adapter = new AssessmentAdapter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessment_list);
        setTitle("Assessments");
        // Set and enable back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        recyclerView.setAdapter(adapter);

        assessmentViewModel = ViewModelProviders.of(this).get(AssessmentViewModel.class);
        assessmentViewModel.getAllAssessments().observe(this, new Observer<List<Assessment>>() {
            @Override
            public void onChanged(@Nullable List<Assessment> assessments) {
                adapter.submitList(assessments);
                // Wait for assessments to populate after on changed and alert if empty -- 1000ms
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (adapter.getCurrentList().isEmpty()) {
                            AlertDialog alertDialog = new AlertDialog.Builder(AssessmentList.this).create();
                            alertDialog.setIcon(android.R.drawable.ic_dialog_alert);
                            alertDialog.setTitle("No Assessments Found");
                            alertDialog.setMessage("Please add an assessment via courses.");
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

        adapter.setOnItemClickListener(new AssessmentAdapter.onItemClickListener() {
            @Override
            public void onItemClick(Assessment assessment) {
                Intent intent = new Intent(AssessmentList.this, AssessmentDetail.class);
                intent.putExtra("Assessment", assessment);
                startActivity(intent);
            }
        });

        // Let the user know to add more assessments via the appropriate course
        View parentLayout = findViewById(android.R.id.content);
        Snackbar.make(parentLayout, "Add more assessments by editing existing courses.", Snackbar.LENGTH_LONG)
                .setAction("Ok", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                })
                .show();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
