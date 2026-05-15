package com.kefawatch.api;

import com.kefawatch.api.dto.ProgressUpsertRequest;
import com.kefawatch.api.support.CurrentUser;
import com.kefawatch.common.api.ApiResponse;
import com.kefawatch.domain.model.WatchProgress;
import com.kefawatch.domain.service.WatchProgressService;
import com.kefawatch.security.AuthPrincipal;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/progress")
public class ProgressController {

    private final WatchProgressService watchProgressService;

    public ProgressController(WatchProgressService watchProgressService) {
        this.watchProgressService = watchProgressService;
    }

    @GetMapping("/{titleId}")
    public ApiResponse<WatchProgress> get(@PathVariable long titleId) {
        AuthPrincipal user = CurrentUser.requireAuthPrincipal();
        return ApiResponse.ok(watchProgressService.get(user.userId(), titleId));
    }

    @PutMapping
    public ApiResponse<WatchProgress> upsert(@Valid @RequestBody ProgressUpsertRequest request) {
        AuthPrincipal user = CurrentUser.requireAuthPrincipal();
        WatchProgress progress = watchProgressService.upsert(
                user.userId(),
                request.titleId(),
                request.episodeId(),
                request.positionSeconds(),
                request.completed()
        );
        return ApiResponse.ok(progress);
    }
}
