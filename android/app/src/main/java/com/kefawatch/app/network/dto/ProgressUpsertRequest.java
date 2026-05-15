package com.kefawatch.app.network.dto;

public class ProgressUpsertRequest {
    public long titleId;
    public Long episodeId;
    public int positionSeconds;
    public boolean completed;

    public ProgressUpsertRequest(long titleId, Long episodeId, int positionSeconds, boolean completed) {
        this.titleId = titleId;
        this.episodeId = episodeId;
        this.positionSeconds = positionSeconds;
        this.completed = completed;
    }
}
