package com.kefawatch.domain.port;

import com.kefawatch.common.api.PageResult;
import com.kefawatch.domain.model.Episode;
import com.kefawatch.domain.model.TitleDetail;
import com.kefawatch.domain.model.TitleSummary;

import java.util.List;
import java.util.Optional;

public interface TitleRepository {

    PageResult<TitleSummary> findPage(int page, int size);

    Optional<TitleDetail> findDetailById(long id);

    List<Episode> findEpisodesByTitleId(long titleId);

    boolean existsById(long id);

    List<TitleSummary> findSummariesByIds(List<Long> ids);

    PageResult<TitleSummary> search(String query, int page, int size);

    long insert(String type, String name, String description, String posterUrl, String trailerUrl, String externalRef);
}
