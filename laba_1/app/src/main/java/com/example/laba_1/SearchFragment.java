package com.example.laba_1;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class SearchFragment extends Fragment {

    private ProgressBar progressBar;
    private EditText editText;
    private ImageView imageView;

    private String ImageURL = "";
    private String pageURL = "";
    private String Title = "";

    public SearchFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_search, container, false);

        progressBar = view.findViewById(R.id.progressBar);
        Button button = view.findViewById(R.id.button_for_searching);
        editText = view.findViewById(R.id.input_for_searching);
        imageView = view.findViewById(R.id.imageView);


        // Установить прослушку
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ButtonClick(v);
            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupMenu(v);
            }
        });

        return view;
    }

    private void showPopupMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(getContext(), view);
        popupMenu.getMenuInflater().inflate(R.menu.image_menu, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (!ImageURL.isEmpty()) {
                    switch (item.getItemId()) {
                        case R.id.download:
                            // Логика для скачивания изображения
                            downloadImage(ImageURL);
                            Toast.makeText(getContext(), "Загрузка изображения", Toast.LENGTH_SHORT).show();
                            return true;
                        case R.id.range:
                            // Логика для оценки изображения
                            showRatingDialog();
                            return true;
                        case R.id.original:
                            // Переход на сайт изображения
                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(pageURL));
                            requireContext().startActivity(intent);
                        default:
                            return false;
                    }
                }
                return false;
            }
        });

        popupMenu.show();
    }

    private void showRatingDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_rate_image, null);
        builder.setView(dialogView);

        RatingBar ratingBar = dialogView.findViewById(R.id.ratingBar);
        builder.setPositiveButton("Оценить", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                float rating = ratingBar.getRating();
                // Логика для обработки рейтинга
                Toast.makeText(getContext(), "Вы оценили изображение на " + rating + " звезд", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void downloadImage(String url) {
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
        request.setTitle(Title);
        request.allowScanningByMediaScanner();
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, Title + ".jpg");

        DownloadManager manager = (DownloadManager) requireActivity().getSystemService(Context.DOWNLOAD_SERVICE);
        manager.enqueue(request);
    }

    public void ButtonClick(View view) {
        String query = editText.getText().toString();
        if (!query.isEmpty()) {
            imageView.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.VISIBLE);

            String accessKey = BuildConfig.UNSPLASH_ACCESS_KEY;
            String url = "https://pixabay.com/api/?key=" + accessKey + "&q=" + query.replaceAll(" ", "+") + "&image_type=photo&pretty=true&lang=ru&orientation=vertical&per_page=3";
            Log.d("URL_string", url);
            new FetchImageTask().execute(url);

            Title = query;
        }
    }

    void showError(String errorMessage) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage(errorMessage)
                .setPositiveButton("OK", null)
                .show();
    }

    private class FetchImageTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            Log.d("FetchImageTask", "doInBackground started");
            String jsonString;
            try {
                jsonString = getJsonFromUrl(urls[0]);
                Log.d("FetchImageTask", "JSON received: " + jsonString);
                JSONObject jsonObject = new JSONObject(jsonString);
                JSONArray results = jsonObject.getJSONArray("hits");
                if (results.length() == 0) {
                    ImageURL = "";
                    requireActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showError("Ничего не найдено");
                            progressBar.setVisibility(View.INVISIBLE);
                        }
                    });
                    return null;
                }
                JSONObject result = results.getJSONObject(0);
                ImageURL = result.getString("largeImageURL");
                pageURL = result.getString("pageURL");
                Log.d("FetchImageTask", "Image URL: " + ImageURL);
                return ImageURL;
            } catch (IOException | JSONException e) {
                Log.e("FetchImageTask", "Error: " + e.getMessage());
                requireActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.INVISIBLE);
                        showError(e.getMessage());
                    }
                });
                return "Error";
            }
        }

        @Override
        protected void onPostExecute(String rawUrl) {
            super.onPostExecute(rawUrl);
            Log.d("FetchImageTask", "onPostExecute: " + rawUrl);
            if (rawUrl != null && !rawUrl.equals("No result") && !rawUrl.equals("Error")) {
                Glide.with(requireActivity()).load(rawUrl).listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        Log.d("Glide", "onLoadFailed");
                        showError("Ошибка загрузки изображения");
                        progressBar.setVisibility(View.INVISIBLE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        Log.d("Glide", "onResourceReady");
                        imageView.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.INVISIBLE);
                        return false;
                    }

                }).transition(DrawableTransitionOptions.withCrossFade(400)).into(imageView);

            }

        }
    }

    public String getJsonFromUrl(String urlString) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            return readStream(in);
        } finally {
            urlConnection.disconnect();
        }
    }

    private String readStream(InputStream in) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        StringBuilder result = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            result.append(line);
        }
        return result.toString();
    }
}
