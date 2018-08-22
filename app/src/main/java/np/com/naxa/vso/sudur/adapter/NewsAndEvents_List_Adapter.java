package np.com.naxa.vso.sudur.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;


import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import np.com.naxa.vso.R;
import np.com.naxa.vso.sudur.model.NewsAndEventsModel;

/**
 * Created by Samir on 11/7/2016.
 */

public class NewsAndEvents_List_Adapter extends RecyclerView.Adapter<NewsAndEvents_List_Adapter.ContactViewHolder> implements View.OnClickListener {


    private List<NewsAndEventsModel> newsList;
    Context context;

    public NewsAndEvents_List_Adapter(Context context, List<NewsAndEventsModel> cList) {
        this.newsList = cList;
        this.context = context;
    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }

    @Override
    public void onBindViewHolder(ContactViewHolder contactViewHolder, int i) {
        NewsAndEventsModel ci = newsList.get(i);
        final SharedPreferences wmbPreference = PreferenceManager
                .getDefaultSharedPreferences(context);

        contactViewHolder.tvNewsTitle.setText(ci.news_title_np);
        contactViewHolder.tvNewsDesc.setText(ci.news_desc_np);
        contactViewHolder.tvNewsDate.setText(ci.news_time_np);
        contactViewHolder.tvNewsTime.setText(ci.news_date_np);

        String Img_Thumb_Url = ci.mThumbnail;

        if(Img_Thumb_Url != null) {
            Glide.with(context)
                    .load(Img_Thumb_Url)
                    .thumbnail(0.5f)
                    .override(60, 60)
                    .into(contactViewHolder.imageViewThumb);
        }
    }

    @Override
    public ContactViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.cardview_news_row, viewGroup, false);

        return new ContactViewHolder(itemView);
    }

    @Override
    public void onClick(View v) {

    }


    public static class ContactViewHolder extends RecyclerView.ViewHolder {
        protected TextView tvNewsTitle, tvNewsDesc, tvNewsDate, tvNewsTime, tvThumbUrl;
        protected ImageView imageViewThumb, imageViewDetail;
        String img_Url;

        public ContactViewHolder(View v) {
            super(v);
            tvNewsTitle = (TextView) v.findViewById(R.id.news_title);
            tvNewsDesc = (TextView) v.findViewById(R.id.news_detail);
            tvNewsDate = (TextView) v.findViewById(R.id.feed_date);
            tvNewsTime = (TextView) v.findViewById(R.id.feed_time);
//            imageView = (ImageView) v.findViewById(R.id.project_imageView);
            imageViewThumb = (ImageView) v.findViewById(R.id.project_imageView_thumb);


        }
    }


    private InputStream OpenHttpConnection(String tvThumbUrl)
            throws IOException
    {
        InputStream in = null;
        int response = -1;

        URL url = new URL(tvThumbUrl);
        URLConnection conn = url.openConnection();

        if (!(conn instanceof HttpURLConnection))
            throw new IOException("Not an HTTP connection");

        try{
            HttpURLConnection httpConn = (HttpURLConnection) conn;
            httpConn.setAllowUserInteraction(false);
            httpConn.setInstanceFollowRedirects(true);
            httpConn.setRequestMethod("GET");
            httpConn.connect();

            response = httpConn.getResponseCode();
            if (response == HttpURLConnection.HTTP_OK) {
                in = httpConn.getInputStream();
            }
        }
        catch (Exception ex)
        {
            throw new IOException("Error connecting");
        }
        return in;
    }


    private Bitmap DownloadImage(String tvThumbUrl) {
        Bitmap bitmap = null;
        InputStream in = null;
        try {
            in = OpenHttpConnection(tvThumbUrl);
            bitmap = BitmapFactory.decodeStream(in);
            in.close();
        } catch (IOException e1) {

            e1.printStackTrace();
            Log.d("Nishin","error"+e1);
        }
        return bitmap;

    }

}

