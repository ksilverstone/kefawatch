package com.kefawatch.app;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.kefawatch.app.network.ApiProvider;
import com.kefawatch.app.network.dto.LoginBody;
import com.kefawatch.app.network.dto.TitlesListDto;
import com.kefawatch.app.network.dto.TokenEnvelope;

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

    private TextInputEditText inputUsername;
    private TextInputEditText inputPassword;
    private TextView textStatus;
    private TitlesAdapter titlesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inputUsername = findViewById(R.id.inputUsername);
        inputPassword = findViewById(R.id.inputPassword);
        textStatus = findViewById(R.id.textStatus);
        MaterialButton buttonLogin = findViewById(R.id.buttonLogin);
        MaterialButton buttonRegister = findViewById(R.id.buttonRegister);
        MaterialButton buttonLoadTitles = findViewById(R.id.buttonLoadTitles);
        RecyclerView recyclerTitles = findViewById(R.id.recyclerTitles);

        titlesAdapter = new TitlesAdapter();
        recyclerTitles.setLayoutManager(new LinearLayoutManager(this));
        recyclerTitles.setAdapter(titlesAdapter);

        buttonLogin.setOnClickListener(v -> doAuth(false));
        buttonRegister.setOnClickListener(v -> doAuth(true));
        buttonLoadTitles.setOnClickListener(v -> loadTitles());

        updateStatus("Hazır. API: " + BuildConfig.API_BASE_URL);
    }

    private SharedPreferences prefs() {
        return getSharedPreferences(PREFS, Context.MODE_PRIVATE);
    }

    private void updateStatus(String message) {
        runOnUiThread(() -> textStatus.setText(message));
    }

    private void toast(String message) {
        runOnUiThread(() -> Toast.makeText(this, message, Toast.LENGTH_LONG).show());
    }

    private void doAuth(boolean register) {
        String username = textOf(inputUsername);
        String password = textOf(inputPassword);
        if (username.isEmpty() || password.length() < 6) {
            toast("Kullanıcı adı ve en az 6 karakter şifre gerekli.");
            return;
        }
        io.execute(() -> {
            try {
                var api = ApiProvider.create();
                Response<TokenEnvelope> response = register
                        ? api.register(new LoginBody(username, password)).execute()
                        : api.login(new LoginBody(username, password)).execute();
                if (!response.isSuccessful() || response.body() == null) {
                    updateStatus("HTTP " + response.code());
                    toast("İstek başarısız: " + response.code());
                    return;
                }
                TokenEnvelope body = response.body();
                if (!body.success || body.data == null || body.data.accessToken == null) {
                    String msg = body.message != null ? body.message : "Bilinmeyen hata";
                    updateStatus(msg);
                    toast(msg);
                    return;
                }
                prefs().edit().putString(KEY_TOKEN, body.data.accessToken).apply();
                updateStatus(register ? "Kayıt tamam, token saklandı." : "Giriş tamam, token saklandı.");
            } catch (IOException e) {
                updateStatus("Ağ hatası: " + e.getMessage());
                toast("Ağ hatası");
            }
        });
    }

    private void loadTitles() {
        io.execute(() -> {
            try {
                Response<TitlesListDto> response = ApiProvider.create().listTitles(0, 50).execute();
                if (!response.isSuccessful() || response.body() == null) {
                    updateStatus("Katalog HTTP " + response.code());
                    return;
                }
                TitlesListDto body = response.body();
                if (!body.success || body.data == null) {
                    updateStatus(body.message != null ? body.message : "Katalog yanıtı geçersiz");
                    return;
                }
                List<TitlesListDto.TitleItem> items = body.data.content != null ? body.data.content : Collections.emptyList();
                runOnUiThread(() -> titlesAdapter.submit(items));
                updateStatus("Toplam öğe: " + body.data.totalElements);
            } catch (IOException e) {
                updateStatus("Ağ hatası: " + e.getMessage());
            }
        });
    }

    @NonNull
    private static String textOf(TextInputEditText editText) {
        if (editText.getText() == null) {
            return "";
        }
        return editText.getText().toString().trim();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        io.shutdownNow();
    }
}
