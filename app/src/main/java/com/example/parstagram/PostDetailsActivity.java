package com.example.parstagram;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import org.parceler.Parcels;

import java.util.Date;

public class PostDetailsActivity extends AppCompatActivity {
    public static final String TAG = "PostDetailsActivity";
    private String post_object_id;

    private TextView detailed_username_text_view;
    private ImageView detailed_post_image_image_view;
    private TextView detailed_description_text_view;
    private TextView timestamp_text_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_details);

        detailed_username_text_view = findViewById(R.id.detailed_username_text_view);
        detailed_post_image_image_view = findViewById(R.id.detailed_post_image_image_view);
        detailed_description_text_view = findViewById(R.id.detailed_description_text_view);
        timestamp_text_view = findViewById(R.id.timestamp_text_view);

        // get passed post
        post_object_id = (String) Parcels.unwrap(getIntent().getParcelableExtra(Post.class.getSimpleName()));

        // set all relevant information
        readObject();
    }

    /**
     * Sets all relevant information of the post using the post object id
     */
    private void readObject() {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Post");
        query.include(Post.KEY_USER);

        query.getInBackground(post_object_id, (object, e) -> {
            if (e == null) {
                // get necessary information
                Post post = (Post) object;
                detailed_username_text_view.setText(post.getUser().getUsername());
                detailed_description_text_view.setText(post.getDescription());
                ParseFile image = post.getImage();
                if (image != null) {
                    Glide.with(this).load(image.getUrl()).into(detailed_post_image_image_view);
                }
                timestamp_text_view.setText(calculateTimeAgo(post.getCreatedAt()));
            } else {
                Log.e(TAG, "Unable to read object", e);
            }
        });
    }

    /**
     * Returns the time ago as an English phrase given a date.
     *
     * @param createdAt the date to be converted to English
     * @return
     */
    private static String calculateTimeAgo(Date createdAt) {

        final int SECOND_MILLIS = 1000;
        final int MINUTE_MILLIS = 60 * SECOND_MILLIS;
        final int HOUR_MILLIS = 60 * MINUTE_MILLIS;
        final int DAY_MILLIS = 24 * HOUR_MILLIS;

        try {
            createdAt.getTime();
            long time = createdAt.getTime();
            long now = System.currentTimeMillis();

            final long diff = now - time;
            if (diff < MINUTE_MILLIS) {
                return "just now";
            } else if (diff < 2 * MINUTE_MILLIS) {
                return "a minute ago";
            } else if (diff < 50 * MINUTE_MILLIS) {
                return diff / MINUTE_MILLIS + "m go";
            } else if (diff < 90 * MINUTE_MILLIS) {
                return "an hour ago";
            } else if (diff < 24 * HOUR_MILLIS) {
                return diff / HOUR_MILLIS + "h go";
            } else if (diff < 48 * HOUR_MILLIS) {
                return "yesterday";
            } else {
                return diff / DAY_MILLIS + "d ago";
            }
        } catch (Exception e) {
            Log.i("Error:", "getRelativeTimeAgo failed", e);
            e.printStackTrace();
        }

        return "";
    }
}