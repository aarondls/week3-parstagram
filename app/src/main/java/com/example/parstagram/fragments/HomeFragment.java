package com.example.parstagram.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.parstagram.CreatePostActivity;
import com.example.parstagram.LoginActivity;
import com.example.parstagram.MainActivity;
import com.example.parstagram.Post;
import com.example.parstagram.PostsAdapter;
import com.example.parstagram.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {
    public static final String TAG = "HomeFragment";
    public static final int REQUEST_CODE = 22;

    private ImageButton create_post_button;
    protected List<Post> all_posts;
    private RecyclerView posts_recycler_view;
    protected PostsAdapter adapter;
    private SwipeRefreshLayout swipe_container;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * The onCreateView method is called when Fragment should create its View object hierarchy,
     * either dynamically or via XML layout inflation.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        // Defines the xml file for the fragment
        return inflater.inflate(R.layout.fragment_home, parent, false);
    }

    /**
     * This event is triggered soon after onCreateView(). Any view setup should occur here.  E.g., view lookups and attaching view listeners.
     *
     * @param view
     * @param savedInstanceState
     */
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // Setup any handles to view objects here
        create_post_button = view.findViewById(R.id.create_post_button);
        posts_recycler_view = view.findViewById(R.id.posts_recycler_view);
        swipe_container = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);

        // initialize the array that will hold posts and create a PostsAdapter
        all_posts = new ArrayList<>();
        adapter = new PostsAdapter(getContext(), all_posts);

        // set the adapter on the recycler view
        posts_recycler_view.setAdapter(adapter);
        // set the layout manager on the recycler view
        posts_recycler_view.setLayoutManager(new LinearLayoutManager(getContext()));
        queryPosts();

        create_post_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // move to activity to create new post
                Intent i = new Intent(getContext(), CreatePostActivity.class);
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
    public void onResume() {
        super.onResume();
        Log.i(TAG, "onResume");
        queryPosts();
    }
}