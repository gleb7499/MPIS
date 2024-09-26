package com.example.laba_3;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class SecondActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_second);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        EditText editTextCity = findViewById(R.id.editTextCity);
        EditText editTextStreet = findViewById(R.id.editTextStreet);
        EditText editTextHouse = findViewById(R.id.editTextHouse);
        EditText editTextCorpus = findViewById(R.id.editTextCorpus);
        EditText editTextDoor = findViewById(R.id.editTextDoor);
        EditText editTextFlat = findViewById(R.id.editTextFlat);

        Intent intent = new Intent();

        findViewById(R.id.buttonOK).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String city = editTextCity.getText().toString();
                final String street = editTextStreet.getText().toString();
                final String house = editTextHouse.getText().toString();
                final String corpus = editTextCorpus.getText().toString();
                final String door = editTextDoor.getText().toString();
                final String flat = editTextFlat.getText().toString();

                if (!city.isEmpty() && !street.isEmpty() && !house.isEmpty()) {
                    intent.putExtra("city", city);
                    intent.putExtra("street", street);
                    intent.putExtra("house", house);
                    intent.putExtra("corpus", corpus);
                    intent.putExtra("door", door);
                    intent.putExtra("flat", flat);
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
        });
    }
}