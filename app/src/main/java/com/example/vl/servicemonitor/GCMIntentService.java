package com.example.vl.servicemonitor;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.google.android.gcm.GCMBaseIntentService;

import static com.example.vl.servicemonitor.CommonUtilities.SENDER_ID;
import static com.example.vl.servicemonitor.CommonUtilities.displayMessage;

public class GCMIntentService extends GCMBaseIntentService {

    private static final String TAG = "GCMIntentService";

    public GCMIntentService() {
        super(SENDER_ID);
    }

    /**
     * Method called on device registered
     **/
    @Override
    protected void onRegistered(Context context, String registrationId) {
        Log.i(TAG, "Device registered: regId = " + registrationId);
        displayMessage(context, "Your device registred with GCM");
        Log.d("NAME", PushActivity.name);
        ServerUtilities.register(context, PushActivity.name, PushActivity.email, registrationId);
    }

    /**
     * Method called on device un registred
     * */
    @Override
    protected void onUnregistered(Context context, String registrationId) {
        Log.i(TAG, "Device unregistered");
        displayMessage(context, getString(R.string.gcm_unregistered));
        ServerUtilities.unregister(context, registrationId);
    }

    /**
     * Method called on Receiving a new message
     * */
    @Override
    protected void onMessage(Context context, Intent intent) {
        Log.i(TAG, "Received message");
        String message = intent.getExtras().getString("price");

        displayMessage(context, message);
        // notifies user
        generateNotification(context, message);
    }

    /**
     * Method called on receiving a deleted message
     * */
    @Override
    protected void onDeletedMessages(Context context, int total) {
        Log.i(TAG, "Received deleted messages notification");
        String message = getString(R.string.gcm_deleted, total);
        displayMessage(context, message);
        // notifies user
        generateNotification(context, message);
    }

    /**
     * Method called on Error
     * */
    @Override
    public void onError(Context context, String errorId) {
        Log.i(TAG, "Received error: " + errorId);
        displayMessage(context, getString(R.string.gcm_error, errorId));
    }

    @Override
    protected boolean onRecoverableError(Context context, String errorId) {
        // log message
        Log.i(TAG, "Received recoverable error: " + errorId);
        displayMessage(context, getString(R.string.gcm_recoverable_error,
                errorId));
        return super.onRecoverableError(context, errorId);
    }

    /**
     * Issues a notification to inform the user that server has sent a message.
     */
    private static void generateNotification(Context context, String message) {

        /*int icon = R.mipmap.ic_launcher;
        long when = System.currentTimeMillis();

        Intent intentValues = new Intent(context, PushActivity.class);
        intentValues.putExtra(PushActivity.INTENT_KEY, message);

        // both of these approaches now work: FLAG_CANCEL, FLAG_UPDATE; the uniqueInt may be the real solution.
        //PendingIntent pendingIntent = PendingIntent.getActivity(this, uniqueInt, showFullQuoteIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        int uniqueInt = (int) (System.currentTimeMillis() & 0xfffffff);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, uniqueInt, intentValues, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification notification = new NotificationCompat.Builder(context)
                .setTicker(message)
                .setSmallIcon(android.R.drawable.ic_menu_view)
                .setContentTitle(message)
                .setContentText(message)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .build();

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(0, notification);*/


        int icon = R.mipmap.ic_launcher;
        long when = System.currentTimeMillis();
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        //Notification notification = new Notification(icon, message, when);

        String title = context.getString(R.string.app_name);
        Intent resultIntent = new Intent(context, PushActivity.class);

        // ME
        Intent intentValues = new Intent(context, PushActivity.class);
        intentValues.putExtra(PushActivity.INTENT_KEY, message);
        // ME

        // set intent so it does not start a new activity
        resultIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);


        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        // Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(PushActivity.class);
        // Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);

        PendingIntent pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);//.getActivity(context, 0, resultIntent, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        Notification nf = builder.setContentIntent(pendingIntent)
                .setSmallIcon(icon).setTicker(message).setWhen(when)
                .setAutoCancel(true).setContentTitle(title)
                .setContentText(message).build();

        // nf.flags |= Notification.FLAG_AUTO_CANCEL;
        // Play default notification sound
        nf.defaults |= Notification.DEFAULT_SOUND;
        nf.defaults |= Notification.DEFAULT_VIBRATE;

        // Lights
        nf.ledARGB = 0xff00ff00;
        nf.ledOnMS = 300;
        nf.ledOffMS = 1000;
        nf.flags |= Notification.FLAG_SHOW_LIGHTS;

        notificationManager.notify(0, nf);

    }

}