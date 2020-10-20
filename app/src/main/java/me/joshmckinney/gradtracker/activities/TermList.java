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

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import me.joshmckinney.gradtracker.R;
import me.joshmckinney.gradtracker.adapter.TermAdapter;
import me.joshmckinney.gradtracker.model.Term;
import me.joshmckinney.gradtracker.viewmodel.CourseViewModel;
import me.joshmckinney.gradtracker.viewmodel.TermViewModel;

public class TermList extends AppCompatActivity implements Serializable {

    public static final int ADD_TERM_REQUEST = 1;
    public static final int EDIT_TERM_REQUEST = 2;

    private TermViewModel termViewModel;
    private CourseViewModel courseViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_list);
        setTitle("Terms");
        // Set and enable back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        FloatingActionButton buttonAddTerm = findViewById(R.id.button_add_term);
        buttonAddTerm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TermList.this, TermAddEdit.class);
                startActivityForResult(intent, ADD_TERM_REQUEST);
            }
        });

        RecyclerView termRecyclerView = findViewById(R.id.recycler_view);
        termRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        termRecyclerView.setHasFixedSize(true);

        final TermAdapter termAdapter = new TermAdapter();
        termRecyclerView.setAdapter(termAdapter);

        courseViewModel = ViewModelProviders.of(this).get(CourseViewModel.class);
        termViewModel = ViewModelProviders.of(this).get(TermViewModel.class);
        termViewModel.getAllTerms().observe(this, new Observer<List<Term>>() {
            @Override
            public void onChanged(@Nullable List<Term> terms) {
                termAdapter.submitList(terms);
                // Wait for terms to populate after on changed and alert if empty -- 1000ms
                final Handler handler = new Handler();
                handler.postDelayed(() -> {
                    if (termAdapter.getCurrentList().isEmpty()) {
                        AlertDialog alertDialog = new AlertDialog.Builder(TermList.this).create();
                        alertDialog.setIcon(android.R.drawable.ic_dialog_alert);
                        alertDialog.setTitle("No Terms Found");
                        alertDialog.setMessage("Please add a term via terms.");
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
                }, 1000);
            }
        });

        // Term item click loads term details (course list)
        termAdapter.setOnItemClickListener(new TermAdapter.onItemClickListener() {
            @Override
            public void onItemClick(Term term) {
                Intent intent = new Intent(TermList.this, TermDetail.class);
                intent.putExtra("Term", term);
                startActivity(intent);
            }
        });
        // Term item long click loads term edit
        termAdapter.setOnLongClickListener(new TermAdapter.onLongClickListener() {
            @Override
            public void onItemLongClick(Term term) {
                Intent intent = new Intent(TermList.this, TermAddEdit.class);
                intent.putExtra(TermAddEdit.EXTRA_TERM_ID, term.getTermId());
                intent.putExtra(TermAddEdit.EXTRA_TERM_TITLE, term.getTermName());
                intent.putExtra(TermAddEdit.EXTRA_TERM_START_DATE, term.getStartDate().toString());
                intent.putExtra(TermAddEdit.EXTRA_TERM_END_DATE, term.getEndDate().toString());
                startActivityForResult(intent, EDIT_TERM_REQUEST);
            }
        });

        View parentLayout = findViewById(android.R.id.content);
        Snackbar.make(parentLayout, "Click a term to view details or long click to edit.", Snackbar.LENGTH_SHORT)
                .setAction("Ok", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                })
                .show();
    }

    // Intent data returned from add/edit and dealt with accordingly
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String title = null;
        Date startDate = null;
        Date endDate = null;

        String myFormat = "MM/dd/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        if(requestCode == ADD_TERM_REQUEST && resultCode == RESULT_OK) {
            title = data.getStringExtra(TermAddEdit.EXTRA_TERM_TITLE);
            String startDateString = data.getStringExtra(TermAddEdit.EXTRA_TERM_START_DATE);
            String endDateString = data.getStringExtra(TermAddEdit.EXTRA_TERM_END_DATE);

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

            Term term = new Term(title, startDate, endDate);
            TermViewModel.insert(term);

            Toast.makeText(this, "Term saved", Toast.LENGTH_LONG).show();
        } else if (requestCode == EDIT_TERM_REQUEST && resultCode == RESULT_OK) {
            int id = data.getIntExtra(TermAddEdit.EXTRA_TERM_ID, -1);
            if (id == -1) {
                Toast.makeText(this, "Couldn't be updated", Toast.LENGTH_SHORT).show();
                return;
            }

            title = data.getStringExtra(TermAddEdit.EXTRA_TERM_TITLE);
            String startDateString = data.getStringExtra(TermAddEdit.EXTRA_TERM_START_DATE);
            String endDateString = data.getStringExtra(TermAddEdit.EXTRA_TERM_END_DATE);

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

            Term term = new Term(title,startDate,endDate);
            term.setTermId(id);
            termViewModel.update(term);

            Toast.makeText(this,"Term Updated", Toast.LENGTH_SHORT).show();
        } else {
                Toast.makeText(this, "Term not saved!", Toast.LENGTH_LONG).show();
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