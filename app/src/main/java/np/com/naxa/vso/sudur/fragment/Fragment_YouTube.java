package np.com.naxa.vso.sudur.fragment;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;

import np.com.naxa.vso.sudur.model.Constants;


public class Fragment_YouTube extends YouTubePlayerSupportFragment {
    private FragmentActivity myContext;
    public static Fragment_YouTube newInstance(String url) {

        Fragment_YouTube f = new Fragment_YouTube();

        Bundle b = new Bundle();
        b.putString("url", url);

        f.setArguments(b);
        f.init();

        return f;
    }

    private void init() {

        initialize(Constants.YOUTUBE_DEVELOPER_KEY, new YouTubePlayer.OnInitializedListener() {

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider arg0, YouTubeInitializationResult arg1) { }

            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player, boolean wasRestored) {
                if (!wasRestored) {
                    player.setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT);
                    player.cueVideo(getArguments().getString("url"));
                }
            }
        });
    }
}

