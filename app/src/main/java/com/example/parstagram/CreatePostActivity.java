package com.example.parstagram;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.List;

public class CreatePostActivity extends AppCompatActivity {
    public static final String TAG = "CreatePostActivity";

    private EditText description_edittext;
    private Button take_pic_button;
    private Button submit_button;
    private ImageView post_image_imageview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);

        description_edittext = findViewById(R.id.description_edittext);
        take_pic_button = findViewById(R.id.take_pic_button);
        submit_button = findViewById(R.id.submit_button);
        post_image_imageview = findViewById(R.id.post_image_imageview);
        
        queryPosts();
    }

    private void queryPosts() {
        // Specify which class to query
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        // Can also include other related class, such as user
        query.include(Post.KEY_USER);
        query.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> posts, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Issue with getting posts", e);
                    return;
                }
                for (Post post : posts) {
                    Log.i(TAG, "Post: " + post.getDescription());
                }
            }
        });
    }
}