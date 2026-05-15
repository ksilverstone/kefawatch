package com.kefawatch.domain.service;

import com.kefawatch.common.api.PageResult;
import com.kefawatch.domain.catalog.TitleMetadataSource;
import com.kefawatch.domain.exception.DomainExceptions;
import com.kefawatch.domain.model.TitleDetail;
import com.kefawatch.domain.model.TitleSummary;
import com.kefawatch.domain.port.TitleRepository;
import com.kefawatch.domain.port.TrendingTitleStore;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TitleCatalogService {

    private final TitleRepository titleRepository;
    private final TitleMetadataSource metadataSource;
    private final TrendingTitleStore trendingTitleStore;

    public TitleCatalogService(TitleRepository titleRepository,
                               TitleMetadataSource metadataSource,
                               TrendingTitleStore trendingTitleStore) {
        this.titleRepository = titleRepository;
        this.metadataSource = metadataSource;
        this.trendingTitleStore = trendingTitleStore;
    }

    public PageResult<TitleSummary> listTitles(int page, int size) {
        return titleRepository.findPage(page, size);
    }

    public PageResult<TitleSummary> searchTitles(String query, int page, int size) {
        return titleRepository.search(query, page, size);
    }

    public TitleDetail createTitle(String type, String name, String description, String posterUrl, String trailerUrl, String externalRef) {
        long id = titleRepository.insert(type, name, description, posterUrl, trailerUrl, externalRef);
        return getTitle(id);
    }

    public TitleDetail getTitle(long id) {
        return metadataSource.findTitle(id)
                .orElseThrow(() -> DomainExceptions.notFound("Title not found"));
    }

    public void recordView(long titleId) {
        if (!titleRepository.existsById(titleId)) {
            throw DomainExceptions.notFound("Title not found");
        }
        trendingTitleStore.recordView(titleId);
    }

    public List<TitleSummary> trending(int limit) {
        List<Long> ids = trendingTitleStore.topIds(limit);
        if (ids.isEmpty()) {
            return titleRepository.findPage(0, limit).content();
        }
        return titleRepository.findSummariesByIds(ids);
    }
}
