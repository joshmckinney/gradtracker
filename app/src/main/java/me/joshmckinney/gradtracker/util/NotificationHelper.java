package me.joshmckinney.gradtracker.util;

import android.annotation.TargetApi;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import me.joshmckinney.gradtracker.R;

public class NotificationHelper extends ContextWrapper {
    public static final String channelID = "channelID";
    public static final String channelName = "Channel Name";
    public static final String EXTRA_ALARM_TITLE =
            "me.joshmckinney.gradtracker.EXTRA_ALARM_TITLE";
    public static final String EXTRA_ALARM_TYPE =
            "me.joshmckinney.gradtracker.EXTRA_ALARM_TYPE";
    private String titleString;
    private int alarmType;
    private String notifAssessmentTitle = "Assessment Goal";
    private String notifAssessmentMessage = " assessment goal date is here!";
    private String notifCourseTitle = "Course Activity";
    private String notifCourseMessageStart = " is beginning today.";
    private String notifCourseMessageEnd = " is ending today.";
    private NotificationManager mManager;
    public NotificationHelper(Context base, Intent intent) {
        super(base);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannel();
        }
        titleString = intent.getStringExtra(EXTRA_ALARM_TITLE);
        alarmType = Integer.parseInt((intent.getStringExtra(EXTRA_ALARM_TYPE)));
    }
    @TargetApi(Build.VERSION_CODES.O)
    private void createChannel() {
        NotificationChannel channel = new NotificationChannel(channelID, channelName, NotificationManager.IMPORTANCE_HIGH);
        getManager().createNotificationChannel(channel);
    }
    public NotificationManager getManager() {
        if (mManager == null) {
            mManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return mManager;
    }
    public NotificationCompat.Builder getChannelNotification() {
        String title;
        String message;
        switch(alarmType) {
            // Assessment
            case 0:
                title = notifAssessmentTitle;
                message = notifAssessmentMessage;
                break;
            // Course Start
            case 1:
                title = notifCourseTitle;
                message = notifCourseMessageStart;
                break;
            // Course End
            case 2:
                title = notifCourseTitle;
                message = notifCourseMessageEnd;
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + alarmType);
        }
        return new NotificationCompat.Builder(getApplicationContext(), channelID)
                .setContentTitle(title)
                .setContentText(titleString + message)
                .setSmallIcon(R.drawable.ic_assessment);
    }
}
