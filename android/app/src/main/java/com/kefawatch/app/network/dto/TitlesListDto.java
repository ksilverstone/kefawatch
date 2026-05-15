package com.kefawatch.app.network.dto;

import java.util.List;

public class TitlesListDto {
    public boolean success;
    public TitlePage data;

    public static class TitlePage {
        public List<TitleItem> content;
        public int page;
        public int size;
        public long totalElements;
    }

    public static class TitleItem {
        public long id;
        public String type;
        public String name;
        public String posterUrl;
    }
}
