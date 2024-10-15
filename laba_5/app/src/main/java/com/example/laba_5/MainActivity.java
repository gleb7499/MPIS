package com.example.laba_5;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.laba_5.Adapters.ViewPagerAdapter;
import com.example.laba_5.Database.DatabaseManager;
import com.example.laba_5.Fragments.FragmentAdd;
import com.example.laba_5.Fragments.FragmentDel;
import com.example.laba_5.Fragments.FragmentShow;
import com.example.laba_5.Fragments.FragmentUpdate;
import com.example.laba_5.Model.NotesManager;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onDestroy() {
        DatabaseManager.getDatabase().close();
        NotesManager.clearNotes();
        super.onDestroy();
    }

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        DatabaseManager.setDatabase(this);

        NotesManager.initializeNotes();

        TabLayout tabLayout = findViewById(R.id.tabLayout);
        ViewPager2 viewPager = findViewById(R.id.ViewPager);

        setMargins(tabLayout);

        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new FragmentShow());
        fragments.add(new FragmentAdd());
        fragments.add(new FragmentDel());
        fragments.add(new FragmentUpdate());

        ViewPagerAdapter pagerAdapter = new ViewPagerAdapter(this, fragments);
        viewPager.setAdapter(pagerAdapter);

        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            String[] titles = {"Show", "Add", "Del", "Update"};
            tab.setText(titles[position]);
        }).attach();
    }
}