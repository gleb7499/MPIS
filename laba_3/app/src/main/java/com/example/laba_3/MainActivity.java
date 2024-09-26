package com.example.laba_3;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private EditText editTextName, editTextLastname, editTextPhone;
    SharedPreferences sharedPreferences;

    @Override
    protected void onStop() {
        Log.d("MainActivity", "onStop called");
        super.onStop();
    }

    @Override
    protected void onPause() {
        Log.d("MainActivity", "onPause called");
        super.onPause();
    }

    @Override
    protected void onResume() {
        Log.d("MainActivity", "onResume called");
        super.onResume();
    }

    @Override
    protected void onStart() {
        Log.d("MainActivity", "onStart called");
        super.onStart();
    }

    @Override
    protected void onDestroy() {
        Log.d("MainActivity", "onDestroy called");
        super.onDestroy();
        sharedPreferences = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("name", editTextName.getText().toString());
        editor.putString("lastname", editTextLastname.getText().toString());
        editor.putString("phone", editTextPhone.getText().toString());
        editor.apply();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("MainActivity", "onCreate called");
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        editTextName = findViewById(R.id.editTextName);
        editTextLastname = findViewById(R.id.editTextLastname);
        editTextPhone = findViewById(R.id.editTextPhone);

        Button button_registration = findViewById(R.id.buttonRegistration);

        sharedPreferences = getPreferences(MODE_PRIVATE);
        String name = sharedPreferences.getString("name", "");
        String lastname = sharedPreferences.getString("lastname", "");
        String phone = sharedPreferences.getString("phone", "");

        if (!name.isEmpty() && !lastname.isEmpty() && !phone.isEmpty()) {
            editTextName.setText(name);
            editTextLastname.setText(lastname);
            editTextPhone.setText(phone);
            button_registration.setText(getString(R.string.log_in));
        }

        Intent intent = new Intent(this, FirstActivity.class);

        button_registration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String name = editTextName.getText().toString();
                final String lastname = editTextLastname.getText().toString();
                final String phone = editTextPhone.getText().toString();

                if (!name.isEmpty() && !lastname.isEmpty() && !phone.isEmpty()) {
                    intent.putExtra("name", name);
                    intent.putExtra("lastname", lastname);
                    intent.putExtra("phone", phone);
                    startActivity(intent);
                }
            }
        });
    }
}