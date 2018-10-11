package np.com.naxa.vso.firebase;

import android.app.Application;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.atomic.AtomicInteger;

import np.com.naxa.vso.R;
import np.com.naxa.vso.database.databaserepository.MessageHelperRepository;
import np.com.naxa.vso.home.HomeActivity;
import np.com.naxa.vso.home.VSO;
import np.com.naxa.vso.utils.SharedPreferenceUtils;
import np.com.naxa.vso.viewmodel.MessageHelperViewModel;
import timber.log.Timber;

public class VSOFirebaseMessagingService extends FirebaseMessagingService {

    SharedPreferenceUtils sharedPreferenceUtils = new SharedPreferenceUtils(VSO.getInstance());
    String title, description;
    MessageHelperViewModel messageHelperViewModel = ViewModelProviders.of((FragmentActivity) VSO.getInstance()).get(MessageHelperViewModel.class);


    @Override
    public void onNewToken(String token) {
        super.onNewToken(token);
        Timber.i("New token: %s", token);
        sharedPreferenceUtils.setValue(SharedPreferenceUtils.TOKEN_ID, token);

        title = "Mew token received";
        description = token;
        sendNotificationTo_Home(new NotificationData(title, description));
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Timber.i("Receivced from %s", remoteMessage.getFrom());
        Timber.i("Payload %s", remoteMessage.getData());
        title = remoteMessage.getNotification().getTitle();
        Timber.i("Body %s", remoteMessage.getNotification().getBody());
        description = remoteMessage.getNotification().getBody();

        Date date = Calendar.getInstance(new Locale("en", "US")).getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        String formattedDate = df.format(date);

        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT-4:00"));
        Date currentLocalTime = cal.getTime();
        DateFormat date1 = new SimpleDateFormat("KK:mm:ss");
        date1.setTimeZone(TimeZone.getTimeZone("GMT+5:45"));
        String localTime = date1.format(currentLocalTime);


        messageHelperViewModel.insert(new MessageHelper(formattedDate, localTime, remoteMessage.toString(), 1));

        sendNotificationTo_Home(new NotificationData(title, description));

    }


    //HOME
    private void sendNotificationTo_Home(NotificationData notificationData) {

        Intent intent = new Intent(this, HomeActivity.class);
        intent.putExtra(NotificationData.TEXT, notificationData.getTextMessage());

        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        NotificationCompat.Builder notificationBuilder = null;

        notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
//                    .setContentTitle(URLDecoder.decode(notificationData.getTitle(), "UTF-8"))
                .setContentTitle(title)
                .setContentText(description)
                .setAutoCancel(true)
                .setVibrate(new long[]{1000, 1000, 1000, 1000})
                .setLights(Color.RED, 3000, 3000)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setContentIntent(pendingIntent);

        if (notificationBuilder != null) {
            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(getID(), notificationBuilder.build());
        } else {
            Timber.i("No FCM Notification");
        }
    }


    /**
     * Generate unique id for notifications
     *
     * @return
     */
    private final static AtomicInteger notificationId = new AtomicInteger(0);

    public static int getID() {
        return notificationId.incrementAndGet();
    }
}
