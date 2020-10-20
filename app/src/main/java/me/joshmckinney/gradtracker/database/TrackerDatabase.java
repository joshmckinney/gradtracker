package me.joshmckinney.gradtracker.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

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
import me.joshmckinney.gradtracker.util.Converter;

@Database(entities = {Term.class, Course.class, Assessment.class, Mentor.class, Note.class}, version = 1, exportSchema = false)
@TypeConverters(Converter.class)
public abstract class TrackerDatabase extends RoomDatabase {
    private static TrackerDatabase instance;
    public abstract TermDao termDao();
    public abstract CourseDao courseDao();
    public abstract AssessmentDao assessmentDao();
    public abstract MentorDao mentorDao();
    public abstract NoteDao noteDao();
    public static synchronized TrackerDatabase getInstance(Context context) {
        if(instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(), TrackerDatabase.class, "tracker_database.db")
                    .fallbackToDestructiveMigration()
                    // .addCallback(roomCallback)
                    .build();
        }
        return instance;
    }

// To create test data on fresh db creation (i.e. new install/wipe data)
// uncomment the .addCallback method and the following code below
//
//    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {
//        @Override
//        public void onCreate(@NonNull SupportSQLiteDatabase db) {
//            super.onCreate(db);
//            new PopulateDbAsyncTask(instance).execute();
//        }
//    };
//
//    private static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void> {
//        private TermDao termDao;
//        private CourseDao courseDao;
//        private AssessmentDao assessmentDao;
//        private MentorDao mentorDao;
//        private NoteDao noteDao;
//
//        private PopulateDbAsyncTask(TrackerDatabase db) {
//            termDao = db.termDao();
//            courseDao = db.courseDao();
//            assessmentDao = db.assessmentDao();
//            mentorDao = db.mentorDao();
//            noteDao = db.noteDao();
//        }
//
//        @Override
//        protected Void doInBackground(Void... voids) {
//            // Terms
//            termDao.insert(new Term("Term 1", (Date.from(Instant.parse("2020-12-01T00:00:00.00Z"))), (Date.from(Instant.parse("2021-05-31T00:00:00.00Z")))));
//
//            // Courses
//            courseDao.insert(new Course("Course 1", (Date.from(Instant.parse("2020-12-01T00:00:00.00Z"))), (Date.from(Instant.parse("2021-01-03T00:00:00.00Z"))), "Completed", 1));
//
//            // Assessments
//            assessmentDao.insert(new Assessment(1,"Test 1",0,(Date.from(Instant.parse("2021-01-01T00:00:00.00Z")))));
//
//            // Mentors
//            mentorDao.insert(new Mentor("Test Mentor", "5551234567", "test@null.com", 1));
//
//            // Notes
//            noteDao.insert(new Note(1, "Test note whoohoo!"));
//            return null;
//        }
//    }
}
