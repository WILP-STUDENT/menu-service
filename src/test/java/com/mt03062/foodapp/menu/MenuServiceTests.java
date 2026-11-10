package com.mt03062.foodapp.menu;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class MenuServiceTests {

    @Autowired
    MockMvc mvc;

    @Test
    void restaurantMenuFlow() throws Exception {
        mvc.perform(post("/restaurants").contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"Spice Hub\",\"city\":\"Bengaluru\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1));

        mvc.perform(post("/restaurants/1/items").contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"Chicken Biryani\",\"price\":220}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.restaurantId").value(1));

        mvc.perform(get("/restaurants/1/menu"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));

        mvc.perform(get("/items/999")).andExpect(status().isNotFound());
        mvc.perform(post("/restaurants").contentType(MediaType.APPLICATION_JSON).content("{}"))
                .andExpect(status().isBadRequest());
    }
}
