package com.example.laba_4;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.laba_4.adapter.PhotoAdapter;
import com.example.laba_4.model.Photo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private void setMargins(View view) {
        ViewCompat.setOnApplyWindowInsetsListener(view, (v, windowInsets) -> {
            Insets insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars());
            ViewGroup.MarginLayoutParams mlp = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
            mlp.leftMargin = insets.left;
            mlp.bottomMargin = insets.bottom;
            mlp.rightMargin = insets.right;
            mlp.topMargin = insets.top;
            v.setLayoutParams(mlp);
            return WindowInsetsCompat.CONSUMED;
        });
    }

    private RecyclerView photosRecyclerView;
    private PhotoAdapter photoAdapter;

    private ProgressBar progressBar;

    private List<Photo> photoList;

    private final static int LENGTH = 15;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        EdgeToEdge.enable(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EditText editTextText = findViewById(R.id.editTextText);
        Button buttonGetData = findViewById(R.id.buttonGetData);
        photosRecyclerView = findViewById(R.id.RecyclerView);
        progressBar = findViewById(R.id.progressBar);

        photoList = new ArrayList<>();

        setMargins(editTextText);
        setMargins(buttonGetData);

        photosRecyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this, LinearLayoutManager.VERTICAL, false));

        buttonGetData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String query = editTextText.getText().toString();
                if (!query.isEmpty()) {
                    progressBar.setVisibility(View.VISIBLE);
                    photosRecyclerView.setVisibility(View.INVISIBLE);
                    String url = "https://pixabay.com/api/?key=" + BuildConfig.UNSPLASH_ACCESS_KEY + "&q=" + query.replaceAll(" ", "+") + "&image_type=photo&lang=ru&orientation=horizontal&per_page=" + LENGTH;
                    Log.d("Query", url);
                    getPhotos(url);
                }
            }
        });
        }

    private void getPhotos(String url) {
        Log.d("", "Start client");
        OkHttpClient client = new OkHttpClient();
        Log.d("", "Start request");
        Request request = new Request.Builder()
                .url(url)
                .build();
        Log.d("", "Start getting response");

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                showError("Ошибка запроса: \n" + e.getMessage());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                Log.d("", "Response: " + response);
                if (response.isSuccessful() && response.body() != null) {
                    String responseBody = response.body().string();
                    try {
                        JSONObject jsonObject = new JSONObject(responseBody);
                        JSONArray hits = jsonObject.getJSONArray("hits");
                        if (hits.length() == 0) {
                            showError("Ничего не найдено!");
                            return;
                        }
                        Log.d("", "hits" + hits);
                        photoList.clear();
                        for (int i = 0; i < LENGTH; ++i) {
                            photoList.add(new Photo(hits.getJSONObject(i).getString("user"), hits.getJSONObject(i).getString("largeImageURL"), hits.getJSONObject(i).getString("userImageURL")));
                        }
                        Log.d("", "start setPhotoAdapter");
                        setPhotoAdapter();
                        Log.d("", "end setPhotoAdapter");
                    } catch (JSONException e) {
                        showError("Ошибка обработки JSON: \n" + e.getMessage());
                    }
                } else {
                    showError("Неизвестная ошибка!");
                }
            }
        });
    }

    private void setPhotoAdapter() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                photoAdapter = new PhotoAdapter(MainActivity.this, photoList);
                photosRecyclerView.setAdapter(photoAdapter);
                progressBar.setVisibility(View.INVISIBLE);
                photosRecyclerView.setVisibility(View.VISIBLE);
            }
        });
    }

    private void showError(final String errorMessage) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setMessage(errorMessage)
                        .setPositiveButton("OK", null)
                        .show();
                progressBar.setVisibility(View.INVISIBLE);
            }
        });

    }

}