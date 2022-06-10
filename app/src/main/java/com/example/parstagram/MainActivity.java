package com.example.parstagram;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = "MainActivity";
    public static final int REQUEST_CODE = 22;

    private Button logout_button;
    private ImageButton create_post_button;
    private RecyclerView posts_recycler_view;
    protected PostsAdapter adapter;
    protected List<Post> all_posts;
    private SwipeRefreshLayout swipe_container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        logout_button = findViewById(R.id.logout_button);
        create_post_button = findViewById(R.id.create_post_button);
        posts_recycler_view = findViewById(R.id.posts_recycler_view);
        swipe_container = (SwipeRefreshLayout) findViewById(R.id.swipe_container);

        // initialize the array that will hold posts and create a PostsAdapter
        all_posts = new ArrayList<>();
        adapter = new PostsAdapter(this, all_posts);

        // set the adapter on the recycler view
        posts_recycler_view.setAdapter(adapter);
        // set the layout manager on the recycler view
        posts_recycler_view.setLayoutManager(new LinearLayoutManager(this));
        queryPosts();

        logout_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Logging out", Toast.LENGTH_SHORT).show();

                // logout user
                ParseUser.logOut();

                // move to login screen
                Intent i = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(i);
                finish();
            }
        });

        create_post_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // move to activity to create new post
                Intent i = new Intent(MainActivity.this, CreatePostActivity.class);
//                startActivityForResult(i, REQUEST_CODE);
                startActivity(i);
            }
        });

        swipe_container.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                queryPosts();
                swipe_container.setRefreshing(false);
            }
        });
        // Configure the refreshing colors
        swipe_container.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
    }

    private void queryPosts() {
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.include(Post.KEY_USER);
        query.setLimit(20);
        query.addDescendingOrder("createdAt");

        query.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> posts, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Issue with getting posts", e);
                    return;
                }

                for (Post post : posts) {
                    Log.i(TAG, "Post: " + post.getDescription() + ", username: " + post.getUser().getUsername());
                }
                adapter.clear();
                all_posts.addAll(posts);
                adapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "onResume");
        // may need a way here to delay?
        queryPosts();
    }

}