package com.kefawatch.app;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.kefawatch.app.network.ApiProvider;
import com.kefawatch.app.network.dto.LoginBody;
import com.kefawatch.app.network.dto.TokenEnvelope;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private static final String PREFS = "kefawatch_prefs";
    private static final String KEY_TOKEN = "jwt";

    private final ExecutorService io = Executors.newSingleThreadExecutor();

    private TextInputEditText inputUsername;
    private TextInputEditText inputPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (prefs().getString(KEY_TOKEN, null) != null) {
            navigateToMain();
            return;
        }

        setContentView(R.layout.activity_login);

        inputUsername = findViewById(R.id.inputUsername);
        inputPassword = findViewById(R.id.inputPassword);
        MaterialButton buttonLogin = findViewById(R.id.buttonLogin);
        MaterialButton buttonRegister = findViewById(R.id.buttonRegister);

        buttonLogin.setOnClickListener(v -> doAuth(false));
        buttonRegister.setOnClickListener(v -> doAuth(true));
    }

    private SharedPreferences prefs() {
        return getSharedPreferences(PREFS, Context.MODE_PRIVATE);
    }

    private void doAuth(boolean register) {
        String username = textOf(inputUsername);
        String password = textOf(inputPassword);
        if (username.isEmpty() || password.length() < 6) {
            runOnUiThread(() -> Toast.makeText(this, "Kullanıcı adı ve en az 6 karakter şifre gerekli.", Toast.LENGTH_LONG).show());
            return;
        }
        
        MaterialButton loginBtn = findViewById(R.id.buttonLogin);
        loginBtn.setEnabled(false);

        io.execute(() -> {
            try {
                var api = ApiProvider.create();
                Response<TokenEnvelope> response = register
                        ? api.register(new LoginBody(username, password)).execute()
                        : api.login(new LoginBody(username, password)).execute();
                        
                if (!response.isSuccessful() || response.body() == null) {
                    runOnUiThread(() -> {
                        Toast.makeText(this, "İstek başarısız: " + response.code(), Toast.LENGTH_LONG).show();
                        loginBtn.setEnabled(true);
                    });
                    return;
                }
                TokenEnvelope body = response.body();
                if (!body.success || body.data == null || body.data.accessToken == null) {
                    String msg = body.message != null ? body.message : "Bilinmeyen hata";
                    runOnUiThread(() -> {
                        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
                        loginBtn.setEnabled(true);
                    });
                    return;
                }
                
                prefs().edit().putString(KEY_TOKEN, body.data.accessToken).apply();
                runOnUiThread(this::navigateToMain);
                
            } catch (IOException e) {
                runOnUiThread(() -> {
                    Toast.makeText(this, "Ağ hatası", Toast.LENGTH_LONG).show();
                    loginBtn.setEnabled(true);
                });
            }
        });
    }

    private void navigateToMain() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
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
