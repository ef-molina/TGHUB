package com.example.tailgatehub;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tailgatehub.adapters.PostAdapter;
import com.example.tailgatehub.models.Post;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class FeedActivity extends AppCompatActivity {

    RecyclerView recyclerView; // this is the view in the activity feed that allows us to make a dynamic list of posts
    ArrayList<Post> postList; // this is an array list of Post objects
    PostAdapter postAdapter; // this class converts the Arraylist of Post objects to UI items


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_feed);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.feed), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        recyclerView = findViewById(R.id.feedRecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        loadPost();
    }

    /**
     * This method will be responsible for fetching data from the backend
     * It calls on our PostRepository().getPost()
     * Which returns an array list of Post data
     * Then if successful, notifies the postAdapter to refresh the UI
     * */
    private void loadPost(){
        try {
            // get JSON data array
            final JSONArray jsonArray = getJsonArray();
            postList = new ArrayList<>();

            Log.d("JSON_load", "length: " + jsonArray.length());

            // loop through the array and create a post object
            // for all the json objects
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject postObject = jsonArray.getJSONObject(i);

                // store all values into strings
                String postId = postObject.getString("postId");
                String userId = postObject.getString("userId");
                String content = postObject.getString("content");
                String img = postObject.getString("imageUrl");

                // make a new post object
                Post post = new Post(postId, userId, content, img);

                // add it to the list
                postList.add(post);
            }

            Log.d("JSON_load", "post list size: " + postList.size());


            /*
             * This is responsible for turning the Post objects
             * into an xml feed_post_card
             * */
            if (postAdapter == null) {
                postAdapter = new PostAdapter(postList);
                recyclerView.setAdapter(postAdapter);
            } else {
                postAdapter.notifyDataSetChanged();
            }

            recyclerView.getViewTreeObserver().addOnGlobalLayoutListener(() -> {
                Log.d("RecyclerView", "Child count: " + recyclerView.getChildCount());
            });


        } catch (IOException | JSONException e) {
            Toast.makeText(this, "Error Loading Posts", Toast.LENGTH_SHORT).show();
            throw new RuntimeException(e);
        }
    }

    @NonNull
    private JSONArray getJsonArray() throws IOException, JSONException {
        AssetManager assetManager = getAssets();
        InputStream inputStream = assetManager.open("mock_post.json");

        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
        StringBuilder jsonString = new StringBuilder();
        String line;

        while ((line = reader.readLine()) != null) {
            jsonString.append(line);
        }

        reader.close();
        inputStream.close();

        // Convert to JSON array
        return new JSONArray(jsonString.toString());
    }
}

