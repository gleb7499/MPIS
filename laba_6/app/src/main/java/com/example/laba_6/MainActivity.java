package com.example.laba_6;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okio.BufferedSink;
import okio.Okio;

public class MainActivity extends AppCompatActivity {

    private EditText editTextNumber;
    private ImageButton imageButtonFind;
    private Button buttonWatch;
    private Button buttonDelete;
    private TextView textView;
    private String url;
    private String last_download;

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

        editTextNumber = findViewById(R.id.editTextNumber);
        imageButtonFind = findViewById(R.id.imageButtonFind);
        buttonWatch = findViewById(R.id.buttonWatch);
        buttonDelete = findViewById(R.id.buttonDelete);
        textView = findViewById(R.id.textView);

        setMargins(buttonWatch);
        setMargins(buttonDelete);

        editTextNumber.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP) {
                    clickImageButtonFind();
                    ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(editTextNumber.getWindowToken(), 0);
                    return true;
                }
                return false;
            }
        });

        imageButtonFind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickImageButtonFind();
            }
        });

        buttonWatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickButtonWatch();
            }
        });
    }

    private void clickButtonWatch() {
        try {
            File file = new File(last_download);
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.fromFile(file), "application/pdf");
            startActivity(intent);
        } catch (Exception e) {
            showError("Приложение для открытия pdf не установлено");
        }
    }

    private void clickImageButtonFind() {
        String query = editTextNumber.getText().toString();
        if (!query.isEmpty()) {
            url = "https://ntv.ifmo.ru/file/journal/" + query.strip() + ".pdf";

            textView.setHint("Поиск файла...");

            new OkHttpClient.Builder().connectTimeout(30, TimeUnit.SECONDS).readTimeout(30, TimeUnit.SECONDS).writeTimeout(30, TimeUnit.SECONDS).build().newCall(new Request.Builder().url(url).build()).enqueue(new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    showError("Ошибка запроса:\n" + e.getMessage());
                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    String responseHeader = response.header("Content-Type");
                    if (responseHeader == null || !responseHeader.contains("html")) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                textView.setHint("Файл найден\nЗагрузка...");

                                Handler handler = new Handler(Looper.getMainLooper());

                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            Response downloadResponse = new OkHttpClient.Builder().connectTimeout(30, TimeUnit.SECONDS).readTimeout(30, TimeUnit.SECONDS).writeTimeout(30, TimeUnit.SECONDS).build().newCall(new Request.Builder().url(url).build()).execute();
                                            String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/" + query.strip() + ".pdf";
                                            FileOutputStream outputStream = new FileOutputStream(path);
                                            BufferedSink sink = Okio.buffer(Okio.sink(outputStream));
                                            sink.writeAll(downloadResponse.body().source());
                                            sink.close();
                                            handler.post(new Runnable() {
                                                @Override
                                                public void run() {
                                                    last_download = path;
                                                    textView.setHint("Файл загружен\n" + last_download);
                                                    buttonWatch.setAlpha(1.0f);
                                                    buttonDelete.setAlpha(1.0f);
                                                    buttonWatch.setClickable(true);
                                                    buttonDelete.setClickable(true);
                                                }
                                            });
                                        } catch (IOException e) {
                                            showError("Ошибка скачивания:\n" + e.getMessage());
                                        }
                                    }
                                }).start();
                            }
                        });
                    } else {
                        showError("Файл не найден :(");
                    }
                }
            });
        } else {
            showError("Заполните поле!");
        }
    }

    private void showError(final String errorMessage) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                textView.setHint(errorMessage);
                buttonWatch.setAlpha(0.5f);
                buttonDelete.setAlpha(0.5f);
                buttonWatch.setClickable(false);
                buttonDelete.setClickable(false);
            }
        });

    }
}