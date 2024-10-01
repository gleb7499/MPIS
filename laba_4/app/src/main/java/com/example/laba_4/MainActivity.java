package com.example.laba_4;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import okhttp3.*;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.laba_4.adapter.PhotoAdapter;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    public RecyclerView photosRecyclerView;
    public PhotoAdapter photoAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        EditText editText = findViewById(R.id.editTextText);


        findViewById(R.id.buttonGetData).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String query = editText.getText().toString();
                if (!query.isEmpty()) {
                    String url = "https://pixabay.com/api/?key=" + BuildConfig.UNSPLASH_ACCESS_KEY + "&q=" + query.replaceAll(" ", "+") + "&image_type=photo&lang=ru&orientation=horizontal&per_page=15";

                }
            }
        });
        }



}