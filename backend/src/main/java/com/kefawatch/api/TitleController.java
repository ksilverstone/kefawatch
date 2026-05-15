package com.kefawatch.api;

import com.kefawatch.common.api.ApiResponse;
import com.kefawatch.common.api.PageResult;
import com.kefawatch.domain.model.TitleDetail;
import com.kefawatch.domain.model.TitleSummary;
import com.kefawatch.domain.service.TitleCatalogService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/titles")
public class TitleController {

    private final TitleCatalogService titleCatalogService;

    public TitleController(TitleCatalogService titleCatalogService) {
        this.titleCatalogService = titleCatalogService;
    }

    @GetMapping
    public ApiResponse<PageResult<TitleSummary>> list(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        return ApiResponse.ok(titleCatalogService.listTitles(page, size));
    }

    @GetMapping("/{id}")
    public ApiResponse<TitleDetail> detail(@PathVariable long id) {
        return ApiResponse.ok(titleCatalogService.getTitle(id));
    }

    @PostMapping("/{id}/views")
    public ApiResponse<Void> recordView(@PathVariable long id) {
        titleCatalogService.recordView(id);
        return ApiResponse.ok(null);
    }
}
