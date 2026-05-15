package com.kefawatch.app;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.kefawatch.app.network.ApiProvider;
import com.kefawatch.app.network.dto.ApiResponse;
import com.kefawatch.app.network.dto.TitleDetailDto;
import com.kefawatch.app.network.dto.WatchlistAddRequest;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Response;

public class TitleDetailActivity extends AppCompatActivity {

    private final ExecutorService io = Executors.newSingleThreadExecutor();
    private long titleId;

    private TextView textTitleName;
    private TextView textTitleDesc;
    private TextView textEpisodes;
    private MaterialButton buttonWatchlist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_title_detail);

        titleId = getIntent().getLongExtra("TITLE_ID", -1);
        String titleName = getIntent().getStringExtra("TITLE_NAME");

        setTitle(titleName != null ? titleName : "Detay");
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        textTitleName = findViewById(R.id.textTitleName);
        textTitleDesc = findViewById(R.id.textTitleDesc);
        textEpisodes = findViewById(R.id.textEpisodes);
        buttonWatchlist = findViewById(R.id.buttonWatchlist);

        buttonWatchlist.setOnClickListener(v -> addToWatchlist());

        if (titleId != -1) {
            loadDetails();
        }
    }

    private void loadDetails() {
        io.execute(() -> {
            try {
                Response<TitleDetailDto> response = ApiProvider.create().getTitle(titleId).execute();
                if (!response.isSuccessful() || response.body() == null || response.body().data == null) {
                    runOnUiThread(() -> Toast.makeText(this, "Detaylar alınamadı", Toast.LENGTH_SHORT).show());
                    return;
                }
                TitleDetailDto.TitleDetailData data = response.body().data;
                runOnUiThread(() -> {
                    textTitleName.setText(data.name);
                    textTitleDesc.setText(data.description != null ? data.description : "Açıklama yok.");
                    int epCount = data.episodes != null ? data.episodes.size() : 0;
                    textEpisodes.setText("Toplam Bölüm: " + epCount);
                });
            } catch (IOException e) {
                runOnUiThread(() -> Toast.makeText(this, "Ağ hatası", Toast.LENGTH_SHORT).show());
            }
        });
    }

    private void addToWatchlist() {
        buttonWatchlist.setEnabled(false);
        SharedPreferences prefs = getSharedPreferences("kefawatch_prefs", Context.MODE_PRIVATE);
        String token = prefs.getString("jwt", null);
        if (token == null) {
            Toast.makeText(this, "Oturum bulunamadı", Toast.LENGTH_SHORT).show();
            buttonWatchlist.setEnabled(true);
            return;
        }

        io.execute(() -> {
            try {
                Response<ApiResponse> response = ApiProvider.create()
                        .addToWatchlist("Bearer " + token, new WatchlistAddRequest(titleId)).execute();
                
                runOnUiThread(() -> {
                    if (response.isSuccessful()) {
                        Toast.makeText(this, "Listeye eklendi!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Hata: " + response.code(), Toast.LENGTH_SHORT).show();
                    }
                    buttonWatchlist.setEnabled(true);
                });
            } catch (IOException e) {
                runOnUiThread(() -> {
                    Toast.makeText(this, "Ağ hatası", Toast.LENGTH_SHORT).show();
                    buttonWatchlist.setEnabled(true);
                });
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        io.shutdownNow();
    }
}
