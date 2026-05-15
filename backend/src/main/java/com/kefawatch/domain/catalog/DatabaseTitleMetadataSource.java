package com.kefawatch.domain.catalog;

import com.kefawatch.domain.model.TitleDetail;
import com.kefawatch.domain.port.TitleRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class DatabaseTitleMetadataSource implements TitleMetadataSource {

    private final TitleRepository titleRepository;

    public DatabaseTitleMetadataSource(TitleRepository titleRepository) {
        this.titleRepository = titleRepository;
    }

    @Override
    public Optional<TitleDetail> findTitle(long id) {
        return titleRepository.findDetailById(id);
    }
}
