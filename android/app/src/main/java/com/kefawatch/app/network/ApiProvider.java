package com.kefawatch.app.network;

import com.kefawatch.app.BuildConfig;
import com.kefawatch.app.network.dto.LoginBody;
import com.kefawatch.app.network.dto.TitlesListDto;
import com.kefawatch.app.network.dto.TokenEnvelope;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public final class ApiProvider {

    private ApiProvider() {
    }

    public static KefawatchApi create() {
        String base = BuildConfig.API_BASE_URL;
        if (!base.endsWith("/")) {
            base = base + "/";
        }
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BASIC);
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(logging)
                .build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(base)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit.create(KefawatchApi.class);
    }

    public interface KefawatchApi {
        @POST("api/v1/auth/login")
        Call<TokenEnvelope> login(@Body LoginBody body);

        @POST("api/v1/auth/register")
        Call<TokenEnvelope> register(@Body LoginBody body);

        @GET("api/v1/titles")
        Call<TitlesListDto> listTitles(@Query("page") int page, @Query("size") int size);

        @GET("api/v1/titles/{id}")
        Call<com.kefawatch.app.network.dto.TitleDetailDto> getTitle(@retrofit2.http.Path("id") long id);

        @POST("api/v1/watchlist")
        Call<com.kefawatch.app.network.dto.ApiResponse> addToWatchlist(@retrofit2.http.Header("Authorization") String token, @Body com.kefawatch.app.network.dto.WatchlistAddRequest body);
    }
}
