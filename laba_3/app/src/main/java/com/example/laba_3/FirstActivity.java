package com.example.laba_3;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class FirstActivity extends AppCompatActivity {

    private Button buttonCallTaxi;
    private TextView textViewRoute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_first);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Intent intent_1 = getIntent();
        Intent intent_2 = new Intent("com.example.laba_3.SecondActivity");

        TextView textViewName = findViewById(R.id.textViewName);
        TextView textViewPhone = findViewById(R.id.textViewPhone);
        textViewRoute = findViewById(R.id.textViewRoute);

        textViewName.setText(getString(R.string.name_and_lastname, intent_1.getStringExtra("name"), intent_1.getStringExtra("lastname")));
        textViewPhone.setText(intent_1.getStringExtra("phone"));

        Button buttonSetPath = findViewById(R.id.buttonSetPath);
        buttonCallTaxi = findViewById(R.id.buttonCallTaxi);

        buttonSetPath.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(intent_2, 3);
            }
        });

        buttonCallTaxi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String route = textViewRoute.getText().toString();
                if (!route.isEmpty()) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(FirstActivity.this);
                    builder.setMessage("Такси успешно заказано!")
                            .setPositiveButton("Спасибо", null)
                            .show();
                }
                else {
                    Toast.makeText(FirstActivity.this, "Укажите маршрут!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 3 && resultCode == RESULT_OK && data != null) {
            String city = data.getStringExtra("city");
            String street = data.getStringExtra("street");
            String house = data.getStringExtra("house");
            String corpus = data.getStringExtra("corpus");
            String door = data.getStringExtra("door");
            String flat = data.getStringExtra("flat");

            String route = "г. " + city + ", ул. " + street + ", дом " + house;
            if (corpus != null && !corpus.isEmpty()) {
                route += ", корпус " + corpus;
            }
            if (door != null && !door.isEmpty()) {
                route += ", подъезд " + door;
            }
            if (flat != null && !flat.isEmpty()) {
                route += ", кв. " + flat;
            }
            textViewRoute.setText(route);

            buttonCallTaxi.setEnabled(true);
            buttonCallTaxi.setAlpha(1.0f);
        }
    }
}