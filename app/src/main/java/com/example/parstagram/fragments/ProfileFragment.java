package com.example.parstagram.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.parstagram.CreatePostActivity;
import com.example.parstagram.LoginActivity;
import com.example.parstagram.PostsAdapter;
import com.example.parstagram.R;
import com.parse.ParseUser;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {
    private Button logout_button;
    private TextView profile_username_textview;
    private TextView profile_date_created_textview;

    public ProfileFragment() {
        // Required empty public constructor
    }

    // The onCreateView method is called when Fragment should create its View object hierarchy,
    // either dynamically or via XML layout inflation.
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        // Defines the xml file for the fragment
        return inflater.inflate(R.layout.fragment_profile, parent, false);
    }

    // This event is triggered soon after onCreateView().
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // Setup any handles to view objects here

        logout_button = view.findViewById(R.id.logout_button);
        profile_username_textview = view.findViewById(R.id.profile_username_textview);
        profile_date_created_textview = view.findViewById(R.id.profile_date_created_textview);

        profile_username_textview.setText("Account username: " + ParseUser.getCurrentUser().getUsername());
        profile_date_created_textview.setText("Date created: " + ParseUser.getCurrentUser().getCreatedAt().toString());

        logout_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Logging out", Toast.LENGTH_SHORT).show();

                // logout user
                ParseUser.logOut();

                // move to login screen
                Intent i = new Intent(getContext(), LoginActivity.class);
                startActivity(i);
                getActivity().finish();
            }
        });
    }
}