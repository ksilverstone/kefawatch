package com.kefawatch.api;

import com.kefawatch.common.api.ApiResponse;
import com.kefawatch.domain.model.TitleSummary;
import com.kefawatch.domain.service.TitleCatalogService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/catalog")
public class CatalogController {

    private final TitleCatalogService titleCatalogService;

    public CatalogController(TitleCatalogService titleCatalogService) {
        this.titleCatalogService = titleCatalogService;
    }

    @GetMapping("/trending")
    public ApiResponse<List<TitleSummary>> trending(@RequestParam(defaultValue = "10") int limit) {
        return ApiResponse.ok(titleCatalogService.trending(limit));
    }
}
