package com.example.tailgatehub.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tailgatehub.R;
import com.example.tailgatehub.models.Post;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * This class converts an arraylist of posts to
 * UI items.
 * */
public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {

//    private Context context;
    private ArrayList<Post> postList; // list of post to display

    // constructor
    public PostAdapter(ArrayList<Post> postList){
        this.postList = postList;
    }

    /**
     * This method is called when RecyclerView needs a new ViewHolder
     * (i.e.) when user is scrolling, or creating views
     * */
    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // We create a view: LayoutInflater turns xml layout into a view object
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.feed_post_card, parent, false);

        // we then return that view
        return new PostViewHolder(view);
    }

    /***
     * This is called to bind the data into the PostViewHolder
     */
    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        Post post = postList.get(position); // get post at current position
        Log.d("PostAdapter", "Binding post at position: " + position); // Debugging log
        // Set data inside the textViews
        holder.username.setText(post.getUserId());
        holder.content.setText(post.getContent());

        // Load image using Picasso
        Picasso.get()
                .load(post.getImageUrl())
                .placeholder(R.drawable.baseline_account_circle_24)
                .error(R.drawable.baseline_account_circle_24)
                .into(holder.img);
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    /**
     * This helper class holds a reference to UI elements inside feed_post_card.xml
     * The main Post adapter class reuses this to create new post cards
     * */
    public static class PostViewHolder extends RecyclerView.ViewHolder {
        TextView username;
        TextView content;
        ImageView img;

        // this takes a view and finds its child elements
        public PostViewHolder(@NonNull View itemView) {
            super(itemView);

            //// views by ids feed_card_post.xml
            username = itemView.findViewById(R.id.feed_card_username);
            content = itemView.findViewById(R.id.feed_card_content);
            img = itemView.findViewById(R.id.feed_card_image);
        }
    }
}
