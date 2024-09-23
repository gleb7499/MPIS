package com.example.laba_1;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;


public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.search);

    }

    PersonFragment personFragment = new PersonFragment();
    SearchFragment searchFragment = new SearchFragment();

    // Переключение экранов нижней панели
    @Override
    public boolean
    onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.search:
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.constlayout, searchFragment)
                        .commit();
                return true;

            case R.id.person:
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.constlayout, personFragment)
                        .commit();
                return true;
        }
        return false;
    }
}
