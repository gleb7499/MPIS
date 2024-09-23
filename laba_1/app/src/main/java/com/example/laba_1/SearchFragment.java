package com.example.laba_1;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.constraintlayout.motion.widget.MotionLayout;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.module.AppGlideModule;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class SearchFragment extends Fragment {

    private MotionLayout motionLayout;
    private ProgressBar progressBar;
    private Button button;
    private EditText editText;
    private ImageView imageView;

    private boolean flag_for_button = false;

    private String rawUrl;

    public SearchFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_search, container, false);

        motionLayout = view.findViewById(R.id.motionLayout);
        progressBar = view.findViewById(R.id.progressBar);
        button = view.findViewById(R.id.button_for_searching);
        editText = view.findViewById(R.id.input_for_searching);
        imageView = view.findViewById(R.id.imageView);

        motionLayout.setTransitionListener(new MotionLayout.TransitionListener() {
            @Override
            public void onTransitionStarted(MotionLayout motionLayout, int startId, int endId) {
                // Анимация началась
            }

            @Override
            public void onTransitionChange(MotionLayout motionLayout, int startId, int endId, float progress) {
                // Анимация в процессе
            }

            @Override
            public void onTransitionCompleted(MotionLayout motionLayout, int currentId) {
                // Анимация завершена
                progressBar.clearAnimation();
                progressBar.setVisibility(View.INVISIBLE);
                if (rawUrl != null) {
                    Glide.with(SearchFragment.this)
                            .load(rawUrl)
                            .into(imageView);
                }
                imageView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onTransitionTrigger(MotionLayout motionLayout, int triggerId, boolean positive, float progress) {
                // Триггер анимации
            }
        });


        // Установить прослушку
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ButtonClick(v);
            }
        });

        return view;
    }

    public void ButtonClick(View view) {
        String query = editText.getText().toString();
        if (!query.isEmpty()) {
            flag_for_button = true;
            imageView.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.VISIBLE);
            motionLayout.transitionToEnd();

            String accessKey = BuildConfig.UNSPLASH_ACCESS_KEY;
            String url = "https://api.unsplash.com/search/photos?query=" + query + "&per_page=1&client_id=" + accessKey;

            new FetchImageTask().execute(url);
        }
    }

    private class FetchImageTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            Log.d("FetchImageTask", "doInBackground started");
            String jsonString;
            try {
                jsonString = getJsonFromUrl(urls[0]);
                Log.d("FetchImageTask", "JSON received: " + jsonString);
                JSONObject jsonObject = new JSONObject(jsonString);
                JSONArray results = jsonObject.getJSONArray("results");
                JSONObject result = results.getJSONObject(0);
                JSONObject urlsObject = result.getJSONObject("urls");
                String rawUrl = urlsObject.getString("raw");
                Log.d("FetchImageTask", "Image URL: " + rawUrl);
                return rawUrl;
            } catch (IOException | JSONException e) {
                e.printStackTrace();
                Log.e("FetchImageTask", "Error: " + e.getMessage());
                return null;
            }
        }

        @Override
        protected void onPostExecute(String rawUrl) {
            super.onPostExecute(rawUrl);
            Log.d("FetchImageTask", "onPostExecute: " + rawUrl);
            SearchFragment.this.rawUrl = rawUrl;
        }
    }


    public String getJsonFromUrl(String urlString) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            return readStream(in);
        } finally {
            urlConnection.disconnect();
        }
    }

    private String readStream(InputStream in) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        StringBuilder result = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            result.append(line);
        }
        return result.toString();
    }
}
