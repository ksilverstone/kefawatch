package com.kefawatch.app.network.dto;

import java.util.List;

public class TitleDetailDto {
    public boolean success;
    public TitleDetailData data;

    public static class TitleDetailData {
        public long id;
        public String type;
        public String name;
        public String description;
        public List<EpisodeDto> episodes;
    }

    public static class EpisodeDto {
        public long id;
        public int seasonNumber;
        public int episodeNumber;
        public String name;
    }
}
