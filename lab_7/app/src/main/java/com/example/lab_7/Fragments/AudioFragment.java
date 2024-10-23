package com.example.lab_7.Fragments;

import static android.app.Activity.RESULT_OK;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.lab_7.R;

import java.io.IOException;
import java.util.List;

public class AudioFragment extends Fragment {

    public AudioFragment() {}

    private Button buttonChooseAudio;
    private TextView textViewNameSong;
    private ImageButton buttonPausePlay;
    private ImageButton buttonStop;
    private SeekBar seekBarMusic;
    private MediaPlayer mediaPlayer;
    private ActivityResultLauncher<Intent> launcher;

    private List<View> musicViews;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK && result.getData() != null && result.getData().getData() != null) {
                Uri uri = result.getData().getData();
                mediaPlayer = new MediaPlayer();
                try {
                    mediaPlayer.setDataSource(requireContext(), uri);
                } catch (IOException e) {
                    Log.d("AudioFragment", e.getMessage());
                }
                try {
                    mediaPlayer.prepare();
                } catch (IOException e) {
                    Log.d("AudioFragment", e.getMessage());
                }
                mediaPlayer.start();
                seekBarMusic.setMax(mediaPlayer.getDuration());
                buttonPausePlay.setImageResource(R.drawable.pause_24);
                textViewNameSong.setText(uri.getLastPathSegment());
                setAlphaClickable(true);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_audio, container, false);
        buttonChooseAudio = view.findViewById(R.id.buttonAudio);
        buttonPausePlay = view.findViewById(R.id.buttonPausePlay);
        buttonStop = view.findViewById(R.id.buttonStop);
        seekBarMusic = view.findViewById(R.id.seekBarMusic);
        textViewNameSong = view.findViewById(R.id.textViewNameSong);

        musicViews = List.of(buttonPausePlay, buttonStop, seekBarMusic, textViewNameSong);

        setAlphaClickable(false);

        buttonChooseAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_audio = new Intent(Intent.ACTION_PICK, MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);
                launcher.launch(intent_audio);
            }
        });

        buttonStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayer != null) {
                    mediaPlayer.stop();
                    mediaPlayer.release();
                    mediaPlayer = null;
                }
            }
        });

        buttonPausePlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayer != null) {
                    if (ContextCompat.getDrawable(requireActivity(), R.drawable.pause_24) == buttonPausePlay.getDrawable()) {
                        mediaPlayer.pause();
                        buttonPausePlay.setImageResource(R.drawable.play_arrow_24);
                    } else {
                        mediaPlayer.start();
                        buttonPausePlay.setImageResource(R.drawable.pause_24);
                    }
                }
            }
        });

        seekBarMusic.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser && mediaPlayer != null) {
                    mediaPlayer.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        return view;
    }

    private void setAlphaClickable(boolean visibleAndClickable) {
        if (visibleAndClickable) {
            for (View view : musicViews) {
                view.setAlpha(1f);
                view.setClickable(true);
            }
        } else {
            for (View view : musicViews) {
                view.setAlpha(0.5f);
                view.setClickable(false);
            }
        }
    }
}