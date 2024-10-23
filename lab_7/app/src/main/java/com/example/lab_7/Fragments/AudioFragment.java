package com.example.lab_7.Fragments;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;

import com.example.lab_7.R;

public class AudioFragment extends Fragment {

    private Button buttonAudio;

    public AudioFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_audio, container, false);
        buttonAudio = view.findViewById(R.id.buttonAudio);
        buttonAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_audio = new Intent(Intent.ACTION_PICK, MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);
                registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null && result.getData().getData() != null) {
                        Uri uri = result.getData().getData();
                        MediaPlayer mediaPlayer = new MediaPlayer();
                        try {
                            mediaPlayer.setDataSource(requireContext(), uri);
                            mediaPlayer.prepare();
                            mediaPlayer.start();
                        } catch (Exception e) {
                            Toast.makeText(requireContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                }).launch(intent_audio);
            }
        });
        return view;
    }
}