package com.kefawatch.app;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
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

public class CatalogFragment extends Fragment {

    private final ExecutorService io = Executors.newSingleThreadExecutor();
    private TitlesAdapter titlesAdapter;
    private SwipeRefreshLayout swipeRefresh;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_catalog, container, false);

        RecyclerView recyclerTitles = view.findViewById(R.id.recyclerTitles);
        swipeRefresh = view.findViewById(R.id.swipeRefresh);

        titlesAdapter = new TitlesAdapter();
        recyclerTitles.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerTitles.setAdapter(titlesAdapter);
        
        swipeRefresh.setOnRefreshListener(this::loadTitles);

        loadTitles();
        return view;
    }

    private void loadTitles() {
        swipeRefresh.setRefreshing(true);
        io.execute(() -> {
            try {
                Response<TitlesListDto> response = ApiProvider.create().listTitles(0, 50).execute();
                if (!response.isSuccessful() || response.body() == null) {
                    if (getActivity() != null) {
                        requireActivity().runOnUiThread(() -> {
                            Toast.makeText(getContext(), "Katalog HTTP " + response.code(), Toast.LENGTH_LONG).show();
                            swipeRefresh.setRefreshing(false);
                        });
                    }
                    return;
                }
                TitlesListDto body = response.body();
                if (!body.success || body.data == null) {
                    if (getActivity() != null) {
                        requireActivity().runOnUiThread(() -> {
                            Toast.makeText(getContext(), body.message != null ? body.message : "Katalog yanıtı geçersiz", Toast.LENGTH_LONG).show();
                            swipeRefresh.setRefreshing(false);
                        });
                    }
                    return;
                }
                List<TitlesListDto.TitleItem> items = body.data.content != null ? body.data.content : Collections.emptyList();
                if (getActivity() != null) {
                    requireActivity().runOnUiThread(() -> {
                        titlesAdapter.submit(items);
                        swipeRefresh.setRefreshing(false);
                    });
                }
            } catch (IOException e) {
                if (getActivity() != null) {
                    requireActivity().runOnUiThread(() -> {
                        Toast.makeText(getContext(), "Ağ hatası", Toast.LENGTH_LONG).show();
                        swipeRefresh.setRefreshing(false);
                    });
                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        io.shutdownNow();
    }
}
