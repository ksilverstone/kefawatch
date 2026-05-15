package com.kefawatch.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kefawatch.AbstractIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class TitleApiIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void listTitles_returnsPagedContent() throws Exception {
        mockMvc.perform(get("/api/v1/titles").param("page", "0").param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.content").isArray());
    }

    @Test
    void registerLoginAndWatchlistFlow() throws Exception {
        String username = "user_" + System.nanoTime();
        MvcResult reg = mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"" + username + "\",\"password\":\"secret12\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.accessToken").exists())
                .andReturn();
        JsonNode tokenNode = objectMapper.readTree(reg.getResponse().getContentAsString()).path("data").path("accessToken");
        String token = tokenNode.asText();

        mockMvc.perform(post("/api/v1/watchlist")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"titleId\":1}"))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/api/v1/watchlist").header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].titleId").value(1));

        mockMvc.perform(put("/api/v1/progress")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"titleId\":1,\"episodeId\":1,\"positionSeconds\":120,\"completed\":false}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.titleId").value(1));

        mockMvc.perform(get("/api/v1/progress/1").header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.positionSeconds").value(120));
    }

    @Test
    void recordView_thenTrendingContainsTitle() throws Exception {
        mockMvc.perform(post("/api/v1/titles/1/views"))
                .andExpect(status().isOk());

        MvcResult res = mockMvc.perform(get("/api/v1/catalog/trending").param("limit", "5"))
                .andExpect(status().isOk())
                .andReturn();
        JsonNode data = objectMapper.readTree(res.getResponse().getContentAsString()).path("data");
        assertThat(data.isArray()).isTrue();
        assertThat(data.size()).isGreaterThan(0);
    }
}
