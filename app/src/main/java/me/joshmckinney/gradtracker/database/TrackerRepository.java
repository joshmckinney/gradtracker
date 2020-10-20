package me.joshmckinney.gradtracker.database;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

import me.joshmckinney.gradtracker.dao.AssessmentDao;
import me.joshmckinney.gradtracker.dao.CourseDao;
import me.joshmckinney.gradtracker.dao.MentorDao;
import me.joshmckinney.gradtracker.dao.NoteDao;
import me.joshmckinney.gradtracker.dao.TermDao;
import me.joshmckinney.gradtracker.model.Assessment;
import me.joshmckinney.gradtracker.model.Course;
import me.joshmckinney.gradtracker.model.Mentor;
import me.joshmckinney.gradtracker.model.Note;
import me.joshmckinney.gradtracker.model.Term;

public class TrackerRepository {
    private static int termId;
    private static int courseId;
    private TermDao termDao;
    private LiveData<List<Term>> allTerms;
    private CourseDao courseDao;
    private LiveData<List<Course>> allCourses;
    private LiveData<List<Course>> termCourses;
    private AssessmentDao assessmentDao;
    private LiveData<List<Assessment>> courseAssessments;
    private LiveData<List<Assessment>> allAssessments;
    private MentorDao mentorDao;
    private LiveData<List<Mentor>> courseMentors;
    private LiveData<List<Mentor>> allMentors;
    private NoteDao noteDao;
    private LiveData<List<Note>> courseNotes;
    private LiveData<List<Note>> allNotes;

    public TrackerRepository(Application application) {
        TrackerDatabase database = TrackerDatabase.getInstance(application);
        termDao = database.termDao();
        allTerms = termDao.getAllTerms();
        courseDao = database.courseDao();
        allCourses = courseDao.getAllCourses();
        termCourses = courseDao.getTermCourses(termId);
        assessmentDao = database.assessmentDao();
        courseAssessments = assessmentDao.getCourseAssessments(courseId);
        allAssessments = assessmentDao.getAllAssessments();
        mentorDao = database.mentorDao();
        courseMentors = mentorDao.getCourseMentors(courseId);
        allMentors = mentorDao.getAllMentors();
        noteDao = database.noteDao();
        courseNotes = noteDao.getCourseNotes(courseId);
        allNotes = noteDao.getAllNotes();
    }

    public static void setTermId(int termId) { TrackerRepository.termId = termId; }
    public static void setCourseId(int courseId) { TrackerRepository.courseId = courseId; }

    public void insert(Term term) {
        new InsertTermAsyncTask(termDao).execute(term);
    }
    public void update(Term term) {
        new UpdateTermAsyncTask(termDao).execute(term);
    }
    public void delete(Term term) {
        new DeleteTermAsyncTask(termDao).execute(term);
    }
    public void deleteAllTerms() { new DeleteAllTermsAsyncTask(termDao).execute(); }

    public void insert(Course course) { new InsertCourseAsyncTask(courseDao).execute(course); }
    public void update(Course course) {
        new UpdateCourseAsyncTask(courseDao).execute(course);
    }
    public void delete(Course course) {
        new DeleteCourseAsyncTask(courseDao).execute(course);
    }
    public void deleteAllCourses() { new DeleteAllCoursesAsyncTask(courseDao).execute(); }

    public void insert(Assessment assessment) { new InsertAssessmentAsyncTask(assessmentDao).execute(assessment); }
    public void update(Assessment assessment) { new UpdateAssessmentAsyncTask(assessmentDao).execute(assessment); }
    public void delete(Assessment assessment) { new DeleteAssessmentAsyncTask(assessmentDao).execute(assessment); }
    public void deleteAllAssessments() { new DeleteAllAssessmentAsyncTask(assessmentDao).execute(); }

    public void insert(Mentor mentor) {
        new InsertMentorAsyncTask(mentorDao).execute(mentor);
    }
    public void update(Mentor mentor) {
        new UpdateMentorAsyncTask(mentorDao).execute(mentor);
    }
    public void delete(Mentor mentor) {
        new DeleteMentorAsyncTask(mentorDao).execute(mentor);
    }
    public void deleteAllMentors() { new DeleteAllMentorAsyncTask(mentorDao).execute(); }

    public void insert(Note note) {
        new InsertNoteAsyncTask(noteDao).execute(note);
    }
    public void update(Note note) {
        new UpdateNoteAsyncTask(noteDao).execute(note);
    }
    public void delete(Note note) {
        new DeleteNoteAsyncTask(noteDao).execute(note);
    }
    public void deleteAllNotes() { new DeleteAllNoteAsyncTask(noteDao).execute(); }

    public LiveData<List<Term>> getAllTerms() {
        return allTerms;
    }
    public LiveData<List<Course>> getTermCourses() { return termCourses; }
    public LiveData<List<Course>> getAllCourses() {
        return allCourses;
    }
    public LiveData<List<Assessment>> getCourseAssessments() { return courseAssessments; }
    public LiveData<List<Assessment>> getAllAssessments() { return allAssessments; }
    public LiveData<List<Mentor>> getCourseMentors() {
        return courseMentors;
    }
    public LiveData<List<Mentor>> getAllMentors() { return allMentors; }
    public LiveData<List<Note>> getCourseNotes() { return courseNotes; }
    public LiveData<List<Note>> getAllNotes() { return allNotes; }

    // Term Tasks

    private static class InsertTermAsyncTask extends AsyncTask<Term, Void, Void> {
        private TermDao termDao;

        private InsertTermAsyncTask(TermDao termDao) {
            this.termDao = termDao;
        }

        @Override
        protected Void doInBackground(Term... terms) {
            termDao.insert(terms[0]);
            return null;
        }
    }

    private static class UpdateTermAsyncTask extends AsyncTask<Term, Void, Void> {
        private TermDao termDao;

        private UpdateTermAsyncTask(TermDao termDao) {
            this.termDao = termDao;
        }

        @Override
        protected Void doInBackground(Term... terms) {
            termDao.update(terms[0]);
            return null;
        }
    }

    private static class DeleteTermAsyncTask extends AsyncTask<Term, Void, Void> {
        private TermDao termDao;

        private DeleteTermAsyncTask(TermDao termDao) {
            this.termDao = termDao;
        }

        @Override
        protected Void doInBackground(Term... terms) {
            termDao.delete(terms[0]);
            return null;
        }
    }

    private static class DeleteAllTermsAsyncTask extends AsyncTask<Void, Void, Void> {
        private TermDao termDao;
        private DeleteAllTermsAsyncTask(TermDao termDao) {
            this.termDao = termDao;
        }
        @Override
        protected Void doInBackground(Void... voids) {
            termDao.deleteAllTerms();
            return null;
        }
    }

    // Course Tasks

    private static class InsertCourseAsyncTask extends AsyncTask<Course, Void, Void> {
        private CourseDao courseDao;

        private InsertCourseAsyncTask(CourseDao courseDao) {
            this.courseDao = courseDao;
        }

        @Override
        protected Void doInBackground(Course... courses) {
            courseDao.insert(courses[0]);
            return null;
        }
    }

    private static class UpdateCourseAsyncTask extends AsyncTask<Course, Void, Void> {
        private CourseDao courseDao;

        private UpdateCourseAsyncTask(CourseDao courseDao) {
            this.courseDao = courseDao;
        }

        @Override
        protected Void doInBackground(Course... courses) {
            courseDao.update(courses[0]);
            return null;
        }
    }

    private static class DeleteCourseAsyncTask extends AsyncTask<Course, Void, Void> {
        private CourseDao courseDao;

        private DeleteCourseAsyncTask(CourseDao courseDao) {
            this.courseDao = courseDao;
        }

        @Override
        protected Void doInBackground(Course... courses) {
            courseDao.delete(courses[0]);
            return null;
        }
    }

    private static class DeleteAllCoursesAsyncTask extends AsyncTask<Void, Void, Void> {
        private CourseDao courseDao;
        private DeleteAllCoursesAsyncTask(CourseDao courseDao) {
            this.courseDao = courseDao;
        }
        @Override
        protected Void doInBackground(Void... voids) {
            courseDao.deleteAllCourses();
            return null;
        }
    }

    // Assessment Tasks

    private static class InsertAssessmentAsyncTask extends AsyncTask<Assessment, Void, Void> {
        private AssessmentDao assessmentDao;

        private InsertAssessmentAsyncTask(AssessmentDao assessmentDao) {
            this.assessmentDao = assessmentDao;
        }

        @Override
        protected Void doInBackground(Assessment... assessments) {
            assessmentDao.insert(assessments[0]);
            return null;
        }
    }

    private static class UpdateAssessmentAsyncTask extends AsyncTask<Assessment, Void, Void> {
        private AssessmentDao assessmentDao;

        private UpdateAssessmentAsyncTask(AssessmentDao assessmentDao) {
            this.assessmentDao = assessmentDao;
        }

        @Override
        protected Void doInBackground(Assessment... assessments) {
            assessmentDao.update(assessments[0]);
            return null;
        }
    }

    private static class DeleteAssessmentAsyncTask extends AsyncTask<Assessment, Void, Void> {
        private AssessmentDao assessmentDao;

        private DeleteAssessmentAsyncTask(AssessmentDao assessmentDao) {
            this.assessmentDao = assessmentDao;
        }

        @Override
        protected Void doInBackground(Assessment... assessments) {
            assessmentDao.delete(assessments[0]);
            return null;
        }
    }

    private static class DeleteAllAssessmentAsyncTask extends AsyncTask<Void, Void, Void> {
        private AssessmentDao assessmentDao;
        private DeleteAllAssessmentAsyncTask(AssessmentDao assessmentDao) {
            this.assessmentDao = assessmentDao;
        }
        @Override
        protected Void doInBackground(Void... voids) {
            assessmentDao.deleteAllAssessments();
            return null;
        }
    }

    // Mentor Tasks

    private static class InsertMentorAsyncTask extends AsyncTask<Mentor, Void, Void> {
        private MentorDao mentorDao;

        private InsertMentorAsyncTask(MentorDao mentorDao) {
            this.mentorDao = mentorDao;
        }

        @Override
        protected Void doInBackground(Mentor... mentors) {
            mentorDao.insert(mentors[0]);
            return null;
        }
    }

    private static class UpdateMentorAsyncTask extends AsyncTask<Mentor, Void, Void> {
        private MentorDao mentorDao;

        private UpdateMentorAsyncTask(MentorDao mentorDao) {
            this.mentorDao = mentorDao;
        }

        @Override
        protected Void doInBackground(Mentor... mentors) {
            mentorDao.update(mentors[0]);
            return null;
        }
    }

    private static class DeleteMentorAsyncTask extends AsyncTask<Mentor, Void, Void> {
        private MentorDao mentorDao;

        private DeleteMentorAsyncTask(MentorDao mentorDao) {
            this.mentorDao = mentorDao;
        }

        @Override
        protected Void doInBackground(Mentor... mentors) {
            mentorDao.delete(mentors[0]);
            return null;
        }
    }

    private static class DeleteAllMentorAsyncTask extends AsyncTask<Void, Void, Void> {
        private MentorDao mentorDao;
        private DeleteAllMentorAsyncTask(MentorDao mentorDao) {
            this.mentorDao = mentorDao;
        }
        @Override
        protected Void doInBackground(Void... voids) {
            mentorDao.deleteAllMentors();
            return null;
        }
    }

    // Note Tasks

    private static class InsertNoteAsyncTask extends AsyncTask<Note, Void, Void> {
        private NoteDao noteDao;

        private InsertNoteAsyncTask(NoteDao noteDao) {
            this.noteDao = noteDao;
        }

        @Override
        protected Void doInBackground(Note... notes) {
            noteDao.insert(notes[0]);
            return null;
        }
    }

    private static class UpdateNoteAsyncTask extends AsyncTask<Note, Void, Void> {
        private NoteDao noteDao;

        private UpdateNoteAsyncTask(NoteDao noteDao) {
            this.noteDao = noteDao;
        }

        @Override
        protected Void doInBackground(Note... notes) {
            noteDao.update(notes[0]);
            return null;
        }
    }

    private static class DeleteNoteAsyncTask extends AsyncTask<Note, Void, Void> {
        private NoteDao noteDao;

        private DeleteNoteAsyncTask(NoteDao noteDao) {
            this.noteDao = noteDao;
        }

        @Override
        protected Void doInBackground(Note... notes) {
            noteDao.delete(notes[0]);
            return null;
        }
    }

    private static class DeleteAllNoteAsyncTask extends AsyncTask<Void, Void, Void> {
        private NoteDao noteDao;
        private DeleteAllNoteAsyncTask(NoteDao noteDao) {
            this.noteDao = noteDao;
        }
        @Override
        protected Void doInBackground(Void... voids) {
            noteDao.deleteAllNotes();
            return null;
        }
    }

}
