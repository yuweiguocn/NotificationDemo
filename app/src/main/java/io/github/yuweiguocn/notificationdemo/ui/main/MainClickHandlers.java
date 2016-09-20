package io.github.yuweiguocn.notificationdemo.ui.main;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.view.View;

import java.util.UUID;

import io.github.yuweiguocn.notificationdemo.R;
import io.github.yuweiguocn.notificationdemo.ui.about.AboutActivity;
import io.github.yuweiguocn.notificationdemo.ui.special.SpecialActivity;

/**
 * Created by growth on 9/14/16.
 */
public class MainClickHandlers {

    private Context ct;

    NotificationCompat.Builder mBuilder;
    private int notifyID = 1;
    private int numMessages = 0;

    private boolean hasStart;
    private boolean hasIndeterminateStart;

    public MainClickHandlers(Context ct) {
        this.ct = ct;
        String title = "New Message";
        String content = "You've received new messages.";
        mBuilder = generateBuilder(title, content);
    }


    private NotificationCompat.Builder generateBuilder(String title, String content) {
        return new NotificationCompat.Builder(ct)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(content)
                .setTicker(content)
                .setAutoCancel(true)
                .setDefaults(NotificationCompat.DEFAULT_ALL);
    }

    private void notify(int notifyID, NotificationCompat.Builder mBuilder) {
        // Creates an explicit intent for an Activity in your app
        Intent resultIntent = new Intent(ct, AboutActivity.class);
        // The stack builder object will contain an artificial back stack for the
        // started Activity.
        // This ensures that navigating backward from the Activity leads out of
        // your application to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(ct);
        // Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(AboutActivity.class);
        // Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) ct.getSystemService(Context.NOTIFICATION_SERVICE);
        // mId allows you to update the notification later on.
        mNotificationManager.notify(notifyID, mBuilder.build());
    }


    public void simpleNotification(View view) {
        String title = "Simple Notification";
        String content = "Hello World!";
        NotificationCompat.Builder mBuilder = generateBuilder(title, content);
        notify(UUID.randomUUID().hashCode(), mBuilder);
    }

    public void specialActivityNotification(View view) {
        String title = "Special Activity Notification";
        String content = "Android 7.0 Nougat is here!";

        NotificationCompat.Builder mBuilder = generateBuilder(title, content);

        // Creates an Intent for the Activity
        Intent notifyIntent =
                new Intent(ct, SpecialActivity.class);
        // Sets the Activity to start in a new, empty task
        notifyIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        // Creates the PendingIntent
        PendingIntent notifyPendingIntent =
                PendingIntent.getActivity(ct, 0, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        // Puts the PendingIntent into the notification builder
        mBuilder.setContentIntent(notifyPendingIntent);
        // Notifications are issued by sending them to the
        // NotificationManager system service.
        NotificationManager mNotificationManager =
                (NotificationManager) ct.getSystemService(Context.NOTIFICATION_SERVICE);
        // Builds an anonymous Notification object from the builder, and
        // passes it to the NotificationManager
        mNotificationManager.notify(UUID.randomUUID().hashCode(), mBuilder.build());

    }


    public void bigTextStyleNotification(View view) {
        String title = "Big Text Style Notification";
        String content = "Android 7.0 Nougat is here! Get your apps ready for the latest version of Android, with new system behaviors to save battery and memory.";
        NotificationCompat.Builder mBuilder = generateBuilder(title, content);

        NotificationCompat.BigTextStyle bigTextStyle = new NotificationCompat.BigTextStyle();
        bigTextStyle.setBigContentTitle("Big Text Style Title");
        bigTextStyle.bigText("Android 7.0 Nougat is here! Get your apps ready for the latest version of Android, with new system behaviors to save battery and memory.");
        // Moves the expanded layout object into the notification object.
        mBuilder.setStyle(bigTextStyle);
        // Issue the notification here.
        notify(UUID.randomUUID().hashCode(), mBuilder);
    }


    public void inboxStyleNotification(View view) {
        String title = "Inbox Style Notification";
        String content = "Android 7.0 Nougat is here! Get your apps ready for the latest version of Android, with new system behaviors to save battery and memory.";
        NotificationCompat.Builder mBuilder = generateBuilder(title, content);

        NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
        // Sets a title for the Inbox in expanded layout
        inboxStyle.setBigContentTitle("Inbox Style Title");
        // Moves events into the expanded layout
        inboxStyle.setSummaryText("Inbox Style Summary Text");
        inboxStyle.addLine("Inbox Style Line 1");
        inboxStyle.addLine("Inbox Style Line 2");
        inboxStyle.addLine("Inbox Style Line 3");
        // Moves the expanded layout object into the notification object.
        mBuilder.setStyle(inboxStyle);
        // Issue the notification here.
        notify(UUID.randomUUID().hashCode(), mBuilder);
    }


    public void bigPictureStyleNotification(View view) {
        String title = "Big Picture Style Notification";
        String content = "Android 7.0 Nougat is here! Get your apps ready for the latest version of Android, with new system behaviors to save battery and memory.";
        NotificationCompat.Builder mBuilder = generateBuilder(title, content);

        NotificationCompat.BigPictureStyle bigPictureStyle = new NotificationCompat.BigPictureStyle();
        bigPictureStyle.setBigContentTitle("Big Picture");
        bigPictureStyle.setSummaryText("Big Picture Summary Text");
        bigPictureStyle.bigPicture(BitmapFactory.decodeResource(ct.getResources(), R.drawable.big_image_android));
        bigPictureStyle.bigLargeIcon(BitmapFactory.decodeResource(ct.getResources(), R.mipmap.ic_launcher));
        // Moves the expanded layout object into the notification object.
        mBuilder.setStyle(bigPictureStyle);
        // Issue the notification here.
        notify(UUID.randomUUID().hashCode(), mBuilder);
    }

    public void mediaStyleNotification(View view) {

        // Creates an explicit intent for an Activity in your app
        Intent resultIntent = new Intent(ct, AboutActivity.class);
        // The stack builder object will contain an artificial back stack for the
        // started Activity.
        // This ensures that navigating backward from the Activity leads out of
        // your application to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(ct);
        // Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(AboutActivity.class);
        // Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent intent =
                stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        String title = "Wonderful music";
        String content = "My Awesome Band";

        NotificationCompat.Builder mBuilder = generateBuilder(title, content);
        mBuilder.setSmallIcon(R.drawable.music);
        mBuilder.addAction(R.drawable.pre, "Pre", intent); // #0
        mBuilder.addAction(R.drawable.pause, "Pause", intent);  // #1
        mBuilder.addAction(R.drawable.next, "Next", intent);     // #2
        mBuilder.setStyle(new android.support.v7.app.NotificationCompat.MediaStyle().setShowActionsInCompactView(1/* #1: pause button */));
        mBuilder.setLargeIcon(BitmapFactory.decodeResource(ct.getResources(), R.drawable.record));
        mBuilder.setAutoCancel(false);
        mBuilder.build();

        notify(9, mBuilder);
    }

    public void updateNotification(View view) {
        // Start of a loop that processes data and then notifies the user
        ++numMessages;
        mBuilder.setContentText("You've received "+numMessages+" messages.")
                .setNumber(numMessages);
        // Because the ID remains unchanged, the existing notification is
        // updated.
        // mId allows you to update the notification later on.
        notify(notifyID, mBuilder);
    }



    public void progressNotification(View view) {
        if (hasStart) {
            return;
        }

        final NotificationManager mNotifyManager =
                (NotificationManager) ct.getSystemService(Context.NOTIFICATION_SERVICE);

        final String title = "Picture Download";
        String content = "Download in progress";
        final NotificationCompat.Builder mBuilder = generateBuilder(title, content);
        mBuilder.setDefaults(NotificationCompat.DEFAULT_LIGHTS);

        // Start a lengthy operation in a background thread
        new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        hasStart = true;
                        int incr;
                        // Do the "lengthy" operation 20 times
                        for (incr = 0; incr <= 100; incr += 5) {
                            // Sets the progress indicator to a max value, the
                            // current completion percentage, and "determinate"
                            // state
                            mBuilder.setProgress(100, incr, false);
                            // Displays the progress bar for the first time.
                            mNotifyManager.notify(0, mBuilder.build());
                            // Sleeps the thread, simulating an operation
                            // that takes time
                            try {
                                // Sleep for 1 seconds
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        // When the loop is finished, updates the notification
                        mBuilder.setContentText("Download complete")
                                // Removes the progress bar
                                .setProgress(0, 0, false);
                        mBuilder.setDefaults(NotificationCompat.DEFAULT_ALL);
                        mNotifyManager.notify(0, mBuilder.build());
                        hasStart = false;
                    }
                }
                // Starts the thread by calling the run() method in its Runnable
        ).start();

    }

    public void progressIndeterminateNotification(View view) {
        if (hasIndeterminateStart) {
            return;
        }

        final NotificationManager mNotifyManager =
                (NotificationManager) ct.getSystemService(Context.NOTIFICATION_SERVICE);

        final String title = "Picture Download";
        String content = "Download in progress";
        final NotificationCompat.Builder mBuilder = generateBuilder(title, content);
        mBuilder.setDefaults(NotificationCompat.DEFAULT_LIGHTS);

        // Start a lengthy operation in a background thread
        new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        hasIndeterminateStart = true;
                        int incr;
                        // Do the "lengthy" operation 20 times
                        for (incr = 0; incr <= 100; incr += 5) {
                            // Sets the progress indicator to a max value, the
                            // current completion percentage, and "determinate"
                            // state
                            mBuilder.setProgress(0, 0, true);
                            // Displays the progress bar for the first time.
                            mNotifyManager.notify(0, mBuilder.build());
                            // Sleeps the thread, simulating an operation
                            // that takes time
                            try {
                                // Sleep for 1 seconds
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        // When the loop is finished, updates the notification
                        mBuilder.setContentText("Download complete")
                                // Removes the progress bar
                                .setProgress(0, 0, false);
                        mBuilder.setDefaults(NotificationCompat.DEFAULT_ALL);
                        mNotifyManager.notify(0, mBuilder.build());
                        hasIndeterminateStart = false;
                    }
                }
                // Starts the thread by calling the run() method in its Runnable
        ).start();

    }


    public void fullScreenNotification(View view) {
        String title = "Floating Notification by Full Screen Intent";
        String content = "Android 7.0 Nougat is here! Get your apps ready for the latest version of Android, with new system behaviors to save battery and memory.";
        NotificationCompat.Builder mBuilder = generateBuilder(title, content);
        // Creates an explicit intent for an Activity in your app
        Intent resultIntent = new Intent(ct, AboutActivity.class);
        // The stack builder object will contain an artificial back stack for the
        // started Activity.
        // This ensures that navigating backward from the Activity leads out of
        // your application to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(ct);
        // Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(AboutActivity.class);
        // Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent fullScreenIntent =
                stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setFullScreenIntent(fullScreenIntent, true);
        // Issue the notification here.
        notify(UUID.randomUUID().hashCode(), mBuilder);
    }

    public void priorityNotification(View view) {

        // Creates an explicit intent for an Activity in your app
        Intent resultIntent = new Intent(ct, AboutActivity.class);
        // The stack builder object will contain an artificial back stack for the
        // started Activity.
        // This ensures that navigating backward from the Activity leads out of
        // your application to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(ct);
        // Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(AboutActivity.class);
        // Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent intent =
                stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);



        String title = "Floating Notification by priority";
        String content = "Android 7.0 Nougat is here! Get your apps ready for the latest version of Android, with new system behaviors to save battery and memory.";
        NotificationCompat.Builder mBuilder = generateBuilder(title, content);
        mBuilder.setPriority(NotificationCompat.PRIORITY_MAX)
                .setCategory(NotificationCompat.CATEGORY_ALARM)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(content))
                .addAction (R.drawable.dismiss,
                        "Dismiss", intent)
                .addAction (R.drawable.snooze,
                        "Snooze", intent);
        notify(UUID.randomUUID().hashCode(), mBuilder);
    }




}
