package com.kefawatch.domain.catalog;

import com.kefawatch.domain.model.TitleDetail;

import java.util.Optional;

/**
 * Strategy abstraction for resolving catalog metadata (DB-backed implementation).
 */
public interface TitleMetadataSource {

    Optional<TitleDetail> findTitle(long id);
}
