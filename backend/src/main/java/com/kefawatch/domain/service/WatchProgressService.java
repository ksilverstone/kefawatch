package com.kefawatch.domain.service;

import com.kefawatch.domain.exception.DomainExceptions;
import com.kefawatch.domain.model.Episode;
import com.kefawatch.domain.model.WatchProgress;
import com.kefawatch.domain.port.TitleRepository;
import com.kefawatch.domain.port.WatchProgressRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WatchProgressService {

    private final WatchProgressRepository progressRepository;
    private final TitleRepository titleRepository;

    public WatchProgressService(WatchProgressRepository progressRepository, TitleRepository titleRepository) {
        this.progressRepository = progressRepository;
        this.titleRepository = titleRepository;
    }

    public WatchProgress get(long userId, long titleId) {
        return progressRepository.findByUserAndTitle(userId, titleId)
                .orElseThrow(() -> DomainExceptions.notFound("Progress not found"));
    }

    public WatchProgress upsert(long userId, long titleId, Long episodeId, int positionSeconds, boolean completed) {
        if (!titleRepository.existsById(titleId)) {
            throw DomainExceptions.notFound("Title not found");
        }
        if (episodeId != null) {
            List<Episode> episodes = titleRepository.findEpisodesByTitleId(titleId);
            boolean ok = episodes.stream().anyMatch(e -> e.id() == episodeId);
            if (!ok) {
                throw DomainExceptions.notFound("Episode not found for title");
            }
        }
        return progressRepository.upsert(userId, titleId, episodeId, positionSeconds, completed);
    }
}
