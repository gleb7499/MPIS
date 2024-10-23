package com.example.lab_7.Fragments;

import static android.app.Activity.RESULT_OK;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
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
    private Handler handler_seekBar, handler_return;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        handler_seekBar = new Handler(Looper.getMainLooper());
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
                buttonPausePlay.setImageResource(R.drawable.pause_24);
                textViewNameSong.setText(uri.getLastPathSegment());

                seekBarMusic.setMax(mediaPlayer.getDuration());
                seekBarMusic.setProgress(mediaPlayer.getCurrentPosition());
//                setAlphaClickable(true);

                handler_seekBar.removeCallbacks(runnable);
                handler_seekBar.postDelayed(runnable, 100); // запустить Runnable объект первый раз через 0.1 секунду
            }
        });
    }

    private final Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                int currentPosition = mediaPlayer.getCurrentPosition();
                int maxDuration = mediaPlayer.getDuration();
                Log.d("AudioFragment", "Current position: " + currentPosition);
                handler_return.post(new Runnable() {
                    @Override
                    public void run() {
                        seekBarMusic.setMax(maxDuration);
                        seekBarMusic.setProgress(currentPosition);
                    }
                });

                Log.d("AudioFragment", "Seekbar progress: " + seekBarMusic.getProgress());
                handler_seekBar.postDelayed(this, 100);
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_audio, container, false);
        buttonChooseAudio = view.findViewById(R.id.buttonAudio);
        buttonPausePlay = view.findViewById(R.id.buttonPausePlay);
        buttonStop = view.findViewById(R.id.buttonStop);
        seekBarMusic = view.findViewById(R.id.seekBarMusic);
        textViewNameSong = view.findViewById(R.id.textViewNameSong);

        handler_return = new Handler(Looper.getMainLooper());

        musicViews = List.of(buttonPausePlay, buttonStop, seekBarMusic, textViewNameSong);

//        setAlphaClickable(false);
        textViewNameSong.setText("Ничего не выбрано");

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
//                    setAlphaClickable(false);
                    textViewNameSong.setText("Ничего не выбрано");
                    seekBarMusic.setProgress(0);
                }
            }
        });

        buttonPausePlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayer != null) {
                    if (mediaPlayer.isPlaying()) {
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
                if (mediaPlayer != null && fromUser) {
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