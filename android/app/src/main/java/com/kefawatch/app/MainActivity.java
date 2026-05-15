package com.kefawatch.app;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.kefawatch.app.network.ApiProvider;
import com.kefawatch.app.network.dto.TitlesListDto;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private static final String PREFS = "kefawatch_prefs";
    private static final String KEY_TOKEN = "jwt";

    private final ExecutorService io = Executors.newSingleThreadExecutor();

    private TitlesAdapter titlesAdapter;
    private SwipeRefreshLayout swipeRefresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        setTitle("Katalog");

        RecyclerView recyclerTitles = findViewById(R.id.recyclerTitles);
        swipeRefresh = findViewById(R.id.swipeRefresh);

        titlesAdapter = new TitlesAdapter();
        recyclerTitles.setLayoutManager(new LinearLayoutManager(this));
        recyclerTitles.setAdapter(titlesAdapter);
        
        swipeRefresh.setOnRefreshListener(this::loadTitles);

        loadTitles();
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, 1, 0, "Çıkış Yap").setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == 1) {
            getSharedPreferences(PREFS, Context.MODE_PRIVATE).edit().remove(KEY_TOKEN).apply();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void loadTitles() {
        swipeRefresh.setRefreshing(true);
        io.execute(() -> {
            try {
                Response<TitlesListDto> response = ApiProvider.create().listTitles(0, 50).execute();
                if (!response.isSuccessful() || response.body() == null) {
                    runOnUiThread(() -> {
                        Toast.makeText(this, "Katalog HTTP " + response.code(), Toast.LENGTH_LONG).show();
                        swipeRefresh.setRefreshing(false);
                    });
                    return;
                }
                TitlesListDto body = response.body();
                if (!body.success || body.data == null) {
                    runOnUiThread(() -> {
                        Toast.makeText(this, body.message != null ? body.message : "Katalog yanıtı geçersiz", Toast.LENGTH_LONG).show();
                        swipeRefresh.setRefreshing(false);
                    });
                    return;
                }
                List<TitlesListDto.TitleItem> items = body.data.content != null ? body.data.content : Collections.emptyList();
                runOnUiThread(() -> {
                    titlesAdapter.submit(items);
                    swipeRefresh.setRefreshing(false);
                });
            } catch (IOException e) {
                runOnUiThread(() -> {
                    Toast.makeText(this, "Ağ hatası", Toast.LENGTH_LONG).show();
                    swipeRefresh.setRefreshing(false);
                });
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        io.shutdownNow();
    }
}
