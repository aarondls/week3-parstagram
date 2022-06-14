package com.example.parstagram;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.parse.ParseFile;

import org.parceler.Parcels;

import java.util.List;

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.ViewHolder> {
    private Context context;
    private List<Post> posts;

    public PostsAdapter(Context context, List<Post> posts) {
        this.context = context;
        this.posts = posts;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_post, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Post post = posts.get(position);
        holder.bind(post);
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView username_text_view;
        private ImageView post_image_image_view;
        private TextView description_text_view;
        private String post_object_id;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            username_text_view = itemView.findViewById(R.id.username_text_view);
            post_image_image_view = itemView.findViewById(R.id.post_image_image_view);
            description_text_view = itemView.findViewById(R.id.description_text_view);
            itemView.setOnClickListener(this);
        }

        /**
         * Bind the post data to the view elements
         *
         * @param post  the post to bind to the view
         */
        public void bind(Post post) {
            username_text_view.setText(post.getUser().getUsername());
            description_text_view.setText(post.getDescription());
            ParseFile image = post.getImage();
            if (image != null) {
                Glide.with(context).load(image.getUrl()).into(post_image_image_view);
            }
            post_object_id = post.getObjectId();
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            // make sure the position is valid
            if (position != RecyclerView.NO_POSITION) {
                Post post = posts.get(position);

                // create intent for the new activity
                Intent intent = new Intent(context, PostDetailsActivity.class);
                // pass only the post id since entire post is not parcelable
                intent.putExtra(Post.class.getSimpleName(), Parcels.wrap(post_object_id));
                // show the activity
                context.startActivity(intent);
            }
        }
    }

    /**
     * Remove all existing posts
     */
    public void clear() {
        posts.clear();
        notifyDataSetChanged();
    }

    /**
     * Add a list of posts
     *
     * @param list
     */
    public void addAll(List<Post> list) {
        posts.addAll(list);
        notifyDataSetChanged();
    }
}
