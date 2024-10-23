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
    private Handler handler_seekBar;

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
                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        seekBarMusic.setProgress(0);
                        buttonPausePlay.setImageResource(R.drawable.play_arrow_24);
                        handler_seekBar.removeCallbacks(runnable);
                    }
                });
                buttonPausePlay.performClick();
                textViewNameSong.setText(uri.getLastPathSegment());
                seekBarMusic.setMax(mediaPlayer.getDuration());
                seekBarMusic.setProgress(mediaPlayer.getCurrentPosition());
                setAlpha(true);
            }
        });
    }

    private final Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (mediaPlayer != null) {
                int currentPosition = mediaPlayer.getCurrentPosition();
                seekBarMusic.setProgress(currentPosition);
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

        musicViews = List.of(buttonPausePlay, buttonStop, seekBarMusic, textViewNameSong);

        setAlpha(false);
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
                    setAlpha(false);
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
                        Log.d("AudioFragment", "start");
                        mediaPlayer.start();
                        buttonPausePlay.setImageResource(R.drawable.pause_24);
                        handler_seekBar.removeCallbacks(runnable);
                        handler_seekBar.postDelayed(runnable, 100); // запустить Runnable объект первый раз через 0.1 секунду

                    }
                }
            }
        });

        seekBarMusic.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (mediaPlayer != null && fromUser) {
                    mediaPlayer.seekTo(progress);
                } else if (mediaPlayer == null) {
                    seekBar.setProgress(0);
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

    private void setAlpha(boolean visibleAndClickable) {
        for (View view : musicViews) {
            view.setAlpha(visibleAndClickable ? 1f : 0.5f);
        }
    }
}