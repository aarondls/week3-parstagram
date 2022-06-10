package com.example.parstagram;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.parceler.Parcels;

import java.io.File;
import java.util.List;

public class CreatePostActivity extends AppCompatActivity {
    public static final String TAG = "CreatePostActivity";
    public static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 22;

    private EditText description_edittext;
    private Button take_pic_button;
    private Button submit_button;
    private ImageView post_image_imageview;
    private File photo_file;
    private String photo_filename = "photo.jpg";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);

        description_edittext = findViewById(R.id.description_edittext);
        take_pic_button = findViewById(R.id.take_pic_button);
        submit_button = findViewById(R.id.submit_button);
        post_image_imageview = findViewById(R.id.post_image_imageview);

        take_pic_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchCamera();
            }
        });

        submit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String description = description_edittext.getText().toString();
                if (description.isEmpty()) {
                    Toast.makeText(CreatePostActivity.this, "Description can't be empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (photo_file == null || post_image_imageview.getDrawable() == null) {
                    Toast.makeText(CreatePostActivity.this, "There is no image", Toast.LENGTH_SHORT).show();
                    return;
                }
                Post post = new Post();
                post.setDescription(description);
                post.setImage(new ParseFile(photo_file));
                post.setUser(ParseUser.getCurrentUser());
                SavePost(post);

                // return to main activity happens inside save post
//                Intent intent = new Intent();
//                intent.putExtra("post", Parcels.wrap(post));
//                intent.putExtra("post", post);
//                setResult(RESULT_OK, intent);
//                finish();
            }
        });
    }

    private void launchCamera() {
        // create Intent to take a picture and return control to the calling application
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        photo_file = getPhotoFileUri(photo_filename);

        Uri fileProvider = FileProvider.getUriForFile(CreatePostActivity.this, "com.codepath.fileprovider", photo_file);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);

        if (intent.resolveActivity(getPackageManager()) != null) {
            // Start the image capture intent to take photo
            startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
        }
    }

    // Returns the File for a photo stored on disk given the fileName
    public File getPhotoFileUri(String fileName) {
        File mediaStorageDir = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), TAG);

        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()){
            Log.d(TAG, "failed to create directory");
        }

        return new File(mediaStorageDir.getPath() + File.separator + fileName);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Bitmap takenImage = BitmapFactory.decodeFile(photo_file.getAbsolutePath());
                // Load the taken image into a preview
                post_image_imageview.setImageBitmap(takenImage);
            } else {
                Toast.makeText(this, "Picture wasn't taken!", Toast.LENGTH_SHORT).show();
            }
        }
    }


    private void SavePost(Post post) {
        // Save post to parse

        // use save instead of saveinbackground to hold up until it is sent
        // may want to use refresh indicator

        post.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Error while saving", e);
                    Toast.makeText(CreatePostActivity.this, "Error while saving", Toast.LENGTH_SHORT).show();
                    finish();
                    return;
                }
                Log.i(TAG, "Saved post");
                // Clear description field
                description_edittext.setText("");
                post_image_imageview.setImageResource(0);
                finish();
            }
        });

    }
}