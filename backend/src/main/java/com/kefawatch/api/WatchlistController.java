package com.kefawatch.api;

import com.kefawatch.api.dto.WatchlistAddRequest;
import com.kefawatch.api.support.CurrentUser;
import com.kefawatch.common.api.ApiResponse;
import com.kefawatch.domain.model.WatchlistEntry;
import com.kefawatch.domain.service.WatchlistService;
import com.kefawatch.security.AuthPrincipal;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/watchlist")
public class WatchlistController {

    private final WatchlistService watchlistService;

    public WatchlistController(WatchlistService watchlistService) {
        this.watchlistService = watchlistService;
    }

    @GetMapping
    public ApiResponse<List<WatchlistEntry>> list() {
        AuthPrincipal user = CurrentUser.requireAuthPrincipal();
        return ApiResponse.ok(watchlistService.list(user.userId()));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<Void> add(@Valid @RequestBody WatchlistAddRequest request) {
        AuthPrincipal user = CurrentUser.requireAuthPrincipal();
        watchlistService.add(user.userId(), request.titleId());
        return ApiResponse.ok(null);
    }

    @DeleteMapping("/{titleId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remove(@PathVariable long titleId) {
        AuthPrincipal user = CurrentUser.requireAuthPrincipal();
        watchlistService.remove(user.userId(), titleId);
    }
}
