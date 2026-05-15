package com.kefawatch.domain.service;

import com.kefawatch.domain.exception.DomainExceptions;
import com.kefawatch.domain.model.WatchlistEntry;
import com.kefawatch.domain.port.TitleRepository;
import com.kefawatch.domain.port.WatchlistRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WatchlistService {

    private final WatchlistRepository watchlistRepository;
    private final TitleRepository titleRepository;

    public WatchlistService(WatchlistRepository watchlistRepository, TitleRepository titleRepository) {
        this.watchlistRepository = watchlistRepository;
        this.titleRepository = titleRepository;
    }

    public List<WatchlistEntry> list(long userId) {
        return watchlistRepository.listByUser(userId);
    }

    public void add(long userId, long titleId) {
        if (!titleRepository.existsById(titleId)) {
            throw DomainExceptions.notFound("Title not found");
        }
        watchlistRepository.add(userId, titleId);
    }

    public void remove(long userId, long titleId) {
        if (!watchlistRepository.exists(userId, titleId)) {
            throw DomainExceptions.notFound("Watchlist entry not found");
        }
        watchlistRepository.remove(userId, titleId);
    }
}
