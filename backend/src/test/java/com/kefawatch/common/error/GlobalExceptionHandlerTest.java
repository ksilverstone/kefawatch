package com.kefawatch.common.error;

import com.kefawatch.AbstractIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class GlobalExceptionHandlerTest extends AbstractIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    void unknownTitle_returns404Wrapped() throws Exception {
        mockMvc.perform(get("/api/v1/titles/999999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.errorCode").exists());
    }

    @Test
    void watchlist_withoutToken_returns401Json() throws Exception {
        mockMvc.perform(get("/api/v1/watchlist"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.errorCode").value("UNAUTHORIZED"));
    }
}
