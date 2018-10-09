package np.com.naxa.vso.firebase;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import timber.log.Timber;

public class VSOFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Timber.i("Receivced from %s", remoteMessage.getFrom());
        Timber.i("Payload %s", remoteMessage.getData());
        Timber.i("Body %s", remoteMessage.getNotification().getBody());
    }
}
